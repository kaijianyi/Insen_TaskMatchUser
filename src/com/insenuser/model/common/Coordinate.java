package com.insenuser.model.common;

/**
 * Description:坐标类
 *
 * @author kjy
 * @since Apr 4, 2020 3:24:09 PM
 */
public class Coordinate {
    // API格式：纬度lat,39，经度lon,116
    // 数据集格式：经度lon,116，纬度lat,39

    // 经度
    private String lon;

    // 纬度
    private String lat;

    // GPS上报时间
    private String gpsTime;

    public Coordinate() {
        super();
    }

    public Coordinate(String lon, String lat, String gpsTime) {
        super();
        this.lon = lon;
        this.lat = lat;
        this.gpsTime = gpsTime;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(String gpsTime) {
        this.gpsTime = gpsTime;
    }

    @Override
    public String toString() {
        return lon + "," + lat;
    }

}
