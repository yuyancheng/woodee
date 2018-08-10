package com.sf.kh.service;

import com.sf.kh.model.UploadFile;

/***
 * 上传文件接口类
 * @author 866316
 *
 */
public interface IUploadFileService {
	/***
	 * 上传文件业务逻辑处理
	 * @param userId:上传用户
	 * @param uploadFile：上传文件内容
	 */
	void dealImportFile(Long userId,UploadFile uploadFile);
}
