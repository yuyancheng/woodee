package com.sf.kh.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/***
 * 文件上传状态类
 * @author 866316
 *
 */
public class UploadFileStatus implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String msg;//返回到前端的消息
	
	/***
	 * 只能来自Constants类的几种状态
	 * 	public static final Integer UPLOAD_FILE_STATUS_UPLOADING = 1;
	 *	public static final Integer UPLOAD_FILE_STATUS_WRITING = 2;
	 *	public static final Integer UPLOAD_FILE_STATUS_SUCCESS = 3;
	 *	public static final Integer UPLOAD_FILE_STATUS_ERROR = 4;
	 */
	private Integer code;
	
	private String fileUUID;//文件uuid
	
	/***
	 * 每条记录的格式：  "字段1|字段2|..字段N,原因"
	 * "a|b|c,没有匹配到部门信息"
	 */
	private List<String> errorList = new ArrayList<String>();//格式校验不通过的记录,返回给前端展示
	
	private Integer numbersOfSuccess = 0 ;//成功记录的条数
	
	private Integer numbersOfError = 0 ;//错误记录的条数

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getFileUUID() {
		return fileUUID;
	}

	public void setFileUUID(String fileUUID) {
		this.fileUUID = fileUUID;
	}

	public List<String> getErrorList() {
		if(null == this.errorList){
			errorList = new ArrayList<String>();
		}
		return errorList;
	}

	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}

	public Integer getNumbersOfSuccess() {	
		return numbersOfSuccess;
	}

	public void setNumbersOfSuccess(Integer numbersOfSuccess) {
		this.numbersOfSuccess = numbersOfSuccess;
	}

	public Integer getNumbersOfError() {
		return numbersOfError;
	}

	public void setNumbersOfError(Integer numbersOfError) {
		
		this.numbersOfError = numbersOfError;
	}
}