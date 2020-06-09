package com.insenuser.model.task;

/**
 * Description:任务类
 *
 * @author kjy
 * @since Apr 4, 2020 3:28:55 PM
 */
public class Task implements Comparable<Task>, Cloneable {
    // id
    private int id;

    // 任务id
    private int taskId;

    // 剩余感知时间
    private int remainSenTime;

    // 原始感知时间
    private int originSenTime;

    // 已完成感知时间
    private int finishSenTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getRemainSenTime() {
        return remainSenTime;
    }

    public void setRemainSenTime(int remainSenTime) {
        this.remainSenTime = remainSenTime;
    }

    public int getOriginSenTime() {
        return originSenTime;
    }

    public void setOriginSenTime(int originSenTime) {
        this.originSenTime = originSenTime;
    }

    public int getFinishSenTime() {
        return finishSenTime;
    }

    public void setFinishSenTime(int finishSenTime) {
        this.finishSenTime = finishSenTime;
    }

    @Override
    public int compareTo(Task task) {
        return this.taskId - task.getTaskId();
    }

    @Override
    public Task clone() throws CloneNotSupportedException {
        return (Task) super.clone();
    }
}
