package com.insenuser.main;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.insenuser.model.common.Platform;
import com.insenuser.model.common.PlatformTotal;
import com.insenuser.model.task.Task;
import com.insenuser.model.user.InsenUser;
import com.insenuser.service.DataService;
import com.insenuser.service.InsenAuctionService;
import com.insenuser.service.McdService;
import com.insenuser.service.RandomService;
import com.insenuser.utils.ConstsUtils;
import com.insenuser.utils.JsonUtils;
import com.insenuser.utils.NumberUtils;

public class InsenUserStart {

    public static void main(String[] args) throws IOException, CloneNotSupportedException {

        // 汇总数据
        List<Platform> platformList = new ArrayList<Platform>();

        // 开始迭代
        for (int i = 1; i <= ConstsUtils.RUNNUM; i++) {
            // 平台汇总数据
            Platform platform = new Platform();

            System.out.println("\n$$$$$$$$$$$$$$$$$$$$$ 开始生成随机数据 $$$$$$$$$$$$$$$$$$$$$");

            Instant ranTimeStart = Instant.now();

            // 实验-用户数据
            List<InsenUser> originBidUserList = new ArrayList<InsenUser>();
            // 实验-任务数据
            List<Task> originTaskList = new ArrayList<Task>();
            // 生成随机数据
            HashMap<List<InsenUser>, List<Task>> originBidMap = RandomService.getRandomData();
            // 从Map中获取实验数据
            for (Map.Entry<List<InsenUser>, List<Task>> originBidEntry : originBidMap.entrySet()) {
                originBidUserList = originBidEntry.getKey();
                originTaskList = originBidEntry.getValue();
            }

            // 生成包含异常用户Json数据，备份
            String originBidUserListStr = JsonUtils.objToFastjson(originBidUserList);
            // 生成任务Json数据，备份
            String originTaskListStr = JsonUtils.objToFastjson(originTaskList);

            Instant ranTimeEnd = Instant.now();
            long ranRunTime = Duration.between(ranTimeStart, ranTimeEnd).toMillis();
            platform.setRandomRunTime(String.valueOf(ranRunTime));

            System.out.println("\n$$$$$$$$$$$$$$$$$$$$$ 结束生成随机数据 $$$$$$$$$$$$$$$$$$$$$");

            System.out.println("\n$$$$$$$$$$$$$$$$$$$$$ 正常拍卖开始 $$$$$$$$$$$$$$$$$$$$$");

            Instant normalTimeStart = Instant.now();

            // 由于下层服务使用深拷贝，并不会改变上层数据，所以可以直接使用originBidUserList，originTaskList
            List<InsenUser> normalWinnerList = InsenAuctionService.startAuction(originBidUserList, originTaskList);

            Instant normalTimeEnd = Instant.now();
            long normalRunTime = Duration.between(normalTimeStart, normalTimeEnd).toMillis();
            // 统计总的支付+时间
            platform.setNormalRunTime(String.valueOf(normalRunTime));
            platform = DataService.getNormalTotal(platform, normalWinnerList);
            System.out.println("\n$$$$$$$$$$$$$$$$$$$$$ 正常拍卖结束 $$$$$$$$$$$$$$$$$$$$$");

            System.out.println("\n$$$$$$$$$$$$$$$$$$$$$ 异常拍卖开始 $$$$$$$$$$$$$$$$$$$$$");

            Instant abnormalTimeStart = Instant.now();

            // 无需重复拍卖
            List<InsenUser> abnormalWinnerList = InsenAuctionService.startAuction(originBidUserList, originTaskList);

            // 判断是否存在异常
            boolean isAbnormal = McdService.isAbnormal(abnormalWinnerList);
            // 浅拷贝，无影响
            List<InsenUser> nowWinnerList = abnormalWinnerList;
            // 本轮拍卖存在异常用户
            while (isAbnormal) {
                // 备份用户数据，每次重置
                List<InsenUser> abnormalBidUserList = JsonUtils.fastjsonToObj(originBidUserListStr,
                        new TypeToken<List<InsenUser>>() {
                        }.getType());
                // 备份任务数据，每次重置
                List<Task> abnormalTaskList = JsonUtils.fastjsonToObj(originTaskListStr, new TypeToken<List<Task>>() {
                }.getType());

                // System.out.println("\n>>>>>>重新拍卖前-原始用户信息：" + originBidUserList.size());
                // for (InsenUser nowBidUser : originBidUserList) {
                // System.out.println("ID：" + nowBidUser.getId() + ", 用户ID：" + nowBidUser.getUserId() + ", 报价："
                // + nowBidUser.getBid() + ", 初始感知时间:" + nowBidUser.getOriginSenTime() + ", 剩余感知时间:"
                // + nowBidUser.getRemainSenTime() + ", 初始关联任务:"
                // + Arrays.toString(nowBidUser.getOriginTaskList().toArray()) + ", 未完成任务:"
                // + Arrays.toString(nowBidUser.getUnfinishTaskList().toArray()) + ", 获胜感知时间:"
                // + nowBidUser.getWinSenTime() + ", 单位成本:" + nowBidUser.getAveCost() + ", 用户获胜任务:"
                // + nowBidUser.getFinishTaskList());
                // }

                // System.out.println("\n>>>>>>当前异常用户id：");
                // for (InsenUser nowBidUser : nowWinnerList) {
                // if (nowBidUser.getCareless() == 1) {
                // System.out.print(nowBidUser.getUserId() + ",");
                // }
                // }

                // 重置竞拍任务数据
                abnormalTaskList = McdService.getReAuctionTaskData(nowWinnerList, abnormalTaskList);

                // 重置参与竞拍的用户集，从初始拍卖数据中删除所有winner，无需其他操作
                for (InsenUser abnormalWinner : abnormalWinnerList) {
                    Iterator<InsenUser> itAbnormalBidUser = abnormalBidUserList.iterator();
                    while (itAbnormalBidUser.hasNext()) {
                        InsenUser deleteUser = itAbnormalBidUser.next();
                        if (abnormalWinner.getUserId() == deleteUser.getUserId()) {
                            itAbnormalBidUser.remove();
                            break;
                        }
                    }
                }

                // System.out.println("\n>>>>>>重新拍卖前-删除winner：" + abnormalBidUserList.size());
                // for (InsenUser nowBidUser : abnormalBidUserList) {
                // System.out.println("ID：" + nowBidUser.getId() + ", 用户ID：" + nowBidUser.getUserId() + ", 报价："
                // + nowBidUser.getBid() + ", 初始感知时间:" + nowBidUser.getOriginSenTime() + ", 剩余感知时间:"
                // + nowBidUser.getRemainSenTime() + ", 初始关联任务:"
                // + Arrays.toString(nowBidUser.getOriginTaskList().toArray()) + ", 未完成任务:"
                // + Arrays.toString(nowBidUser.getUnfinishTaskList().toArray()) + ", 获胜感知时间:"
                // + nowBidUser.getWinSenTime() + ", 单位成本:" + nowBidUser.getAveCost() + ", 用户获胜任务:"
                // + nowBidUser.getFinishTaskList());
                // }

                // 再次进行拍卖后的结果
                nowWinnerList = InsenAuctionService.startAuction(abnormalBidUserList, abnormalTaskList);

                // System.out.println("\nNext获胜者：");
                // for (InsenUser nowBidUser : nowWinnerList) {
                // System.out.println("ID：" + nowBidUser.getId() + ", 用户ID：" + nowBidUser.getUserId() + ", 报价："
                // + nowBidUser.getBid() + ", 初始感知时间:" + nowBidUser.getOriginSenTime() + ", 剩余感知时间:"
                // + nowBidUser.getRemainSenTime() + ", 初始关联任务:"
                // + Arrays.toString(nowBidUser.getOriginTaskList().toArray()) + ", 未完成任务:"
                // + Arrays.toString(nowBidUser.getUnfinishTaskList().toArray()) + ", 获胜感知时间:"
                // + nowBidUser.getWinSenTime() + ", 单位成本:" + nowBidUser.getAveCost() + ", 用户获胜任务:"
                // + nowBidUser.getFinishTaskList());
                // }

                // 合并所有的获胜者，包括异常+正常用户
                abnormalWinnerList.addAll(nowWinnerList);
                // 判断重新拍卖后是否有异常用户
                isAbnormal = McdService.isAbnormal(nowWinnerList);

                // System.out.println("\n所有获胜者：" + abnormalWinnerList.size());
                // for (InsenUser nowBidUser : abnormalWinnerList) {
                // System.out.println("ID：" + nowBidUser.getId() + ", 用户ID：" + nowBidUser.getUserId() + ", 报价："
                // + nowBidUser.getBid() + ", 初始感知时间:" + nowBidUser.getOriginSenTime() + ", 剩余感知时间:"
                // + nowBidUser.getRemainSenTime() + ", 初始关联任务:"
                // + Arrays.toString(nowBidUser.getOriginTaskList().toArray()) + ", 未完成任务:"
                // + Arrays.toString(nowBidUser.getUnfinishTaskList().toArray()) + ", 获胜感知时间:"
                // + nowBidUser.getWinSenTime() + ", 单位成本:" + nowBidUser.getAveCost() + ", 用户获胜任务:"
                // + nowBidUser.getFinishTaskList());
                // }
            }

            Instant abnormalTimeEnd = Instant.now();
            long abnormalRunTime = Duration.between(abnormalTimeStart, abnormalTimeEnd).toMillis();
            // 统计总的支付+时间
            platform.setAbnormalRunTime(String.valueOf(abnormalRunTime));
            platform = DataService.getAbnormalTotal(platform, abnormalWinnerList);

            System.out.println("\n$$$$$$$$$$$$$$$$$$$$$ 异常拍卖结束 $$$$$$$$$$$$$$$$$$$$$");

            System.out.println("\n$$$$$$$$$$$$$$$$$$$$$ MCD拍卖开始 $$$$$$$$$$$$$$$$$$$$$");

            Instant mcdTimeStart = Instant.now();

            // 备份用户数据
            List<InsenUser> mcdBidUserList = JsonUtils.fastjsonToObj(originBidUserListStr,
                    new TypeToken<List<InsenUser>>() {
                    }.getType());
            // 通过MCD算法去除异常用户数据
            mcdBidUserList = McdService.getMcdSenuser(mcdBidUserList);
            // 进行MCD拍卖
            List<InsenUser> mcdWinnerList = InsenAuctionService.startAuction(mcdBidUserList, originTaskList);
            // 获取MCD拍卖的数据
            platform = DataService.getMcdTotal(platform, mcdWinnerList);

            Instant mcdTimeEnd = Instant.now();
            long mcdRunTime = Duration.between(mcdTimeStart, mcdTimeEnd).toMillis();
            // 统计总的支付+时间
            platform.setMcdRunTime(String.valueOf(mcdRunTime));

            System.out.println("\n$$$$$$$$$$$$$$$$$$$$$ MCD拍卖结束 $$$$$$$$$$$$$$$$$$$$$");

            System.out.println("\n~~~~~~~~~~第" + i + "轮拍卖结束~~~~~~~~~\n");

            // 存入当前汇总数据
            platformList.add(platform);

            System.out.println();
        }

        PlatformTotal platformTotal = new PlatformTotal();

        // 数据求和
        for (Platform platform : platformList) {

            // 求和，100次随机数据-运行时间
            platformTotal.setRandomRunTimeTotal(
                    NumberUtils.addStr(platformTotal.getRandomRunTimeTotal(), platform.getRandomRunTime()));

            // 求和，100次标准-感知时间
            platformTotal.setNormalTimeTotal(platformTotal.getNormalTimeTotal() + platform.getNormalTime());
            // 求和，100次标准-支付
            platformTotal
                    .setNormalPayTotal(NumberUtils.addStr(platformTotal.getNormalPayTotal(), platform.getNormalPay()));
            // 求和，100次标准-运行时间
            platformTotal.setNormalRunTimeTotal(
                    NumberUtils.addStr(platformTotal.getNormalRunTimeTotal(), platform.getNormalRunTime()));

            // 求和，100次异常-感知时间
            platformTotal.setAbnormalTimeTotal(platformTotal.getAbnormalTimeTotal() + platform.getAbnormalTime());
            // 求和，100次异常-支付
            platformTotal.setAbnormalPayTotal(
                    NumberUtils.addStr(platformTotal.getAbnormalPayTotal(), platform.getAbnormalPay()));
            // 求和，100次异常-运行时间
            platformTotal.setAbnormalRunTimeTotal(
                    NumberUtils.addStr(platformTotal.getAbnormalRunTimeTotal(), platform.getAbnormalRunTime()));

            // 求和，100次MCD-感知时间
            platformTotal.setMcdTimeTotal(platformTotal.getMcdTimeTotal() + platform.getMcdTime());
            // 求和，100次MCD-支付
            platformTotal.setMcdPayTotal(NumberUtils.addStr(platformTotal.getMcdPayTotal(), platform.getMcdPay()));
            // 求和，100次MCD-运行时间
            platformTotal.setMcdRunTimeTotal(
                    NumberUtils.addStr(platformTotal.getMcdRunTimeTotal(), platform.getMcdRunTime()));

        }

        // 平均，100次随机数据-运行时间
        platformTotal.setRandomRunTimeAve(NumberUtils.divisionStr2(platformTotal.getRandomRunTimeTotal(),
                String.valueOf(ConstsUtils.RUNNUM * 1000)));

        // 平均，100次异常-感知时间
        platformTotal.setNormalTimeAve(NumberUtils.divisionStr(String.valueOf(platformTotal.getNormalTimeTotal()),
                String.valueOf(ConstsUtils.RUNNUM)));
        // 平均，100次异常-支付
        platformTotal.setNormalPayAve(
                NumberUtils.divisionStr(platformTotal.getNormalPayTotal(), String.valueOf(ConstsUtils.RUNNUM)));
        // 平均，100次标准-运行时间
        platformTotal.setNormalRunTimeAve(NumberUtils.divisionStr2(platformTotal.getNormalRunTimeTotal(),
                String.valueOf(ConstsUtils.RUNNUM * 1000)));

        // 平均，100次异常-感知时间
        platformTotal.setAbnormalTimeAve(NumberUtils.divisionStr(String.valueOf(platformTotal.getAbnormalTimeTotal()),
                String.valueOf(ConstsUtils.RUNNUM)));
        // 平均，100次异常-支付
        platformTotal.setAbnormalPayAve(
                NumberUtils.divisionStr(platformTotal.getAbnormalPayTotal(), String.valueOf(ConstsUtils.RUNNUM)));
        // 平均，100次异常-运行时间
        platformTotal.setAbnormalRunTimeAve(NumberUtils.divisionStr2(platformTotal.getAbnormalRunTimeTotal(),
                String.valueOf(ConstsUtils.RUNNUM * 1000)));

        // 平均，100次MCD-感知时间
        platformTotal.setMcdTimeAve(NumberUtils.divisionStr(String.valueOf(platformTotal.getMcdTimeTotal()),
                String.valueOf(ConstsUtils.RUNNUM)));
        // 平均，100次MCD-支付
        platformTotal.setMcdPayAve(
                NumberUtils.divisionStr(platformTotal.getMcdPayTotal(), String.valueOf(ConstsUtils.RUNNUM)));
        // 平均，100次MCD-运行时间
        platformTotal.setMcdRunTimeAve(NumberUtils.divisionStr2(platformTotal.getMcdRunTimeTotal(),
                String.valueOf(ConstsUtils.RUNNUM * 1000)));

        System.out.println(">>>>>>>随机数据：");
        // System.out.println("随机数据总时间：" + platformTotal.getRandomRunTimeTotal());
        System.out.println("随机数据平均时间：" + platformTotal.getRandomRunTimeAve());

        System.out.println(">>>>>>>标准算法：");
        // System.out.println("全部感知时间：" + platformTotal.getNormalTimeTotal());
        // System.out.println("全部支付成本：" + platformTotal.getNormalPayTotal());
        // System.out.println("全部运行时间：" + platformTotal.getNormalRunTimeTotal());
        System.out.println("平均感知时间：" + platformTotal.getNormalTimeAve());
        System.out.println("平均支付成本：" + platformTotal.getNormalPayAve());
        System.out.println("平均运行时间：" + platformTotal.getNormalRunTimeAve());

        System.out.println(">>>>>>>异常算法：");
        // System.out.println("全部感知时间：" + platformTotal.getAbnormalTimeTotal());
        // System.out.println("全部支付成本：" + platformTotal.getAbnormalPayTotal());
        // System.out.println("全部运行时间：" + platformTotal.getAbnormalRunTimeTotal());
        System.out.println("平均感知时间：" + platformTotal.getAbnormalTimeAve());
        System.out.println("平均支付成本：" + platformTotal.getAbnormalPayAve());
        System.out.println("平均运行时间：" + platformTotal.getAbnormalRunTimeAve());

        System.out.println(">>>>>>>MCD算法：");
        // System.out.println("全部感知时间：" + platformTotal.getMcdTimeTotal());
        // System.out.println("全部支付成本：" + platformTotal.getMcdPayTotal());
        // System.out.println("全部运行时间：" + platformTotal.getMcdRunTimeTotal());
        System.out.println("平均感知时间：" + platformTotal.getMcdTimeAve());
        System.out.println("平均支付成本：" + platformTotal.getMcdPayAve());
        System.out.println("平均运行时间：" + platformTotal.getMcdRunTimeAve());

        System.out.println();
    }
}
