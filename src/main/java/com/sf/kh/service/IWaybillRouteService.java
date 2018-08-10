package com.sf.kh.service;

import java.io.File;
import java.util.List;

import com.sf.kh.model.UploadFileStatus;
import com.sf.kh.model.WaybillRoute;

public interface IWaybillRouteService{
	
	UploadFileStatus uploadFile(File file,String fileName,String contentType,Long userId);

	List<WaybillRoute> getByWaybillNo(String waybillNo);
}
