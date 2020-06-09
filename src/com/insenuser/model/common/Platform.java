package com.insenuser.model.common;

/**
 * Description:平台数据类
 *
 * @author kjy
 * @since Apr 13, 2020 1:34:39 PM
 */
public class Platform {

    // 标准-总支付成本
    private String normalPay = "0";

    // 标准-总感知时间
    private int normalTime;

    // 异常-总支付成本
    private String abnormalPay = "0";

    // 异常-总感知时间
    private int abnormalTime;

    // MCD-总支付成本
    private String mcdPay = "0";

    // MCD-总感知时间
    private int mcdTime;

    // 随机数据-运行时间
    private String randomRunTime = "0";

    // 标准-拍卖运行时间
    private String normalRunTime = "0";

    // 异常-拍卖运行时间
    private String abnormalRunTime = "0";

    // MCD-拍卖运行时间
    private String mcdRunTime = "0";

    public String getNormalPay() {
        return normalPay;
    }

    public void setNormalPay(String normalPay) {
        this.normalPay = normalPay;
    }

    public int getNormalTime() {
        return normalTime;
    }

    public void setNormalTime(int normalTime) {
        this.normalTime = normalTime;
    }

    public String getAbnormalPay() {
        return abnormalPay;
    }

    public void setAbnormalPay(String abnormalPay) {
        this.abnormalPay = abnormalPay;
    }

    public int getAbnormalTime() {
        return abnormalTime;
    }

    public void setAbnormalTime(int abnormalTime) {
        this.abnormalTime = abnormalTime;
    }

    public String getMcdPay() {
        return mcdPay;
    }

    public void setMcdPay(String mcdPay) {
        this.mcdPay = mcdPay;
    }

    public int getMcdTime() {
        return mcdTime;
    }

    public void setMcdTime(int mcdTime) {
        this.mcdTime = mcdTime;
    }

    public String getRandomRunTime() {
        return randomRunTime;
    }

    public void setRandomRunTime(String randomRunTime) {
        this.randomRunTime = randomRunTime;
    }

    public String getNormalRunTime() {
        return normalRunTime;
    }

    public void setNormalRunTime(String normalRunTime) {
        this.normalRunTime = normalRunTime;
    }

    public String getAbnormalRunTime() {
        return abnormalRunTime;
    }

    public void setAbnormalRunTime(String abnormalRunTime) {
        this.abnormalRunTime = abnormalRunTime;
    }

    public String getMcdRunTime() {
        return mcdRunTime;
    }

    public void setMcdRunTime(String mcdRunTime) {
        this.mcdRunTime = mcdRunTime;
    }

}
