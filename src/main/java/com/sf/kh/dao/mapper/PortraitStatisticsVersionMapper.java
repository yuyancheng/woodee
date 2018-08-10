package com.sf.kh.dao.mapper;

import com.sf.kh.model.PortraitStatisticsVersion;

public interface PortraitStatisticsVersionMapper {
	/**
	 * 获取最大的版本号记录
	 * @param id
	 * @return
	 */
    PortraitStatisticsVersion selectByMaxVersion();
}