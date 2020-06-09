package com.insenuser.service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.insenuser.model.task.Task;
import com.insenuser.model.user.InsenUser;
import com.insenuser.utils.ConstsUtils;

public class RandomService {

    /**
     * 入口函数
     * 
     * @throws CloneNotSupportedException
     */
    public static HashMap<List<InsenUser>, List<Task>> getRandomData() throws IOException, CloneNotSupportedException {

        // 获取任务
        List<Task> taskList = TaskService.getRandomTask(ConstsUtils.TASKNUM, ConstsUtils.TASKMINID,
                ConstsUtils.TASKMAXID, ConstsUtils.TASKMINTIME, ConstsUtils.TASKMAXTIME);
        // 获取用户
        List<InsenUser> insenUserList = InsenUserService.getRandomUser(ConstsUtils.USERNUM, ConstsUtils.USERMINID,
                ConstsUtils.USERMAXID, ConstsUtils.USERMINTIME, ConstsUtils.USERMAXTIME, ConstsUtils.USERMINBID,
                ConstsUtils.USERMAXBID);

        // 按照id排序
        Collections.sort(taskList);
        Collections.sort(insenUserList);

        // 前置异常用户
        insenUserList = McdService.putAbnormalUser(insenUserList);

        // 使用深拷贝，关联任务->用户
        List<InsenUser> bidUserList = TaskService.getTaskWithInSenUser(taskList, insenUserList);

        // System.out.println("\n输出所有用户数据：");
        // for (InsenUser nowBidUser : bidUserList) {
        // System.out.println(
        // "ID：" + nowBidUser.getId() + ", 用户ID：" + nowBidUser.getUserId() + ", 报价：" + nowBidUser.getBid()
        // + ", 初始感知时间:" + nowBidUser.getOriginSenTime() + ", 剩余感知时间:" + nowBidUser.getRemainSenTime()
        // + ", 关联任务:" + Arrays.toString(nowBidUser.getOriginTaskList().toArray()) + ", 未完成任务:"
        // + Arrays.toString(nowBidUser.getUnfinishTaskList().toArray()) + ", 获胜感知时间:"
        // + nowBidUser.getWinSenTime());
        // System.out.println("###################################################################");
        // }

        // System.out.println("\n输出所有任务数据：");
        // for (Task task : taskList) {
        // System.out.println("ID：" + task.getId() + ", 任务ID：" + task.getTaskId() + ", 任务初始感知时间:"
        // + task.getOriginSenTime() + ", 任务剩余感知时间:" + task.getRemainSenTime());
        // System.out.println("###################################################################");
        // }
        // 返回值
        HashMap<List<InsenUser>, List<Task>> bidMap = new HashMap<List<InsenUser>, List<Task>>();
        bidMap.put(bidUserList, taskList);
        return bidMap;
    }

}
