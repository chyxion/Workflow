package com.shs.commons.workflow.instance.services;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import com.shs.commons.workflow.activities.ActivityService;
import com.shs.commons.workflow.core.WfEngine;
import com.shs.commons.workflow.instance.dao.WfFlowDAO;
import com.shs.commons.workflow.instance.dao.WfNodeDAO;
import com.shs.commons.workflow.instance.dao.WfTaskDAO;
import com.shs.commons.workflow.instance.models.WfFlow;
import com.shs.commons.workflow.instance.models.WfNode;
import com.shs.commons.workflow.instance.models.WfRoute;
import com.shs.commons.workflow.instance.models.WfTask;
import com.shs.commons.workflow.interfaces.IWfActivity;
import com.shs.commons.workflow.models.WfVar;
import com.shs.commons.workflow.template.models.WfTplNode;
import com.shs.commons.workflow.template.services.WfTemplateService;
import com.shs.framework.core.BaseService;
import com.shs.framework.dao.BaseDAO.ConnectionOperator;
import com.shs.framework.utils.DateUtils;
import com.shs.framework.utils.UUID;
/**
 * @version 0.1
 * @author chyxion
 * @describe: 任务服务
 * @date created: Jan 18, 2013 2:59:16 PM
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public class WfTaskService extends BaseService {
	private WfTaskDAO taskDAO = new WfTaskDAO(dao);
    private WfTemplateService tplService = new WfTemplateService();
    private WfFlowService flowService = new WfFlowService();
    private ActivityService activityService = new ActivityService();
    private WfNodeDAO nodeDAO = new WfNodeDAO(dao);
    private WfRouteService routeService = new WfRouteService();
	/**
	 * 接受人工任务，用户打开代办任务时，即已经接受该任务。
	 */
	public void accept(Connection dbConnection, WfTask task) throws Exception {
		//任务处于分配状态，则可以被接受
		if (WfTask.STATUS_ASSIGNED.equals(task.getStatus())) {
			logger.debug("accept task");
			logger.debug(task.getNode().getTplNode().getName());
			// 接收任务
			task.setAcceptTime(DateUtils.now14());
			task.setStatus(WfTask.STATUS_ACCEPTED);
			taskDAO.acceptTask(dbConnection, task);
			// 如果是抢占式任务，则Kill 兄弟任务
			if (WfTplNode.TASK_TYPE_PREEMPTIVE.equals(task.getNode().getTplNode().getType())) {
				taskDAO.dieBrotherTasks(dbConnection, task);
			}
		}
	}
	/**
	 * 查找一个任务
	 * @param dbConnection
	 * @param taskID
	 * @return
	 * @throws Exception
	 */
	public WfTask get(Connection dbConnection, String taskID) throws Exception {
		WfFlowService flowService = new WfFlowService();
		WfFlow flow = flowService.get(dbConnection, 
				taskDAO.findOwnerFlowID(dbConnection, taskID));
		WfTask task = null;
		for (WfNode node : flow.getNodes().values()) {
			if ((task = node.getTasks().get(taskID)) != null)
				break;
		}
		return task;
	}
	/**
	 * 列出待办任务
	 * @return
	 * @throws Exception
	 */
	public JSONObject list() throws Exception {
		return dao.execute(new ConnectionOperator() {
			@Override
			public void run() throws Exception {
				result = list(dbConnection, 
						//params.getUser().toString(),  // user id 
						params.get(),  // user id 
						params.getInt("start"), 
						params.getInt("limit"));
			}
		});
	}
	public JSONObject list(Connection dbConnection, String userID, int start, int limit) throws Exception {
		return new JSONObject()
		.put("data", taskDAO.listAllTodo(dbConnection, userID, start, limit))
		.put("total", taskDAO.tasksCount(dbConnection, userID));
	}
	/**
	 * 任务历史记录
	 * @return
	 * @throws Exception
	 */
	public JSONArray tasksHistory() throws Exception {
		validate("FLOW_ID", NOT_BLANK);
		return taskDAO.listHistory(params.get("FLOW_ID"));
	}
	public JSONArray tasksHistory(Connection dbConnection, String flowID) throws Exception {
		return taskDAO.listHistory(dbConnection, flowID);
	}
	/**
	 * 本人任务历史
	 * @return
	 * @throws Exception
	 */
	public JSONObject myTasksHistory() throws Exception {
		return dao.execute(new ConnectionOperator() {
			@Override
			public void run() throws Exception {
				String userID = params.get("user_id"); // user_id
				result = new JSONObject().put("data", 
					taskDAO.findTasksByUserID(dbConnection, userID,
					params.getInt("start"), 
					params.getInt("limit")))
				.put("total", taskDAO.tasksCount(dbConnection, userID));	
			}
		});
	}
	/**
	 * 任务列表，根据业务编号
	 * @param dbConnection
	 * @param bizID
	 * @return
	 * @throws Exception
	 */
	public JSONArray tasksListByBizID(Connection dbConnection, String bizID) throws Exception {
		return new JSONArray(taskDAO.listHistoryByBizID(dbConnection, bizID));
	}
	public List<Map<String, Object>> tasksMapListByBizID(Connection dbConnection, String bizID) throws Exception {
		return taskDAO.listHistoryByBizID(dbConnection, bizID);
	}
	/**
	 * 提交任务，更新任务状态为完成，如果传入变量，增加或更新流程变量
	 * @param dbConnection
	 * @param task
	 * @param newVars
	 * @throws Exception
	 */
	public void commit(Connection dbConnection, WfTask task, Map<String, WfVar> newVars) throws Exception {
		// 0 完成任务
		task.setDoneTime(DateUtils.now14());
		task.setStatus(WfTask.STATUS_COMPLETED);
		taskDAO.complete(dbConnection, task);
		logger.debug("Complete Task");
		logger.debug(task.toJO());
		// 1更新流程变量
		flowService.addVars(dbConnection, task.getNode().getWfFlow(), newVars);
		// 2 活动再次放入引擎队列
		WfEngine.getInstance().getActivityQueue().enqueue(activityService.instanceAtivity(task.getNode()));
	}
	/**
	 * 提交任务，没有流程变量
	 * @param dbConnection
	 * @param task
	 * @throws Exception
	 */
	public void commit(Connection dbConnection, WfTask task) throws Exception {
		commit(dbConnection, task, null);
	}
	/**
	 * 提交任务
	 * @param dbConnection
	 * @param taskID
	 * @param opinion
	 * @throws Exception
	 */
	public void commit(Connection dbConnection, String taskID, String opinion) throws Exception {
		WfTask task = get(dbConnection, taskID);
		task.setOpinion(opinion);
		commit(dbConnection, task);
	}
	/**
	 * 任务回滚
	 * @param dbConnection
	 * @param task
	 * @throws Exception
	 */
	public void rollback(Connection dbConnection, WfTask task, Map<String, WfVar> newVars) throws Exception {
		WfFlow flow = task.getNode().getWfFlow();
		// 驳回的前提是：该任务类型是抢占式；否则不允许驳回（审批页面不设驳回功能），驳回到流程申请结点
		// 0完成任务
		String now = DateUtils.now14();

		// 2 重新创建申请节点，持久化
		WfNode applyNode = new WfNode();
		applyNode.setID(UUID.get());
		applyNode.setStartTime(now);
		applyNode.setStatus(WfNode.STATUS_RUNNING);
		applyNode.setTplNode(tplService.getApplicantNode(flow.getWfTpl()));
		applyNode.setWfFlow(flow);
		nodeDAO.newNode(dbConnection, applyNode.toJO());
		// 添加到流程
		flow.getNodes().put(applyNode.getID(), applyNode);
		// 3 生成连接，指向申请节点，该连接没有模板连接
		WfRoute route = new WfRoute();
		route.setID(UUID.get());
		route.setFromNode(task.getNode());
		route.setToNode(applyNode);
		routeService.newRoute(dbConnection, route);
		// 添加到流程
		flow.getRoutes().put(route.getID(), route);
		
		// 1更新流程变量
		flowService.addVars(dbConnection, flow, newVars);
		
		// 0 完成任务
		task.setDoneTime(now);
		task.setStatus(WfTask.STATUS_COMPLETED);
		// 持久化
		taskDAO.complete(dbConnection, task);
		// 1更新流程变量
		flowService.addVars(dbConnection, task.getNode().getWfFlow(), newVars);
		// 2 活动再次放入引擎队列
		IWfActivity activity = activityService.instanceAtivity(task.getNode());
		// 标志回滚!!!重要
		activity.setRollback(true);
		// 添加后继活动!!!重要
		activity.addPostActivity(activityService.instanceAtivity(applyNode));
		WfEngine.getInstance().getActivityQueue().enqueue(activity);
	}
	/**
	 * 回滚任务，无流程变量
	 * @param dbConnection
	 * @param task
	 * @throws Exception
	 */
	public void rollback(Connection dbConnection, WfTask task) throws Exception {
		rollback(dbConnection, task, null);
	}
	/**
	 * 审批任务
	 * @throws Exception
	 */
	public void approve() throws Exception {
		dao.executeTransaction(new ConnectionOperator() {
			@Override
			public void run() throws Exception {
				WfTask task = get(dbConnection, params.get("TASK_ID"));
				assert task != null;
				// 审批意见
				task.setOpinion(params.get("OPINION"));
				commit(dbConnection, task);
			}
		});
	}
	/**
	 * 驳回任务
	 * @throws Exception
	 */
	public void reject() throws Exception {
		dao.execute(new ConnectionOperator() {
			@Override
			public void run() throws Exception {
				WfTask task = get(dbConnection, params.get("TASK_ID"));
				// 审批意见
				task.setOpinion(params.get("OPINION"));
				rollback(dbConnection, task);
			}
		});
	}
	/**
	 * 接受任务
	 * @throws Exception
	 */
	public void accept() throws Exception {
		validate("TASK_ID", NOT_BLANK);
		dao.executeTransaction(new ConnectionOperator() {
			@Override
			public void run() throws Exception {
				WfTask task = get(dbConnection, params.get("TASK_ID"));
				assert task != null;
				accept(dbConnection, task);
			}
		});
	}
	/**
	 * 取消流程
	 * @param dbConnection
	 * @param taskID
	 * @throws Exception
	 */
	public void cancel(Connection dbConnection, String taskID) throws Exception {
		WfTask task = get(dbConnection, taskID);
		String now = DateUtils.now14();
		task.setOpinion("取消流程");
		// 0 完成任务
		task.setDoneTime(now);
		task.setStatus(WfTask.STATUS_COMPLETED);
		taskDAO.complete(dbConnection, task);
		// 完成节点
		WfNode node = task.getNode();
		node.setDoneTime(now);
		node.setStatus(WfNode.STATUS_COMPLETED);
		new WfNodeDAO(dao).completeNode(dbConnection, node);
		// 完成流程
		WfFlow flow = node.getWfFlow();
		flow.setDoneTime(now);
		flow.setStatus(WfFlow.STATUS_CANCELED);
		new WfFlowDAO(dao).complete(dbConnection, flow);
	}
}
