package com.sf.kh.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import code.ponfee.commons.extract.DataExtractor;
import code.ponfee.commons.extract.DataExtractorBuilder;
import code.ponfee.commons.extract.FileTooBigException;
import code.ponfee.commons.extract.ProcessResult;

import com.sf.kh.dao.IDespatchBatchDao;
import com.sf.kh.dao.IScreenStatisticsDao;
import com.sf.kh.dao.IWaybillDao;
import com.sf.kh.model.DespatchBatch;
import com.sf.kh.model.UploadFileStatus;
import com.sf.kh.model.Waybill;
import com.sf.kh.service.IWaybillService;
import com.sf.kh.util.DateUtil;
/***
 * 运单数据导入
 * @author 866316
 *
 */
@Service
public class WaybillServiceImpl implements IWaybillService{
	
	private static final Logger logger = LoggerFactory.getLogger(WaybillServiceImpl.class);
	
	private @Resource IWaybillDao waybillDao;
	
	private @Resource IDespatchBatchDao despatchBatchDao;
	
	private @Resource JdbcTemplate jdbcTemplate;
	
	private @Resource IScreenStatisticsDao screenStatisticsDao;
	
	@Override
	public UploadFileStatus uploadFile(Date batchTime,File file,String fileName,String contentType,Long userId){
		//1,标题
		String[] headers = new String[]{"运单号","客户卡号","寄件时间","付款方式","实际重量","寄方省份","寄方城市",
				"收方省份","收方城市","快件状态","异常时间","异常说明","签收时间","派送成功时长（H）","长-宽-高","拖寄物"};
		
		ProcessResult<String[]> process = null;
		try {
			//2,获取文件流
			InputStream fileInputStream = new FileInputStream(file);
			String text = IOUtils.toString(fileInputStream, "GBK");
			fileInputStream = new ByteArrayInputStream(text.getBytes("UTF-8"));
			DataExtractor<String[]> extractor = DataExtractorBuilder.newBuilder(
					fileInputStream, fileName, contentType, headers
			    ).build();
			
			List<Waybill> insertWaybillList = new ArrayList<Waybill>();
			
			List<DespatchBatch> batchList = new ArrayList<DespatchBatch>();

			process = extractor.filter((rowNumber, data) -> {
				if(rowNumber == 0){//第一行列头过滤
					return null;
				}
	            String[] array = removeEqual(data);
				Waybill waybill = new Waybill();
				waybill.setWaybillNo((array[0]).trim());waybill.setCustNo(array[1]);
				if(!StringUtils.isBlank(array[2])){
					waybill.setConsignedTime(array[2]);
					try {
						waybill.setConsignedDate(DateUtil.formatDate(this.parseStrDate(array[2])));
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("发运信息导入-运单号{}的寄件时间{}转换错误，原因如下：",array[0],array[2],e.getMessage());
						return "发运信息导入-运单号"+array[0]+"的寄件时间"+array[2]+"转换错误，原因如下："+e.getMessage();
					}
				}
				waybill.setPaymentType(array[3]);
				if(!StringUtils.isBlank(array[4])){
					waybill.setRealWeightQty(Double.parseDouble(array[4]));
				}else{
					waybill.setRealWeightQty(0.0);
				}
				waybill.setSenderProvince(array[5]);waybill.setSenderCity(array[6]);
				waybill.setDestProvince(array[7]);waybill.setDestCity(array[8]);
				waybill.setWaybillStatus(array[9]);
				if(!StringUtils.isBlank(array[10])){
					waybill.setExceptionTime(array[10]);
				}
				//异常说明特殊处理
				String exceptionRemark = array[11];
				if(!StringUtils.isBlank(exceptionRemark)){
					String[] result = exceptionRemark.split("/");
					if(result.length==3){
						String sendException = result[0];
						if("no".equals(sendException)){//没有派送异常,那么异常原因和异常时间都清空
							waybill.setExceptionDescribe(null);
							waybill.setExceptionTime(null);
						}else{//有派送异常，那么一定有派送异常时间
							waybill.setExceptionDescribe(sendException);
						}
						//超时未签收，超时无物流处理
						try{
							waybill.setNoSign(Integer.valueOf(result[1]));//为负数表示没有未签收路由
							waybill.setNoRoute(Integer.valueOf(result[2]));//为负数表示没有超时路由
						}catch(NumberFormatException nfe){
							logger.error("发运信息导入-运单号{}超时未签收或超时无物流转换异常，字段值是：{}，原因是：{}",array[0],exceptionRemark,nfe);
							return "发运信息导入-运单号"+array[0]+"超时未签收或超时无物流转换异常，字段值是："+exceptionRemark+"，原因是："+nfe.getMessage();
						}		
					}
				}
				if(!StringUtils.isBlank(array[12])){
					waybill.setSignInTime(array[12]);
					try {
						waybill.setSignInDate(DateUtil.formatDate(this.parseStrDate(array[12])));
					} catch (Exception e) {
						logger.error("发运信息导入-运单号{}签收时间转换异常，字段值是：{}，原因是：{}",array[0],array[12],e.getMessage());
						return "发运信息导入-运单号"+array[0]+"签收时间转换异常，字段值是："+array[12]+"，原因是："+e.getMessage();
					}
				}
				if(StringUtils.isBlank(array[13])){
					waybill.setDeliveryDuration("0");
				}else{
					waybill.setDeliveryDuration(array[13]);
				}

				String curbage = array[14];
				if(!StringUtils.isBlank(curbage)){
					curbage = curbage.trim();
					waybill.setCubageDescribe(curbage);
					String[] arr = curbage.split("-");
					if(3 == arr.length){//长 *宽 *高
						waybill.setCubage(Double.parseDouble(arr[0])*Double.parseDouble(arr[1])*Double.parseDouble(arr[2]));
					}else{
						waybill.setCubage(0.0);
					}
				}else{
					waybill.setCubageDescribe("0");
					waybill.setCubage(0.0);
				}
				waybill.setTow(array[15]);
				//批次处理
				if(!StringUtils.isBlank(array[15])){
					DespatchBatch db = new DespatchBatch();
					db.setWaybillNo((array[0]).trim());
					String currentTow = (array[15]).trim();
					boolean isPureNumber=currentTow.matches("[0-9]+");
					if(true == isPureNumber){
						try{
							db.setBatchId(Long.parseLong(currentTow));
							db.setUpdateTm(DateUtil.formatDateYmDhMs(batchTime));
							batchList.add(db);
						}catch(NumberFormatException nfe){
							logger.error("运单号：{},拖寄物：{},拖寄物转换异常：{}",array[0],currentTow,nfe.getMessage());
							return "运单号："+array[0]+",拖寄物："+array[15]+",拖寄物不是数值转换失败!";
						}
					}else{
						logger.info("运单号：{}对应拖寄物的不能转换为纯数字内容：{};",array[0],currentTow);
					}
				}
				waybill.setCreateTm(DateUtil.formatDateYmDhMs(batchTime));
				//已签收就更新已签收记录
				if("已签收".equals(waybill.getWaybillStatus().trim())){
					waybill.setUpdateTm(DateUtil.formatDateYmDhMs(batchTime));
				}
				insertWaybillList.add(waybill);
				return null;
			});
			//统一新增或更新
			int length = 500;
			while(insertWaybillList.size()>0){
				if(insertWaybillList.size()<500){
					length = insertWaybillList.size();
				}
				List<Waybill> subList = insertWaybillList.subList(0, length);
				waybillDao.saveOrUpdateList(subList);
				insertWaybillList.subList(0, length).clear();
			}
			//批次号更新
			length = 500;
			while(batchList.size()>0){
				if(batchList.size()<500){
					length = batchList.size();
				}
				List<DespatchBatch> subList = batchList.subList(0, length);
				despatchBatchDao.updateWaybill(subList);
				batchList.subList(0, length).clear();
			}
			screenStatisticsDao.cleanCache();//清空大屏配置
		} catch (FileTooBigException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		if(null == process){
			return new UploadFileStatus();
		}
		UploadFileStatus  ufs = new UploadFileStatus();
		ufs.setErrorList(process.getErrors());
		ufs.setNumbersOfError(process.getErrors().size());
		ufs.setNumbersOfSuccess(process.getData().size()-1);
		return ufs;
	}
	
	private Date parseStrDate(String str){
		if(StringUtils.isBlank(str)){
			return null;
		}
		String[] strArr = str.split("/");
		SimpleDateFormat formatter = null;
		if(strArr.length>1){//用斜杠分割
			formatter = new SimpleDateFormat("yyyy/M/d HH:mm");
		}else{
			strArr = str.split("-");
			if(strArr.length>1){
				formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			}
		}
		if(null == formatter){
			return null;
		}
		try {
			return formatter.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			logger.error("发运物资-日期导入错误"+e.getMessage());
		}
		return null;
	}
	
	Pattern pattern = Pattern.compile("=\"(.*)\"");
	private String[] removeEqual(String[] arr){
		for(int i=0;i<arr.length;i++){
			Matcher mather = pattern.matcher(arr[i]);
			if(true == mather.find()){
				arr[i] = mather.group(1);
			}
		}
		return arr;
	}
	
	@Override
	public void executeProc(String batchUpdateDate){
		jdbcTemplate.execute("call prc_index_rank_history()");
		jdbcTemplate.execute("call prc_goods_stastics()");
		jdbcTemplate.execute("call prc_kh_delivery()");
	}
	
	public static void main(String[] args){
		String test  ="=\"abc\"";
		Pattern patterns = Pattern.compile("=\"(\\S*)\"");
		Matcher mather = patterns.matcher(test);
		System.out.println(mather.find()+", "+mather.group(1)+" xx");
		String exceptionRemark = "阿斯顿发动发动反/ns_5";
		String[] result = exceptionRemark.split("/");
		if(result.length==1){
			System.out.println(result[0]);
		}else{
			for(int i = 0;i<result.length;i++){
				if(i == 0){
					System.out.println(result[0]);
				}else{
					String temp = result[i];
					if(temp.indexOf("nr")>=0){//超时无路由
						temp = temp.substring(3);
						System.out.println(Integer.valueOf(temp));
					}else if(temp.indexOf("ns")>=0){//超时未签收
						temp = temp.substring(3);
						System.out.println(Integer.valueOf(temp));
					}
				}
			}
		}
	}
}
