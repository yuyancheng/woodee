package com.sf.kh.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageRequestParams;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import code.ponfee.commons.util.ZipUtils;

import com.alibaba.fastjson.JSON;
import com.sf.kh.model.ImportLog;
import com.sf.kh.model.UploadFileStatus;
import com.sf.kh.model.User;
import com.sf.kh.service.IBaseGoodsService;
import com.sf.kh.service.IImportLogService;
import com.sf.kh.service.IWaybillRouteService;
import com.sf.kh.service.IWaybillService;
import com.sf.kh.util.Constants;
import com.sf.kh.util.DateUtil;
import com.sf.kh.util.WebContextHolder;

/***
 * 灯塔数据导入
 * @author 866316
 */
@RestController
@RequestMapping(path = "despatchData")
public class DespatchDataController {
	private static final Logger logger = LoggerFactory.getLogger(DespatchDataController.class);
	
	private @Resource IBaseGoodsService baseGoodsService;
	
	private @Resource IWaybillService waybillService;
	
	private @Resource IWaybillRouteService waybillRouteService;
	
	private @Resource IImportLogService importLogService;
	
	@Value("${sharePath}")
	private String sharePath;
	
	/***
	 * 上传所有数据:里面又有压缩文件,包含：waybill.zip【运单数据】 和 route.zip【路由数据】
	 * @param session
	 * @param file
	 */
	@PostMapping(path = "importData")
	public Result<UploadFileStatus> importData(HttpSession session,
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
		//获取更新批次时间
		Calendar cal = Calendar.getInstance();
		Date batchUpdateDate = cal.getTime();
		String  batchUpdateDateStr = DateUtil.formatDateYmDhMs(batchUpdateDate);
		
		//添加日期路径
		filePath = filePath+ "dengta" + File.separator+ DateUtil.formatDate(new Date());
		UUID fileNameUUID = UUID.randomUUID();
        try {
            InputStream is = file.getInputStream();
            String originFileName = file.getOriginalFilename();
            
            //获取文件扩展名
            String ext = Constants.getPostfix(originFileName);
            //文件名字
            String newFileName = fileNameUUID + Constants.POINT + ext;
            //存储文件
            File newFile = new File(filePath, newFileName);
            if(newFile.length()>10485760){//10MB
            	return Result.failure(ResultCode.BAD_REQUEST,"zip压缩文件超过10MB，请联系管理员!");
            }
            FileUtils.copyInputStreamToFile(is, newFile);
            List<UploadFileStatus> resultList = new ArrayList<UploadFileStatus>();
            if(!"zip".equals(ext)){
            	return Result.failure(ResultCode.BAD_REQUEST,"不是zip压缩文件!");
            }
        	try {
				ZipUtils.unzip(filePath+File.separator+newFileName);
				File folder = new File(filePath+File.separator+fileNameUUID);
				boolean isConformRequire = true;//是否符合格式要求
				//是一个文件夹
				if(folder.isDirectory()){
					String[] fileList = folder.list();//又包含两个zip文件
					for(int i =0;i<fileList.length;i++){
						File readFile = new File(filePath+File.separator+fileNameUUID+File.separator + fileList[i]);
						String readFileName = readFile.getName();
						if(readFileName.lastIndexOf(".")<0){
							isConformRequire = false;
							continue;
						}
						String readFilePre =  readFileName.substring(0,readFileName.lastIndexOf("."));
						String readFilePreExt = readFileName.substring(readFileName.lastIndexOf(".")+1);
						if(!"zip".equals(readFilePreExt)){//里面不是2个压缩文件继续跳出
							isConformRequire = false;
							continue;
						}
						String sonFolderRelativePath = filePath+File.separator+fileNameUUID;
						ZipUtils.unzip(sonFolderRelativePath+File.separator+readFileName);
						File sonFolder = new File(sonFolderRelativePath+File.separator+readFilePre);
						if(!sonFolder.isDirectory()){
							isConformRequire = false;
							continue;
						}
						String[] sonFileList = sonFolder.list();//运单数据
						String factFolderRelativePath = sonFolderRelativePath + File.separator+readFilePre;
						for(int j =0;j<sonFileList.length;j++){
    						File sonReadFile = new File(factFolderRelativePath + File.separator + sonFileList[j]);
    						if(sonFolder.getName().equals("waybill")){
    							logger.info("导入运单文件成功，具体路径：{}"+sonReadFile.getAbsolutePath());
    							resultList.add(waybillService.uploadFile(batchUpdateDate,sonReadFile, sonFileList[j], "text/plain", userId));
    						}else if(sonFolder.getName().equals("route")){
    							logger.info("导入路由文件成功，具体路径：{}"+sonReadFile.getAbsolutePath());
    							resultList.add(waybillRouteService.uploadFile(sonReadFile, sonFileList[j], "text/plain", userId));
    						}else{
    							isConformRequire = false;
    							break;
    						}
    					}
					}
				}
				if(false == isConformRequire){
					return Result.failure(ResultCode.BAD_REQUEST,"zip压缩文件内容不符合规范要求!");
				}
			} catch (ZipException e) {
				logger.error("上传物资记录异常原因-文件解压失败原因如下{}",e);
				ImportLog log = new ImportLog();
	            log.setFileKey(fileNameUUID.toString());
	            log.setImportFileName(file.getOriginalFilename());
	            log.setImportTime(DateUtil.formatDateYmDhMs(new Date()));
	            log.setType(1);
	            log.setStatus(0);
	            log.setUserId(String.valueOf(userId));
	            UploadFileStatus resultUFS = new UploadFileStatus();
	            String error = e.getMessage();
	            if(error.length()>500){
	            	error = error.substring(0, 500);
	            }
	            resultUFS.setMsg("上传物资记录异常原因-文件解压失败原因如下:"+error);
	            log.setMessage(JSON.toJSONString(resultUFS));
	            importLogService.insert(log);
	            return Result.failure(ResultCode.SERVER_ERROR,e.getMessage());
			}
        	UploadFileStatus resultUFS = new UploadFileStatus();
        	if(resultList.size() >0){//有插入或更新内容才执行存储过程
                for(UploadFileStatus rufs: resultList){
                	if(null != rufs.getErrorList()){
                		resultUFS.getErrorList().addAll(rufs.getErrorList());
                		resultUFS.setNumbersOfSuccess(resultUFS.getNumbersOfSuccess()+rufs.getNumbersOfSuccess());
                		resultUFS.setNumbersOfError(resultUFS.getNumbersOfError()+rufs.getNumbersOfError());
                	}
                }
                resultUFS.setNumbersOfSuccess(resultUFS.getNumbersOfSuccess());
                waybillService.executeProc(batchUpdateDateStr);
                logger.info("执行三个存储过程成功，批次时间是：{}",batchUpdateDateStr);
        	}else{
        		resultUFS.getErrorList().add("此次上传未更新或插入任何数据!");
        	}
            ImportLog log = new ImportLog();
            log.setFileKey(fileNameUUID.toString());
            log.setImportFileName(file.getOriginalFilename());
            log.setImportTime(DateUtil.formatDateYmDhMs(new Date()));
            log.setType(1);
            log.setStatus(1);
            log.setUserId(String.valueOf(userId));
            UploadFileStatus saveResult = new UploadFileStatus();
            saveResult.setNumbersOfError(resultUFS.getNumbersOfError());
            saveResult.setNumbersOfSuccess(resultUFS.getNumbersOfSuccess());
            //只存储50条记录到日志数据库，但全部都打印到日志文件中
            if(resultUFS.getErrorList().size()>50){
            	logger.info("打印超过50条插入或更新数据导致的错误原因--------开始-------");
            	for(int i=0,iSize = resultUFS.getErrorList().size();i<iSize;i++){
            		 logger.info(resultUFS.getErrorList().get(i));
            	}
            	logger.info("打印超过50条插入或更新数据导致的错误原因--------结束-------");
            	saveResult.setErrorList(resultUFS.getErrorList().subList(0, 50));
            }else{
            	saveResult.setErrorList(resultUFS.getErrorList());
            }
            log.setMessage(JSON.toJSONString(saveResult));
            importLogService.insert(log);
    		return Result.success(resultUFS);
        } catch (IOException e) {
        	ImportLog log = new ImportLog();
            log.setFileKey(fileNameUUID.toString());
            log.setImportFileName(file.getOriginalFilename());
            log.setImportTime(DateUtil.formatDateYmDhMs(new Date()));
            log.setType(1);
            log.setStatus(0);
            log.setUserId(String.valueOf(userId));
            UploadFileStatus resultUFS = new UploadFileStatus();
            String error = e.getMessage();
            if(error.length()>500){
            	error = error.substring(0,500);
            }
            resultUFS.setMsg("上传物资记录异常原因:"+error);
            log.setMessage(JSON.toJSONString(resultUFS));
            importLogService.insert(log);
        	logger.error("上传物资记录异常原因：{}",e);
            return Result.failure(ResultCode.SERVER_ERROR,e.getMessage());
        }
	}
	/***
	 * 获取导入的日志内容
	 */
	@GetMapping(path = "logList")
	public Result<Page<ImportLog>> getImportDataLog(HttpSession session,PageRequestParams params){
		params.put("userId", (Object)WebContextHolder.currentUser().getId());
		params.put("type", "1");//代表发运物资的导入
        return importLogService.query4page(params.getParams());
	}
}
