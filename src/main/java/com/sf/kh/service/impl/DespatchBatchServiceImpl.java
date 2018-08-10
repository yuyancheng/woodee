package com.sf.kh.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.sf.kh.service.IBaseGoodsService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import code.ponfee.commons.constrain.Constraint;
import code.ponfee.commons.log.LogAnnotation;
import code.ponfee.commons.log.LogAnnotation.LogType;
import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;

import com.sf.kh.dao.IBaseDictDao;
import com.sf.kh.dao.IBaseGoodsDao;
import com.sf.kh.dao.IDespatchBatchDao;
import com.sf.kh.dao.IDespatchGoodsDao;
import com.sf.kh.dao.IGoodsDeptDao;
import com.sf.kh.model.BaseDict;
import com.sf.kh.model.BaseGoods;
import com.sf.kh.model.Department;
import com.sf.kh.model.DespatchBatch;
import com.sf.kh.model.DespatchGoods;
import com.sf.kh.model.GoodsDept;
import com.sf.kh.model.Organization;
import com.sf.kh.service.IDepartmentService;
import com.sf.kh.service.IDespatchBatchService;
import com.sf.kh.util.Constants;
import com.sf.kh.util.DateUtil;

/***
 * 
 * @author 866316
 *
 */
@Service
public class DespatchBatchServiceImpl implements IDespatchBatchService {

    private @Resource IDespatchBatchDao despatchBatchDao;
    
    private @Resource IDespatchGoodsDao despatchGoodsDao;
    
    private @Resource IBaseGoodsDao baseGoodsDao;
    
    private @Resource IBaseDictDao baseDictDao;
    
    private @Resource IGoodsDeptDao goodsDeptDao;
    
    private @Resource IDepartmentService departmentServiceImpl;

	@Resource
	private IBaseGoodsService baseGoodsService;
    
    @Override
	public long getTodayMaxBatchCode() {
    	//设置查询从今天0分0秒开始
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	cal.set(Calendar.MILLISECOND, 0);
		DespatchBatch batchObj = despatchBatchDao.getTodayMaxBatchCode(DateUtil.formatDateYmDhMs(cal.getTime()));
		if(null == batchObj){
			return -1;
		}
		return batchObj.getBatchId();
	}

	@Override
	public Result<Boolean> validBatchNumber(String batchId) {
		Result<Boolean> result = new Result<Boolean>();
		DespatchBatch batchObj = despatchBatchDao.getByBatchId(batchId);
		if(null ==  batchObj){
			result.setData(false);//不存在
			result.setMsg("该批次号已被使用!");
		}else{
			result.setData(true);//存在
			result.setMsg("该批次号已被使用!");
		}
		return result;
	}

	
	@LogAnnotation(type = LogType.QUERY)
    @Constraint(field = "pageNum", notNull = false, min = 1)
    @Constraint(field = "pageSize", notNull = false, min = 1)
	public @Override Result<Page<DespatchBatch>> query4page(Map<String, ?> params) {
		 Page<DespatchBatch>  page = despatchBatchDao.query4page(params);
		 page.process(db -> {
			 db.setSendCompanyName(this.getDepartmentLevenName(db.getSendCompanyId()));
			 db.setReceiveCompanyName(this.getDepartmentLevenName(db.getReceiveCompanyId()));
		 });
		 return Result.success(page);
	}
	
	/***
	 * 获取单位层级
	 * @param id
	 * @return
	 */
	private String getDepartmentLevenName(Long id){
		if(null == id){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		Department departObj = departmentServiceImpl.getDeptWithHierarchicalOrgById(id);
		if(null == departObj){
			return "";
		}
		sb.append(departObj.getDeptName());
		/*Organization org = departObj.getOrganization();
		if(null != org){
			while(org.getParentOrganization() != null){
				sb.insert(0, org.getOrgName());
				org = org.getParentOrganization();
			}
		}*/
		return sb.toString();
	}


	@Override
	public Result<DespatchBatch> print(String batchId,Long deptId) {
		List<DespatchGoods> goodsList = null;
		if(null == batchId){
			return Result.failure(ResultCode.BAD_REQUEST,"必须传递batchId参数");
		}
		DespatchBatch batchObj = despatchBatchDao.getByBatchId((String)batchId);
		if(null == batchObj){
			return Result.success("未找到物资记录", new DespatchBatch());
		}
		//不是我收的或者不是我发的都不允许看  MODIFIED by KUN
		if(null != deptId && !
				(deptId.equals(batchObj.getSendCompanyId())
				||deptId.equals(batchObj.getReceiveCompanyId()))
				){
			return Result.success("未找到物资记录", new DespatchBatch());
		}
		batchObj.setSenderFullAddress(this.trimAddress(batchObj.getSenderAddrProvince())+
				this.trimAddress(batchObj.getSenderAddrCity())+
				this.trimAddress(batchObj.getSenderAddrArea())+
				this.trimAddress(batchObj.getSenderAddrOther()));
		batchObj.setReceiverFullAddress(this.trimAddress(batchObj.getReceiverAddrProvince())+
				this.trimAddress(batchObj.getReceiverAddrCity())+
				this.trimAddress(batchObj.getReceiverAddrArea())+
				this.trimAddress(batchObj.getReceiverAddrOther()));
		//是自己的记录，则获取该批次号的物资清单
		goodsList = despatchGoodsDao.getByBatchId(String.valueOf(batchObj.getBatchId()));
		Map<Long,BaseDict> baseDictMap = baseDictDao.getMapByType(Constants.BASE_DICT_GOODS_TYPE);
		
		List<DespatchGoods> factGoodsList = new ArrayList<DespatchGoods>();
		if(null != goodsList){
			for(DespatchGoods despatchGoods: goodsList){
				BaseGoods bg = baseGoodsDao.getBaseGoodsById(despatchGoods.getGoodsId());
				if(null == bg){
					continue;
				}
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
					despatchGoods.setBaseGoods(bg);
					factGoodsList.add(despatchGoods);
				}
			}
			batchObj.setDespatchGoodsList(factGoodsList);
		}
		batchObj.setPrintTm(DateUtil.formatDateYmDhMs(new Date()));
		return Result.success(batchObj);
	}
	
	private String trimAddress(String address){
		if(null == address){
			return "";
		}else if("".equals(address.trim())){
			return "";
		}else{
			return address;
		}
	}

	@Override
	public Result<String> insertDespatchBatch(DespatchBatch despatchBatch) {
		boolean result = despatchGoodsDao.saveList(despatchBatch.getDespatchGoodsList());
		if(false == result){
			return Result.failure(100, "未获取到物资清单，请刷新页面重试!");
		}
		int record = despatchBatchDao.insertDespatchBatch(despatchBatch);
		if(0 == record){
			return Result.failure(500, "发运批次新增失败请刷新页面重试!");
		}
		return Result.success(despatchBatch.getBatchId()+"");
	}

	@Override
	public DespatchBatch insertBatchId(DespatchBatch despatchBatch) {
		return despatchBatchDao.insertBatchId(despatchBatch);
	}
	
	@Override
	public Result<String> updateByBatchId(DespatchBatch despatchBatch) {
		boolean result = despatchGoodsDao.saveList(despatchBatch.getDespatchGoodsList());
		if(false == result){
			return Result.failure(100, "未获取到物资清单，请刷新页面重试!");
		}
		int finalResult = despatchBatchDao.updateByBatchId(despatchBatch);
		if(0 == finalResult){
			return Result.failure(ResultCode.SERVER_ERROR,"物资新增失败！");
		}else{
			return Result.success("物资新增成功!");
		}
	}

	@Override
	public Result<List<DespatchGoods>> getGoodsList(String batchId, Long userId) {
		List<DespatchGoods> goodsList = despatchGoodsDao.getByBatchId(batchId);
		return Result.success(goodsList);
	}
	
	
	@Override
	public Result<Boolean> updatePrintTime(String batchId, Long userId) {
		String printTm = DateUtil.formatDateYmDhMs(new Date());
		int result = despatchBatchDao.updatePrintTime(batchId, printTm, userId);
		if(result>0){
			return Result.success(true);
		}else{
			return Result.failure(ResultCode.SERVER_ERROR,"更新打印时间出错");
		}
	}

	@Override
	public Result<List<DespatchGoods>> getGoodsListWithBase(String batchId) {
		List<DespatchGoods> goodsList = despatchGoodsDao.getByBatchId(batchId);

		if(CollectionUtils.isNotEmpty(goodsList)){
			List<Long> ids = goodsList.stream().map(DespatchGoods::getGoodsId).collect(Collectors.toList());

			if(CollectionUtils.isNotEmpty(ids)){
				List<BaseGoods> baseGoods = baseGoodsService.getListByIds(ids);
				Map<Long, BaseGoods> goodsMap = baseGoods.stream().collect(Collectors.toMap(BaseGoods::getId, Function.identity()));

				goodsList.stream().forEach(o->o.setBaseGoods(goodsMap.get(o.getGoodsId())));
			}
		}

		return Result.success(goodsList);
	}


	
}
