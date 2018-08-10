package com.sf.kh.model;

import java.io.Serializable;

public class Waybill implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;

    private String waybillNo;//运单号

    private String custNo;//客户卡号，月结卡号
    
    private String consignedDate;//寄件日期

    private String consignedTime;//寄件时间

    private String paymentType;//付款方式

    private Double realWeightQty;//实际重量

    private String senderProvince;//寄方省份

    private String senderCity;//寄方城市

    private String destProvince;//收方省份

    private String destCity;//收方城市

    private String waybillStatus;//快件状态

    private String exceptionTime;//异常时间

    private String exceptionDescribe;//异常描述
    
    private String signInDate;//签收日期

    private String signInTime;//签收时间

    private String deliveryDuration;//派送时长

    private Double cubage=0.0;//体积
    
    private String cubageDescribe;//导入的体积：长-宽-高

    private String tow;//拖寄物：批次号
    
    private Integer noSign=-5;//超时未签收:默认为-1，有数字，则代表超时多少天未签收
    
    private Integer noRoute=-3;//超时无物流：默认为-1，有数字，则代表超时多少天无物流
    
    private String createTm;//同一批次时间的新增运单
    
    private String updateTm;//同一批次时间的已签收运单

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType == null ? null : paymentType.trim();
    }

    public Double getRealWeightQty() {
        return realWeightQty;
    }

    public void setRealWeightQty(Double realWeightQty) {
        this.realWeightQty = realWeightQty;
    }

    public String getSenderProvince() {
        return senderProvince;
    }

    public void setSenderProvince(String senderProvince) {
        this.senderProvince = senderProvince == null ? null : senderProvince.trim();
    }

    public String getSenderCity() {
        return senderCity;
    }

    public void setSenderCity(String senderCity) {
        this.senderCity = senderCity == null ? null : senderCity.trim();
    }

    public String getDestProvince() {
        return destProvince;
    }

    public void setDestProvince(String destProvince) {
        this.destProvince = destProvince == null ? null : destProvince.trim();
    }

    public String getDestCity() {
        return destCity;
    }

    public void setDestCity(String destCity) {
        this.destCity = destCity == null ? null : destCity.trim();
    }

    public String getWaybillStatus() {
        return waybillStatus;
    }

    public void setWaybillStatus(String waybillStatus) {
        this.waybillStatus = waybillStatus == null ? null : waybillStatus.trim();
    }
    
    public String getExceptionDescribe() {
        return exceptionDescribe;
    }

    public void setExceptionDescribe(String exceptionDescribe) {
        this.exceptionDescribe = exceptionDescribe == null ? null : exceptionDescribe.trim();
    }

    public String getDeliveryDuration() {
        return deliveryDuration;
    }

    public void setDeliveryDuration(String deliveryDuration) {
        this.deliveryDuration = deliveryDuration == null ? null : deliveryDuration.trim();
    }

    public Double getCubage() {
		return cubage;
	}

	public void setCubage(Double cubage) {
		this.cubage = cubage;
	}

	public String getCubageDescribe() {
		return cubageDescribe;
	}

	public void setCubageDescribe(String cubageDescribe) {
		this.cubageDescribe = cubageDescribe;
	}

	public String getTow() {
        return tow;
    }

    public void setTow(String tow) {
        this.tow = tow == null ? null : tow.trim();
    }

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public String getConsignedTime() {
		return consignedTime;
	}

	public void setConsignedTime(String consignedTime) {
		this.consignedTime = consignedTime;
	}

	public String getExceptionTime() {
		return exceptionTime;
	}

	public void setExceptionTime(String exceptionTime) {
		this.exceptionTime = exceptionTime;
	}

	public String getSignInTime() {
		return signInTime;
	}

	public void setSignInTime(String signInTime) {
		this.signInTime = signInTime;
	}

	public Integer getNoSign() {
		return noSign;
	}

	public void setNoSign(Integer noSign) {
		this.noSign = noSign;
	}

	public Integer getNoRoute() {
		return noRoute;
	}

	public void setNoRoute(Integer noRoute) {
		this.noRoute = noRoute;
	}

	public String getUpdateTm() {
		return updateTm;
	}

	public void setUpdateTm(String updateTm) {
		this.updateTm = updateTm;
	}

	public String getConsignedDate() {
		return consignedDate;
	}

	public void setConsignedDate(String consignedDate) {
		this.consignedDate = consignedDate;
	}

	public String getSignInDate() {
		return signInDate;
	}

	public void setSignInDate(String signInDate) {
		this.signInDate = signInDate;
	}

	public String getCreateTm() {
		return createTm;
	}

	public void setCreateTm(String createTm) {
		this.createTm = createTm;
	}
}