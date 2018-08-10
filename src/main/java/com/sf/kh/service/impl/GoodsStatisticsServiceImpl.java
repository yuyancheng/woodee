package com.sf.kh.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.Result;

import com.sf.kh.dao.ICategoryStatisticsVersionDataDao;
import com.sf.kh.dao.IDepartmentStatisticsVersionDataDao;
import com.sf.kh.dao.IGoodsStatisticsDetailDtoDao;
import com.sf.kh.dao.IOverviewVersionDataDao;
import com.sf.kh.dao.ITimeStatisticsVersionDataDao;
import com.sf.kh.dto.CategoryStatisticsDto;
import com.sf.kh.dto.DepartmentStatisticsDto;
import com.sf.kh.dto.GoodsStatisticsDetailDto;
import com.sf.kh.dto.OverviewVersionDataDto;
import com.sf.kh.dto.TimeStatisticsDto;
import com.sf.kh.model.CategoryStatisticsVersionData;
import com.sf.kh.model.DepartmentStatisticsVersionData;
import com.sf.kh.model.Department;
import com.sf.kh.model.OverviewVersionData;
import com.sf.kh.model.TimeStatisticsVersionData;
import com.sf.kh.service.IDepartmentService;
import com.sf.kh.service.IGoodsStatisticsService;
/***
 * 物资统计
 * @author 866316
 *
 */
@Service
public class GoodsStatisticsServiceImpl implements IGoodsStatisticsService{
	
	private static final Logger logger = LoggerFactory.getLogger(GoodsStatisticsServiceImpl.class);

	private @Resource IOverviewVersionDataDao overviewVersionDataDao;
	
	private @Resource IDepartmentStatisticsVersionDataDao departmentStatisticsVersionDataDao;
	
	private @Resource ICategoryStatisticsVersionDataDao categoryStatisticsVersionDataDao;
	
	private @Resource ITimeStatisticsVersionDataDao timeStatisticsVersionDataDao;
	
	private @Resource IGoodsStatisticsDetailDtoDao goodsStatisticsDetailDtoDao;
	
	private @Resource IDepartmentService departmentService;

	@Override
	public Result<OverviewVersionDataDto> getOverviewVersionData(
			String custNo,Department myDept,List<Department> deptList) {
		List<String> companyIds = new ArrayList<String>();
		String deptName = null;
		for(Department dept: deptList){
			companyIds.add(String.valueOf(dept.getId()));
			if(!"全部".equals(custNo) && dept.getCustNo().equals(custNo)){
				deptName = dept.getDeptName();
			}
		}
		List<OverviewVersionData> sendResult = overviewVersionDataDao.getListByCompanyIdsAndType(
				companyIds, "1");
		
		List<OverviewVersionData> receiveResult = overviewVersionDataDao.getListByCompanyIdsAndType(
				companyIds, "2");
		OverviewVersionDataDto ovdd = new OverviewVersionDataDto();
		for(OverviewVersionData send : sendResult){
			ovdd.setSendCount(ovdd.getSendCount()+send.getAmount());
			ovdd.setSendCubage(ovdd.getSendCubage()+send.getCubage());
			ovdd.setSendWeight(ovdd.getSendWeight()+send.getWeight());
			ovdd.setDate(send.getDate());
		}
		for(OverviewVersionData receive : receiveResult){
			ovdd.setReceiveCount(ovdd.getReceiveCount()+receive.getAmount());
			ovdd.setReceiveCubage(ovdd.getReceiveCubage()+receive.getCubage());
			ovdd.setReceiveWeight(ovdd.getReceiveWeight()+receive.getWeight());
		}
		//格式化
		ovdd.setSendCubage(this.transfer(ovdd.getSendCubage()));
		ovdd.setSendWeight(this.transfer(ovdd.getSendWeight()));
		ovdd.setReceiveCubage(this.transfer(ovdd.getReceiveCubage()));
		ovdd.setReceiveWeight(this.transfer(ovdd.getReceiveWeight()));
		if("全部".equals(custNo)){//所有月结的数据
			ovdd.setDeptName(myDept.getDeptName());
		}else{//具体月结的数据
			ovdd.setDeptName(deptName);
		}
		return Result.success(ovdd);
	}
	
	private double transfer(Double f){
		if(null == f){
			return 0;
		}
		BigDecimal bg = new BigDecimal(f);  
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
	}

	@Override
	public Result<Map<String,List<DepartmentStatisticsDto>>> getDepartmentStatisticsData(List<Department> deptList) {
		List<Long> deptNos = new ArrayList<Long>();
		for(Department temp : deptList){
			deptNos.add(temp.getId());
		}
		//1，获取发货物资数据
		List<DepartmentStatisticsVersionData> sendList = 
				departmentStatisticsVersionDataDao.selectByDeptNosAndType(deptNos, "1");
		//2,获取收运物资数据
		List<DepartmentStatisticsVersionData> receiveList = 
				departmentStatisticsVersionDataDao.selectByDeptNosAndType(deptNos, "2");
		//加工收运物资数据统计
		Map<String, List<DepartmentStatisticsDto>> resultMap= new HashMap<String,List<DepartmentStatisticsDto>>();
		resultMap.put("send", this.processDeptData("send",sendList));
		resultMap.put("receive", this.processDeptData("receive",receiveList));
		return Result.success(resultMap);
	}
	
	private List<DepartmentStatisticsDto> processDeptData(String type,List<DepartmentStatisticsVersionData> list){
		Map<String,DepartmentStatisticsDto> deptMap = new HashMap<String,DepartmentStatisticsDto>();
		//加工发运物资数据统计
		for(DepartmentStatisticsVersionData dvsd : list){
			//根据部门id，获取顶级部门id
			Department tempDept;
			if("send".equals(type)){
				tempDept = departmentService.getDeptWithHierarchicalOrgById(dvsd.getReceiveDeptId());
			}else{
				tempDept = departmentService.getDeptWithHierarchicalOrgById(dvsd.getSendDeptId());
			}
			if(null == tempDept){
				continue;
			}
			String statisticName = tempDept.getBiRelName();
			if(null == statisticName){
				continue;
			}
			DepartmentStatisticsDto dsd = deptMap.get(statisticName);
			if(null == dsd){
				dsd = new DepartmentStatisticsDto();
				dsd.setDeptName(statisticName);
			}
			dsd.setDeptAmount(dsd.getDeptAmount()+dvsd.getAmount());
			deptMap.put(statisticName, dsd);
		}
		int count = 0;//总量
		for (Map.Entry<String, DepartmentStatisticsDto> entry : deptMap.entrySet()) { 
			count += entry.getValue().getDeptAmount();
		}
		if(0 == count){//没有统计数据
			return new ArrayList<DepartmentStatisticsDto>();
		}
		List<DepartmentStatisticsDto> result = new ArrayList<DepartmentStatisticsDto>();
		for (Map.Entry<String, DepartmentStatisticsDto> entry : deptMap.entrySet()) { 
			DepartmentStatisticsDto dto = entry.getValue();
			BigDecimal bg = new BigDecimal(dto.getDeptAmount()/count);
			double temp = bg.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();//率
			dto.setDeptRate(temp);
			result.add(dto);
		}
		return result;
	}

	@Override
	public Result<List<CategoryStatisticsDto>> getCategoryStatisticsData(List<Department> deptList) {
		List<Long> deptNos = new ArrayList<Long>();
		for(Department temp : deptList){
			deptNos.add(temp.getId());
		}
		List<CategoryStatisticsVersionData> sendList = 
				categoryStatisticsVersionDataDao.getListByDeptNosAndType(deptNos, "1");
		List<CategoryStatisticsVersionData> receiveList = 
				categoryStatisticsVersionDataDao.getListByDeptNosAndType(deptNos, "2");
		
		Map<String,CategoryStatisticsDto> categoryMap = new HashMap<String,CategoryStatisticsDto>();
		for(CategoryStatisticsVersionData csvd : sendList){
			CategoryStatisticsDto temp = categoryMap.get(csvd.getCategoryName());
			if(null == temp){
				temp = new CategoryStatisticsDto();
				temp.setCategoryName(csvd.getCategoryName());
			}
			temp.setSendAmount(temp.getSendAmount()+csvd.getAmount());
			categoryMap.put(csvd.getCategoryName(), temp);
		}
		for(CategoryStatisticsVersionData csvd : receiveList){
			CategoryStatisticsDto temp = categoryMap.get(csvd.getCategoryName());
			if(null == temp){
				temp = new CategoryStatisticsDto();
				temp.setCategoryName(csvd.getCategoryName());
			}
			temp.setReceiveAmount(temp.getReceiveAmount()+csvd.getAmount());
			categoryMap.put(csvd.getCategoryName(), temp);
		}
		List<CategoryStatisticsDto> result = new ArrayList<CategoryStatisticsDto>();
		
		for (Map.Entry<String, CategoryStatisticsDto> entry : categoryMap.entrySet()) { 
			result.add(entry.getValue());
		}
		return Result.success(result);
	}

	@Override
	public Result<List<TimeStatisticsDto>> getTimeStatisticsVersionData(List<Department> deptList) {
		List<Long> deptNos = new ArrayList<Long>();
		for(Department temp : deptList){
			deptNos.add(temp.getId());
		}
		
		List<TimeStatisticsVersionData> sendList = timeStatisticsVersionDataDao.getListByDeptNosAndType(
				deptNos, "1");
		List<TimeStatisticsVersionData> receiveList = timeStatisticsVersionDataDao.getListByDeptNosAndType(
				deptNos, "2");
		Map<String, TimeStatisticsDto> timeMap = new HashMap<String,TimeStatisticsDto>();
		for(TimeStatisticsVersionData tsvd : sendList){
			TimeStatisticsDto t = timeMap.get(tsvd.getMonth());
			if(null == t){
				t = new TimeStatisticsDto();
				t.setMonth(tsvd.getMonth());
			}
			t.setSendAmount(t.getSendAmount()+tsvd.getAmount());
			timeMap.put(tsvd.getMonth(), t);
		}
		for(TimeStatisticsVersionData tsvd : receiveList){
			TimeStatisticsDto t = timeMap.get(tsvd.getMonth());
			if(null == t){
				t = new TimeStatisticsDto();
				t.setMonth(tsvd.getMonth());
			}
			t.setReceiveAmount(t.getReceiveAmount()+tsvd.getAmount());
			timeMap.put(tsvd.getMonth(), t);
		}
		List<TimeStatisticsDto> result = new ArrayList<TimeStatisticsDto>();
		for (Map.Entry<String, TimeStatisticsDto> entry : timeMap.entrySet()) { 
			result.add(entry.getValue());
		}
		Collections.sort(result,new Comparator<TimeStatisticsDto>(){
			public int compare(TimeStatisticsDto t1,TimeStatisticsDto t2){
				return t1.getMonth().compareTo(t2.getMonth());
			}
		});
		return Result.success(result);
	}
	
	
	@Override
	public Result<Page<GoodsStatisticsDetailDto>> query4pageBySend(Map<String, Object> params) {
		return Result.success(goodsStatisticsDetailDtoDao.query4page(params));
	}
	
	@Override
	public Result<Page<GoodsStatisticsDetailDto>> query4pageByReceive(Map<String, Object> params) {
		return Result.success(goodsStatisticsDetailDtoDao.query4page(params));
	}
	
	
	@Override
	public File exportsGoodsList(String fileFolderPath,String type,Map<String, Object> params){
		String[] headers = new String[]{"物资编码","物资名称","专业","类别","单位","已发运","已签收","未签收","签收率"};
		if("2".equals(type)){
			headers = new String[]{"物资编码","物资名称","专业","类别","单位","已接收"};
		}
		UUID fileNameUUID = UUID.randomUUID();
		String fileName = fileFolderPath+File.separator+fileNameUUID+".csv";//设置文件名
		File csvFile = null;  
		BufferedWriterWrapper writer = null;
		try {
			csvFile = new File(fileName);  
			File parent = csvFile.getParentFile();  
			if(parent != null && !parent.exists()) {  
			      parent.mkdirs();  
			}
			csvFile.createNewFile();
			//创建输出流
			writer = new BufferedWriterWrapper(csvFile);
			StringBuilder sbTitle = new StringBuilder(8192);
			//1,添加csv文件列头
			if(null != headers){
				for(int i=0,len = headers.length;i<len;i++){
					sbTitle.append("\""+headers[i].toString()+"\",");
				}
				writer.write(sbTitle.toString());
			}
			if(null!= writer){
				writer.newLine();
                this.queryFromDbAndWrite(writer,type,params);
            }
            writer.flush();
            writer.close();
            writer = null;
		}catch(Exception e){
		    logger.error("导出文件异常原因：{}", e.getMessage());
		}
		return csvFile;
	}
	
	private void queryFromDbAndWrite(BufferedWriterWrapper writer,String type,Map<String,Object> params){
        try {
            StringBuilder sb = new StringBuilder(8192);
            List<GoodsStatisticsDetailDto> resultList = goodsStatisticsDetailDtoDao.exportAllList(params);
  
            int index = 0;
            for(int i=0,iSize=resultList.size();i<iSize;i++){
            	 GoodsStatisticsDetailDto temp = resultList.get(i);
            	 sb.append("\""+ifNullString(temp.getGoodsCode())+"\",\""+ifNullString(temp.getGoodsName())+"\",");
	        	 sb.append("\""+ifNullString(temp.getFirstCategoryName())+"\",\""+ifNullString(temp.getSecondCategoryName())+"\",");
	        	 sb.append("\""+ifNullString(temp.getGoodsUnit())+"\",");
	        	
            	 if("1".equals(type)){
            		 sb.append("\""+ifNullString(temp.getSendCount())+"\",\""+ifNullString(temp.getReceiveCount())+"\",");
            		 sb.append("\""+ifNullString(temp.getUnReceiveCount())+"\",\""+ifNullString(temp.getReceiveRate()+"%")+"\",\n");
            	 }else{
    	        	 sb.append("\""+ifNullString(temp.getReceiveCount())+"\",\n");
            	 }
	        	 index++;
	             if(index==200){
	                 writer.write(sb.toString());
	                 sb.setLength(0);
	                 index = 0;//又从0开始
	             }
            }
            /***
             * 存在两种情况：
             *      1，没有超过200的；
             *      2，曾经超出过200，但是剩下的不到200；需要插入剩余的
             */
        	if(sb.length()>0){
            	writer.write(sb.substring(0, sb.length()-1));
            }
            logger.info("导出文件成功");
        }catch (Exception e) {
            logger.error("导出文件异常原因：{}", e.getMessage());
        }
	}
	
	
	public static String ifNullString(Object obj){
		if(null==obj){
			return "";
		}else{
			String val = String.valueOf(obj);
			if(val.indexOf("\"")>=0){
				return val.replace("\"", "'");
			}else{
				return val;
			}
		} 
	}
    
    public static Object ifNull(Object obj){
		if(null == obj){
			return "";
		}else{
			return obj;
		}
	}
	
	private static class BufferedWriterWrapper {
        OutputStream outer;
        OutputStreamWriter writer;
        BufferedWriter buffer;
        BufferedWriterWrapper (File file) throws FileNotFoundException {
            this.outer = new FileOutputStream(file);
            this.writer = new OutputStreamWriter(outer, Charset.forName("GBK"));
            this.buffer = new BufferedWriter(writer, 8192);
        }
        
        public void write(String line) throws IOException {
            buffer.write(line);
        }
        
        public void flush() throws IOException {
            buffer.flush();
            writer.flush();
            outer.flush();
        }
        
        public void newLine() throws IOException {
            buffer.newLine();
        }
        public void close() {
            if (buffer != null) try {
                buffer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            buffer = null;
            
            if (writer != null) try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer = null;
            
            if (outer != null) try {
                outer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outer = null;
        }
    }
	
}
