package com.shs.commons.workflow.models.flowchart.instances;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.shs.commons.workflow.models.AbstractModel;
import com.shs.commons.workflow.models.flowchart.templates.WfTemplate;
import com.shs.commons.workflow.models.flowchart.templates.WfTemplateNode;
import com.shs.framework.dao.BaseDAO;

/**
 * @class describe: 
 * @version 0.1
 * @date created: Mar 1, 2012 2:23:37 PM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class WfInstance extends AbstractModel{
	public final static String TABLE_NAME = "wf_flow";
	public static final String ID_NAME = "f_flid";
	
	private String id;
	private String name;
	private String bizID;
	private String timeStart;
	private String timeDone;
	private String status;
	private String userID;
	private String userName;
	private String userUnitId;
	private String userUnitName;
	
	private Map<String, WfInstanceNode> nodes;
	private Map<String, WfInstanceRoute> routes;
	private WfTemplate wfTpl;
	public WfInstance(String id, String name, String businessId,
			String timeStart, String timeDone, String status,
			WfInstance parentFlow, String userId, String userName,
			String userUnit, String userUnitName,
			Map<String, WfInstanceNode> nodes,
			Map<String, WfInstanceRoute> routes,
			WfTemplate workflowTemplate) {
		super();
		this.id = id;
		this.name = name;
		this.bizID = businessId;
		this.timeStart = timeStart;
		this.timeDone = timeDone;
		this.status = status;
		this.userID = userId;
		this.userName = userName;
		this.userUnitId = userUnit;
		this.userUnitName = userUnitName;
		this.nodes = nodes;
		this.routes = routes;
		this.wfTpl = workflowTemplate;
	}
	//data access
	/**
	 * 根据主键id查找
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static WfInstance get(String id) throws Exception {
        return new BaseDAO().query(
                new BaseDAO.ResultSetOperator() {
                    @Override
                    public void run() throws Exception {
                        if (resultSet.next()) {
                            result = getInstanceFromResultSet(dbConnection, resultSet);
                        }
                    }
                }, "select * from wf_flow where f_flid = ?", id);
	}
	/**
	 * 返回所有
	 * @return
	 * @throws Exception 
	 */
	public static List<WfInstance> list() throws Exception{
		return AbstractModel.findAllBySQL("select * from " + TABLE_NAME, WfInstance.class);
	}
	/**
	 * 从ResultSet中构建实例
	 * @param rs
	 * @return
	 * @throws Exception 
	 */
	public static WfInstance getInstanceFromResultSet(Connection dbConnection, ResultSet rs) throws Exception {
		String id = rs.getString(ID_NAME);
		String name = rs.getString("f_name");
		String businessId = rs.getString("f_bid");
		String timeStart = rs.getString("f_stme");
		String timeDone = rs.getString("f_etme");
		String status = rs.getString("f_stus");
        String pFlowID = rs.getString("f_pflw");
		WfInstance pFlow = null;
        if (pFlowID != null)
            pFlow = WfInstance.get(pFlowID);

		WfTemplate workflowTemplate = WfTemplate.get(dbConnection, rs.getString("f_tmid"));
		String userId = rs.getString("f_usrid");
		String userName = rs.getString("f_usrname");
		String userUnitId = rs.getString("f_orgid");
		String userUnitName = rs.getString("f_orgname");
		//节点
		HashMap<String, WfInstanceNode> mapNodes = new HashMap<String, WfInstanceNode>();
		List<WfInstanceNode> nodesList = WfInstanceNode.findAllByFlowID(dbConnection, id);
		for (WfInstanceNode it : nodesList) 
			mapNodes.put(it.getId(), it);
		//节点连接
		HashMap<String, WfInstanceRoute> routesMap = new HashMap<String, WfInstanceRoute>();
		List<WfInstanceRoute> routesList = WfInstanceRoute.findAllByFlowID(dbConnection, id);
		for (WfInstanceRoute it : routesList) 
			routesMap.put(it.getId(), it);
				
		return new WfInstance(id, name, businessId, 
				timeStart, timeDone, status, 
				pFlow, userId, userName,
				userUnitId, userUnitName, mapNodes, routesMap, workflowTemplate);
	}
	/**
	 * 查找一组节点后紧跟的节点组
	 * @param nodes
	 * @return
	 */
	public List<WfInstanceNode> nextNodes(List<WfInstanceNode> nodes){
		Collection<WfInstanceRoute> routesList = this.routes.values();
		List<WfInstanceNode> nextNodes = new LinkedList<WfInstanceNode>();
		
		for (WfInstanceNode node : nodes) 
			for (WfInstanceRoute it : routesList) 
				if (node.getId().equals(it.getFromNodeId())){
					WfInstanceNode nd = this.nodes.get(it.getToNodeId());
					if (!nextNodes.contains(nd)) //对于多个节点后跟一个节点的情况, 只保留一个
						nextNodes.add(nd);
				}
					
		return nextNodes;
	}

	/**
	 * 返回本流程的起始节点
	 * @return
	 */
	public List<WfInstanceNode> startNodes(){
		List<WfInstanceNode> startNodesList = new LinkedList<WfInstanceNode>();
		Collection<WfInstanceRoute> routesList = this.routes.values();
		List<String> fromNodeIDs = WfInstanceRoute.getFromNodeIDs(routesList);
		List<String> toNodeIds = WfInstanceRoute.getToNodeIDs(routesList);
		//在from节点集合中, 但是不在to节点集合中 
		for (String nodeID : fromNodeIDs)
			if (!toNodeIds.contains(nodeID))
				startNodesList.add(this.nodes.get(nodeID));
				
		return startNodesList;
	}
	
	/**
	 * 返回本流程实例的中的最后节点 
	 * @return
	 */
	public List<WfInstanceNode> lastNodes() {
		List<WfInstanceNode> lastNodesList = new LinkedList<WfInstanceNode>();
		Collection<WfInstanceRoute> routesList = this.routes.values();
		List<String> fromNodeIds = WfInstanceRoute.getFromNodeIDs(routesList);
		List<String> toNodeIds = WfInstanceRoute.getToNodeIDs(routesList);
		//在to节点集合中, 但是不在from节点集合中
		for (String nodeId : toNodeIds) 
			if (!fromNodeIds.contains(nodeId))
				lastNodesList.add(this.nodes.get(nodeId));
				
		return lastNodesList;
	}
	/**
	 * 工具方法, 返回实例节点的模板节点 
	 * @param instanceNodes
	 * @return
	 */
	public static List<WfTemplateNode> tplNodes(List<WfInstanceNode> instanceNodes) {
		LinkedList<WfTemplateNode> templateNodesList = new LinkedList<WfTemplateNode>();
		for (WfInstanceNode node : instanceNodes) 
			templateNodesList.add(node.getTemplateNode());
		return templateNodesList;
	}
	/**
	 * 返回流程实例最后节点之后一层模板节点
	 * @return
	 */
	public List<WfTemplateNode> nodesAfterLast() {
		return wfTpl.nextNodes(tplNodes(lastNodes()));
	}

	public String getUserID() {
		return userID;
	}

	public String getUserName() {
		return userName;
	}


	public String getUserUnitId() {
		return userUnitId;
	}

	public String getUserUnitName() {
		return userUnitName;
	}

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getTimeStart() {
		return timeStart;
	}


	public String getTimeDone() {
		return timeDone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBizID() {
		return bizID;
	}

	public Map<String, WfInstanceRoute> getRoutes() {
		return routes;
	}

	public WfTemplate getWfTpl() {
		return wfTpl;
	}
}
