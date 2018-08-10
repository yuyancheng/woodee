package com.sf.kh.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.sf.kh.auth.UrlPermissionMatcher;
import com.sf.kh.dao.IPermitDao;
import com.sf.kh.dao.IRolePermitDao;
import com.sf.kh.dto.PermitFlat;
import com.sf.kh.dto.PermitTree;
import com.sf.kh.model.Permit;
import com.sf.kh.service.IPermitService;
import com.sf.kh.util.CommonUtils;
import com.sf.kh.util.Constants;

import code.ponfee.commons.constrain.Constraint;
import code.ponfee.commons.constrain.Constraints;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;

/**
 * The permit service interface implementation
 *
 * @author Ponfee
 */
@Service("permitService")
public class PermitServiceImpl implements IPermitService {

    private @Resource IPermitDao permitDao;
    private @Resource IRolePermitDao rolePermitDao;

    @Override
    public Result<Void> add(Permit permit) {
        int affectedRows = permitDao.add(permit);
        UrlPermissionMatcher.invalidPermits();
        return CommonUtils.oneRowAffected(affectedRows);
    }

    @Override
    public Result<Void> update(Permit permit) {
        int affectedRows = permitDao.update(permit);
        UrlPermissionMatcher.invalidPermits();
        return CommonUtils.oneRowAffected(affectedRows);
    }

    @Constraints(@Constraint(notEmpty = true, msg = "权限编号列表不能为空"))
    @Override
    public Result<Void> delete(String[] ids) {
        int affectedRows = permitDao.delByPermitIds(Lists.newArrayList(ids));
        if (affectedRows < Constants.ONE_ROW_AFFECTED) {
            return Result.failure(ResultCode.OPS_CONFLICT, "操作失败");
        }

        for (String id : ids) {
            rolePermitDao.delByPermitId(id);
        }
        UrlPermissionMatcher.invalidPermits();
        return Result.SUCCESS;
    }

    @Override
    public Result<Permit> get(String id) {
        return Result.success(permitDao.getByPermitId(id));
    }

    @Override
    public Result<PermitTree> listAsTree() {
        PermitTree root = new PermitTree(PermitTree.DEFAULT_ROOT_NAME);
        root.build(permitDao.queryAll());
        return Result.success(root);
    }

    @Override
    public Result<List<PermitFlat>> listAsFlat() {
        PermitTree root = new PermitTree(PermitTree.DEFAULT_ROOT_NAME);
        root.build(permitDao.queryAll());
        return Result.success(root.flat());
    }

}
