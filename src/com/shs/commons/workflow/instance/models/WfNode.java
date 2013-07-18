package com.shs.commons.workflow.instance.models;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.shs.commons.workflow.template.models.interfaces.IWfTplNode;

/**
 * @class describe: 流程节点实例, 活动
 * @version 0.1
 * @date created: 2012-2-24 下午5:34:40
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class WfNode {
	Logger logger = Logger.getLogger(getClass());
	/**
	 * 活动执行中
	 */
    public static final String STATUS_RUNNING = "RUNNING";
	/**
	 * 人工活动等待执行
	 */
    public static final String STATUS_WAITING = "WAITING";
    /**
     * 活动执行结束
     */
    public static final String STATUS_COMPLETED = "COMPLETED";
    public final static String IS_ROLLBACK = "Y";
    
	private String id;
    private WfFlow wfFlow;
	private IWfTplNode tplNode;
	private String startTime;
	private String doneTime;
	private String status;
	private Map<String, WfTask> tasks = new HashMap<String, WfTask>();
	
	public JSONObject toJO() throws Exception {
		return new JSONObject()
				.put("ID", id)
				.put("FLOW_ID", wfFlow.getID())
				.put("TPL_NODE_ID", tplNode.getID())
				.put("START_TIME", startTime)
				.put("DONE_TIME", doneTime)
				.put("STATUS", status);
	}
	public String getID() {
		return id;
	}
	public void setID(String id) {
		this.id = id;
	}

    public WfFlow getWfFlow() {
        return wfFlow;
    }

    public void setWfFlow(WfFlow wfFlow) {
        this.wfFlow = wfFlow;
    }

    public IWfTplNode getTplNode() {
        return tplNode;
    }
    /**
     * 返回前置节点
     * @return
     * @throws Exception 
     */
    public WfNode getBeforeNode() throws Exception {
    	logger.debug("Get Before Node");
    	WfNode beforeNode = null;
    	for (WfRoute route : wfFlow.getRoutes().values()) {
    		if (route.getToNode().getID().equals(this.id)) {
    			beforeNode = route.getFromNode();
    		}
    	}
    	logger.debug(beforeNode.toJO());
    	return beforeNode;
    }
    public void setTplNode(IWfTplNode tplNode) {
        this.tplNode = tplNode;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String timeStart) {
        this.startTime = timeStart;
    }

    public String getDoneTime() {
        return doneTime;
    }

    public void setDoneTime(String timeDone) {
        this.doneTime = timeDone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Map<String, WfTask> getTasks() {
        return tasks;
    }
    public void setTasks(Map<String, WfTask> tasks) {
        this.tasks = tasks;
    }
    /**
     * 所有任务都处理过
     * @return
     */
    public boolean isAllTasksDealt() {
    	for (WfTask task : tasks.values()) {
			if (WfTask.STATUS_ASSIGNED.equals(task.getStatus())){
				return false;
			}
		}
    	return true;
    }
}
