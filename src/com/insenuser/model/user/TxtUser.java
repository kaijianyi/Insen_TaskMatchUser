package com.insenuser.model.user;

import com.insenuser.model.common.Coordinate;

/**
 * Description:对应TXT文件用户类
 *
 * @author kjy
 * @since Apr 5, 2020 8:10:01 PM
 */
public class TxtUser implements Comparable<TxtUser>, Cloneable {
    // API格式：纬度39，经度116
    // 数据集格式：经度116，纬度39

    // id
    private int id;
    // 用户id
    private int userId;
    // 时间
    private String gpsTime;
    // 经纬度
    private Coordinate cor;

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

    public String getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(String gpsTime) {
        this.gpsTime = gpsTime;
    }

    public Coordinate getCor() {
        return cor;
    }

    public void setCor(Coordinate cor) {
        this.cor = cor;
    }

    @Override
    public int compareTo(TxtUser txtUser) {
        return this.gpsTime.compareTo(txtUser.getGpsTime());
    }

    @Override
    public TxtUser clone() throws CloneNotSupportedException {
        return (TxtUser) super.clone();
    }
}
