package com.sf.kh.service;

import java.util.List;

import com.sf.kh.dto.PermitFlat;
import com.sf.kh.dto.PermitTree;
import com.sf.kh.model.Permit;

import code.ponfee.commons.model.Result;

/**
 * The permit service interface
 *
 * @author Ponfee
 */
public interface IPermitService {

    /**
     * 新增权限
     * 
     * @param permit
     * @return
     */
    Result<Void> add(Permit permit);

    /**
     * 更新权限
     * 
     * @param permit
     * @return
     */
    Result<Void> update(Permit permit);

    /**
     * 删除权限
     * 删除角色权限关联表t_role_permit数据
     * 
     * @param ids
     * @return
     */
    Result<Void> delete(String[] ids);

    /**
     * 通过权限编号获取权限信息
     * 
     * @param id
     * @return
     */
    Result<Permit> get(String id);

    /**
     * 查询所有权限列表并树形展示
     * 
     * @return
     */
    Result<PermitTree> listAsTree();

    /**
     * 查询所有权限列表并扁平展示
     * 
     * @return
     */
    Result<List<PermitFlat>> listAsFlat();

}
