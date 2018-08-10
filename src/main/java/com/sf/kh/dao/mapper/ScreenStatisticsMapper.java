package com.sf.kh.dao.mapper;

import com.sf.kh.model.ScreenConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ScreenStatisticsMapper {

    int insertOrUpdate(ScreenConfig config);

    int deleteByUserId(Long userId);

    ScreenConfig getByUserIdAndScreenCode(@Param("userId") long userId,
                                          @Param("screenCode") String screenCode);

    Map<String, Object> waybillStatusDistribution(ScreenConfig config);

    List<Map<String, Object>> waybillCityDistribution(ScreenConfig config);

    List<Map<String, Object>> waybillDeliverFlow(ScreenConfig config);

    List<Map<String, Object>> waybillOnlineFlow(ScreenConfig config);

    List<Map<String, Object>> waybillSignedFlow(ScreenConfig config);

    List<Map<String, Object>> waybillExceptFlow(ScreenConfig config);
}