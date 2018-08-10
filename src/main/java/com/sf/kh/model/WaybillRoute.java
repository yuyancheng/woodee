package com.sf.kh.model;

import code.ponfee.commons.model.Result;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import java.io.Serializable;
/***
 * 运单路由
 * @author 866316
 *
 */
public class WaybillRoute implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;

    private String waybillNo;

    private String routeDate;

    private String routeDescribe;

    private String barDate;

    private Short waybilStatus;

    private String day;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getRouteDate() {
        return routeDate;
    }

    public void setRouteDate(String routeDate) {
        this.routeDate = routeDate == null ? null : routeDate.trim();
    }

    public String getRouteDescribe() {
        return routeDescribe;
    }

    public void setRouteDescribe(String routeDescribe) {
        this.routeDescribe = routeDescribe == null ? null : routeDescribe.trim();
    }

	public Short getWaybilStatus() {
        return waybilStatus;
    }

    public void setWaybilStatus(Short waybilStatus) {
        this.waybilStatus = waybilStatus;
    }

	public String getBarDate() {
		return barDate;
	}

	public void setBarDate(String barDate) {
		this.barDate = barDate;
	}


    public static void main(String[] args) {
        WaybillRoute route = new WaybillRoute();
        route.setBarDate("2018-01-01 10:10:10");
        route.setRouteDate("2018-01-01 10:10:10");
        route.setRouteDescribe("签收 星期三");
        route.setWaybillNo("555555");
        route.setWaybilStatus(new Integer(2).shortValue());
        route.setId(1L);

        System.out.println(JSONObject.toJSONString(Result.success(Lists.newArrayList(route))));

    }
}