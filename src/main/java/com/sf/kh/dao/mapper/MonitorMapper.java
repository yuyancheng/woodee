package com.sf.kh.dao.mapper;

import com.sf.kh.model.Shipment;
import com.sf.kh.model.ShipmentDetail;
import com.sf.kh.model.StatisticPair;

import java.util.List;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/7/5 20:36
 * @Description:
 */
public interface MonitorMapper {

    List<StatisticPair> getShipmentStatistic(Map<String, Object> params);

    List<Shipment> getShipments(Map<String, Object> params);

    List<Shipment> getShipmentsWithDetail(Map<String, Object> params);

    List<ShipmentDetail> getShipmentDetail(Long batchId);

    List<Shipment> getReceiveList(Map<String, Object> params);

    Integer getShipmentsWithDetailCount(Map<String, Object> params);
}
