package com.insenuser.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.insenuser.model.task.Task;
import com.insenuser.model.user.InsenUser;
import com.insenuser.utils.ConstsUtils;
import com.insenuser.utils.RandomUtils;

public class McdService {

    /**
     * 设置随机异常用户
     * 
     * @param originBidUserList
     * @return
     */
    public static List<InsenUser> putAbnormalUser(List<InsenUser> originBidUserList) {
        // 防止生成重复数字
        List<Integer> exitList = new ArrayList<Integer>();
        // 异常用户的数量
        int carelessNum = (int) (ConstsUtils.BREAKPOINT * originBidUserList.size());
        // 模拟异常用户
        while (exitList.size() < carelessNum) {
            int number = RandomUtils.getRandom(0, originBidUserList.size() - 1);
            if (!exitList.contains(number)) {
                exitList.add(number);
                originBidUserList.get(number).setCareless(1);
                // TODO 添加一条异常坐标数据
            }
        }
        return originBidUserList;
    }

    /**
     * 判断是否存在异常用户
     * 
     * @param abnormalWinnerList
     * @return
     */
    public static boolean isAbnormal(List<InsenUser> abnormalWinnerList) {
        for (InsenUser abnormalWinner : abnormalWinnerList) {
            // 获得异常用户id
            if (abnormalWinner.getCareless() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 重新设置竞拍任务数据
     * 
     * @param abnormalWinnerList
     * @param abnormalTaskList
     * @return
     */
    public static List<Task> getReAuctionTaskData(List<InsenUser> abnormalWinnerList, List<Task> abnormalTaskList) {

        // System.out.println("\n>>>>>>>重新拍卖前-原始任务信息:");
        // for (Task updateTask : abnormalTaskList) {
        // System.out.println("任务id：" + updateTask.getTaskId() + ", 任务初始感知时间：" + updateTask.getOriginSenTime()
        // + ", 任务完成感知时间：" + updateTask.getFinishSenTime() + ", 任务剩余感知时间：" + updateTask.getRemainSenTime());
        // }

        // 由于下层服务使用深拷贝，并不会改变上层数据，将所有任务设置为已完成状态
        for (Task abnormalTask : abnormalTaskList) {
            abnormalTask.setFinishSenTime(abnormalTask.getOriginSenTime());
            abnormalTask.setRemainSenTime(0);
        }

        // System.out.println("\n>>>>>>>重新拍卖前-所有任务设置为已完成:");
        // for (Task updateTask : abnormalTaskList) {
        // System.out.println("任务id：" + updateTask.getTaskId() + ", 任务初始感知时间：" + updateTask.getOriginSenTime()
        // + ", 任务完成感知时间：" + updateTask.getFinishSenTime() + ", 任务剩余感知时间：" + updateTask.getRemainSenTime());
        // }

        // 遍历获胜者
        for (InsenUser abnormalUser : abnormalWinnerList) {
            // 储存失败的任务id
            List<Integer> abnormalTaskId = new ArrayList<Integer>();
            // 找到异常用户
            if (abnormalUser.getCareless() == 1) {
                // 深拷贝, 获得异常用户失败的任务
                abnormalTaskId.addAll(abnormalUser.getFinishTaskList());
                // 遍历所有任务
                for (Task abnormalTask : abnormalTaskList) {
                    // 发现失败的任务
                    if (abnormalTaskId.contains(abnormalTask.getTaskId())) {
                        // 重置失败任务的剩余感知时间
                        abnormalTask.setRemainSenTime(abnormalTask.getRemainSenTime() + abnormalUser.getWinSenTime());
                        abnormalTask
                                .setFinishSenTime(abnormalTask.getOriginSenTime() - abnormalTask.getRemainSenTime());
                    }
                }

                // System.out.println("\n异常用户id：" + abnormalUser.getUserId() + ", 任务id："
                // + Arrays.toString(abnormalUser.getFinishTaskList().toArray()) + ", 获胜感知时间："
                // + abnormalUser.getWinSenTime());

                // System.out.println("\n>>>>>>>重新拍卖前-重新设置未完成任务:");
                // for (Task updateTask : abnormalTaskList) {
                // System.out.println("任务id：" + updateTask.getTaskId() + ", 任务初始感知时间：" + updateTask.getOriginSenTime()
                // + ", 任务完成感知时间：" + updateTask.getFinishSenTime() + ", 任务剩余感知时间："
                // + updateTask.getRemainSenTime());
                // }
            }
        }
        return abnormalTaskList;
    }

    /**
     * 去除异常用户数据
     * 
     * @param mcdBidUserList
     * @return
     */
    // TODO 调用高德地图的API
    public static List<InsenUser> getMcdSenuser(List<InsenUser> mcdBidUserList) {
        Iterator<InsenUser> iteratorUser = mcdBidUserList.iterator();
        while (iteratorUser.hasNext()) {
            InsenUser deleteUser = iteratorUser.next();
            if (deleteUser.getCareless() == 1) {
                iteratorUser.remove();
            }
        }
        // 调用MCD算法时间损耗
        int waitTime = mcdBidUserList.size() / 10;
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mcdBidUserList;
    }
}
