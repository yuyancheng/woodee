package com.sf.kh.service;

import code.ponfee.commons.model.Result;
import com.sf.kh.model.ScreenConfig;

import java.util.List;
import java.util.Map;

/**
 * The screen statistics service interface
 *
 * @author 01367825
 */
public interface IScreenStatisticsService {

    /**
     * 保存或更新大屏配置信息
     *
     * @param config the ScreenConfig
     * @return the result is success or fail with code
     */
    Result<Void> saveOrUpdateScreenConfig(ScreenConfig config);

    /**
     * Get ScreenConfig by userId and screenCode
     *
     * @param userId
     * @param screenCode
     * @return
     */
    Result<ScreenConfig> getScreenConfig(long userId, String screenCode);

    /**
     * 快件概览状态分布
     *
     * @param config
     * @return
     */
    Result<Map<String, Object>> waybillStatusDistribution(ScreenConfig config);

    /**
     * 快件概览城市分布
     *
     * @param config
     * @return
     */
    Result<List<Map<String, Object>>> waybillCityDistribution(ScreenConfig config);

    /**
     * 件量流向在派
     *
     * @param config
     * @return
     */
    Result<List<Map<String, Object>>> waybillDeliverFlow(ScreenConfig config);

    /**
     * 件量流向在途
     *
     * @param config
     * @return
     */
    Result<List<Map<String, Object>>> waybillOnlineFlow(ScreenConfig config);

    /**
     * 件量流向已签收
     *
     * @param config
     * @return
     */
    Result<List<Map<String, Object>>> waybillSignedFlow(ScreenConfig config);

    /**
     * 件量流向异常
     *
     * @param config
     * @return
     */
    Result<List<Map<String, Object>>> waybillExceptFlow(ScreenConfig config);
}
