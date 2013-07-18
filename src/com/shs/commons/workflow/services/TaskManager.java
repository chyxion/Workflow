package com.shs.commons.workflow.services;
import com.shs.commons.workflow.core.TaskAssignPolicyParser;
import com.shs.commons.workflow.instance.dao.WfTaskDAO;
import com.shs.commons.workflow.instance.models.WfNode;
import com.shs.commons.workflow.instance.models.WfTask;
import com.shs.commons.workflow.interfaces.IWfActivity;
import com.shs.framework.dao.BaseDAO;
import com.shs.framework.utils.DateUtils;
import com.shs.framework.utils.UUID;

import java.sql.Connection;
import java.util.List;
import org.apache.log4j.Logger;
/**
 * <p>Title: </p>
 * <p>Description:实现任务分派 </p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: ShengHangSoft </p>
 * <p>Date: 2011-5-16</p>
 * <p>Time: 14:24:48</p>
 * <p>@version 1.0</p>
 */
public class TaskManager {
	private Logger logger = Logger.getLogger(getClass());
	private TaskAssignPolicyParser parser = new TaskAssignPolicyParser();
	private WfTaskDAO taskDAO = new WfTaskDAO(new BaseDAO());
    /**
     * 分派任务，产生人工任务列表
     * 活动进入运行状态
     * 任务为新建状态
     */
    public List<WfTask> assignTasks(Connection dbConnection, IWfActivity activity) throws Exception {
        String assignPolicy = activity.getWfNode().getTplNode().getAssignPolicy();
    	logger.debug("parse assign policy");
    	logger.debug(assignPolicy);
    	List<WfTask> taskList = null;
    	//直接到用户,比如 USER('U001,U002')
    	if (assignPolicy.startsWith(TaskAssignPolicyParser.POLICY_USER)) {
    		taskList = parser.parseTaskForUsers(dbConnection, activity, assignPolicy);
    	}
    	//角色分配,比如 ROLE('R001','R002')
    	else if (assignPolicy.startsWith(TaskAssignPolicyParser.POLICY_ROLE)) {
    		taskList = parser.parseTasksForRoles(dbConnection, activity, assignPolicy);
    	} else if (assignPolicy.startsWith(TaskAssignPolicyParser.POLICY_ACTION)){
    		taskList = parser.parseTasksByAction(dbConnection, activity, assignPolicy);
    	}
    	else {
    		throw new Exception("任务分配策略[" + assignPolicy + "]解析出错！");
    	}
    	return taskList;
    }

	public void newTasks(Connection dbConnection, List<WfTask> taskList) throws Exception {
		for (WfTask task : taskList) {
			taskDAO.newTask(dbConnection, task.toJO());
		}
	}
	public void newTask(Connection dbConnection, WfTask task) throws Exception {
		taskDAO.newTask(dbConnection, task.toJO());
	}
    /**
     * 任务分配给流程发起人。
     * @return
     */
    public WfTask createTaskForApplicant(IWfActivity activity) {
        WfTask task = new WfTask();
        WfNode node = activity.getWfNode();
        task.setAssignTime(DateUtils.now14());
        task.setNode(node);
        // 流程发起人
        task.setUser(node.getWfFlow().getUser());
        task.setID(UUID.get());
        task.setStatus(WfTask.STATUS_ASSIGNED);
    	logger.debug("assign task for applicant");
    	logger.debug(node.getWfFlow().getUser().getName());
        return task;
    }
}
