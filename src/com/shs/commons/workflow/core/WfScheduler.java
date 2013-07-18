package com.shs.commons.workflow.core;
import java.util.List;

import org.apache.log4j.Logger;

import com.shs.commons.workflow.instance.dao.WfFlowDAO;
import com.shs.commons.workflow.instance.models.WfFlow;
import com.shs.commons.workflow.interfaces.IWfActivity;
import com.shs.framework.dao.BaseDAO;
import com.shs.framework.dao.BaseDAO.ConnectionOperator;
import com.shs.framework.utils.DateUtils;

/**
 * <p>Title: </p>
 * <p>Description: 活动调度器，负责执行ActivityQueue中的Activity</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: ShengHangSoft </p>
 * <p>Date: 2011-5-16</p>
 * <p>Time: 9:48:31</p>
 * <p>@version 1.0</p>
 */

public class WfScheduler implements Runnable {
	Logger logger = Logger.getLogger(getClass());
    /**
     * 本调度器使用的Activity队列
     */
    private ActivityQueue activityQueue;

    /**
     * 本调度器的索引号
     */
    private int index;

    /**
     * 运行状态控制标记
     */
    private boolean stop;

    /**
     * 本调度器使用的线程实例
     */
    private Thread scheduler;

    /**
     * 构造器
     * @param activityQueue
     * @param index  线程序号
     */
    public WfScheduler(ActivityQueue activityQueue, int index) {
        this.activityQueue = activityQueue;
        this.index = index;
    }

    /**
     * 启动该调度器
     */
    public void start() {
        scheduler = new Thread(this);
        scheduler.start();
    }
    /**
     * 当前线程正在操作的流程
     */
    private WfFlow flow;
    /**
     * 执行调度
     */
    public void run() {
        while (!stop) {
        	try {
	            //1. 从引擎的活动队列中获得活动
	            final IWfActivity activity = activityQueue.dequeue();
				//2. 执行活动
	            if (activity != null) {
	            	// 记录当前操作流程
	            	flow = activity.getWfNode().getWfFlow();
	            	logger.debug("run node");
	            	logger.debug(activity.getWfNode().getTplNode().getName());
	                //2.1 执行活动
		        	new BaseDAO().executeTransaction(new ConnectionOperator() {
						@Override
						public void run() throws Exception {
							List<IWfActivity> postActivities;
							// 执行活动
							postActivities = activity.doExec(dbConnection);
			                //3. 如果产生新的后续活动，将它们加入到活动队列中
			                if ((postActivities != null) && (postActivities.size() > 0)) {
			                	logger.debug("run post activites");
			                    WfEngine.getInstance().getActivityQueue().enqueue(postActivities);
			                }
						}
					});
		        	logger.debug("node end");
	            	logger.debug(activity.getWfNode().getTplNode().getName());
	            }
	        } catch (Exception e) {
	        	try { // 流程异常结束
		        	flow.setDoneTime(DateUtils.now14());
		        	flow.setStatus(WfFlow.STATUS_EXCEPTIONAL);
		        	flow.setNote("流程异常结束[" + e.getMessage() + "]");
					new WfFlowDAO(new BaseDAO()).complete(flow);
				} catch (Exception fe) {
					fe.printStackTrace();
				}
	        	e.printStackTrace();
			} 
        }
        //当循环退出的时候调用
    	scheduler = null;
    }
    /**
     * 停止该调度器
     */
    public void stop() {
        stop = true;
    }

	public int getIndex() {
		return index;
	}
}

