package com.sf.kh.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import javax.servlet.http.HttpServletResponse;
import code.ponfee.commons.jedis.JedisClient;
import code.ponfee.commons.util.SpringContextHolder;
import com.google.common.collect.ImmutableMap;
import com.sf.kh.model.UploadFileStatus;


public final class Constants{
	
	/**包裹类型*/
	public static final String PACKAGE_SINGLE = "1"; // 包裹单一类型
    
	public static final String PACKAGE_MIX = "2"; // 包裹混合类型
	
    public static final int ONE_ROW_AFFECTED = 1;
	
	//字典表中，物资管理关联的专业、类别字段的类型
	public static final String BASE_DICT_GOODS_TYPE = "1";
    
	public static final String POINT = ".";
	
	public static final String EMPTY = "";
	
	public final static String NEW_LINE_SEPARATOR="\n";

	public static final String PATH_SEPARATOR = ":";
	
	public static final String ROLE_GENERAL = "GENERAL"; // 普通角色
	public static final String ROLE_MANAGER = "MANAGER"; // 管理员角色

	public static final String VALID = "valid";

	public static final String PAGE_NUM = "pageNum";
	public static final String PAGE_SIZE = "pageSize";


	//文件状态
	public static final Integer UPLOAD_FILE_STATUS_UPLOADING = 1;
	public static final Integer UPLOAD_FILE_STATUS_WRITING = 2;
	public static final Integer UPLOAD_FILE_STATUS_SUCCESS = 3;
	public static final Integer UPLOAD_FILE_STATUS_ERROR = 4;
	public static final Map<Integer, String> UPLOAD_FILE_MAP_STATUS = new ImmutableMap.Builder<Integer, String>()
					            .put(Constants.UPLOAD_FILE_STATUS_UPLOADING, "正在上传文件")
					            .put(Constants.UPLOAD_FILE_STATUS_WRITING, "正在读写数据")
					            .put(Constants.UPLOAD_FILE_STATUS_SUCCESS, "文件导入成功")
					            .put(Constants.UPLOAD_FILE_STATUS_ERROR, "文件导入失败").build();
   
    
    public static String getPackageType(String type){
    	if(PACKAGE_SINGLE.equals(type)){
    		return "单品";
    	}else{
    		return "混合";
    	}
    }
    
    public static String getPostfix(String path) {
        if (path == null || Constants.EMPTY.equals(path.trim())) {
            return Constants.EMPTY;
        }
        if (path.contains(Constants.POINT)) {
            return path.substring(path.lastIndexOf(Constants.POINT) + 1, path.length());
        }
        return Constants.EMPTY;
    }

	/***
	 * 导出文件
	 * @param response
	 * @param fileName：导出给用户展示的文件名
	 * @param file：导出给用户的文件
	 * @throws IOException
	 */
	public static void exportFile(HttpServletResponse response, String fileName,File file) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		GZIPOutputStream gout = new GZIPOutputStream(bout);

		try(InputStream in = new FileInputStream(file) ){
			byte[] factByte = new byte[in.available()];
			in.read(factByte);//流读取到数组里面
			gout.write(factByte);//写到压缩流里面
			gout.close();//关闭压缩流
			byte gzip[] = bout.toByteArray();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
			response.setHeader("content-encoding", "gzip");
			response.setHeader("Content-Length", gzip.length + "");
			response.setHeader("Set-Cookie", "fileDownload=true; path=/");
			response.setCharacterEncoding("UTF-8");
			response.getOutputStream().write(gzip);
		}catch(Exception e){
			throw e;
		}
	}

    /***
     * 更新上传文件状态
     * @param fileUUID
     * @param code
     * @param message
     */
    public static void updateUploadStatus(byte[] fileUUID,Integer code,String message,Integer numbersOfSuccess,
    		Integer numbersOfError,List<String> errorList){
 		UploadFileStatus ufs = SpringContextHolder.getBean(JedisClient.class).valueOps().getObject(
 				fileUUID, UploadFileStatus.class);
 		if(null == message){
 			message = Constants.UPLOAD_FILE_MAP_STATUS.get(code);
 			if(null == message){
 				message = "";
 			}
 		}
 		if(null != code){
 			ufs.setCode(code);
 		}
 		ufs.setNumbersOfSuccess(null == numbersOfSuccess?0:numbersOfSuccess);
 		ufs.setNumbersOfError(null == numbersOfError?0:numbersOfError);
 		ufs.setErrorList(errorList);
 		SpringContextHolder.getBean(JedisClient.class).valueOps().setObject(fileUUID, ufs);
     }

}
