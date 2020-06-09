package com.insenuser.service;

import java.util.List;

import com.insenuser.model.common.Platform;
import com.insenuser.model.user.InsenUser;
import com.insenuser.utils.NumberUtils;

public class DataService {

    /**
     * 获得正常汇总数据
     * 
     * @param platform
     * @param normalWinnerList
     * @return
     */
    public static Platform getNormalTotal(Platform platform, List<InsenUser> normalWinnerList) {
        for (InsenUser normal : normalWinnerList) {
            platform.setNormalTime(platform.getNormalTime() + normal.getWinSenTime());
            platform.setNormalPay(NumberUtils.addStr(platform.getNormalPay(), normal.getPay()));
        }
        return platform;
    }

    /**
     * 获取异常汇总数据
     * 
     * @param platform
     * @param abnormalWinnerList
     * @return
     */
    public static Platform getAbnormalTotal(Platform platform, List<InsenUser> abnormalWinnerList) {
        for (InsenUser abnormal : abnormalWinnerList) {
            // 因为异常用户的等待时间
            if (abnormal.getCareless() == 1) {
                // 设置异常感知时间
                platform.setAbnormalTime(platform.getAbnormalTime() + abnormal.getWinSenTime());
            }
            if (abnormal.getCareless() == 0) {
                // 统计感知时间
                platform.setAbnormalTime(platform.getAbnormalTime() + abnormal.getWinSenTime());
                // 统计支付
                platform.setAbnormalPay(NumberUtils.addStr(platform.getAbnormalPay(), abnormal.getPay()));
            }
        }
        return platform;
    }

    /**
     * 获得MCD拍卖数据
     * 
     * @param platform
     * @param mcdWinnerList
     * @return
     */
    public static Platform getMcdTotal(Platform platform, List<InsenUser> mcdWinnerList) {
        for (InsenUser mcdWinner : mcdWinnerList) {
            platform.setMcdTime(platform.getMcdTime() + mcdWinner.getWinSenTime());
            platform.setMcdPay(NumberUtils.addStr(platform.getMcdPay(), mcdWinner.getPay()));
        }
        return platform;
    }

}
