package com.sf.kh.dao.mapper;

import com.sf.kh.model.OverviewVersion;

public interface OverviewVersionMapper {

    OverviewVersion selectMaxVersion();
}