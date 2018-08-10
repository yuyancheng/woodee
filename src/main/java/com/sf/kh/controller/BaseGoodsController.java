package com.sf.kh.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageRequestParams;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;

import com.sf.kh.model.BaseGoods;
import com.sf.kh.model.GoodsDept;
import com.sf.kh.model.UploadFileStatus;
import com.sf.kh.model.User;
import com.sf.kh.service.IBaseGoodsService;
import com.sf.kh.util.Constants;
import com.sf.kh.util.DateUtil;
import com.sf.kh.util.WebContextHolder;


/***
 * 物资管理
 * @author 866316
 *
 */
@RestController
@RequestMapping(path = "baseGoods")
public class BaseGoodsController {
	private static final Logger logger = LoggerFactory.getLogger(BaseGoodsController.class);
	private @Resource IBaseGoodsService baseGoodsService;
	
	@Value("${sharePath}")
	private String sharePath;
	/***
	 * 获取物资管理分页记录
	 * @param params
	 * @return
	 */
	@GetMapping(path = "list")
    public Result<Page<BaseGoods>> list(HttpSession session,PageRequestParams params) {
        return baseGoodsService.query4page(params.getParams());
    }
	
	
	/***
	 * 删除物资记录
	 * @param params
	 * @return
	 */
	@PostMapping(path = "delete")
    public Result<Boolean> delete(HttpSession session,@RequestParam(value = "id", required = true) String id) {
		return baseGoodsService.delete((String)id);
    }
	
	/***
	 * 修改物资记录
	 * @param params
	 * @return
	 */
	@PostMapping(path = "update")
    public Result<Boolean> update(HttpSession session, @RequestBody BaseGoods baseGoods) {
		return baseGoodsService.update(WebContextHolder.currentUser().getId(),baseGoods);
    }
	
	/***
	 * 新增物资记录
	 * @param params
	 * @return
	 */
	@PostMapping(path = "add")
    public Result<Boolean> add(HttpSession session,@RequestBody BaseGoods baseGoods) {
		return baseGoodsService.add(WebContextHolder.currentUser().getId(),baseGoods);
    }
	
	/***
	 * 根据类别获取物资集合
	 * @param params
	 * @return
	 */
	@PostMapping(path = "getGoodsByCategoryId")
    public Result<List<BaseGoods>> getGoodsByCategoryId(HttpSession session,@RequestParam(value = "categoryId", required = true) String categoryId) {
		return baseGoodsService.getGoodsByCategoryId(categoryId);
    }
	
	
	/***
	 * 获取所有的物资维表数据
	 * @param params
	 * @return
	 */
	@PostMapping(path = "getAllData")
    public Result<List<BaseGoods>> getAllData(HttpSession session) {
		return baseGoodsService.getAllData();
    }
	
	/***
	 * 下载物资模板文件
	 */
	@GetMapping("templete")
	public void downloadTemplete(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		try{
			String templeFileName = "物资编码模板.xls";
			String templeFilePath = this.sharePath+"templetes"+File.separator+"goods.xls";
			File file = new File(templeFilePath);
			if(false == file.exists()){
				//获取项目路径下的文件
				String templeftPath = request.getServletContext().getRealPath("/uploadFile/templetes/goods.xls");
				file = new File(templeftPath);
				if(true == file.exists()){
					Constants.exportFile(response, templeFileName, file);
				}else{
					logger.error("{}路径下没有获取到文件",templeFilePath);
					return;
				}
			}
			Constants.exportFile(response, templeFileName, file);
			logger.info("物资编码模板导出成功");
		}catch(Exception e){
			logger.error("导出物资编码模板失败原因：{}",e.getMessage());
		}
	}
	
	/***
	 * 上传物资记录
	 * @param session
	 * @param file
	 */
	@PostMapping(path = "uploadGoods")
	public Result<UploadFileStatus> uploadGoods(HttpSession session,
			@RequestParam("file") MultipartFile file){
		User user = WebContextHolder.currentUser();
		Long userId = user.getId();
		//1,存储文件
		String filePath;
		if (StringUtils.isBlank(sharePath)) {
		   filePath = session.getServletContext().getRealPath("/uploadFile/");
		} else {
		   filePath = sharePath.trim();
		}
		//添加日期路径
		filePath = filePath+ "goods" + File.separator+ DateUtil.formatDate(new Date());
        try {
            InputStream is = file.getInputStream();
            String originFileName = file.getOriginalFilename();
            UUID fileNameUUID = UUID.randomUUID();
            //获取文件扩展名
            String ext = Constants.getPostfix(originFileName);
            //文件名字
            String newFileName = fileNameUUID + Constants.POINT + ext;
            //存储文件
            File newFile = new File(filePath, newFileName);
            FileUtils.copyInputStreamToFile(is, newFile);
    		return baseGoodsService.uploadFile(newFile, newFileName, file.getContentType(), userId);
        } catch (IOException e) {
        	logger.error("上传物资记录异常原因：{}",e.getMessage());
            return Result.failure(ResultCode.SERVER_ERROR,e.getMessage());
        }
	}
	
	/***
	 * 导出文件
	 * @param session
	 */
	@GetMapping(path = "export")
	public void exportData(HttpSession session,HttpServletResponse response){
		String fileFolderPath;
		if (StringUtils.isBlank(sharePath)) {
			fileFolderPath = session.getServletContext().getRealPath("/uploadFile/");
		} else {
			fileFolderPath = sharePath.trim();
		}
		fileFolderPath = "export"+File.separator;
		File file = baseGoodsService.exportBaseGoods(fileFolderPath);
		String templeFileName = "物资数据.csv";
		try {
			Constants.exportFile(response, templeFileName, file);
			logger.info("物资数据导出成功");
		} catch (IOException e) {
			logger.info("物资数据导出异常原因：{}",e.getMessage());
		}
	}
	
	@PostMapping(path = "getParentOrg")
	public Result<String> getParentOrgByFirstCategoryId(HttpSession session,@RequestBody Map<String, Object> map){
		Object obj = map.get("firstCategoryId");
		if(null == obj){
			return Result.failure(ResultCode.BAD_REQUEST,"参数不能为空!");
		}
		GoodsDept gd = new GoodsDept();
		gd.setParentCategoryId(Long.valueOf(String.valueOf(obj)).longValue());
		List<GoodsDept> gdList = baseGoodsService.getGoodsDept(gd);
		if(gdList!=null && gdList.size()>0){
			return Result.success(String.valueOf(gdList.get(0).getParentOrgId()));
		}else{
			return Result.failure(ResultCode.BAD_REQUEST,"未匹配到记录，请刷新重试");
		}
	}
	
	
}
