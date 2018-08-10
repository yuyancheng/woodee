package com.sf.kh.dao;

import com.sf.kh.model.ScreenConfig;

import java.util.List;
import java.util.Map;

/**
 * 大屏配置及统计
 *
 * @author 01367825
 */
public interface IScreenStatisticsDao {

    int insertOrUpdate(ScreenConfig config);

    int deleteByUserId(Long userId);

    ScreenConfig getByUserIdAndScreenCode(long userId, String screenCode);

    Map<String, Object> waybillStatusDistribution(ScreenConfig config);

    List<Map<String, Object>> waybillCityDistribution(ScreenConfig config);

    List<Map<String, Object>> waybillDeliverFlow(ScreenConfig config);

    List<Map<String, Object>> waybillOnlineFlow(ScreenConfig config);

    List<Map<String, Object>> waybillSignedFlow(ScreenConfig config);

    List<Map<String, Object>> waybillExceptFlow(ScreenConfig config);

    void cleanCache();
}