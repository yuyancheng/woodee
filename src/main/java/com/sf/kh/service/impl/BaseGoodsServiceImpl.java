package com.sf.kh.service.impl;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sf.kh.dao.IBaseDictDao;
import com.sf.kh.dao.IBaseGoodsDao;
import com.sf.kh.dao.IGoodsDeptDao;
import com.sf.kh.model.BaseDict;
import com.sf.kh.model.BaseGoods;
import com.sf.kh.model.GoodsDept;
import com.sf.kh.model.Organization;
import com.sf.kh.model.UploadFileStatus;
import com.sf.kh.service.IBaseGoodsService;
import com.sf.kh.service.IOrganizationService;
import com.sf.kh.util.Constants;
import com.sf.kh.util.DateUtil;

import code.ponfee.commons.extract.DataExtractor;
import code.ponfee.commons.extract.DataExtractorBuilder;
import code.ponfee.commons.extract.FileTooBigException;
import code.ponfee.commons.extract.ProcessResult;
import code.ponfee.commons.jedis.JedisClient;
import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;

/***
 * 获取物资管理数据
 * @author 866316
 *
 */
@Service
public class BaseGoodsServiceImpl implements IBaseGoodsService {
	
	private static final Logger logger = LoggerFactory.getLogger(BaseGoodsServiceImpl.class);

    private @Resource IBaseGoodsDao baseGoodsDao;
    
    private @Resource IBaseDictDao baseDictDao;
    
    private @Resource IGoodsDeptDao goodsDeptDao;
   
    private @Resource IOrganizationService organizationServiceImpl;
    
    private @Resource JedisClient jedisClient;
    
    /***
     * 根据任何id获取对应的数据
     * @param gd
     * @return
     */
    @Override
    public List<GoodsDept> getGoodsDept(GoodsDept gd){
    	return goodsDeptDao.getGoodsDeptId(gd);
    }
    
    /***
     * 根据物资id集合，获取物资信息，并补全分类信息
     * @param ids
     * @return
     */
    public List<BaseGoods> getListByIds(List<Long> ids){
    	List<BaseGoods> goodsList = baseGoodsDao.getBaseGoodsByIds(ids);
    	Map<Long,BaseDict> baseDictMap = baseDictDao.getMapByType(Constants.BASE_DICT_GOODS_TYPE);
    	for(int i =0 ;i<goodsList.size();i++){
    		BaseGoods bg = goodsList.get(i);
    		GoodsDept goodsDept = goodsDeptDao.getGoodsDeptById(bg.getGoodsDeptId());
			if(null == goodsDept){
				continue;
			}
			BaseDict bd = baseDictMap.get(goodsDept.getCategoryId());
			if(null == bd.getParentId()){
				bg.setFirstCategoryId(bd.getId());
				bg.setFirstCategoryName(bd.getName());
				bg.setSecondCategoryId(-1L);
				bg.setSecondCategoryName("-");
			}else{
				bg.setFirstCategoryId(bd.getParentId());//设置父类id
				bg.setFirstCategoryName(baseDictMap.get(bd.getParentId()).getName());
				bg.setSecondCategoryId(bd.getId());
				bg.setSecondCategoryName(bd.getName());
			}
    	}
    	return goodsList;
    }

	@Override
	public Result<Page<BaseGoods>> query4page(Map<String, Object> params) {
		String firstCategoryId = "-1",secondCategoryId = "-1";
		Object firstCategory = params.get("firstCategoryId");
		if(null != firstCategory){
			firstCategoryId = (String)firstCategory;
		}
		Object secondCategory = params.get("secondCategoryId");
		if(null != secondCategory){
			secondCategoryId = (String)secondCategory;
		}
		//专业下面的所有子类别
		if(!"-1".equals(firstCategoryId) ){
			List<Long> goodsDeptIdList = new ArrayList<Long>();
			if("-1".equals(secondCategoryId)){
				List<BaseDict> sonList = baseDictDao.getSonsByTypeAndParentId(Constants.BASE_DICT_GOODS_TYPE, Long.parseLong(firstCategoryId));
				for(int i=0 ,sonSize = sonList.size();i<sonSize;i++){
					GoodsDept tempGoodsDept = new GoodsDept();
					tempGoodsDept.setParentCategoryId(sonList.get(i).getParentId());
					tempGoodsDept.setCategoryId(sonList.get(i).getId());
					List<GoodsDept> goodsDeptList  = goodsDeptDao.getGoodsDeptId(tempGoodsDept);
					for(GoodsDept gd : goodsDeptList){
						goodsDeptIdList.add(gd.getId());
					}
				}
			}else{
				GoodsDept tempGoodsDept = new GoodsDept();
				tempGoodsDept.setCategoryId(Long.parseLong(secondCategoryId));
				List<GoodsDept> goodsDeptList  = goodsDeptDao.getGoodsDeptId(tempGoodsDept);
				for(GoodsDept gd : goodsDeptList){
					goodsDeptIdList.add(gd.getId());
				}
			}
			params.put("goodsDeptId", goodsDeptIdList);
		}
		Object search = params.get("search");
		if(search!=null){
			String searchStr = String.valueOf(search).trim();//去前后空格
			params.put("search",searchStr);
		}
		Page<BaseGoods> baseGoodslist = baseGoodsDao.query4page(params);
		Map<Long,BaseDict> baseDictMap = baseDictDao.getMapByType(Constants.BASE_DICT_GOODS_TYPE);
		//填充专业、类别数据
		baseGoodslist.process(bg ->{
			GoodsDept goodsDept = goodsDeptDao.getGoodsDeptById(bg.getGoodsDeptId());
			if(null == goodsDept){
				return;
			}
			BaseDict bd = baseDictMap.get(goodsDept.getCategoryId());
			if(null == bd){
				bg.setFirstCategoryName("-");
				bg.setSecondCategoryName("-");
				return ;
			}
			if(null == bd.getParentId()){
				bg.setFirstCategoryId(bd.getId());
				bg.setFirstCategoryName(bd.getName());
				bg.setSecondCategoryId(-1L);
				bg.setSecondCategoryName("-");
			}else{
				bg.setFirstCategoryId(bd.getParentId());//设置父类id
				bg.setFirstCategoryName(baseDictMap.get(bd.getParentId()).getName());
				bg.setSecondCategoryId(bd.getId());
				bg.setSecondCategoryName(bd.getName());
			}
			Organization org = organizationServiceImpl.getAscendingHierarchicalOrgById(goodsDept.getOrgId());
			if(null == org){
				bg.setProfessionDeptId(-1L);
				bg.setProfessionDeptName("-");
				bg.setOperatingDeptId(-1L);
				bg.setOperatingDeptName("-");
			}else{
				bg.setProfessionDeptId(org.getParentOrganization().getId());
				bg.setProfessionDeptName(org.getParentOrganization().getOrgName());
				bg.setOperatingDeptId(org.getId());
				bg.setOperatingDeptName(org.getOrgName());
			}
		});
		
		return Result.success(baseGoodslist);
	}

	@Override
	public Result<Boolean> delete(String id) {
		String[] ids;
		if(id.indexOf(",")>=0){
			ids = id.split(",");
		}else{
			ids = new String[]{id};
		}
		List<Integer> resultArr = new ArrayList<Integer>();
		for(int i=0;i<ids.length;i++){
			int result = baseGoodsDao.delete(ids[i]);
			if(result>0){
				resultArr.add(result);
			}
		}
		if(resultArr.size() == ids.length){
			return Result.success(true);
		}else{
			return Result.failure(500, "删除失败，请重试！");
		}
	}

	@Override
	public Result<Boolean> update(Long userId,BaseGoods baseGoods) {
		baseGoods = this.logicDeal(userId,baseGoods);
		if(null == baseGoods){
			return Result.failure(ResultCode.BAD_REQUEST, "参数传递错误！");
		}
		BaseGoods oldBg = baseGoodsDao.getBaseGoodsById(baseGoods.getId());
		//要修改物资编码，则需要检查
		if(!oldBg.getGoodsCode().equals(baseGoods.getGoodsCode())){
			//查找新物资编码是否存在
			BaseGoods existsBg = baseGoodsDao.getGoodsByGoodsCode(baseGoods.getGoodsCode());
			if(null != existsBg){
				return Result.failure(ResultCode.BAD_REQUEST,"物资编码已被占用！");
			}
		}
		int result = baseGoodsDao.update(baseGoods);
		if(result>0){
			return Result.success(true);
		}else{
			return Result.failure(500, "修改物资失败，请重试！");
		}
	}

	@Override
	public Result<Boolean> add(Long userId,BaseGoods baseGoods) {
		baseGoods = this.logicDeal(userId,baseGoods);
		if(null == baseGoods){
			return Result.failure(ResultCode.BAD_REQUEST, "参数传递错误！");
		}
		BaseGoods existsBg = baseGoodsDao.getGoodsByGoodsCode(baseGoods.getGoodsCode());
		if(null != existsBg && 1 == existsBg.getValid()){
			return Result.failure(ResultCode.BAD_REQUEST,"物资编码已被占用！");
		}else{
			baseGoods.setValid(1);
		}
		baseGoods.setCreateBy(userId);
		baseGoods.setCreateTm(DateUtil.formatDateYmDhMs(new Date()));
		List<BaseGoods> bgList = new ArrayList<BaseGoods>();
		bgList.add(baseGoods);
		baseGoodsDao.saveOrUpdateList(bgList);
		return Result.success("物资新增成功!", true);
	}
	
	/***
	 * 业务逻辑梳理
	 * @param baseGoods
	 * @return
	 */
	public BaseGoods  logicDeal(Long userId,BaseGoods baseGoods){
		if("-1".equals(String.valueOf(baseGoods.getSecondCategoryId()))){//代表选择的是其他，则需要录入
			if(null == baseGoods.getFirstCategoryId()){//父类别必须传递
				return null;
			}
			if(null == baseGoods.getProfessionDeptId()){//父专业局也必须传递
				return null;
			}
			BaseDict insertBaseDict = this.addSecondCategory(userId, baseGoods);
			if(null == insertBaseDict.getId()){
				return null;
			}
			//2,添加到物资与单位的关系表
			GoodsDept goodsDept = this.addGoodsDept(userId, baseGoods, insertBaseDict);
			if(null == goodsDept.getId()){
				return null;
			}
			
			//更新关联id记录
			baseGoods.setGoodsDeptId(goodsDept.getId());
		}else{
			GoodsDept gd = new GoodsDept();
			gd.setCategoryId(baseGoods.getSecondCategoryId());
			List<GoodsDept> gdList = goodsDeptDao.getGoodsDeptId(gd);
			for(GoodsDept temp :gdList){
				baseGoods.setGoodsDeptId(temp.getId());
			}
		}
		return baseGoods;
	}
	
	/***
	 * 新增类别
	 *
	 * @param userId
	 * @param baseGoods
	 */
	private BaseDict addSecondCategory(Long userId,BaseGoods baseGoods){
		BaseDict insertBaseDict = new BaseDict();
		insertBaseDict.setName(baseGoods.getSecondCategoryName());
		insertBaseDict.setType(Integer.valueOf(Constants.BASE_DICT_GOODS_TYPE));
		insertBaseDict.setCreateBy(userId);
		insertBaseDict.setCreateTm(new Date());
		insertBaseDict.setParentId(Long.valueOf(baseGoods.getFirstCategoryId()));
		insertBaseDict = baseDictDao.insert(insertBaseDict);
		return insertBaseDict;
	}
	
	/***
	 * 新增物资和部门的关系记录
	 * @param userId
	 * @param baseGoods
	 */
	private GoodsDept addGoodsDept(Long userId,BaseGoods baseGoods,BaseDict baseDict){
		GoodsDept goodsDept = new GoodsDept();
		goodsDept.setParentOrgId(baseGoods.getProfessionDeptId());//获取选中的专业局
		goodsDept.setOrgId(baseGoods.getOperatingDeptId());//获取选择的业务处
		goodsDept.setParentCategoryId(baseDict.getParentId());//获取选择的父类
		goodsDept.setCategoryId(baseDict.getId());//新增类别
		goodsDept = goodsDeptDao.insert(goodsDept);
		return goodsDept;
	}

	/**
	 * 返回所有的物资维表数据
	 * 专业：FirstCategory
	 * 类别：SecondCategory
	 */
	@Override
	public Result<List<BaseGoods>> getAllData() {
		List<BaseGoods> resultList = new ArrayList<BaseGoods>();
		List<BaseGoods> baseGoodsList = baseGoodsDao.getAllData();
		Map<Long,BaseDict> baseDictMap = baseDictDao.getMapByType(Constants.BASE_DICT_GOODS_TYPE);
		for(BaseGoods bg : baseGoodsList){
			//根据关联表id获取关联对象的类别id
			GoodsDept goodsDept = goodsDeptDao.getGoodsDeptById(bg.getGoodsDeptId());
			if(null == goodsDept){
				continue;
			}
			BaseDict baseDict = baseDictMap.get(goodsDept.getCategoryId());
			if(null == baseDict){
				continue;
			}
			if(null != baseDict.getParentId()){
				BaseDict baseDictParent = baseDictMap.get(baseDict.getParentId());
				bg.setFirstCategoryId(baseDictParent.getId());
				bg.setFirstCategoryName(baseDictParent.getName());
				bg.setSecondCategoryId(baseDict.getId());
				bg.setSecondCategoryName(baseDict.getName());
				resultList.add(bg);
			}
		}
		return Result.success(resultList);
	}

	@Override
	public Result<List<BaseGoods>> getGoodsByCategoryId(String categoryId) {
		return Result.success(baseGoodsDao.getGoodsByCategoryId(categoryId));
	}
	
	@Override
	public Result<UploadFileStatus> uploadFile(File file,String fileName,String contentType,Long userId){
		//1,标题
		String[] headers = new String[]{"物资编码","品名","单位","专业(一级)","类别(二级)","所属业务局","所属业务处"};
		//2,获取文件流
		try {
			InputStream fileInputStream = new FileInputStream(file);
			if(file.getAbsoluteFile().getName().indexOf(".csv")>0){
				InputStreamReader irs = new InputStreamReader(fileInputStream,"GBK");
				byte[] da = IOUtils.toByteArray(irs, "UTF-8");
				fileInputStream = new ByteArrayInputStream(da);
			}
			DataExtractor<String[]> extractor = DataExtractorBuilder.newBuilder(
					fileInputStream, fileName, contentType, headers
			    ).build();
			List<BaseGoods> baseGoodsList = new ArrayList<BaseGoods>();
			Map<Long,BaseDict> baseDictMap = baseDictDao.getMapByType(Constants.BASE_DICT_GOODS_TYPE);
			ProcessResult<String[]> process = extractor.filter((rowNumber, data) -> {
				if(0 == rowNumber){//列头
					return null;
				}
	            String[] array = (String[]) data;
	            if(StringUtils.isBlank(array[0]) || StringUtils.isBlank(array[1]) ||
	            		StringUtils.isBlank(array[2]) ||StringUtils.isBlank(array[3]) ||
	            		StringUtils.isBlank(array[4]) || StringUtils.isBlank(array[5]) ||
	            		StringUtils.isBlank(array[6]) ){
	            	return array[0]+"|"+array[1]+"|"+array[2]+"|"+array[3]+"|"+
		            		array[4]+"|"+array[5]+"|"+array[6]+",此记录必须提供完整的记录值";
	            }
	            BaseGoods baseGoods = new BaseGoods();
				baseGoods.setGoodsCode(array[0]);//物资编码
				baseGoods.setGoodsName(array[1]);//物资品名
				baseGoods.setGoodsUnit(array[2]);//物资单位
				//先判断单位是否匹配
				Long parentDeptId = -1L,deptId = -1L;
				List<Organization> orgList = organizationServiceImpl.getAscendingHierarchicalOrgByNameForMaterial(array[6]);
				for(Organization org : orgList){
					Organization orgParents = org.getParentOrganization();
					if(null != orgParents){
						if(array[5].trim().equals(orgParents.getOrgName())){
							parentDeptId = org.getParentOrgId();
							deptId = org.getId();	
							break;
						}
					}
				}
				if(-1 == deptId){//代表没有找到数据
					return  array[0]+"|"+array[1]+"|"+array[2]+"|"+array[3]+"|"+
            			array[4]+"|"+array[5]+"|"+array[6]+",此记录未匹配到组织或单位";
				}
				//判断专业局是否配置
				GoodsDept specialGoodsDept= new GoodsDept();
				specialGoodsDept.setParentOrgId(parentDeptId);
				//先查询这个专业局是否配置专业
				List<GoodsDept>  specialList= goodsDeptDao.getGoodsDeptId(specialGoodsDept);
				if(specialList.size()>0){//专业局已配置专业
					//拿到自己的专业名字
					BaseDict existsBd = baseDictMap.get(specialList.get(0).getParentCategoryId());
					//查询是否是新专业
					BaseDict baseDict = baseDictDao.getBaseDictByTypeAndName(Constants.BASE_DICT_GOODS_TYPE, array[3]);
					if(null == baseDict){//新专业,但是自己配置了专业，不能重复配置
						return  array[0]+"|"+array[1]+"|"+array[2]+"|"+array[3]+"|"+
            				array[4]+"|"+array[5]+"|"+array[6]+",此记录"+array[5]+"已配置了"+existsBd.getName();
					}else{//已有专业，但是与自己的不匹配
						if(!existsBd.getName().equals(baseDict.getName())){
							return  array[0]+"|"+array[1]+"|"+array[2]+"|"+array[3]+"|"+
		            				array[4]+"|"+array[5]+"|"+array[6]+",此记录"+array[5]+"已配置了"+existsBd.getName();
						}
					}
				}
				BaseDict specialBaseDict = this.getBaseDict(userId, array[3],null);
				BaseDict categoryBaseDict = this.getBaseDict(userId, array[4],specialBaseDict.getId());
				GoodsDept goodsDept= new GoodsDept();
				goodsDept.setParentOrgId(parentDeptId);//关联父部门
				goodsDept.setOrgId(deptId);//关联部门
				goodsDept.setParentCategoryId(specialBaseDict.getId());//关联父类别
				goodsDept.setCategoryId(categoryBaseDict.getId());//关联类别
				List<GoodsDept>  searchList= goodsDeptDao.getGoodsDeptId(goodsDept);//先查
				if(null == searchList || searchList.size() == 0){
					goodsDept = goodsDeptDao.insert(goodsDept);//插入类别与部门之间的关系
				}else{
					goodsDept = searchList.get(0);
				}
				baseGoods.setGoodsDeptId(goodsDept.getId());
				baseGoods.setCreateBy(userId);
				baseGoods.setCreateTm(DateUtil.formatDateYmDhMs(new Date()));
				
	            BaseGoods existsGoods = baseGoodsDao.getGoodsByGoodsCode(array[0]);
	            if(null != existsGoods){//直接跳过
	            	if(existsGoods.getValid()==2){//已失效，那么就还原
	            		baseGoods.setId(existsGoods.getId());
	            		baseGoods.setValid(1);//设置生效
	            	}else{
		            	return  array[0]+"|"+array[1]+"|"+array[2]+"|"+array[3]+"|"+
			            		array[4]+"|"+array[5]+"|"+array[6]+",此记录物资编码已存在";
	            	}
				}
				baseGoodsList.add(baseGoods);
				return null;
	        });
			int length = 500;
			while(baseGoodsList.size()>0){
				if(baseGoodsList.size()<500){
					length = baseGoodsList.size();
				}
				List<BaseGoods> sublist = baseGoodsList.subList(0, length);
				baseGoodsDao.saveOrUpdateList(sublist);
				baseGoodsList.subList(0, length).clear();
			}
			UploadFileStatus  ufs = new UploadFileStatus();
			ufs.setErrorList(process.getErrors());
			ufs.setNumbersOfError(process.getErrors().size());
			ufs.setNumbersOfSuccess(process.getData().size()-1);
			return Result.success(ufs);
		} catch (FileTooBigException e) {
			logger.error("", e);
			return Result.failure(ResultCode.SERVER_ERROR, e.getMessage());
		} catch (IOException e) {
			logger.error("", e);
			return Result.failure(ResultCode.SERVER_ERROR, e.getMessage());
		} catch (Exception e) {
			logger.error("", e);
			return Result.failure(ResultCode.SERVER_ERROR, e.getMessage());
		}
	}
	
	@Override
	public List<Long> getListByCategoryIds(Long parentCategoryId,List<Long> categoryIds){
		return baseGoodsDao.queryGoodsIdBySpecialtyIdAndCategoryIds(parentCategoryId, categoryIds);
	}
	
	/**
	 * 获取专业、类别
	 * @param UserId
	 * @param name
	 * @return
	 */
	private BaseDict getBaseDict(Long userId,String name,Long parentId){
		BaseDict baseDict = baseDictDao.getBaseDictByTypeAndName(Constants.BASE_DICT_GOODS_TYPE, name);
		if(null == baseDict){
			baseDict = new BaseDict();
			baseDict.setType(Integer.valueOf(Constants.BASE_DICT_GOODS_TYPE));
			baseDict.setName(name);
			baseDict.setCreateBy(userId);
			baseDict.setCreateTm(new Date());
			baseDict.setParentId(parentId == null? -1:parentId);
			baseDictDao.insert(baseDict);//保存专业
		}
		return baseDict;
	}
	
	@Override
	public File exportBaseGoods(String fileFolderPath){
		String[] headers = new String[]{"物资编码","品名","单位","专业(一级)","类别(二级)","所属业务局","所属业务处"};
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
                this.queryFromDbAndWrite(writer);
            }
            writer.flush();
            writer.close();
            writer = null;
		}catch(Exception e){
		    logger.error("导出文件异常原因：{}", e.getMessage());
		}
		return csvFile;
	}
	
	 //创建文件内容
    private void queryFromDbAndWrite(BufferedWriterWrapper writer){
		StringBuffer sql = new StringBuffer();
		sql.append("select bg.goods_code,bg.goods_name,bg.goods_unit,");
		sql.append(" (select name from t_base_dict where id = (select parent_id from t_base_dict "); 
		sql.append(" where id  = (select category_id from t_goods_dept where id = bg.goods_dept_id))) as first_category,");
		sql.append(" (select name from t_base_dict where id = (select category_id from t_goods_dept where id = bg.goods_dept_id)) as second_category,");
		sql.append(" (select org_name from t_org where id = ");
		sql.append(" (select parent_org_id from t_goods_dept where id = bg.goods_dept_id)) as profession_dept, ");
		sql.append(" (select org_name from t_org where id = (select org_id from t_goods_dept where id = bg.goods_dept_id)) as operating_dept ");
		sql.append(" from t_base_goods bg ");
		logger.info("导出物资sql语句：{}",sql.toString());
        try {
            StringBuilder sb = new StringBuilder(8192);
            List<Map<String,Object>> resultList = baseGoodsDao.executeSelectSql(sql.toString());
            Map<String,Object> temp;
            int index = 0;
            for(int i=0,iSize=resultList.size();i<iSize;i++){
            	 temp = resultList.get(i);
            	 sb.append("\""+ifNullString(temp.get("goods_code"))+"\",\""+ifNullString(temp.get("goods_name"))+"\",");
            	 sb.append("\""+ifNullString(temp.get("goods_unit"))+"\",\""+ifNullString(temp.get("first_category"))+"\",");
            	 sb.append("\""+ifNullString(temp.get("second_category"))+"\",\""+ifNullString(temp.get("profession_dept"))+"\",");
            	 sb.append("\""+ifNullString(temp.get("operating_dept"))+"\",\n");
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
