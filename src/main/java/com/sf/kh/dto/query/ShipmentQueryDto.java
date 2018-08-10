package com.sf.kh.dto.query;

import java.util.List;

/**
 * @Auther: 01378178
 * @Date: 2018/7/5 19:46
 * @Description:
 */
public class ShipmentQueryDto {

    // 付款方式
    private String paymentType;


    // 寄件时间 from
    private String timeFrom;

    // 寄件时间 to
    private String timeTo;

    // 发运组织类型id
    private Long sendOrgTypeId;

    // 发送组织
    private Long sendOrgId;

    // 发送单位
    private Long sendDeptId;

    // 接收组织类型id
    private Long rcvOrgTypeId;

    // 接收组织
    private Long rcvOrgId;

    // 接收单位
    private Long rcvDeptId;

    // 运单状态
    private String waybillStatus;

    // 单号
    private List<String> bisNums;

    // 当前页
    private Integer pageNum;

    // 每页大小
    private Integer pageSize;

    // 月结卡号
    private String custNo;

    /**
     * 查询的状态标志[1 已发运 ， 2 已签收， 3 在途， 4 正在派送 5 异常]
     *
     */
    private int waybillStatusCode;

    public static final int SHIPPED = 1;
    public static final int SIGNED = 2;
    public static final int INTRANSIT = 3;
    public static final int DELIVERING = 4;
    public static final int EXCEPTION = 5;

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public Long getSendOrgId() {
        return sendOrgId;
    }

    public void setSendOrgId(Long sendOrgId) {
        this.sendOrgId = sendOrgId;
    }

    public Long getSendDeptId() {
        return sendDeptId;
    }

    public void setSendDeptId(Long sendDeptId) {
        this.sendDeptId = sendDeptId;
    }

    public Long getRcvOrgId() {
        return rcvOrgId;
    }

    public void setRcvOrgId(Long rcvOrgId) {
        this.rcvOrgId = rcvOrgId;
    }

    public Long getRcvDeptId() {
        return rcvDeptId;
    }

    public void setRcvDeptId(Long rcvDeptId) {
        this.rcvDeptId = rcvDeptId;
    }

    public String getWaybillStatus() {
        return waybillStatus;
    }

    public void setWaybillStatus(String waybillStatus) {
        this.waybillStatus = waybillStatus;
    }

    public List<String> getBisNums() {
        return bisNums;
    }

    public void setBisNums(List<String> bisNums) {
        this.bisNums = bisNums;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public int getWaybillStatusCode() {
        return waybillStatusCode;
    }

    public void setWaybillStatusCode(int waybillStatusCode) {
        this.waybillStatusCode = waybillStatusCode;
    }

    public Long getSendOrgTypeId() {
        return sendOrgTypeId;
    }

    public void setSendOrgTypeId(Long sendOrgTypeId) {
        this.sendOrgTypeId = sendOrgTypeId;
    }

    public Long getRcvOrgTypeId() {
        return rcvOrgTypeId;
    }

    public void setRcvOrgTypeId(Long rcvOrgTypeId) {
        this.rcvOrgTypeId = rcvOrgTypeId;
    }
}
