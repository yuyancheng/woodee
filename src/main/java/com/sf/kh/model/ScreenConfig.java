package com.sf.kh.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.ImmutableMap;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Big screen config
 *
 * @author 01367825
 */
public class ScreenConfig implements java.io.Serializable {

    private static final long serialVersionUID = -2413923381336251675L;

    public static final String SCREEN_OVERVIEW = "waybill-qty-overview";
    public static final String SCREEN_MONITOR = "waybill-status-monitor";
    public static final String SCREEN_DESPATCH = "waybill-despatch-analysis";
    public static final String SCREEN_FLOW = "waybill-flow-analysis";
    public static final Map<String, String> SCREEN_CODES = ImmutableMap.of(
        SCREEN_OVERVIEW, "快件概览",
        SCREEN_MONITOR, "状态监控",
        SCREEN_DESPATCH,"配送分析",
        SCREEN_FLOW,"流向分析"
    );

    private Long id; // 主键ID
    private Long userId; // 用户ID
    private String screenCode; // 大屏代码
    private String screenName; // 大屏名称

    private Long senderOrgTypeId; // 发送方组织类型ID
    private Long senderOrgId; // 发送单位组织ID
    private Long senderDeptId; // 发送单位ID
    private Long receiverOrgTypeId; // 接收方组织类型ID
    private Long receiverOrgId; // 接收单位组织ID
    private Long receiverDeptId; // 接收单位ID

    private Long specialtyId; // 专业ID
    //private String specialtyName; // 专业名称
    private Long categoryId; // 类别ID
    //private String categoryName; // 类别名称
    private Long goodsId; // 物资ID
    //private String goodsName; // 物资名称
    private String startCity; // 始发城市

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTm; // 统计开始时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTm; // 统计查询结束时间

    private Date createTm; // 创建时间
    private Date updateTm; // 更新时间
    private int version; // 版本号

    // -----------------------------------------数据库查询用
    private List<Long> senderDeptIds; // 发送单位ID列表
    private List<String> senderCustNos; // 发送单位月结卡号列表
    private List<Long> receiverDeptIds; // 接收单位ID列表
    private List<String> receiverCustNos; // 接收单位月结卡号列表
    private List<Long> goodsIds; // 物资ID列表

    // -----------------------------------------前端编辑字段回填用
    //private List<Long> senderDeptIdPath; // 发送单位组织ID路径
    //private List<Long> receiverOrgIdPath; // 接收单位组织ID路径

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getScreenCode() {
        return screenCode;
    }

    public void setScreenCode(String screenCode) {
        this.screenCode = screenCode;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public Long getSenderOrgId() {
        return senderOrgId;
    }

    public void setSenderOrgId(Long senderOrgId) {
        this.senderOrgId = senderOrgId;
    }

    public Long getSenderDeptId() {
        return senderDeptId;
    }

    public void setSenderDeptId(Long senderDeptId) {
        this.senderDeptId = senderDeptId;
    }

    public Long getReceiverOrgId() {
        return receiverOrgId;
    }

    public void setReceiverOrgId(Long receiverOrgId) {
        this.receiverOrgId = receiverOrgId;
    }

    public Long getReceiverDeptId() {
        return receiverDeptId;
    }

    public void setReceiverDeptId(Long receiverDeptId) {
        this.receiverDeptId = receiverDeptId;
    }

    public Long getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(Long specialtyId) {
        this.specialtyId = specialtyId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Date getStartTm() {
        return startTm;
    }

    public void setStartTm(Date startTm) {
        this.startTm = startTm;
    }

    public Date getEndTm() {
        return endTm;
    }

    public void setEndTm(Date endTm) {
        this.endTm = endTm;
    }

    public Date getCreateTm() {
        return createTm;
    }

    public void setCreateTm(Date createTm) {
        this.createTm = createTm;
    }

    public Date getUpdateTm() {
        return updateTm;
    }

    public void setUpdateTm(Date updateTm) {
        this.updateTm = updateTm;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<Long> getSenderDeptIds() {
        return senderDeptIds;
    }

    public void setSenderDeptIds(List<Long> senderDeptIds) {
        this.senderDeptIds = senderDeptIds;
    }

    public List<Long> getReceiverDeptIds() {
        return receiverDeptIds;
    }

    public void setReceiverDeptIds(List<Long> receiverDeptIds) {
        this.receiverDeptIds = receiverDeptIds;
    }

    public List<String> getSenderCustNos() {
        return senderCustNos;
    }

    public void setSenderCustNos(List<String> senderCustNos) {
        this.senderCustNos = senderCustNos;
    }

    public List<String> getReceiverCustNos() {
        return receiverCustNos;
    }

    public void setReceiverCustNos(List<String> receiverCustNos) {
        this.receiverCustNos = receiverCustNos;
    }

    public List<Long> getGoodsIds() {
        return goodsIds;
    }

    public void setGoodsIds(List<Long> goodsIds) {
        this.goodsIds = goodsIds;
    }

    public Long getSenderOrgTypeId() {
        return senderOrgTypeId;
    }

    public void setSenderOrgTypeId(Long senderOrgTypeId) {
        this.senderOrgTypeId = senderOrgTypeId;
    }

    public Long getReceiverOrgTypeId() {
        return receiverOrgTypeId;
    }

    public void setReceiverOrgTypeId(Long receiverOrgTypeId) {
        this.receiverOrgTypeId = receiverOrgTypeId;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }
}
