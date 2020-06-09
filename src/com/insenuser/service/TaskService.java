package com.insenuser.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.insenuser.model.task.Task;
import com.insenuser.model.user.InsenUser;
import com.insenuser.utils.ConstsUtils;
import com.insenuser.utils.RandomUtils;

public class TaskService {

    /**
     * 生成随机任务
     * 
     * @param taskNum
     * @param minTaskId
     * @param maxTaskId
     * @param minTaskTime
     * @param maxTaskTime
     * @return
     */
    public static List<Task> getRandomTask(int taskNum, int taskMinId, int taskMaxId, int taskMinTime,
            int taskMaxTime) {
        List<Task> taskList = new ArrayList<Task>();
        // 防止生成重复数字
        List<Integer> exitList = new ArrayList<Integer>();
        while (taskList.size() < taskNum) {
            Task task = new Task();
            // 任务id范围是[1，730]
            int taskId = RandomUtils.getRandom(taskMinId, taskMaxId);
            if (!exitList.contains(taskId)) {
                exitList.add(taskId);
                task.setTaskId(taskId);
                int needTime = RandomUtils.getRandom(taskMinTime, taskMaxTime);
                task.setOriginSenTime(needTime);
                task.setRemainSenTime(needTime);
                taskList.add(task);
            }
        }
        // 按照taskId升序
        Collections.sort(taskList);
        // 排序后编号
        for (int j = 0; j < taskList.size(); j++) {
            taskList.get(j).setId(j + 1);
        }
        return taskList;
    }

    /**
     * 使用深拷贝，关联任务与用户
     * 
     * @param originTaskList
     * @param originUserList
     * @throws CloneNotSupportedException
     */
    public static List<InsenUser> getTaskWithInSenUser(List<Task> originTaskList, List<InsenUser> originUserList)
            throws CloneNotSupportedException {
        // 任务关联的Map
        HashMap<Task, List<InsenUser>> taskUserMap = new HashMap<Task, List<InsenUser>>();
        // 遍历任务
        for (Task originTask : originTaskList) {
            // 深拷贝
            Task task = originTask.clone();
            // 任务task关联的用户集合
            List<InsenUser> insenUserList = new ArrayList<InsenUser>();
            // 在对单个任务进行匹配时，防止相同用户重复加入竞标后出现引用错误
            List<Integer> exitList = new ArrayList<Integer>();
            // 任务AOI数据
            int taskAOI = RandomUtils.getRandom(20, 110);
            // 非敏感型可以参加多个任务
            while (insenUserList.size() < taskAOI) {
                // list编号从0开始
                int ranNum = RandomUtils.getRandom(0, ConstsUtils.USERNUM - 1);
                if (!exitList.contains(ranNum)) {
                    // 防止单回合生成重复数字
                    exitList.add(ranNum);
                    // 深拷贝
                    InsenUser insenUser = originUserList.get(ranNum).clone();
                    // 关联任务id数组
                    List<Integer> taskIdList = new ArrayList<Integer>();
                    taskIdList.add(task.getTaskId());
                    insenUser.setOriginTaskList(taskIdList);
                    insenUserList.add(insenUser);
                }
            }

            // 按照userId排序输出
            Collections.sort(insenUserList);
            // 排序后编号
            for (int j = 0; j < insenUserList.size(); j++) {
                insenUserList.get(j).setId(j + 1);
            }
            taskUserMap.put(task, insenUserList);
        }

        // System.out.println("\n>>>>>>>>>> 输出任务关联用户id <<<<<<<<<<");
        // for (Map.Entry<Task, List<InsenUser>> taskMapUserEntry : taskUserMap.entrySet()) {
        // Task task = taskMapUserEntry.getKey();
        // System.out.println(">>>>>>>>>>任务id：" + task.getTaskId() + "<<<<<<<<<<");
        // List<InsenUser> taskMapUserList = taskMapUserEntry.getValue();
        // for (InsenUser user : taskMapUserList) {
        // System.out.println("用户id：" + user.getUserId());
        // }
        // }

        // 处理重叠区域问题,更新用户的关联任务id
        List<InsenUser> bidTotalList = new ArrayList<InsenUser>();
        // 将所有任务关联的用户汇总，去重作用
        for (Map.Entry<Task, List<InsenUser>> taskMapUserEntry : taskUserMap.entrySet()) {
            List<InsenUser> bidMapUserList = taskMapUserEntry.getValue();
            for (InsenUser bidMapUser : bidMapUserList) {
                InsenUser insenUserTotal = bidMapUser.clone();
                bidTotalList.add(insenUserTotal);
            }
        }

        // 返回值
        List<InsenUser> bidUserList = new ArrayList<InsenUser>();

        // 储存已存入用户id
        List<Integer> exitUserId = new ArrayList<Integer>();

        // 每个用户有关联多个任务id，设置每个用户关联的任务id数组，
        for (int i = 0; i < bidTotalList.size(); i++) {
            // 当前用户
            InsenUser nowUser = bidTotalList.get(i);
            // 如果不存在
            if (!exitUserId.contains(nowUser.getUserId())) {
                // 存入id
                exitUserId.add(nowUser.getUserId());
                // 深度复制
                InsenUser bidUser = nowUser.clone();
                // 直接存入
                bidUserList.add(bidUser);
            } else if (exitUserId.contains(nowUser.getUserId())) {
                for (InsenUser bidUser : bidUserList) {
                    // 找到重复的用户
                    if (bidUser.getUserId() == nowUser.getUserId()) {
                        // 获取唯一的任务id
                        int newTaskId = nowUser.getOriginTaskList().get(0);
                        // 存入新的任务id
                        bidUser.getOriginTaskList().add(newTaskId);
                    }
                }
            }
        }

        // 初始化每个用户当前未完成的任务列表
        for (InsenUser bidUser : bidUserList) {
            Collections.sort(bidUser.getOriginTaskList());
            bidUser.setUnfinishTaskList(bidUser.getOriginTaskList());
        }

        // 按照用户id排序
        Collections.sort(bidUserList);
        // 排序后编号
        for (int j = 0; j < bidUserList.size(); j++) {
            bidUserList.get(j).setId(j + 1);
        }

        // System.out.println("\n>>>>>>>>>> 输出用户关联的任务id <<<<<<<<<<");
        // for (InsenUser bidUser : bidUserList) {
        // System.out.println(bidUser.getUserId() + "：" + Arrays.toString(bidUser.getOriginTaskList().toArray()));
        // }

        return bidUserList;
    }

}
