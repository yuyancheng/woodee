package com.sf.kh.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import code.ponfee.commons.jedis.JedisClient;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import com.sf.kh.model.UploadFile;
import com.sf.kh.model.UploadFileStatus;
import com.sf.kh.model.User;
import com.sf.kh.service.IBaseGoodsService;
import com.sf.kh.service.IUploadFileService;
import com.sf.kh.service.IWaybillRouteService;
import com.sf.kh.service.IWaybillService;
import com.sf.kh.util.Constants;
import com.sf.kh.util.DateUtil;
import com.sf.kh.util.WebContextHolder;


@RestController
@RequestMapping(path = "upload")
public class UploadFileController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${sharePath}")
	private String sharePath;
	
	private @Resource JedisClient jedisClient;
	
	private @Resource IBaseGoodsService baseGoodsService;
	
	private @Resource IWaybillService waybillService;
	
	private @Resource IWaybillRouteService waybillRouteService;
	
	private static final String UPLOAD_FILE_UUID = "only_used_upload_file_get_status";
   
	 /**
     * 文件上传
     * @param type : 对应的业务逻辑处理类
     * @param file : 上传的文件
     * @param fileType : 上传文件的扩展名
     */
    @RequestMapping(value = "file", method = RequestMethod.POST)
    @ResponseBody
    public Result<UploadFileStatus> upload(HttpSession session,
                               @RequestParam("type") String type,
                               @RequestParam("file") MultipartFile file) {
    	
    	logger.info("upload file type: {}", type);
        User user = WebContextHolder.currentUser();
        Long userId = user.getId();
        IUploadFileService uploadFileService = null;
        //1,根据type获取对应的接口类
        /*if("route".equals(type)){
        	uploadFileService = waybillRouteService;
        }*/
        if(null == uploadFileService){
        	return Result.failure(ResultCode.BAD_REQUEST, "参数提供错误");
        }
        //2,存储文件
        String filePath;
        if (StringUtils.isBlank(sharePath)) {
            filePath = session.getServletContext().getRealPath("/uploadFile/");
        } else {
            filePath = sharePath.trim();
        }
        UploadFile uploadFile = this.saveFile(filePath, type, file,userId);
        if(null == uploadFile){
        	return Result.failure(ResultCode.SERVER_ERROR,"文件存储至服务器失败!");
        }
        session.setAttribute(UPLOAD_FILE_UUID+"_"+userId, uploadFile.getFileNamePrefix());
        //3,获取文件流并处理文件
        uploadFileService.dealImportFile(userId,uploadFile);
        UploadFileStatus ufs = new UploadFileStatus ();
        ufs.setCode(Constants.UPLOAD_FILE_STATUS_UPLOADING);
        ufs.setMsg(Constants.UPLOAD_FILE_MAP_STATUS.get(ufs.getCode()));
        return Result.success(ufs);      		
    }
    
    /**
     * 文件上传状态获取
     */
    @RequestMapping(value = "getStatus", method = RequestMethod.POST)
    @ResponseBody
    public Result<UploadFileStatus> getStatus(HttpSession session) {
    	logger.info(this.sharePath);
        User user = WebContextHolder.currentUser();
        Long userId = user.getId();
        Object obj = session.getAttribute(UPLOAD_FILE_UUID+"_"+userId);
        if(null == obj){
        	return Result.failure(Constants.UPLOAD_FILE_STATUS_ERROR,"服务器内部发生错误，请重新导入数据!");
        }
        String fileUUID = (String) obj;
        UploadFileStatus ufs = jedisClient.valueOps().getObject(fileUUID.getBytes(), UploadFileStatus.class);
        if(null == ufs){
        	return Result.failure(Constants.UPLOAD_FILE_STATUS_ERROR,"服务器内部发生错误，请重新导入数据!");
        }
        Integer code = ufs.getCode();
        if(null == code){
        	code = Constants.UPLOAD_FILE_STATUS_UPLOADING;
        }else{
        	String msg = Constants.UPLOAD_FILE_MAP_STATUS.get(code);
        	if(null == msg){//状态码错误
        		code = Constants.UPLOAD_FILE_STATUS_ERROR;
        	}
        }
        String message = ufs.getMsg();
        if(StringUtils.isBlank(message)){
        	ufs.setMsg(Constants.UPLOAD_FILE_MAP_STATUS.get(ufs.getCode()));
	   	}
        return Result.success(ufs);
    }
    
    
    /***
     * 存储文件
     * @param filePath
     * @param type
     * @param file
     * @return
     */
    private  UploadFile  saveFile(String filePath, String type, MultipartFile file,Long userId){
        String originFileName;
        String newFileName;
        File newFile;
        UploadFile uploadFile = null;
        //添加日期目录
        filePath = filePath + DateUtil.formatDate(new Date());
        try {
            InputStream is = file.getInputStream();
            originFileName = file.getOriginalFilename();
            UUID fileNameUUID = UUID.randomUUID();
            //1,以文件作为key，里面有文件的状态返回的消息内容
            UploadFileStatus ups = new UploadFileStatus();
            ups.setCode(Constants.UPLOAD_FILE_STATUS_UPLOADING);
            ups.setFileUUID(fileNameUUID.toString());
            ups.setMsg(Constants.UPLOAD_FILE_MAP_STATUS.get(Constants.UPLOAD_FILE_STATUS_UPLOADING));
            jedisClient.valueOps().setObject(fileNameUUID.toString().getBytes(), ups);
            String ext = Constants.getPostfix(originFileName);
            newFileName = fileNameUUID + Constants.POINT + ext;
            newFile = new File(filePath + File.separator + type, newFileName);
            FileUtils.copyInputStreamToFile(is, newFile);
            uploadFile = new UploadFile();
            uploadFile.setFileNamePrefix(fileNameUUID.toString());
            uploadFile.setFileName(newFileName);
            uploadFile.setFileType(file.getContentType()==null?ext:file.getContentType());
            uploadFile.setFileSize(file.getSize());
            uploadFile.setRelativePath(filePath + File.separator + type + File.separator);
            uploadFile.setAbsolutePath(filePath + File.separator + type + File.separator + newFileName);
            uploadFile.setCreator(userId);
            uploadFile.setCreateTm(new Date());
        } catch (IOException e) {
        	logger.error(e.getMessage());
            e.printStackTrace();
        }
        return uploadFile;
    }

	public String getSharePath() {
		return sharePath;
	}

	public void setSharePath(String sharePath) {
		this.sharePath = sharePath;
	}
}
