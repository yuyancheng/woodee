package com.sf.kh.model;

import java.io.Serializable;

/***
 * 物资纵向统计-类别/物资top10
 * @author 866316
 *
 */
public class PortraitStatisticsName implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;

    private Integer type;

    private String typeName;

    private String top10Name;

    private Long goodsNum;

    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public String getTop10Name() {
        return top10Name;
    }

    public void setTop10Name(String top10Name) {
        this.top10Name = top10Name == null ? null : top10Name.trim();
    }

    public Long getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Long goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}