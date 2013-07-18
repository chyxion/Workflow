package com.shs.commons.workflow.core;
import com.shs.commons.workflow.interfaces.IWfActivity;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.log4j.Logger;
/**
 * @version 0.1
 * @author chyxion
 * @describe: 流程活动队列
 * @date created: Jan 18, 2013 1:51:01 PM
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public class ActivityQueue {
	private Logger logger = Logger.getLogger(getClass());
    /**
     * 当前被管理的列表
     */
    private Queue<IWfActivity> queue = new LinkedBlockingDeque<IWfActivity>();
    /**
     * 清空队列
     */
    public synchronized void clear() {
        queue.clear();
    }

    /**
     * 判断队列是否为空
     * @return boolean
     */
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * 返回队列的长度
     * @return int
     */
    public synchronized int size() {
        return queue.size();
    }

    /**
     * 在队列尾部增加一个新活动
     * @param activity
     */
    public synchronized void enqueue(IWfActivity activity) {
		logger.debug("activity enqueue");
		logger.debug(activity.getWfNode().getTplNode().getName());
        queue.add(activity);
        logger.debug("queue size");
        logger.debug(queue.size());
        notifyAll();
    }

    /**
     * 在队列尾部加入一组活动
     * @param activities
     */
    public synchronized void enqueue(Collection<IWfActivity> activities) {
        queue.addAll(activities);
        notifyAll();
    }

    /**
     * 队列的队首活动出队
     * @return Activity
     */
    public synchronized IWfActivity dequeue() throws Exception {
        while (queue.isEmpty()) {
        	logger.debug("not activity in queue，waiting");
        	this.wait();
        }
        logger.debug("activity dequeue");
        return queue.poll();
    }

    /**
     * 从队列中清除指定流程的所有活动
     * @param flowID
     */
    public synchronized void removeAllByFlowID(String flowID) {
    	Iterator<IWfActivity> it = queue.iterator();
    	while(it.hasNext()) {
            IWfActivity activity = it.next();
            if (activity.getWfNode().getWfFlow().getID().equals(flowID)) {
                it.remove();
            }
    	}
    }
}

