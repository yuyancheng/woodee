package com.sf.kh.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sf.kh.dao.IBaseDictDao;
import com.sf.kh.dao.IBaseGoodsDao;
import com.sf.kh.dao.IGoodsDeptDao;
import com.sf.kh.dto.UserDto;
import com.sf.kh.model.BaseDict;
import com.sf.kh.model.BaseGoods;
import com.sf.kh.model.Department;
import com.sf.kh.model.GoodsDept;
import com.sf.kh.service.IBaseDictService;
import com.sf.kh.service.IDepartmentService;
import com.sf.kh.util.Constants;

import code.ponfee.commons.model.Result;

/***
 * 获取维表数据
 * @author 866316
 *
 */
@Service
public class BaseDictServiceImpl implements IBaseDictService {

    private @Resource IBaseDictDao baseDictDao;
    
    private @Resource IBaseGoodsDao baseGoodsDao;
    
    private @Resource IDepartmentService deptService;
    
    private @Resource IGoodsDeptDao goodsDeptDao;
    
    
    private List<BaseDict> baseDictList = new ArrayList<>();

    /***
     * 获取物资下拉框专业/类别集合
     * @return
     */
    @Override
	public Result<List<BaseDict>> getGoodsTypes() {
		List<BaseDict> list  = this.getDictList(Constants.BASE_DICT_GOODS_TYPE);
		if(null == list){
			list = new ArrayList<BaseDict>();
		}
		return Result.success(list);
	}
    
    /***
     * 获取物资维表集合
     * @return
     */
    @Override
	public Result<List<BaseDict>> getGoodsData() {
		List<BaseDict> list  = this.getDictList(Constants.BASE_DICT_GOODS_TYPE);
		if(null == list){
			list = new ArrayList<BaseDict>();
		}
		for(BaseDict bd : list){
			List<BaseDict> childrens = bd.getChildrens();
			for(BaseDict son :childrens){
				List<BaseGoods> goodsList = baseGoodsDao.getGoodsByCategoryId(String.valueOf(son.getId()));
				if(null == goodsList ||(goodsList!=null&&goodsList.size()==0)){
					continue;
				}
				son.setGoods(goodsList);
			}
		}
		return Result.success(list);
	}
    
    private List<BaseDict> getDictList(String type){
    	List<BaseDict> resultList = new ArrayList<BaseDict>();
    	//获取记录
    	baseDictList = baseDictDao.getListByType(type);
		for (BaseDict bd : baseDictList) {
			if(null == bd.getParentId() || "".equals(bd.getParentId())
					|| "-1".equals(bd.getParentId().toString())){
				resultList.add(generateTreeNode(bd.getId()));
			}
		}
    	return resultList;
    }
    
    
	/**
	 * 递归生成Tree结构数据
	 * 
	 * @param rootId
	 * @return
	 */
	public BaseDict generateTreeNode(Long rootId) {
		BaseDict root = this.getNodeById(rootId);
		List<BaseDict> childrenTreeNode = this.getChildrenNodeById(rootId);
		for (BaseDict item : childrenTreeNode) {
			BaseDict node = this.generateTreeNode(item.getId());
			root.getChildrens().add(node);
		}
		return root;
	}

	/**
	 * 
	 * @param nodeId
	 * @return
	 */
	private BaseDict getNodeById(Long nodeId) {
		BaseDict bd = new BaseDict();
		for (BaseDict item : baseDictList) {
			if ((item.getId()+"").equals(nodeId+"")) {
				bd = item;
				break;
			}
		}
		return bd;
	}

	/**
	 * 
	 * @param nodeId
	 * @return
	 */
	private List<BaseDict> getChildrenNodeById(Long nodeId) {
		List<BaseDict> childrenTreeNode = new ArrayList<>();
		for (BaseDict item : this.baseDictList) {
			if ((item.getParentId()+"").equals(nodeId+"")) {
				childrenTreeNode.add(item);
			}
		}
		return childrenTreeNode;
	}

	@Override
	public Result<List<BaseDict>> getGoodsDataByStatistics(UserDto userDto) {
		List<BaseDict> list = null; 
		if("MANAGER".equals(userDto.getRoleCode())){//超级管理员
			list = this.getDictList(Constants.BASE_DICT_GOODS_TYPE);
		}else{
			if (null != userDto.getOrgMaterialMark() && 1 == userDto.getOrgMaterialMark()){//物资相关用户
				Department dept = deptService.getDeptWithHierarchicalOrgById(userDto.getDeptId());
				List<BaseDict> resultList = new ArrayList<BaseDict>();
				if(null == dept.getOrganization().getParentOrganization()){//专业局
					BaseDict searchBd = new BaseDict();
					searchBd.setId(dept.getOrganization().getId());
					baseDictList = baseDictDao.getListByTypeAndObj(Constants.BASE_DICT_GOODS_TYPE, searchBd);
					for (BaseDict bd : baseDictList) {
						if(null == bd.getParentId() || "".equals(bd.getParentId())
								|| "-1".equals(bd.getParentId().toString())){
							resultList.add(generateTreeNode(bd.getId()));
						}
					}
					list = resultList;
				}else{//业务处
					BaseDict searchParent = new BaseDict();
					searchParent.setId(dept.getOrganization().getParentOrganization().getId());
					List<BaseDict> searchParentDictList = baseDictDao.getListByTypeAndObj(Constants.BASE_DICT_GOODS_TYPE, searchParent);
					BaseDict searchSon = new BaseDict();
					searchSon.setParentId(dept.getOrganization().getParentOrganization().getId());
					searchSon.setId(dept.getOrganization().getId());
					List<BaseDict> searchSonDictList =  baseDictDao.getListByTypeAndObj(Constants.BASE_DICT_GOODS_TYPE, searchSon);
					for(BaseDict bdParent :  searchParentDictList){
						List<BaseDict> bdChildren = new ArrayList<BaseDict>();
						for(BaseDict bdSon : searchSonDictList){
							if(bdSon.getParentId().equals(bdParent.getId())){
								bdChildren.add(bdSon);
							}
						}
						if(bdChildren.size() == 0){//不在范围内，则删除掉
							searchParentDictList.remove(bdParent);
						}else{
							bdParent.setChildrens(bdChildren);
						}
					}
					list = searchParentDictList;
				}
			}else{//与物资无关的用户
				list = this.getDictList(Constants.BASE_DICT_GOODS_TYPE);
			}
		}
		if(null == list){
			list = new ArrayList<BaseDict>();
		}
		for(BaseDict bd : list){
			List<BaseDict> childrens = bd.getChildrens();
			for(BaseDict son :childrens){
				List<BaseGoods> goodsList = baseGoodsDao.getGoodsByCategoryId(String.valueOf(son.getId()));
				if(null == goodsList ||(goodsList!=null&&goodsList.size()==0)){
					continue;
				}
				son.setGoods(goodsList);
			}
		}
		return Result.success(list);
	}

	@Override
	public Result<Map<String, String>> getCategoryInfo(UserDto userDto) {
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("specialtyId", "-1");
		resultMap.put("categoryId", "-1");
		if (!"MANAGER".equals(userDto.getRoleCode()) && null != userDto.getOrgMaterialMark() 
				&& 1 == userDto.getOrgMaterialMark()){//物资相关用户
			Department dept = deptService.getDeptWithHierarchicalOrgById(userDto.getDeptId());
			if(null == dept.getOrganization().getParentOrganization()){//专业局
				GoodsDept gd = new GoodsDept();
				gd.setParentOrgId(dept.getOrganization().getId());
				List<GoodsDept> gdList = goodsDeptDao.getGoodsDeptId(gd);
				for(GoodsDept temp : gdList){
					resultMap.put("specialtyId", String.valueOf(temp.getParentCategoryId()));
					break;
				}
			}else{//业务处
				GoodsDept gd = new GoodsDept();
				gd.setParentOrgId(dept.getOrganization().getParentOrganization().getId());
				gd.setOrgId(dept.getOrganization().getId());
				List<GoodsDept> gdList = goodsDeptDao.getGoodsDeptId(gd);
				String parentCategoryId = null;
				StringBuffer categorySb = new StringBuffer();
				
				for(GoodsDept temp : gdList){
					if(null == parentCategoryId){
						parentCategoryId = String.valueOf(temp.getParentCategoryId());
					}
					if(categorySb.indexOf(temp.getCategoryId()+",")>=0){
						continue;
					}
					categorySb.append(temp.getCategoryId()+",");
				}
				resultMap.put("specialtyId", parentCategoryId);
				if(categorySb.length()>0){
					resultMap.put("categoryId", categorySb.substring(0, categorySb.length()-1));
				}else{
					resultMap.put("categoryId", "-2");
				}
			}
		}
		return Result.success(resultMap);
	}
}