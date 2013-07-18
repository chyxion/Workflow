package com.shs.commons.workflow.instance.services;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import com.shs.commons.services.UserService;
import com.shs.commons.workflow.instance.dao.WfNodeDAO;
import com.shs.commons.workflow.instance.dao.WfTaskDAO;
import com.shs.commons.workflow.instance.models.WfFlow;
import com.shs.commons.workflow.instance.models.WfNode;
import com.shs.commons.workflow.instance.models.WfTask;
import com.shs.commons.workflow.template.models.WfTemplate;
import com.shs.framework.core.BaseService;
import com.shs.framework.dao.BaseDAO;
import com.shs.framework.utils.DateUtils;

/**
 * @version 0.1
 * @author chyxion
 * @describe: 
 * @date created: Dec 29, 2012 10:49:17 AM
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class WfNodeService extends BaseService {
	private WfNodeDAO nodeDAO = new WfNodeDAO(dao);
	private WfTaskDAO taskDAO = new WfTaskDAO(dao);
	public void newNode(Connection dbConnection, WfNode node) throws Exception {
		nodeDAO.newNode(dbConnection, node.toJO());
	}
	public WfNodeService(BaseDAO dao) {
		super();
		this.dao = dao;
	}
	public WfNodeService() {
		super();
	}
	
	/**
	 * 查找流程节点
	 * @param dbConnection
	 * @param jaNodeIDs
	 * @return
	 * @throws Exception
	 */
	public Map<String, WfNode> findAll(Connection dbConnection, JSONArray jaNodeIDs) throws Exception {
		JSONArray jaNodes = nodeDAO.findAll(dbConnection, jaNodeIDs);
		Map<String, WfNode> nodes = new HashMap<String, WfNode>();
		Map<String, WfFlow> flows = new HashMap<String, WfFlow>();
		
		WfFlowService flowService = new WfFlowService();
		for (int i = 0; i < jaNodes.length(); ++i) {
			JSONObject joNode = jaNodes.getJSONObject(i);
			// 在已查找过的流程中查找
			String flowID = joNode.getString("FLOW_ID");
			WfFlow flow = flows.get(flowID);
			if (flow == null) { // 未查找过数据库，查数据库
				flow = flowService.get(dbConnection, flowID);
				flows.put(flowID, flow);
			} 
			WfNode node = flow.getNodes().get(joNode.getString("ID"));
			nodes.put(node.getID(), node);
		}
		return nodes;
	}
	public void findAll(Connection dbConnection, WfFlow flow) throws Exception {
		UserService userService = new UserService();
		WfTemplate flowTpl = flow.getWfTpl();
    	// 流程节点
    	JSONArray jaNodes = nodeDAO.findAllByFlowID(dbConnection, flow.getID());
    	Map<String, WfNode> nodes = new HashMap<String, WfNode>();
    	for (int j = 0; j < jaNodes.length(); ++j) {
    		JSONObject joNode = jaNodes.getJSONObject(j);
    		WfNode node = nodeDAO.getNodeFromJO(joNode);
    		String nodeID = node.getID();
    		// 任务
    		JSONArray jaTasks = taskDAO.findAllByNodeID(dbConnection, nodeID);
    		Map<String, WfTask> tasks = new HashMap<String, WfTask>();
    		for (int k = 0; k < jaTasks.length(); ++k) {
    			JSONObject joTask = jaTasks.getJSONObject(k);
    			WfTask task = taskDAO.getTaskFromJO(joTask);
    			// 用户
    			task.setUser(userService.get(dbConnection, joTask.getString("USER_ID")));
    			// 节点
    			task.setNode(node);
    			tasks.put(task.getID(), task);
    		}
    		node.setTasks(tasks);
    		// 模板节点
    		node.setTplNode(flowTpl.getNodes().get(joNode.getString("TPL_NODE_ID")));
    		// 流程
    		node.setWfFlow(flow);
    		nodes.put(nodeID, node);
    	}
    	flow.setNodes(nodes);
	}
	/**
	 * 完成节点
	 * @param dbConnection
	 * @param node
	 */
	public void completeNode(Connection dbConnection, WfNode node) throws Exception {
        node.setStatus(WfNode.STATUS_COMPLETED);
        //修改完成时间
        node.setDoneTime(DateUtils.now14());
        //持久化
		nodeDAO.completeNode(dbConnection, node);
	}
}
