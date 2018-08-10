package com.sf.kh.service.impl;

import com.google.common.collect.Lists;
import com.sf.kh.dao.IMonitorDao;
import com.sf.kh.model.*;
import com.sf.kh.service.IBaseGoodsService;
import com.sf.kh.service.IDepartmentService;
import com.sf.kh.service.IMonitorService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Auther: 01378178
 * @Date: 2018/7/5 20:31
 * @Description:
 */
@Service
public class MonitorServiceImpl implements IMonitorService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IMonitorDao monitorDao;

    @Resource
    private IBaseGoodsService baseGoodsService;

    @Resource
    private IDepartmentService departmentService;


    static final String INTRANSIT = "在途";
    static final String SIGNED = "已签收";
    static final String DELIVERYING = "正在派送";
    static final String EXCEPTION = "异常";
    static final String SHIPPED = "已发运";


    @Override
    public ShipmentStatistic getShipmentStatistic(Map<String, Object> params) {


        List<StatisticPair> staticValue = monitorDao.getShipmentStatistic(params);

        ShipmentStatistic shipment = new ShipmentStatistic();

        for (StatisticPair val : staticValue) {

            String status = val.getStatus();
            int count = val.getCount();

            switch (status) {
                case INTRANSIT:
                    shipment.setIntransit(count);
                    break;
                case SIGNED:
                    shipment.setSigned(count);
                    break;
                case DELIVERYING:
                    shipment.setDeliverying(count);
                    break;
                case EXCEPTION:
                    shipment.setException(count);
                    break;
                case SHIPPED:
                    shipment.setShipped(count);
                    break;
                default :
                    break;
            }
        }
        double signRatio = shipment.getShipped() == 0 ? 0 : Double.valueOf(shipment.getSigned()) / Double.valueOf(shipment.getShipped()) * 100.0;
        shipment.setSignRatio(BigDecimal.valueOf(signRatio).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        return shipment;
    }

    @Override
    public List<Shipment> getShipments(Map<String, Object> params) {
        return monitorDao.getShipments(params);
    }

    @Override
    public List<Shipment> getShipmentsWithDetail(Map<String, Object> params) {

        List<Shipment> shipments = monitorDao.getShipmentsWithDetail(params);

        if(CollectionUtils.isEmpty(shipments)){
            return Lists.newArrayList();
        }


        List<Long> ids = shipments.stream().filter(o->CollectionUtils.isNotEmpty(o.getShipmentDetails()))
                .flatMap(o->o.getShipmentDetails().stream().map(ShipmentDetail::getGoodsId))
                .collect(Collectors.toList());


        if(CollectionUtils.isEmpty(ids)){
            logger.warn("no goods detail related to bath.");
            return shipments;
        }


        List<BaseGoods> glist = baseGoodsService.getListByIds(ids);

        if(CollectionUtils.isEmpty(glist)){
            return shipments;
        }

        final Map<Long, BaseGoods> finalMap  = glist.stream().collect(Collectors.toMap(BaseGoods::getId, Function.identity()));

        for(Shipment shipment : shipments){

            List<ShipmentDetail> tds = shipment.getShipmentDetails();

            if(CollectionUtils.isNotEmpty(tds)){
                populateGoodsInfo(tds, finalMap);
            }
        }

        return shipments;
    }

    @Override
    public Integer getShipmentsWithDetailCount(Map<String, Object> params) {
        return monitorDao.getShipmentsWithDetailCount(params);
    }

    @Override
    public List<ShipmentDetail> getShipmentDetail(Long batchId) {

        List<ShipmentDetail> details = monitorDao.getShipmentDetail(batchId);

        if(CollectionUtils.isNotEmpty(details)){

            List<Long> goodIds = details.stream().map(ShipmentDetail::getGoodsId).collect(Collectors.toList());

            List<BaseGoods> glist = baseGoodsService.getListByIds(goodIds);

            if(CollectionUtils.isNotEmpty(glist)){
                final Map<Long, BaseGoods> finalMap  = glist.stream().collect(Collectors.toMap(BaseGoods::getId, Function.identity()));
                populateGoodsInfo(details, finalMap);
            }
        }
        return details;
    }

    public List<Shipment> getReceiveList(Map<String, Object> params){
        return monitorDao.getReceiveList(params);
    }


    private void populateGoodsInfo(List<ShipmentDetail> details,Map<Long, BaseGoods> finalMap){

        details.stream().forEach(g->{
            BaseGoods bg = finalMap.get(g.getGoodsId());

            g.setCategory(bg==null ? null : bg.getFirstCategoryName());
            g.setChildCategory(bg==null ? null : bg.getSecondCategoryName());
            g.setName(bg==null ? null : bg.getGoodsName());
            g.setUnit(bg==null ? null : bg.getGoodsUnit());
        });
    }
}
