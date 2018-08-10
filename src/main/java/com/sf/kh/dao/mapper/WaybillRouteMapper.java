package com.sf.kh.dao.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.sf.kh.model.WaybillRoute;

public interface WaybillRouteMapper {
	
    WaybillRoute getTop1ByWaybillNo(String waybillNo);
    
    int saveList(List<WaybillRoute> list);
    
    int deleteByWaybillNo(@Param("waybillNos")List<String> waybillNos);

    List<WaybillRoute> getByWaybillNo(String wayBillNo);
    
    List<String> getSignWaybillsByRecent15Days(@Param("startDate")String startDate,
    		@Param("endDate")String endDate);
}