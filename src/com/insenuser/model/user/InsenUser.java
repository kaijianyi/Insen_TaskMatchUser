package com.insenuser.model.user;

import java.util.List;

import com.insenuser.model.common.Coordinate;
import com.insenuser.model.common.SenseArea;

/**
 * Description:非敏感型竞标用户类
 *
 * @author kjy
 * @since Apr 4, 2020 3:24:28 PM
 */
public class InsenUser implements Comparable<InsenUser>, Cloneable {

    // API格式：纬度lat,39，经度lon,116
    // 数据集格式：经度lon,116，纬度lat,39

    // id
    private int id;
    // 用户id
    private int userId;
    // 用户出价
    private int bid;
    // 原始感知时间
    private int originSenTime;
    // 剩余可用感知时间
    private int remainSenTime;
    // 竞拍成功的感知时间
    private int winSenTime;
    // 关联的任务id
    private List<Integer> originTaskList;
    // 用户关联的当前尚未完成的任务
    private List<Integer> unfinishTaskList;
    // 用户获胜后，关联的未完成的任务
    private List<Integer> finishTaskList;

    // 竞拍单位成本
    private String aveCost;
    // 竞拍获胜后的收益
    private String pay = "0";
    // 异常用户标记，0-正常，1异常
    private int careless;

    // 历史bid数据，若深拷贝需要特殊处理
    private List<List<Coordinate>> areaList;
    // 划分的mini感知区域，与taskIdList有关，由于用户都是理性的，默认用户会在最大相交区域投入所有的感知时间
    private List<SenseArea> saList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getOriginSenTime() {
        return originSenTime;
    }

    public void setOriginSenTime(int originSenTime) {
        this.originSenTime = originSenTime;
    }

    public int getRemainSenTime() {
        return remainSenTime;
    }

    public void setRemainSenTime(int remainSenTime) {
        this.remainSenTime = remainSenTime;
    }

    public int getWinSenTime() {
        return winSenTime;
    }

    public void setWinSenTime(int winSenTime) {
        this.winSenTime = winSenTime;
    }

    public List<Integer> getOriginTaskList() {
        return originTaskList;
    }

    public void setOriginTaskList(List<Integer> originTaskList) {
        this.originTaskList = originTaskList;
    }

    public List<Integer> getUnfinishTaskList() {
        return unfinishTaskList;
    }

    public void setUnfinishTaskList(List<Integer> unfinishTaskList) {
        this.unfinishTaskList = unfinishTaskList;
    }

    public List<Integer> getFinishTaskList() {
        return finishTaskList;
    }

    public void setFinishTaskList(List<Integer> finishTaskList) {
        this.finishTaskList = finishTaskList;
    }

    public String getAveCost() {
        return aveCost;
    }

    public void setAveCost(String aveCost) {
        this.aveCost = aveCost;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public int getCareless() {
        return careless;
    }

    public void setCareless(int careless) {
        this.careless = careless;
    }

    public List<List<Coordinate>> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<List<Coordinate>> areaList) {
        this.areaList = areaList;
    }

    public List<SenseArea> getSaList() {
        return saList;
    }

    public void setSaList(List<SenseArea> saList) {
        this.saList = saList;
    }

    @Override
    public int compareTo(InsenUser user) {
        return this.userId - user.getUserId();
    }

    @Override
    public InsenUser clone() throws CloneNotSupportedException {
        return (InsenUser) super.clone();
    }

}
