package com.sf.kh.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: 01378178
 * @Date: 2018/6/27 15:41
 * @Description:
 */
public class BaseModel implements Serializable {

    private Long createBy;

    private Date createTm;

    private Long updateBy;

    private Date updateTm;

    private Integer version;

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTm() {
        return createTm;
    }

    public void setCreateTm(Date createTm) {
        this.createTm = createTm;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTm() {
        return updateTm;
    }

    public void setUpdateTm(Date updateTm) {
        this.updateTm = updateTm;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
