package com.insenuser.model.common;

import java.util.List;

/**
 * Description:非敏感型用户的SA区域，目前不使用
 *
 * @author kjy
 * @since Apr 25, 2020 9:14:29 AM
 */
public class SenseArea {
    // id
    private int id;
    // 用户投入在mini区域的感知时间
    private int time;
    // mini感知区域的坐标
    private List<Coordinate> corLIst;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<Coordinate> getCorLIst() {
        return corLIst;
    }

    public void setCorLIst(List<Coordinate> corLIst) {
        this.corLIst = corLIst;
    }

}
