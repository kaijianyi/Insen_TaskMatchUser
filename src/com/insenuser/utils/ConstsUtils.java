package com.insenuser.utils;

import java.util.Date;

/**
 * Description:静态类
 *
 * @author kjy
 * @since Apr 21, 2020 5:09:01 PM
 */
public class ConstsUtils {
    // 总执行次数
    public static final int RUNNUM = 5;
    // BP值
    public static final float BREAKPOINT = 0.25f;
    // 随机任务数
    public static final int TASKNUM = 40;
    // 随机用户数
    public static final int USERNUM = 140;
    // 任务最小感知时间
    public static final int TASKMINTIME = 5;
    // 任务最大感知时间
    public static final int TASKMAXTIME = 15;

    // 区域形状：每个区域有几个坐标组成
    public static final int PERAREACORNUM = 3;
    // 需要构造至少3组历史区域
    public static final int AREANUM = 3;
    // 开始时间
    public static final Date STARTTIME = TimeUtils.string2Date("2008-02-02 14:00:00");
    // 结束时间
    public static final Date ENDTIME = TimeUtils.string2Date("2008-02-02 15:00:00");
    // 异常用户开始时间
    public static final Date CARELESSSTARTTIME = TimeUtils.string2Date("2008-02-03 01:00:00");
    // 异常用户结束时间
    public static final Date CARELESSENDTIME = TimeUtils.string2Date("2008-02-03 23:00:00");

    // 任务最小编号
    public static final int TASKMINID = 1;
    // 任务最大编号
    public static final int TASKMAXID = 730;

    // 用户最小编号
    public static final int USERMINID = 1;
    // 用户最大编号
    public static final int USERMAXID = 10357;
    // 用户最小感知时间
    public static final int USERMINTIME = 5;
    // 用户最大感知时间
    public static final int USERMAXTIME = 10;
    // 用户最大感知时间
    public static final int USERMINBID = 6;
    // 用户最大感知时间
    public static final int USERMAXBID = 10;
}
