package com.insenuser.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.insenuser.model.task.Task;
import com.insenuser.model.user.InsenUser;
import com.insenuser.utils.JsonUtils;
import com.insenuser.utils.NumberUtils;

public class InsenAuctionService {

    /**
     * 开始入口
     * 
     * @param bidMap
     * @return
     */
    public static List<InsenUser> startAuction(List<InsenUser> originBidUserList, List<Task> originTaskList) {
        // 返回值
        List<InsenUser> winnerList = new ArrayList<InsenUser>();

        // 深拷贝用户
        String originBidUserListStr = JsonUtils.objToFastjson(originBidUserList);
        // 深拷贝任务
        String originTaskListStr = JsonUtils.objToFastjson(originTaskList);

        // 竞拍流程使用：竞拍用户
        List<InsenUser> winnerBackUpUserList = JsonUtils.fastjsonToObj(originBidUserListStr,
                new TypeToken<List<InsenUser>>() {
                }.getType());
        // 竞拍流程使用：竞拍任务
        List<Task> winnerBackUpTaskList = JsonUtils.fastjsonToObj(originTaskListStr, new TypeToken<List<Task>>() {
        }.getType());

        System.out.println("\n$$$$$$$$$$$$$$$$$ winnerSelection开始 $$$$$$$$$$$$$$$$");
        winnerList = winnerSelection(winnerBackUpUserList, winnerBackUpTaskList);
        System.out.println("$$$$$$$$$$$$$$$$$ winnerSelection结束 $$$$$$$$$$$$$$$$");

        System.out.println("\n$$$$$$$$$$$$$$$$ paymentDetermination开始 $$$$$$$$$$$$$$$$");
        winnerList = paymentDetermination(originBidUserList, originTaskList, winnerList);
        System.out.println("$$$$$$$$$$$$$$$$ paymentDetermination结束 $$$$$$$$$$$$$$$$");

        return winnerList;

    }

    /*
     * 1-0、Winner Selection
     */
    private static List<InsenUser> winnerSelection(List<InsenUser> winnerBackUpUserList,
            List<Task> winnerBackUpTaskList) {

        // 获胜者总集合，返回值
        List<InsenUser> winnerList = new ArrayList<InsenUser>();

        for (Task winnerBackUpTask : winnerBackUpTaskList) {

            // 任务剩余感知时间>0,浅拷贝,自动更新
            while (winnerBackUpTask.getRemainSenTime() > 0) {

                // System.out.println("\n>>>当前任务id：" + winnerBackUpTask.getTaskId() + ", 剩余时间："
                // + winnerBackUpTask.getRemainSenTime());
                System.out.println("\n>>>>>>>>>> 拍卖开始 <<<<<<<<<<");

                // 获得竞拍当前任务的所有用户列表，使用浅拷贝
                List<InsenUser> nowBidUserList = getNowBidUser(winnerBackUpTask.getTaskId(), winnerBackUpUserList);

                // System.out.println("\n>>>>>>>>>>当前任务所有竞拍用户列表:" + nowBidUserList.size());
                // for (InsenUser nowBidUser : nowBidUserList) {
                // System.out.println("用户ID：" + nowBidUser.getUserId() + ", 报价：" + nowBidUser.getBid() + ", 初始感知时间:"
                // + nowBidUser.getOriginSenTime() + ", 旧-剩余感知时间:" + nowBidUser.getRemainSenTime()
                // + ", 初始关联任务:" + Arrays.toString(nowBidUser.getOriginTaskList().toArray()) + ", 旧-未完成任务:"
                // + Arrays.toString(nowBidUser.getUnfinishTaskList().toArray()) + ", 获胜感知时间:"
                // + nowBidUser.getWinSenTime() + ", 旧-单位成本:" + nowBidUser.getAveCost() + ", 旧-用户获胜任务:"
                // + nowBidUser.getFinishTaskList());
                // }

                // 去除上一轮获胜者
                for (InsenUser winner : winnerList) {
                    Iterator<InsenUser> nowBidUserUser = nowBidUserList.iterator();
                    while (nowBidUserUser.hasNext()) {
                        InsenUser deleteUser = nowBidUserUser.next();
                        if (deleteUser.getUserId() == winner.getUserId()) {
                            nowBidUserUser.remove();
                        }
                    }
                }

                // System.out.println("\n>>>>>>>>>>当前任务(去除获胜者)后竞拍用户列表:" + nowBidUserList.size());
                // 重置上回合设置的剩余感知时间，剩余的用户恢复默认值
                for (InsenUser nowBidUser : nowBidUserList) {
                    nowBidUser.setRemainSenTime(nowBidUser.getOriginSenTime());
                }

                // for (InsenUser nowBidUser : nowBidUserList) {
                // System.out.println("用户ID：" + nowBidUser.getUserId() + ", 报价：" + nowBidUser.getBid() + ", 初始感知时间:"
                // + nowBidUser.getOriginSenTime() + ", 新-剩余感知时间:" + nowBidUser.getRemainSenTime()
                // + ", 初始关联任务:" + Arrays.toString(nowBidUser.getOriginTaskList().toArray()) + ", 旧-未完成任务:"
                // + Arrays.toString(nowBidUser.getUnfinishTaskList().toArray()) + ", 获胜感知时间:"
                // + nowBidUser.getWinSenTime() + ", 旧-单位成本:" + nowBidUser.getAveCost() + ", 旧-用户获胜任务:"
                // + nowBidUser.getFinishTaskList());
                // }

                // 计算当前任务下，用户当前回合的感知时间分配计划，使用浅拷贝
                TSAservice(nowBidUserList, winnerBackUpTaskList);

                // System.out.println("\n>>>>>>>>>>TSA后数据:" + nowBidUserList.size());
                // for (InsenUser nowBidUser : nowBidUserList) {
                // System.out.println("用户ID：" + nowBidUser.getUserId() + ", 报价：" + nowBidUser.getBid() + ", 初始感知时间:"
                // + nowBidUser.getOriginSenTime() + ", 新-剩余感知时间:" + nowBidUser.getRemainSenTime()
                // + ", 初始关联任务:" + Arrays.toString(nowBidUser.getOriginTaskList().toArray()) + ", 新-未完成任务:"
                // + Arrays.toString(nowBidUser.getUnfinishTaskList().toArray()) + ", 获胜感知时间:"
                // + nowBidUser.getWinSenTime() + ", 旧-单位成本:" + nowBidUser.getAveCost() + ", 旧-用户获胜任务:"
                // + nowBidUser.getFinishTaskList());
                // }

                // 获得winner
                InsenUser winner = getWinner(nowBidUserList);
                // 加入获胜者集合
                winnerList.add(winner);
                // 从候选者集合剔除获胜者
                nowBidUserList.remove(winner);

                // System.out.println("\n>>>>>>>更新时间前:");
                // for (Task updateTask : winnerBackUpTaskList) {
                // System.out.println("任务id：" + updateTask.getTaskId() + ", 任务初始感知时间：" + updateTask.getOriginSenTime()
                // + ", 任务完成感知时间：" + updateTask.getFinishSenTime() + ", 任务剩余感知时间："
                // + updateTask.getRemainSenTime());
                // }

                // 更新winner关联的所有任务的时间
                List<Integer> taskIdList = winner.getOriginTaskList();
                // 更新任务剩余感知时间
                List<Integer> finishTaskList = new ArrayList<Integer>();
                for (Task updateTask : winnerBackUpTaskList) {
                    // 只更新时间>0的任务
                    if (taskIdList.contains(updateTask.getTaskId()) && updateTask.getRemainSenTime() > 0) {
                        // 记录当前用户本轮完成的任务
                        finishTaskList.add(updateTask.getTaskId());
                        // 更新已完成时间
                        updateTask.setFinishSenTime(updateTask.getFinishSenTime() + winner.getWinSenTime());
                        // 更新剩余时间
                        updateTask.setRemainSenTime(updateTask.getOriginSenTime() - updateTask.getFinishSenTime());
                        // 补偿纠正已完成时间
                        if (updateTask.getFinishSenTime() > updateTask.getOriginSenTime()) {
                            updateTask.setFinishSenTime(updateTask.getOriginSenTime());
                        }
                        // 补偿纠正剩余时间
                        if (updateTask.getRemainSenTime() < 0) {
                            updateTask.setRemainSenTime(0);
                        }
                    }
                }

                // 更新用户获胜后，在本回合完成的任务
                winner.setFinishTaskList(finishTaskList);

                // System.out.println("\n>>>>>>>更新获胜者信息：");
                // System.out.println(">>>>>>获胜者ID：" + winner.getUserId() + ", 报价：" + winner.getBid() + ", 初始感知时间:"
                // + winner.getOriginSenTime() + ", 剩余感知时间:" + winner.getRemainSenTime() + ", 关联任务:"
                // + Arrays.toString(winner.getOriginTaskList().toArray()) + ", 未完成任务:"
                // + Arrays.toString(winner.getUnfinishTaskList().toArray()) + ", 获胜感知时间:" + winner.getWinSenTime()
                // + ", 单位成本:" + winner.getAveCost() + ", 用户获胜任务:" + winner.getFinishTaskList());

                // System.out.println("\n>>>>>>>涉及到的任务id:");
                // System.out.println(Arrays.toString(finishTaskList.toArray()));

                // System.out.println("\n>>>>>>>更新时间后:");
                // for (Task updateTask : winnerBackUpTaskList) {
                // System.out.println("任务id：" + updateTask.getTaskId() + ", 任务初始感知时间：" + updateTask.getOriginSenTime()
                // + ", 任务完成感知时间：" + updateTask.getFinishSenTime() + ", 任务剩余感知时间："
                // + updateTask.getRemainSenTime());
                // }

                System.out.println("\n>>>>>>>>>> 拍卖结束 <<<<<<<<<<");
            }
        }
        return winnerList;
    }

    /*
     * 1-1、TSA算法：迭代使用，选出每个用户在当前回合最大的SA+感知时间
     */
    private static void TSAservice(List<InsenUser> nowBidUserList, List<Task> winnerBackUpTaskList) {

        System.out.println("\n>>>>>>>>> 执行TSA算法开始 <<<<<<<<<");
        // TODO TSA算法第1步：选出用户所有SA中，与关联任务交集最大的SA
        createSAarea();

        // 假设最大SA与关联的所有任务相交
        for (InsenUser nowBidUser : nowBidUserList) {

            // System.out.println("\n>>>>>>>>>用户id：" + nowBidUser.getUserId());

            // 当前用户关联的未完成任务id序列
            List<Integer> taskIdList = nowBidUser.getUnfinishTaskList();

            // 当前用户关联的任务
            List<Task> nowTaskList = new ArrayList<Task>();
            // 获取当前用户关联的任务信息
            for (int nowTaskId : taskIdList) {
                for (Task winnerBackUp : winnerBackUpTaskList) {
                    // 保证该任务没有被完成
                    if (nowTaskId == winnerBackUp.getTaskId() && winnerBackUp.getRemainSenTime() > 0) {
                        nowTaskList.add(winnerBackUp);
                        break;
                    }
                }
            }

            // 更新竞标用户当前未完成的任务数量
            List<Integer> nowTaskIdList = new ArrayList<Integer>();
            for (Task nowTask : nowTaskList) {
                nowTaskIdList.add(nowTask.getTaskId());
            }
            nowBidUser.setUnfinishTaskList(nowTaskIdList);

            // 按照任务id升序
            Collections.sort(nowTaskList);

            // TODO
            // TODO 论文中方案，最小感知时间
            // TODO
            // TSA算法第2步：选择与最大感知时间SA相交任务中，感知时间最少的任务
            // 假设最小感知时间
            // int minTaskTime = nowTaskList.get(0).getRemainSenTime();
            // System.out.println("任务-" + nowTaskList.get(0).getTaskId() + ":" + minTaskTime);
            // for (int i = 1; i < nowTaskList.size(); i++) {
            // int nowTaskTime = nowTaskList.get(i).getRemainSenTime();
            // System.out.println("任务-" + nowTaskList.get(i).getTaskId() + ":" + nowTaskTime);
            // if (minTaskTime > nowTaskTime) {
            // minTaskTime = nowTaskTime;
            // }
            // }
            // System.out.println("最小的任务时间：" + minTaskTime);
            // // TSA算法第3步：比较最大SA的感知时间+关联的任务感知时间，选择较小的
            // int minTime = nowBidUser.getRemainSenTime();
            // System.out.println("用户剩余感知时间：" + minTime);
            // if (minTime > minTaskTime) {
            // nowBidUser.setRemainSenTime(minTaskTime);
            // }

            // TODO
            // TODO 实验使用这个方案，最大感知时间
            // TODO
            // TSA算法第2步：选择与最大感知时间SA相交任务中，感知时间最少的任务
            // 假设最大感知时间
            int maxTaskTime = nowTaskList.get(0).getRemainSenTime();
            System.out.println("任务-" + nowTaskList.get(0).getTaskId() + ":" + maxTaskTime);
            for (int i = 1; i < nowTaskList.size(); i++) {
                int nowTaskTime = nowTaskList.get(i).getRemainSenTime();
                System.out.println("任务-" + nowTaskList.get(i).getTaskId() + ":" + nowTaskTime);
                if (maxTaskTime < nowTaskTime) {
                    maxTaskTime = nowTaskTime;
                }
            }
            System.out.println("最大的任务时间：" + maxTaskTime);
            // TSA算法第3步：比较最大SA的感知时间+关联的任务感知时间，选择较小的
            int minTime = nowBidUser.getRemainSenTime();
            System.out.println("用户剩余感知时间：" + minTime);
            if (minTime > maxTaskTime) {
                nowBidUser.setRemainSenTime(maxTaskTime);
            }

            // System.out.println(">>>>>>最终最小时间：" + nowBidUser.getRemainSenTime());
        }
    }

    /*
     * 1-2、获得竞拍当前任务的用户集合,使用浅拷贝
     */
    private static List<InsenUser> getNowBidUser(int nowTaskId, List<InsenUser> winnerBackUpUserList) {
        // 竞拍当前任务的用户
        List<InsenUser> nowBidUserList = new ArrayList<InsenUser>();
        // 遍历找出参与该任务的用户
        for (InsenUser winnerBackUpUser : winnerBackUpUserList) {
            // 用户竞拍该任务+用户剩余感知时间>0
            if (winnerBackUpUser.getOriginTaskList().contains(nowTaskId)) {
                // 浅拷贝
                nowBidUserList.add(winnerBackUpUser);
            }
        }
        return nowBidUserList;
    }

    /*
     * 1-3、创建SA区域，说明：本实验使用最理想情况，每个用户只有1个SA，那么用户的活跃区域==SA，该SA也是用户所有关联任务区域的交集，因此拍卖结束winner感知时间会有盈余
     */
    private static void createSAarea() {
        // TODO 设计完善SA方法
    }

    /*
     * 1-4、获胜者算法
     */
    private static InsenUser getWinner(List<InsenUser> nowBidUserList) {
        System.out.println("\n>>>>>>>计算winner单位成本：");
        // 返回值
        InsenUser winner = new InsenUser();
        // 计算单位价格
        for (InsenUser nowBidUser : nowBidUserList) {
            // 获取最小竞标时间
            // int minTime = NumberUtils.getMin(taskReaminSenTime, nowBidUser.getRemainSenTime());
            int minTime = nowBidUser.getRemainSenTime();

            // TODO
            // TODO 非敏感型用户单位成本
            // TODO
            String aveCostStr = NumberUtils.division(nowBidUser.getBid(),
                    nowBidUser.getUnfinishTaskList().size() * minTime);

            // TODO
            // TODO 敏感型用户单位成本,实验时去掉注释
            // TODO
            // String aveCostStr = NumberUtils.division(nowBidUser.getBid(), minTime);

            nowBidUser.setAveCost(aveCostStr);

            // System.out.println("用户ID：" + nowBidUser.getUserId() + ", 报价：" + nowBidUser.getBid() + ", 初始感知时间:"
            // + nowBidUser.getOriginSenTime() + ", 新-剩余感知时间:" + nowBidUser.getRemainSenTime() + ", 关联任务:"
            // + Arrays.toString(nowBidUser.getOriginTaskList().toArray()) + ", 新-未完成任务:"
            // + Arrays.toString(nowBidUser.getUnfinishTaskList().toArray()) + ", 获胜感知时间:"
            // + nowBidUser.getWinSenTime() + ", 新-单位成本:" + nowBidUser.getAveCost() + ", 新-用户获胜任务:"
            // + nowBidUser.getFinishTaskList());
            // System.out.println("############################################################################");

        }

        // 获得winner
        winner = getMinAveCost(nowBidUserList);
        // 更新获胜时间
        winner.setWinSenTime(winner.getRemainSenTime());
        // 更新剩余时间
        winner.setRemainSenTime(winner.getOriginSenTime() - winner.getWinSenTime());

        // System.out.println(">>>>>>获胜者ID：" + winner.getUserId() + ", 报价：" + winner.getBid() + ", 初始感知时间:"
        // + winner.getOriginSenTime() + ", 剩余感知时间:" + winner.getRemainSenTime() + ", 关联任务:"
        // + Arrays.toString(winner.getOriginTaskList().toArray()) + ", 未完成任务:"
        // + Arrays.toString(winner.getUnfinishTaskList().toArray()) + ", 获胜感知时间:" + winner.getWinSenTime()
        // + ", 单位成本:" + winner.getAveCost() + ", 用户获胜任务:" + winner.getFinishTaskList());

        return winner;
    }

    /*
     * 1-5、选择最小的竞标成本的获胜
     */
    private static InsenUser getMinAveCost(List<InsenUser> nowBidUserList) {
        // 假设第一个人竞拍成本最小
        InsenUser winner = nowBidUserList.get(0);
        for (int i = 1; i < nowBidUserList.size(); i++) {
            if (Float.valueOf(winner.getAveCost()) > Float.valueOf(nowBidUserList.get(i).getAveCost())) {
                winner = nowBidUserList.get(i);
            }
        }
        return winner;
    }

    /**
     * 2-0、Payment Determination
     */
    private static List<InsenUser> paymentDetermination(List<InsenUser> originBidUserList, List<Task> originTaskList,
            List<InsenUser> winnerList) {
        // 生成Json数据，备份
        String originBidUserListStr = JsonUtils.objToFastjson(originBidUserList);
        String originTaskListStr = JsonUtils.objToFastjson(originTaskList);

        for (InsenUser winner : winnerList) {
            // 支付获胜者
            List<InsenUser> nextWinnerList = new ArrayList<InsenUser>();

            System.out.println("$$$$$$$$$$$$$$$$ 当前获胜者：" + winner.getUserId() + " $$$$$$$$$$$$$$$$");

            // 备份
            List<InsenUser> payBackUpUserList = JsonUtils.fastjsonToObj(originBidUserListStr,
                    new TypeToken<List<InsenUser>>() {
                    }.getType());
            // 备份
            List<Task> payBackUpTaskList = JsonUtils.fastjsonToObj(originTaskListStr, new TypeToken<List<Task>>() {
            }.getType());

            // System.out.println("\n>>>>>>>>>>所有竞拍用户列表:" + payBackUpUserList.size());
            // for (InsenUser payBackUpUser : payBackUpUserList) {
            // System.out.println("id:" + payBackUpUser.getId() + ", 用户ID：" + payBackUpUser.getUserId() + ", 报价："
            // + payBackUpUser.getBid() + ", 初始感知时间:" + payBackUpUser.getOriginSenTime() + ", 剩余感知时间:"
            // + payBackUpUser.getRemainSenTime() + ", 初始关联任务:"
            // + Arrays.toString(payBackUpUser.getOriginTaskList().toArray()) + ", 未完成任务:"
            // + Arrays.toString(payBackUpUser.getUnfinishTaskList().toArray()) + ", 获胜感知时间:"
            // + payBackUpUser.getWinSenTime() + ", 单位成本:" + payBackUpUser.getAveCost());
            // }

            // 删除获胜者
            Iterator<InsenUser> payBackUpUserUser = payBackUpUserList.iterator();
            while (payBackUpUserUser.hasNext()) {
                InsenUser deleteUser = payBackUpUserUser.next();
                if (deleteUser.getUserId() == winner.getUserId()) {
                    payBackUpUserUser.remove();
                }
            }

            // System.out.println("\n>>>>>>>>>>所有竞拍(删除获胜者)用户列表:" + payBackUpUserList.size());
            // for (InsenUser payBackUpUser : payBackUpUserList) {
            // System.out.println("id:" + payBackUpUser.getId() + ", 用户ID：" + payBackUpUser.getUserId() + ", 报价："
            // + payBackUpUser.getBid() + ", 初始感知时间:" + payBackUpUser.getOriginSenTime() + ", 剩余感知时间:"
            // + payBackUpUser.getRemainSenTime() + ", 初始关联任务:"
            // + Arrays.toString(payBackUpUser.getOriginTaskList().toArray()) + ", 未完成任务:"
            // + Arrays.toString(payBackUpUser.getUnfinishTaskList().toArray()) + ", 获胜感知时间:"
            // + payBackUpUser.getWinSenTime() + ", 单位成本:" + payBackUpUser.getAveCost());
            // }

            // 重新进行竞拍
            nextWinnerList = winnerSelection(payBackUpUserList, payBackUpTaskList);

            // TODO 不可以删除
            for (InsenUser nextWinner : nextWinnerList) {
                // System.out.println("\n>>>>>>次级获胜者ID：" + nextWinner.getUserId() + ", 报价：" + nextWinner.getBid()
                // + ", 获胜感知时间:" + nextWinner.getWinSenTime());
                winner.setPay(getPay(winner, nextWinner));
                // System.out.println(">>>>>>获胜者ID：" + winner.getUserId() + ", 报价：" + winner.getBid() + ", 获胜感知时间:"
                // + winner.getWinSenTime() + ", 支付价格:" + winner.getPay());
            }
        }
        return winnerList;
    }

    /*
     * 2-1、支付函数
     */
    private static String getPay(InsenUser winner, InsenUser nextWinner) {
        String winnerPay = winner.getPay();
        int winnerSenTime = winner.getWinSenTime();
        // 保留2位小数
        String nextPay = getNextPay(winnerSenTime, nextWinner.getWinSenTime(), nextWinner.getBid());
        // System.out.println(">>>>>>>>当前价格：" + nextPay);
        winnerPay = NumberUtils.getStrMax(winnerPay, nextPay);
        return winnerPay;
    }

    /*
     * 2-2、计算支付价格
     */
    public static String getNextPay(int winnerSenTime, int nextSenTime, int nextBid) {
        float result = (float) winnerSenTime / nextSenTime * nextBid;
        String nextPay = String.format("%.2f", result);
        return nextPay;
    }

}
