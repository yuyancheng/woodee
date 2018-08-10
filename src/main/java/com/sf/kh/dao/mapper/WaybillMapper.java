package com.sf.kh.dao.mapper;

import java.util.List;
import com.sf.kh.model.Waybill;

public interface WaybillMapper {


    int saveList(List<Waybill> list);

    int updateByIds(List<Waybill> list);
    
    Waybill selectByWaybillNo(String waybillNo);
    
    /***
     * 插入或更新运单
     * @param list
     * @return
     */
    int saveOrUpdateList(List<Waybill> list);
}