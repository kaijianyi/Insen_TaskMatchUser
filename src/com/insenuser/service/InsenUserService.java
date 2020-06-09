package com.insenuser.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.insenuser.model.common.Coordinate;
import com.insenuser.model.user.InsenUser;
import com.insenuser.model.user.TxtUser;
import com.insenuser.utils.ConstsUtils;
import com.insenuser.utils.FileUtils;
import com.insenuser.utils.RandomUtils;
import com.insenuser.utils.TimeUtils;

public class InsenUserService {

    /**
     * 生成随机用户
     * 
     * @param userNum
     * @param userMinId
     * @param userMaxId
     * @param userMinTime
     * @param userMaxTime
     * @param userMinBid
     * @param userMaxBid
     * @return
     * @throws CloneNotSupportedException
     */
    public static List<InsenUser> getRandomUser(int userNum, int userMinId, int userMaxId, int userMinTime,
            int userMaxTime, int userMinBid, int userMaxBid) throws CloneNotSupportedException {
        List<InsenUser> insenUserBidList = new ArrayList<InsenUser>();
        // 防止生成重复数字
        List<Integer> exitList = new ArrayList<Integer>();
        while (insenUserBidList.size() < userNum) {
            int ranUserId = RandomUtils.getRandom(userMinId, userMaxId);
            if (!exitList.contains(ranUserId)) {
                exitList.add(ranUserId);
                // 读取对应id的文件数据
                InsenUser insenUser = readFile(ranUserId);
                // 对应id数据满足条件，则生成感知时间等数据
                if (insenUser != null) {
                    // 用户id范围是[1，10357]
                    insenUser.setUserId(ranUserId);
                    // 感知时间范围是[5，10]
                    int senTime = RandomUtils.getRandom(userMinTime, userMaxTime);
                    insenUser.setOriginSenTime(senTime);
                    insenUser.setRemainSenTime(senTime);
                    // 竞标成本范围是[6，10]
                    insenUser.setBid(RandomUtils.getRandom(userMinBid, userMaxBid));
                    insenUserBidList.add(insenUser);
                }
            }
        }
        // 按照userId升序
        Collections.sort(insenUserBidList);
        return insenUserBidList;
    }

    /*
     * 根据userId读取文件
     */
    public static InsenUser readFile(int ranUserId) throws CloneNotSupportedException {
        InsenUser insenUserBid = new InsenUser();
        // 文件路径
        String filePath = "/Users/kjy/Downloads/MACBOOK/Paper/taxi/" + ranUserId + ".txt";
        // 读取全部文档数据
        List<String> txtUserStrList = FileUtils.readTxtFile(filePath);
        // 转换为对应类
        List<TxtUser> txtUserList = str2TxtUser(txtUserStrList);
        // 筛选范围内的时间数据
        List<TxtUser> filterUserList = new ArrayList<TxtUser>();
        for (TxtUser txtUser : txtUserList) {
            if (TimeUtils.isEffectiveDate(TimeUtils.string2Date(txtUser.getGpsTime()), ConstsUtils.STARTTIME,
                    ConstsUtils.ENDTIME)) {
                // 深拷贝
                filterUserList.add(txtUser.clone());
            }
        }
        // 按时间升序
        Collections.sort(filterUserList);
        // MCD算法要求
        if (filterUserList.size() >= ConstsUtils.AREANUM * ConstsUtils.PERAREACORNUM) {
            insenUserBid = createArea(filterUserList);
            return insenUserBid;
        }
        return null;
    }

    /*
     * 字符串转换为对象
     */
    private static List<TxtUser> str2TxtUser(List<String> strList) {
        List<TxtUser> txtUserList = new ArrayList<TxtUser>();
        for (int i = 0; i < strList.size(); i++) {
            TxtUser txtUser = new TxtUser();
            String[] userArr = strList.get(i).split("\\,");

            txtUser.setUserId(Integer.valueOf(userArr[0]));
            txtUser.setGpsTime(userArr[1]);

            Coordinate cor = new Coordinate();
            cor.setGpsTime(userArr[1]);
            cor.setLon(userArr[2]);
            cor.setLat(userArr[3]);

            txtUser.setCor(cor);

            txtUserList.add(txtUser);
        }
        return txtUserList;
    }

    /*
     * 构造区域
     */
    private static InsenUser createArea(List<TxtUser> filterUserList) {
        InsenUser insenUser = new InsenUser();
        // 合并GPS数据
        List<List<Coordinate>> areaList = new ArrayList<List<Coordinate>>();
        // 可生成的历史区域数量
        int areaNum = filterUserList.size() / ConstsUtils.PERAREACORNUM;
        // [0,3,6],[1,4,7],[2,5,8]，间隔获取坐标来组建区域
        for (int i = 0; i < areaNum; i++) {
            List<Coordinate> corList = new ArrayList<Coordinate>();
            for (int j = i; j < filterUserList.size(); j += areaNum) {
                Coordinate cor = filterUserList.get(j).getCor();
                corList.add(cor);
            }
            areaList.add(corList);
        }
        insenUser.setAreaList(areaList);
        return insenUser;
    }

    /**
     * 根据userId增加一条异常数据
     * 
     * @param carelessUser
     * @return
     */
    // TODO 完成该算法
    public static InsenUser readCarelessFile(InsenUser carelessUser) {
        return null;
    }

}