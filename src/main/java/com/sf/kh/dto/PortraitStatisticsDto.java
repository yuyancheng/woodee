package com.sf.kh.dto;

import java.io.Serializable;

/***
 * 物资纵向统计-请求参数DTO
 * @author 866316
 *
 */
public class PortraitStatisticsDto implements Serializable{

	private static final long serialVersionUID = 1L;

    private Integer type;

    private String typeName;

    private Integer version;

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}