package com.sf.kh.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sf.kh.dto.UserDto;
import com.sf.kh.enums.UserType;
import com.sf.kh.model.ScreenConfig;
import com.sf.kh.service.IScreenStatisticsService;
import com.sf.kh.util.WebContextHolder;
import com.sf.kh.web.WebException;

import code.ponfee.commons.collect.Collects;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import code.ponfee.commons.util.Dates;

@RestController
@RequestMapping("screen")
public class ScreenStatisticsController {

    private @Resource IScreenStatisticsService service;

    @PostMapping("config/save")
    public Result<Void> save(@RequestBody ScreenConfig config) {
        String screenName;
        if ((screenName = ScreenConfig.SCREEN_CODES.get(config.getScreenCode())) == null) {
            return Result.failure(ResultCode.BAD_REQUEST, "大屏代码不能为空");
        }

        if (ScreenConfig.SCREEN_FLOW.equals(config.getScreenCode())
                && StringUtils.isBlank(config.getStartCity())) {
            return Result.failure(ResultCode.BAD_REQUEST, "始发城市不能为空");
        }

        UserDto user = WebContextHolder.currentUserDto();
        if (UserType.DEPT_SELF == user.getUserType() // 单位普通
            && !user.getDeptId().equals(config.getSenderDeptId())) {
            return Result.failure(ResultCode.BAD_REQUEST, "普通单位权限用户必须选择所属部门");
        }

        if (UserType.SPECIAL_BIZ == user.getUserType() // 专业处
            && (config.getSpecialtyId() == null || config.getSpecialtyId() < 1)) {
            return Result.failure(ResultCode.BAD_REQUEST, "专业处用户必须选择专业");
        }

        config.setUserId(user.getId());
        config.setScreenName(screenName);
        return service.saveOrUpdateScreenConfig(config);
    }

    @GetMapping("config/get")
    public Result<ScreenConfig> get(@RequestParam("screenCode") String screenCode) {
        return service.getScreenConfig(WebContextHolder.currentUser().getId(), screenCode);
    }

    @GetMapping("statistics/overview")
    public Result<Object> overview() {
        ScreenConfig config = getConfig(ScreenConfig.SCREEN_OVERVIEW);
        return Result.success(Collects.toMap(
            "status", service.waybillStatusDistribution(config).getData(),
            "city_rank", service.waybillCityDistribution(config).getData(),
            "screen_name", ScreenConfig.SCREEN_CODES.get(ScreenConfig.SCREEN_OVERVIEW),
            "start_tm", Dates.format(config.getStartTm()),
            "end_tm", config.getEndTm() == null ? "" : Dates.format(config.getEndTm()),
            "calc_date", new Date()
        ));
    }

    @GetMapping("statistics/monitor")
    public Result<Object> monitor() {
        ScreenConfig config = getConfig(ScreenConfig.SCREEN_MONITOR);
        return Result.success(Collects.toMap(
            "deliver", service.waybillDeliverFlow(config).getData(),
            "online", service.waybillOnlineFlow(config).getData(),
            "except", service.waybillExceptFlow(config).getData(),
            "signed", service.waybillSignedFlow(config).getData(),
            "screen_name", ScreenConfig.SCREEN_CODES.get(ScreenConfig.SCREEN_MONITOR),
            "start_tm", Dates.format(config.getStartTm()),
            "end_tm", config.getEndTm() == null ? "" : Dates.format(config.getEndTm()),
            "calc_date", new Date()
        ));
    }

    private ScreenConfig getConfig(String screenCode) {
        Long userId = WebContextHolder.currentUser().getId();
        ScreenConfig config = service.getScreenConfig(userId, screenCode).getData();
        if (config == null) {
            throw new WebException(ResultCode.NOT_FOUND.getCode(), "请先配置大屏");
        }
        return config;
    }
}
