package com.sf.kh.dao.mapper;

import java.util.List;

import com.sf.kh.model.GoodsDept;
/***
 * 物资和部门关系表
 * @author 866316
 *
 */
public interface GoodsDeptMapper {

	/***
	 * 新增关系记录
	 * @param record
	 * @return
	 */
	int insert(GoodsDept record);
    /***
     * 获取主键id号
     * @param record
     * @return
     */
    List<GoodsDept> select(GoodsDept record);
    
    /***
     * 根据主键获取记录
     * @param goodsDeptId
     * @return
     */
    GoodsDept selectById(Long goodsDeptId);
}