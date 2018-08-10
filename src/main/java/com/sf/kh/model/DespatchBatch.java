package com.sf.kh.model;
/***
 * 发运批次类
 * @author 866316
 *
 */
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DespatchBatch implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;

    private Long batchId;

    private String waybillNo;

    private Long sendCompanyId;

    private String sender;

    private String senderTel;

    private String senderAddrProvince;
    
    private String senderAddrCity;
    
    private String senderAddrArea;
    
    private String senderAddrOther;

    private Integer receiveCompanyType;

    private Long receiveCompanyId;

    private String receiver;

    private String receiverTel;

    private String receiverAddrProvince;
    
    private String receiverAddrCity;
    
    private String receiverAddrArea;
    
    private String receiverAddrOther;

    private Integer goodsNum;

    private Integer packageType;

    private String remark;

    private String createBy;
    
    private String createTm;

    private String updateBy;

    private String updateTm;

    private Integer version;
    
    private String printTm;//打印时间
    
    private String senderFullAddress;//设置发件地址:Province+City+Area+Other
    
    private String receiverFullAddress;//设置收件地址：Province+City+Area+Other
    
    private String sendCompanyName;//发运单位
    
    private String receiveCompanyName;//接收单位
    
    private List<DespatchGoods> despatchGoodsList;//物资清单列表
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getWaybillNo() {
        return waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo == null ? null : waybillNo.trim();
    }

    public Long getSendCompanyId() {
        return sendCompanyId;
    }

    public void setSendCompanyId(Long sendCompanyId) {
        this.sendCompanyId = sendCompanyId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender == null ? null : sender.trim();
    }

    public String getSenderTel() {
        return senderTel;
    }

    public void setSenderTel(String senderTel) {
        this.senderTel = senderTel == null ? null : senderTel.trim();
    }

    

    public Integer getReceiveCompanyType() {
        return receiveCompanyType;
    }

    public void setReceiveCompanyType(Integer receiveCompanyType) {
        this.receiveCompanyType = receiveCompanyType;
    }

    public Long getReceiveCompanyId() {
        return receiveCompanyId;
    }

    public void setReceiveCompanyId(Long receiveCompanyId) {
        this.receiveCompanyId = receiveCompanyId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver == null ? null : receiver.trim();
    }

    public String getReceiverTel() {
        return receiverTel;
    }

    public void setReceiverTel(String receiverTel) {
        this.receiverTel = receiverTel == null ? null : receiverTel.trim();
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Integer getPackageType() {
        return packageType;
    }

    public void setPackageType(Integer packageType) {
        this.packageType = packageType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public String getCreateTm() {
		return createTm;
	}

	public void setCreateTm(String createTm) {
		this.createTm = createTm;
	}

    public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getUpdateTm() {
		return updateTm;
	}

	public void setUpdateTm(String updateTm) {
		this.updateTm = updateTm;
	}

	public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

	public String getSendCompanyName() {
		return sendCompanyName;
	}

	public void setSendCompanyName(String sendCompanyName) {
		this.sendCompanyName = sendCompanyName;
	}

	public String getReceiveCompanyName() {
		return receiveCompanyName;
	}

	public void setReceiveCompanyName(String receiveCompanyName) {
		this.receiveCompanyName = receiveCompanyName;
	}

	public List<DespatchGoods> getDespatchGoodsList() {
		return despatchGoodsList;
	}

	public void setDespatchGoodsList(List<DespatchGoods> despatchGoodsList) {
		this.despatchGoodsList = despatchGoodsList;
	}

	public String getSenderFullAddress() {
		return senderFullAddress;
	}

	public void setSenderFullAddress(String senderFullAddress) {
		this.senderFullAddress = senderFullAddress;
	}

	public String getReceiverFullAddress() {
		return receiverFullAddress;
	}

	public void setReceiverFullAddress(String receiverFullAddress) {
		this.receiverFullAddress = receiverFullAddress;
	}

	public String getSenderAddrProvince() {
		return senderAddrProvince;
	}

	public void setSenderAddrProvince(String senderAddrProvince) {
		this.senderAddrProvince = senderAddrProvince;
	}

	public String getSenderAddrCity() {
		return senderAddrCity;
	}

	public void setSenderAddrCity(String senderAddrCity) {
		this.senderAddrCity = senderAddrCity;
	}

	public String getSenderAddrArea() {
		return senderAddrArea;
	}

	public void setSenderAddrArea(String senderAddrArea) {
		this.senderAddrArea = senderAddrArea;
	}

	public String getSenderAddrOther() {
		return senderAddrOther;
	}

	public void setSenderAddrOther(String senderAddrOther) {
		this.senderAddrOther = senderAddrOther;
	}

	public String getReceiverAddrProvince() {
		return receiverAddrProvince;
	}

	public void setReceiverAddrProvince(String receiverAddrProvince) {
		this.receiverAddrProvince = receiverAddrProvince;
	}

	public String getReceiverAddrCity() {
		return receiverAddrCity;
	}

	public void setReceiverAddrCity(String receiverAddrCity) {
		this.receiverAddrCity = receiverAddrCity;
	}

	public String getReceiverAddrArea() {
		return receiverAddrArea;
	}

	public void setReceiverAddrArea(String receiverAddrArea) {
		this.receiverAddrArea = receiverAddrArea;
	}

	public String getReceiverAddrOther() {
		return receiverAddrOther;
	}

	public void setReceiverAddrOther(String receiverAddrOther) {
		this.receiverAddrOther = receiverAddrOther;
	}

	public String getPrintTm() {
		return printTm;
	}

	public void setPrintTm(String printTm) {
		this.printTm = printTm;
	}
	
}