package com.shs.commons.workflow.interfaces;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

import com.shs.commons.workflow.activities.ActivityService;
import com.shs.commons.workflow.exceptions.WfException;
import com.shs.commons.workflow.instance.dao.WfNodeDAO;
import com.shs.commons.workflow.instance.models.WfFlow;
import com.shs.commons.workflow.instance.models.WfNode;
import com.shs.commons.workflow.instance.services.WfNodeService;
import com.shs.commons.workflow.services.ActionService;
import com.shs.commons.workflow.services.FlowManager;
import com.shs.commons.workflow.services.TaskManager;
import com.shs.framework.dao.BaseDAO;
import com.shs.framework.utils.DateUtils;
//import com.shs.idps.system.services.SystemMsgService;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: ShengHangSoft </p>
 * <p>Date: 2011-5-16</p>
 * <p>Time: 11:10:31</p>
 * <p>@version 1.0</p>
 */
public abstract class IWfActivity {
	protected Logger logger = Logger.getLogger(getClass());
	/**
	 * 流程管理器
	 */
	protected FlowManager flowManager = new FlowManager();
	/**
	 * 活动服务类
	 */
	protected ActivityService activityService = new ActivityService();
	/**
	 * 节点服务类
	 */
	protected WfNodeService nodeService = new WfNodeService();
	/**
	 * 动作服务
	 */
	protected ActionService actionService = new ActionService();
	/**
	 * 任务服务
	 */
	protected TaskManager taskManager = new TaskManager();
	//protected SystemMsgService systemMsgService = new SystemMsgService();
	/**
	 * 标志活动执行回滚
	 */
	protected boolean rollback;
	/**
	 * 后继活动
	 */
	protected List<IWfActivity> postActivities = new LinkedList<IWfActivity>();
    /**
     * 流程节点
     */
    protected WfNode wfNode;
    /**
     * 构造器
     * @param node
     * @param processVO
     */
    public IWfActivity(WfNode node) {
    	this.wfNode = node;
    }
    /**
     * 抽象方法，在子类中实现。
     * @return
     * @throws WfException
     */
    abstract protected List<IWfActivity> execute(Connection dbConnection) throws Exception;
    /**
     * 执行活动
     * @param dbConnection
     * @return
     * @throws Exception
     */
    public List<IWfActivity> doExec(Connection dbConnection) throws Exception {
    	// 流程正在运行
    	if (wfNode.getWfFlow().getStatus().equals(WfFlow.STATUS_RUNNING)) {
    		return execute(dbConnection);
    	} else { // 流程不处在正在运行状态
    		wfNode.setDoneTime(DateUtils.now14());
    		wfNode.setStatus(WfNode.STATUS_COMPLETED);
    		new WfNodeDAO(new BaseDAO()).completeNode(dbConnection, wfNode);
    	}
    	return null;
    }
	public WfNode getWfNode() {
		return wfNode;
	}
	public void setWfNode(WfNode wfNode) {
		this.wfNode = wfNode;
	}
	/**
	 * 标志执行回滚操作
	 * @param isRollback
	 */
	public void setRollback(boolean isRollback) {
		this.rollback = isRollback;
	}
	/**
	 * 添加后继活动
	 * @param activity
	 */
	public void addPostActivity(IWfActivity activity) {
		postActivities.add(activity);
	}
}
