package com.sf.kh.service;

import java.io.File;
import java.util.Date;

import com.sf.kh.model.UploadFileStatus;

public interface IWaybillService{

	UploadFileStatus uploadFile(Date batchTime,File file,String fileName,String contentType,Long userId);
	
	void executeProc(String batchTime);
}
