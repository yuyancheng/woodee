package com.sf.kh.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import code.ponfee.commons.extract.DataExtractor;
import code.ponfee.commons.extract.DataExtractorBuilder;
import code.ponfee.commons.extract.FileTooBigException;
import code.ponfee.commons.extract.ProcessResult;
import com.sf.kh.dao.IWaybillRouteDao;
import com.sf.kh.model.UploadFileStatus;
import com.sf.kh.model.WaybillRoute;
import com.sf.kh.service.IWaybillRouteService;
/***
 * 运单数据导入
 * @author 866316
 *
 */
@Service
public class WaybillRouteServiceImpl implements IWaybillRouteService{
	
	private static final Logger logger = LoggerFactory.getLogger(WaybillRouteServiceImpl.class);
	
	private @Resource IWaybillRouteDao waybillRouteDao;

	@Override
	public UploadFileStatus uploadFile(File file, String fileName,
			String contentType, Long userId) {
		//1,标题
		String[] headers = new String[]{"运单号","日期描述", "路由描述", "巴枪日期", "快件状态"};
		//2,获取文件流
		try {
			InputStream fileInputStream = new FileInputStream(file);
			String text = IOUtils.toString(fileInputStream, "GBK");
			fileInputStream = new ByteArrayInputStream(text.getBytes("UTF-8"));
			DataExtractor<String[]> extractor = DataExtractorBuilder.newBuilder(
					fileInputStream, fileName, contentType, headers
			    ).build();
			List<WaybillRoute> waybillRouteList = new ArrayList<WaybillRoute>();
			Map<String,Boolean> deleteWaybillNos = new HashMap<String,Boolean>();
			
			ProcessResult<String[]> process = extractor.filter((rowNumber, data) -> {
				if(rowNumber == 0){//第一行列头过滤
					return null;
				}
				String[] array = data;
				WaybillRoute waybillRoute = new WaybillRoute();
				String waybillNo = array[0];
				if(null == deleteWaybillNos.get(waybillNo)){
					deleteWaybillNos.put(waybillNo, true);
				}
				waybillRoute.setWaybillNo(waybillNo);
				waybillRoute.setRouteDate(array[1]);
				waybillRoute.setRouteDescribe(array[2]);
				try {
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(Long.valueOf(array[3]));
					SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					waybillRoute.setBarDate(df1.format(cal.getTime()));
				} catch (NumberFormatException e) {
					logger.info("运单号为{}的路由的巴枪日期转换失败",waybillNo);
					return waybillNo+"|"+array[1]+"|"+array[2]+"|"+array[3]+
							"|"+array[4]+",此运单巴枪日期转换异常";
				}
				short statusType = 0;
				if("已签收".equals(array[4])){
					statusType = 1;
				}
				waybillRoute.setWaybilStatus(statusType);
				waybillRouteList.add(waybillRoute);
				return null;
			});
			//当前已签收的运单
			List<String> signWaybillNos = waybillRouteDao.getSignWaybillsByRecent15Days();
			//当前需要删除的单
			List<String> mapKeyList = new ArrayList<String>(deleteWaybillNos.keySet()); 
			//真实需要删除的单
			List<String> needDeleteWaybill = new ArrayList<String>();
			for(String mk : mapKeyList){
				//这个单不在已签收范围内，代表是需要是需要更新的单
				if(!signWaybillNos.contains(mk)){
					needDeleteWaybill.add(mk);
				}
			}
			if(needDeleteWaybill.size()>0){//删除真实需要删除的单号
				waybillRouteDao.deleteByWaybillNo(needDeleteWaybill);
			}
			
			//真实需要插入的单号
			List<WaybillRoute> realSaveOrUpdateList = new ArrayList<WaybillRoute>();
			for(WaybillRoute wr :waybillRouteList){
				//这个单不在已签收范围内，是需要新增的
				if(!signWaybillNos.contains(wr.getWaybillNo())){
					realSaveOrUpdateList.add(wr);
				}
			}
			//统一新增
			int length = 500;
			while(realSaveOrUpdateList.size()>0){
				if(realSaveOrUpdateList.size()<500){
					length = realSaveOrUpdateList.size();
				}
				List<WaybillRoute> subList = realSaveOrUpdateList.subList(0, length);
				waybillRouteDao.saveList(subList);
				realSaveOrUpdateList.subList(0, length).clear();
			}
			UploadFileStatus  ufs = new UploadFileStatus();
			ufs.setErrorList(process.getErrors());
			ufs.setNumbersOfError(process.getErrors().size());
			ufs.setNumbersOfSuccess(process.getData().size()-1);
			return ufs;
		} catch (FileTooBigException e) {
			logger.error("{}",e);
		} catch (IOException e) {
			logger.error("{}",e);
		} catch (Exception e) {
			logger.error("{}",e);
		}
		UploadFileStatus  ufs = new UploadFileStatus();
		ufs.setNumbersOfError(0);
		ufs.setNumbersOfSuccess(0);
		return ufs;
	}

	@Override
	public List<WaybillRoute> getByWaybillNo(String waybillNo) {

		List<WaybillRoute> list = null;
		if(StringUtils.isNotBlank(waybillNo)){
			list = waybillRouteDao.getByWaybillNo(waybillNo);
		}

		return list == null ? Lists.newArrayList() : list;
	}
}
