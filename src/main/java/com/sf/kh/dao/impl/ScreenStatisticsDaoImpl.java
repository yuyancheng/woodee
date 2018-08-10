package com.sf.kh.dao.impl;

import code.ponfee.commons.jedis.JedisClient;
import com.sf.kh.dao.IScreenStatisticsDao;
import com.sf.kh.dao.mapper.ScreenStatisticsMapper;
import com.sf.kh.model.ScreenConfig;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static code.ponfee.commons.util.MessageFormats.format;

/**
 * The IScreenStatisticsDao implementation class
 *
 * @author Ponfee
 */
@SuppressWarnings("unchecked")
@Repository
public class ScreenStatisticsDaoImpl implements IScreenStatisticsDao {

    private static final String SCREEN_KEY = "screen:config:cache:#{uid}:#{scode}";
    private static final String STAICS_KEY = "screen:statis:cache:#{uid}:#{scode}:#{type}";
    private static final int STAICS_TM = 7200; // 2 hours

    private @Resource ScreenStatisticsMapper mapper;
    private @Resource JedisClient jedis;

    @Override public int insertOrUpdate(ScreenConfig config) {
        int rows =  mapper.insertOrUpdate(config);
        jedis.keysOps().del(format(SCREEN_KEY, config.getUserId(), config.getScreenCode()));
        jedis.keysOps().delWithWildcard(format(STAICS_KEY, config.getUserId(), config.getScreenCode(), "*"));
        return rows;
    }

    @Override public int deleteByUserId(Long userId) {
        return mapper.deleteByUserId(userId);
    }

    @Override public ScreenConfig getByUserIdAndScreenCode(long userId, String screenCode) {
        String key = format(SCREEN_KEY, userId, screenCode);
        ScreenConfig config = jedis.valueOps().getObject(key, ScreenConfig.class, false);
        if (config == null) {
            config = mapper.getByUserIdAndScreenCode(userId, screenCode);
            cache(key, config, 86400);
        }
        return config;
    }

    @Override public Map<String, Object> waybillStatusDistribution(ScreenConfig config) {
        String key = format(STAICS_KEY, config.getUserId(), config.getScreenCode(), "status");
        Map<String, Object> data = jedis.valueOps().getObject(key, HashMap.class, false);
        if (data == null) {
            data = mapper.waybillStatusDistribution(config);
            cache(key, data, STAICS_TM);
        }
        return data;
    }

    @Override public List<Map<String, Object>> waybillCityDistribution(ScreenConfig config) {
        String key = format(STAICS_KEY, config.getUserId(), config.getScreenCode(), "city");
        List<Map<String, Object>> data = jedis.valueOps().getObject(key, ArrayList.class, false);
        if (data == null) {
            data = mapper.waybillCityDistribution(config);
            cache(key, data, STAICS_TM);
        }
        return data;
    }

    @Override public List<Map<String, Object>> waybillDeliverFlow(ScreenConfig config) {
        String key = format(STAICS_KEY, config.getUserId(), config.getScreenCode(), "deliver");
        List<Map<String, Object>> data = jedis.valueOps().getObject(key, ArrayList.class, false);
        if (data == null) {
            data = mapper.waybillDeliverFlow(config);
            cache(key, data, STAICS_TM);
        }
        return data;
    }

    @Override public List<Map<String, Object>> waybillOnlineFlow(ScreenConfig config) {
        String key = format(STAICS_KEY, config.getUserId(), config.getScreenCode(), "online");
        List<Map<String, Object>> data = jedis.valueOps().getObject(key, ArrayList.class, false);
        if (data == null) {
            data = mapper.waybillOnlineFlow(config);
            cache(key, data, STAICS_TM);
        }
        return data;
    }

    @Override public List<Map<String, Object>> waybillSignedFlow(ScreenConfig config) {
        String key = format(STAICS_KEY, config.getUserId(), config.getScreenCode(), "signed");
        List<Map<String, Object>> data = jedis.valueOps().getObject(key, ArrayList.class, false);
        if (data == null) {
            data = mapper.waybillSignedFlow(config);
            cache(key, data, STAICS_TM);
        }
        return data;
    }

    @Override public List<Map<String, Object>> waybillExceptFlow(ScreenConfig config) {
        String key = format(STAICS_KEY, config.getUserId(), config.getScreenCode(), "except");
        List<Map<String, Object>> data = jedis.valueOps().getObject(key, ArrayList.class, false);
        if (data == null) {
            data = mapper.waybillExceptFlow(config);
            cache(key, data, STAICS_TM);
        }
        return data;
    }

    @Override public void cleanCache() {
        jedis.keysOps().delWithWildcard("screen:statis:cache:*");
    }

    private void cache(String key, Object obj, int expire) {
        if (obj != null) {
            jedis.valueOps().setObject(key, obj, false, expire);
        }
    }

    public static void main(String[] args) {
        System.out.println(format(STAICS_KEY, 1,2, "*"));
    }

}
