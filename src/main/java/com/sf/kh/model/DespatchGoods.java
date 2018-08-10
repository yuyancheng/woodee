package com.sf.kh.model;

import java.io.Serializable;
import java.util.Date;
/***
 * 物资清单
 * @author 866316
 *
 */
public class DespatchGoods implements Serializable{
 
	private static final long serialVersionUID = 1L;

	private Long id;

    private Long batchId;

    private Long goodsId;
    
    private BaseGoods baseGoods;//物资维表基础数据

    private Integer goodsNum;//物资数量

    private String remark;//物资备注

    private String createBy;

    private String createTm;

    private String updateTm;

    private Integer version;

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

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
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

	public BaseGoods getBaseGoods() {
		return baseGoods;
	}

	public void setBaseGoods(BaseGoods baseGoods) {
		this.baseGoods = baseGoods;
	}
}