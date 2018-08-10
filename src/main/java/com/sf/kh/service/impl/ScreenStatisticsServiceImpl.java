package com.sf.kh.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sf.kh.dao.IBaseGoodsDao;
import com.sf.kh.dao.IScreenStatisticsDao;
import com.sf.kh.dao.IUserDao;
import com.sf.kh.dto.UserDto;
import com.sf.kh.dto.convert.UserConverter;
import com.sf.kh.enums.UserType;
import com.sf.kh.model.Department;
import com.sf.kh.model.ScreenConfig;
import com.sf.kh.model.User;
import com.sf.kh.service.IDepartmentService;
import com.sf.kh.service.IOrganizationService;
import com.sf.kh.service.IOrganizationTypeService;
import com.sf.kh.service.IScreenStatisticsService;

import code.ponfee.commons.collect.Collects;
import code.ponfee.commons.constrain.Constraint;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;

/**
 * The screen statistics service implementation
 *
 * @author 01367825
 */
@Service
public class ScreenStatisticsServiceImpl implements IScreenStatisticsService {

    @Resource private IScreenStatisticsDao dao;
    @Resource private IUserDao userDao;
    @Resource private IBaseGoodsDao baseGoodsDao;
    @Resource private IDepartmentService deptService;
    @Resource private IOrganizationService orgService;
    @Resource private IOrganizationTypeService orgTypeService;

    @Constraint(field = "userId")
    @Constraint(field = "screenCode", notBlank = true)
    @Constraint(field = "screenName", notBlank = true)
    @Constraint(field = "startTm")
    @Override public Result<Void> saveOrUpdateScreenConfig(ScreenConfig config) {
        if (config.getEndTm() != null && !config.getEndTm().after(config.getStartTm())) {
            return Result.failure(ResultCode.BAD_REQUEST, "结束时间不能小于开始时间");
        }

        if ((config.getSenderOrgTypeId() == null || config.getSenderOrgTypeId() < 1)
            && (config.getSenderOrgId() != null && config.getSenderOrgId() > 0)) {
            return Result.failure(ResultCode.BAD_REQUEST, "选择的发送方组织对应的组织类型ID不能为空");
        } else if ((config.getSenderOrgId() == null || config.getSenderOrgId() < 1)
            && (config.getSenderDeptId() != null && config.getSenderDeptId() > 0)) {
            return Result.failure(ResultCode.BAD_REQUEST, "选择的发送方单位对应的组织ID不能为空");
        }

        if ((config.getReceiverOrgTypeId() == null || config.getReceiverOrgTypeId() < 1)
            && (config.getReceiverOrgId() != null && config.getReceiverOrgId() > 0)) {
            return Result.failure(ResultCode.BAD_REQUEST, "选择的接收方组织对应的组织类型ID不能为空");
        } else if ((config.getReceiverOrgId() == null || config.getReceiverOrgId() < 1)
            && (config.getReceiverDeptId() != null && config.getReceiverDeptId() > 0)) {
            return Result.failure(ResultCode.BAD_REQUEST, "选择的接收方单位对应的组织ID不能为空");
        }

        if ((config.getSpecialtyId() == null || config.getSpecialtyId() < 1)
            && (config.getCategoryId() != null && config.getCategoryId() > 0)) {
            return Result.failure(ResultCode.BAD_REQUEST, "选择的类别对应的专业ID不能为空");
        } else if ((config.getCategoryId() == null || config.getCategoryId() < 1)
            && (config.getGoodsId() != null && config.getGoodsId() > 0)) {
            return Result.failure(ResultCode.BAD_REQUEST, "选择的品类对应的类别ID不能为空");
        }

        if ((config.getSenderOrgTypeId() != null && config.getSenderOrgTypeId() > 0)
                && orgTypeService.getOrgTypeById(config.getSenderOrgTypeId()) == null) {
            return Result.failure(ResultCode.NOT_FOUND, "发送方组织类型ID不存在：" + config.getSenderOrgTypeId());
        }
        if ((config.getReceiverOrgTypeId() != null && config.getReceiverOrgTypeId() > 0)
                && orgTypeService.getOrgTypeById(config.getReceiverOrgTypeId()) == null) {
            return Result.failure(ResultCode.NOT_FOUND, "接收方组织类型ID不存在：" + config.getReceiverOrgTypeId());
        }
        if ((config.getSenderOrgId() != null && config.getSenderOrgId() >0)
            && orgService.getById(config.getSenderOrgId()) == null) {
            return Result.failure(ResultCode.NOT_FOUND, "发送方组织ID不存在：" + config.getSenderOrgId());
        }
        if ((config.getReceiverOrgId() != null && config.getReceiverOrgId() > 0)
            && orgService.getById(config.getReceiverOrgId()) == null) {
            return Result.failure(ResultCode.NOT_FOUND, "接收方组织ID不存在：" + config.getReceiverOrgId());
        }
        if ((config.getSenderDeptId() != null && config.getSenderDeptId() > 0)
            && deptService.getById(config.getSenderDeptId()) == null) {
            return Result.failure(ResultCode.NOT_FOUND, "发送方单位ID不存在：" + config.getSenderDeptId());
        }
        if ((config.getReceiverDeptId() != null && config.getReceiverDeptId() > 0)
            && deptService.getById(config.getReceiverDeptId()) == null) {
            return Result.failure(ResultCode.NOT_FOUND, "接收方单位ID不存在：" + config.getReceiverDeptId());
        }

        dao.insertOrUpdate(config);
        return Result.SUCCESS;
    }

    @Override public Result<ScreenConfig> getScreenConfig(long userId, String screenCode) {
        ScreenConfig c = dao.getByUserIdAndScreenCode(userId, screenCode);
        if (c != null) {
            Set<Long> deptIds = Sets.newHashSet();
            Set<String> custNos = Sets.newHashSet();
            queryDeptIds(c.getSenderOrgTypeId(), c.getSenderOrgId(), c.getSenderDeptId(), deptIds, custNos);
            c.setSenderDeptIds(Lists.newArrayList(deptIds));
            c.setSenderCustNos(Lists.newArrayList(custNos));

            deptIds.clear();
            custNos.clear();
            queryDeptIds(c.getReceiverOrgTypeId(), c.getReceiverOrgId(), c.getReceiverDeptId(), deptIds, custNos);
            c.setReceiverDeptIds(Lists.newArrayList(deptIds));
            c.setReceiverCustNos(Lists.newArrayList(custNos));

            c.setGoodsIds(queryGoodsIds(c.getSpecialtyId(), c.getCategoryId(), c.getGoodsId(), userId));
        }
        return Result.success(c);
    }

    @Override
    public Result<Map<String, Object>> waybillStatusDistribution(ScreenConfig config) {
        return Result.success(dao.waybillStatusDistribution(config));
    }

    @Override public Result<List<Map<String, Object>>> waybillCityDistribution(ScreenConfig config) {
        return Result.success(dao.waybillCityDistribution(config));
    }

    @Override public Result<List<Map<String, Object>>> waybillDeliverFlow(ScreenConfig config) {
        return Result.success(dao.waybillDeliverFlow(config));
    }

    @Override public Result<List<Map<String, Object>>> waybillOnlineFlow(ScreenConfig config) {
        return Result.success(dao.waybillOnlineFlow(config));
    }

    @Override public Result<List<Map<String, Object>>> waybillSignedFlow(ScreenConfig config) {
        return Result.success(dao.waybillSignedFlow(config));
    }

    @Override public Result<List<Map<String, Object>>> waybillExceptFlow(ScreenConfig config) {
        return Result.success(dao.waybillExceptFlow(config));
    }

    private void queryDeptIds(Long orgTypeId, Long orgId, Long deptId,
                              Set<Long> deptIds, Set<String> custNos) {
        if (deptId != null && deptId > 0) {
            Department dept = deptService.getById(deptId);
            if (dept != null) {
                deptIds.add(dept.getId());
                custNos.add(Objects.toString(dept.getCustNo(), ""));
            }
        } else if (orgId != null && orgId > 0) {
            for (Department dept : deptService.getAllDeptsByOrgId(orgId)) {
                deptIds.add(dept.getId());
                custNos.add(Objects.toString(dept.getCustNo(), ""));
            }
        } else if (orgTypeId != null && orgTypeId > 0) {
            Map<String, Object> params = Collects.toMap("orgTypeId", orgTypeId);
            for (Department dept : deptService.getDeptsWithHierarchicalOrgByOrgTypeId(params)) {
                deptIds.add(dept.getId());
                custNos.add(Objects.toString(dept.getCustNo(), ""));
            }
        } else {
            // do nothing
        }
    }

    private List<Long> queryGoodsIds(Long specialtyId, Long categoryId, Long goodsId, long userId) {
        if (specialtyId == null || specialtyId < 1) {
            return null;
        } else if (categoryId == null || categoryId < 1) {
            // 专业下的类别
            User user = userDao.getById(userId);
            if (user == null) {
                throw new IllegalArgumentException("用户ID不存在：" + userId);
            }
            user = userDao.getByUsername(user.getUsername());
            if (user.getDeptId() != null && user.getDeptId() > 0) {
                user.setDept(deptService.getDeptWithHierarchicalOrgById(user.getDeptId()));
            }
            UserDto dto =  new UserConverter().convert(user);
            Long parentOrgId = null, orgId = null;
            if (UserType.SPECIAL_BIZ == dto.getUserType()) { // 业务处
                parentOrgId = dto.getOrgPathList().get(dto.getOrgPathList().size() - 2).getOrgId();
                orgId = dto.getOrgId();
            }
            List<Long> goodsList = baseGoodsDao.queryGoodsIdBySpecialtyIdAndCategoryId(specialtyId, null, parentOrgId, orgId);
            if (UserType.SPECIAL_BIZ == dto.getUserType() && CollectionUtils.isEmpty(goodsList)) {
                goodsList = Collections.singletonList(-1L);
            }
            return goodsList;
        } else if (goodsId == null || goodsId < 1) {
            return baseGoodsDao.queryGoodsIdBySpecialtyIdAndCategoryId(specialtyId, categoryId, null, null);
        } else {
            return Lists.newArrayList(goodsId);
        }
    }
}
