package com.sf.kh.model;


import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.List;

/**
 * @Auther: 01378178
 * @Date: 2018/7/5 19:29
 * @Description:
 */
public class Shipment implements java.io.Serializable{

    private Long id;

    // 运单号
    private String waybillNo;

    // 月结卡号
    private String custNo;

    // 付款方式
    private String paymentType;

    // 运单状态
    private String waybillStatus;

    // 批次号
    private Long batchId;

    // 包裹类型: 混合, 单品
    private String packageType;

    // 物资数量
    private Integer goodsNum;

    // 发送单位地址
    private String sendAddr;

    // 接受单位地址
    private String rcvAddr;

    // 异常原因
    private String exceptionDesc;

    // 发运时间
    private Date consignedTime;

    // 签收时间
    private Date signInTime;

    private String signInTimStr;

    // 备注
    private String remark;


    private List<ShipmentDetail> shipmentDetails;


    public String getConsignedTimeStr(){
        if(this.getConsignedTime()!=null){
            return DateFormatUtils.format(this.getConsignedTime(), "yyyy-MM-dd HH:mm:ss");
        }
        return "";
    }


    public enum WaybillStatusEnum{
        SHIPPED(1, "已发运"),
        SIGNED(2, "已签收"),
        INTRANSIT(3, "在途"),
        DELIVERING(4, "正在派送"),
        EXCEPTION(5, "异常"),

        // 查询使用, 数据库中没这个状态
        TORECEIVE(6, "待接收");


        private int status;
        private String desc;

        WaybillStatusEnum(int status, String desc){
            this.status = status;
            this.desc = desc;
        }

        public int getStatus() {
            return status;
        }

        public String getDesc() {
            return desc;
        }

        public static String getDescByStatus(Integer status){
            if(status == null){
                return null;
            }

            WaybillStatusEnum[] pairs  = WaybillStatusEnum.values();
            for(WaybillStatusEnum v : pairs){
                if(v.getStatus()==status.intValue()){
                    return v.desc;
                }
            }
            return null;
        }

        public static Integer getStatusByDesc(String desc){
            WaybillStatusEnum[] pairs  = WaybillStatusEnum.values();
            for(WaybillStatusEnum v : pairs){
                if(v.getDesc().equals(desc)){
                    v.getStatus();
                }
            }
            return null;
        }
    }

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

    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getWaybillStatus() {
        return waybillStatus;
    }

    public void setWaybillStatus(String waybillStatus) {
        this.waybillStatus = waybillStatus;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getSendAddr() {
        return sendAddr;
    }

    public void setSendAddr(String sendAddr) {
        this.sendAddr = sendAddr;
    }

    public String getRcvAddr() {
        return rcvAddr;
    }

    public void setRcvAddr(String rcvAddr) {
        this.rcvAddr = rcvAddr;
    }

    public String getExceptionDesc() {
        return exceptionDesc;
    }

    public void setExceptionDesc(String exceptionDesc) {
        this.exceptionDesc = exceptionDesc;
    }

    public Date getConsignedTime() {
        return consignedTime;
    }

    public void setConsignedTime(Date consignedTime) {
        this.consignedTime = consignedTime;
    }

    public List<ShipmentDetail> getShipmentDetails() {
        return shipmentDetails;
    }

    public void setShipmentDetails(List<ShipmentDetail> shipmentDetails) {
        this.shipmentDetails = shipmentDetails;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getSignInTime() {
        return signInTime;
    }

    public void setSignInTime(Date signInTime) {
        this.signInTime = signInTime;
    }

    public String getSignInTimStr() {
        if(this.getSignInTime()!=null){
            return DateFormatUtils.format(this.getConsignedTime(), "yyyy-MM-dd HH:mm:ss");
        }
        return "";
    }

    public void setSignInTimStr(String signInTimStr) {
        this.signInTimStr = signInTimStr;
    }
}
