package com.shs.commons.workflow.core;
import org.apache.log4j.Logger;

import com.shs.commons.workflow.exceptions.WfException;
/**
 * <p>Title: </p>
 * <p>Description: 工作流引擎</p>
 * 启动引擎。
 * 停止引擎。
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: ShengHangSoft </p>
 * <p>Date: 2011-5-16</p>
 * <p>Time: 9:34:25</p>
 * <p>@version 1.0</p>
 */
public class WfEngine {
	private Logger logger = Logger.getLogger(getClass());
    /**
     * 运行状态
     */
    public final static String STATUS_RUNNING = "RUNNING";

    /**
     * 停止状态
     */
    public final static String STATUS_STOPPED = "STOPPED";

    /**
     * 单态类，引擎自身对象引用
     */
    private static WfEngine engine;

    /**
     * 引擎默认并发调度数
     */
    private final int NUM_SCHEDULERS = 1;

    /**
     * 引擎状态（运行、停止）
     */
    private String status;

    /**
     * 活动队列
     */
    private ActivityQueue activities;

    /**
     * 调度器
     */
    private WfScheduler[] schedulers;

    /**
     * 私有构造器
     */
    private WfEngine() {
        status = WfEngine.STATUS_STOPPED;
    }

    /**
     * 获取引擎实例，采用singleton设计模式
     * @return 引擎实例
     */
    public static WfEngine getInstance() {
        if (engine == null) {
            engine = new WfEngine();
        }
        return engine;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ActivityQueue getActivityQueue() {
        return activities;
    }

    public void setActivityQueue(ActivityQueue activityQueue) {
        this.activities = activityQueue;
    }

    /**
     * 启动工作流引擎
     */
    public void start() throws WfException {
    	logger.debug("start workflow engine");
        //若引擎已经启动，则抛出异常
        if (status.equals(WfEngine.STATUS_RUNNING)) {
            throw new WfException(WfException.ENGINE_STARTED);
        }
        //1. 初始化活动队列
        this.activities = new ActivityQueue();
        
        //2. 初始化一组线程调度器
        this.schedulers = new WfScheduler[NUM_SCHEDULERS];
        for (int i = 0; i < NUM_SCHEDULERS; i++) {
            schedulers[i] = new WfScheduler(activities, i);
            schedulers[i].start();
        }
        //3. 修改引擎的状态为运行状态
        status = WfEngine.STATUS_RUNNING;
    }

    /**
     * 停止工作流引擎
     */
    public void stop() throws WfException {
        //若引擎已经停止，则抛出异常
        if (status.equals( WfEngine.STATUS_STOPPED )) {
            throw new WfException(WfException.ENGINE_STOPED);
        }
        //1. 停止调度器线程
        for (int i = 0; i < NUM_SCHEDULERS; i++) {
            schedulers[i].stop();
        }
        schedulers = null;
        //2. 清空活动对列
        activities.clear();
        activities = null;
    }
}
