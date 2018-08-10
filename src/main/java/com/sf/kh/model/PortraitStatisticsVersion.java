package com.sf.kh.model;

import java.io.Serializable;
/***
 * 物资纵向统计-版本号
 * @author 866316
 *
 */
public class PortraitStatisticsVersion implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long id;

    private Integer version;

    private String updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? null : updateTime.trim();
    }
}