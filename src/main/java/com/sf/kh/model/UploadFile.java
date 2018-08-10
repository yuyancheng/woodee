package com.sf.kh.model;

import java.io.Serializable;
import java.util.Date;

public class UploadFile implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String fileNamePrefix;//文件名不带扩展名

	private String fileName;//文件名
    
	private Long fileSize;//文件大小
    
	private String fileType;//文件类型
	
	private String relativePath;//文件相对路径
   
	private String absolutePath;//文件绝对路径
    
	private Long creator;//上传者
	
	private Date createTm;//创建时间
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public Long getCreator() {
		return creator;
	}
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	public Date getCreateTm() {
		return createTm;
	}
	public void setCreateTm(Date createTm) {
		this.createTm = createTm;
	}
	public String getRelativePath() {
		return relativePath;
	}
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}
	public String getAbsolutePath() {
		return absolutePath;
	}
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	public String getFileNamePrefix() {
		return fileNamePrefix;
	}
	public void setFileNamePrefix(String fileNamePrefix) {
		this.fileNamePrefix = fileNamePrefix;
	}
}
