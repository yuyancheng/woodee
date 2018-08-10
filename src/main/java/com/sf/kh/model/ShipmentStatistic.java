package com.sf.kh.model;

/**
 * @Auther: 01378178
 * @Date: 2018/7/5 20:09
 * @Description:
 */
public class ShipmentStatistic {

    // 已发送
    private int shipped;

    // 已签收
    private int signed;

    // 在途
    private int intransit;

    // 派送中
    private int deliverying;

    // 异常
    private int exception;

    // 签收率
    private double signRatio;

    public int getShipped() {
        return shipped;
    }

    public void setShipped(int shipped) {
        this.shipped = shipped;
    }

    public int getSigned() {
        return signed;
    }

    public void setSigned(int signed) {
        this.signed = signed;
    }

    public int getIntransit() {
        return intransit;
    }

    public void setIntransit(int intransit) {
        this.intransit = intransit;
    }

    public int getDeliverying() {
        return deliverying;
    }

    public void setDeliverying(int deliverying) {
        this.deliverying = deliverying;
    }

    public int getException() {
        return exception;
    }

    public void setException(int exception) {
        this.exception = exception;
    }

    public double getSignRatio() {
        return signRatio;
    }

    public void setSignRatio(double signRatio) {
        this.signRatio = signRatio;
    }
}
