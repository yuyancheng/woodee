package com.sf.kh.dao.impl;

import code.ponfee.commons.model.PageHandler;
import com.sf.kh.dao.IMonitorDao;
import com.sf.kh.dao.mapper.MonitorMapper;
import com.sf.kh.model.Shipment;
import com.sf.kh.model.ShipmentDetail;
import com.sf.kh.model.StatisticPair;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/7/5 20:35
 * @Description:
 */
@Repository
public class MonitorDaoImpl implements IMonitorDao {

    @Resource
    private MonitorMapper monitorMapper;



    @Override
    public List<StatisticPair> getShipmentStatistic(Map<String, Object> params) {
        return monitorMapper.getShipmentStatistic(params);
    }

    @Override
    public List<Shipment> getShipments(Map<String, Object> params) {
        PageHandler.NORMAL.handle(params);
        return  monitorMapper.getShipments(params);
    }
    @Override
    public List<Shipment> getReceiveList(Map<String, Object> params) {
        PageHandler.NORMAL.handle(params);
        return  monitorMapper.getReceiveList(params);
    }

    @Override
    public List<Shipment> getShipmentsWithDetail(Map<String, Object> params) {

        PageHandler.NORMAL.handle(params);
        return monitorMapper.getShipmentsWithDetail(params);
    }


    @Override
    public Integer getShipmentsWithDetailCount(Map<String, Object> params){
        return monitorMapper.getShipmentsWithDetailCount(params);
    }

    @Override
    public List<ShipmentDetail> getShipmentDetail(Long batchId) {
        return monitorMapper.getShipmentDetail(batchId);
    }

}
