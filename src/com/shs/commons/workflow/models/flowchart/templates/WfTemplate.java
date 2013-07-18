/**
 * file describe: Template.java
 *
 */
package com.shs.commons.workflow.models.flowchart.templates;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import com.shs.commons.workflow.models.AbstractModel;

/**
 * @class describle: 流程模板, : wf_tmpt
 * @version 0.1
 * @date created: 2012-2-24 下午3:46:45
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class WfTemplate extends AbstractModel {
	public static final String TABLE_NAME = "wf_tmpt";
	public static final String ID_NAME = "f_tmid"; 
	
	private String id;
	private String name;
	private String description;
	private String status;
	private WfTplType type;
	private String beforeAction;
	private String afterAction;
	private Map<String, WfTemplateNode> nodes;
	private Map<String, WfTemplateRoute> routes;
	private Map<String, WfTemplateVar> variables;

	public WfTemplate(String id, String name, String description,
			String status, String beforeAction, String afterAction,
			WfTplType type,
			Map<String, WfTemplateNode> nodes,
			Map<String, WfTemplateRoute> routes, Map<String, WfTemplateVar> variables) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.status = status;
		this.beforeAction = beforeAction;
		this.afterAction = afterAction;
		this.type = type;
		this.nodes = nodes;
		this.routes = routes;
		this.variables = variables;
	}
	public static WfTemplate get(String id) throws Exception{
		return AbstractModel.findByID (TABLE_NAME, ID_NAME, id,
					WfTemplate.class);
	}

    public static WfTemplate get(Connection dbConnection, String id) throws Exception{
        return AbstractModel.findByID (dbConnection, TABLE_NAME, ID_NAME, id, WfTemplate.class);
    }
	public static List<WfTemplate> list() throws Exception{
		return AbstractModel.<WfTemplate>findAllBySQL("select * from " + TABLE_NAME,
                WfTemplate.class);
	}
	@SuppressWarnings("unchecked")
	public static WfTemplate getInstanceFromResultSet(ResultSet rs) throws Exception {
		String id = rs.getString("f_tmid");
		String name = rs.getString("f_name");
		String description = rs.getString("f_note");
		String status = rs.getString("f_stus");
		String beforeAction = rs.getString("f_pactn");
		String afterAction = rs.getString("f_eactn");
		String typeId = rs.getString("f_tpid");
		//添加节点
		HashMap<String, WfTemplateNode> nodesMap = new HashMap<String, WfTemplateNode>();
		List<WfTemplateNode> nodesList = WfTemplateNode.findAllWhere("f_tmid = '" + id + "'");
		
		for (WfTemplateNode it : nodesList) 
			nodesMap.put(it.getId(), it);
				
		//查处模板下的边,
		Map<String, WfTemplateRoute> routesMap = new HashMap<String, WfTemplateRoute>();
		List<WfTemplateRoute> routesList = WfTemplateRoute.findAllWhere("f_tmid = '" + id + "'");
		
		for (WfTemplateRoute it : routesList) 
			routesMap.put(it.getId(), it);
				
		Map<String, WfTemplateVar> variablesMap = new HashMap<String, WfTemplateVar>();
		List<WfTemplateVar> variablesList = WfTemplateVar.findByTemplateId(id);
		
		for (WfTemplateVar it : variablesList) 
			variablesMap.put(it.getName(), it);
				
		return new WfTemplate(id, name, description, status, beforeAction, afterAction, WfTplType.get(typeId), nodesMap, routesMap, variablesMap);
	}
	//tools
	
	/**
	 * 查找一组节点后紧跟的节点组 
	 * @param currNodes
	 * @return
	 */
	public List<WfTemplateNode> nextNodes(List<WfTemplateNode> currNodes){
		Collection<WfTemplateRoute> routesList = this.routes.values();
		List<WfTemplateNode> nextNodes = new LinkedList<WfTemplateNode>();
		
		for (WfTemplateNode node : currNodes) 
			for (WfTemplateRoute it : routesList) 
				if (node.getId().equals(it.getFromNodeId())) {
					WfTemplateNode nd = this.nodes.get(it.getToNodeId());
					if (!nextNodes.contains(nd)) //如果多个节点指向一个节点, 只取一个
						nextNodes.add(nd);
				}
		return nextNodes;
	}
	/**
	 * 找出节点的入边
	 * @param nodes
	 * @return
	 */
	public List<WfTemplateRoute> inRoutes(List<WfTemplateNode> nodes){
		List<WfTemplateRoute> inRoutesList = new LinkedList<WfTemplateRoute>();
		Collection<WfTemplateRoute> routesList = routes.values();
		for (WfTemplateNode node : nodes) 
			for (WfTemplateRoute route : routesList) 
				if (route.getToNodeId().equals(node.getId()))
					inRoutesList.add(route);
		return inRoutesList;
	}
	/**
	 * 返回节点nodes之后的所有连接边
	 * @param nodes
	 * @return
	 */
	public void routesAfter(List<WfTemplateNode> nodes, List<WfTemplateRoute> afterRoutes){
		List<WfTemplateNode> nextNodes = nextNodes(nodes);
		if (nextNodes.size() > 0) {
			afterRoutes.addAll(inRoutes(nextNodes));
			routesAfter(nextNodes, afterRoutes);
		}
	}
	//getter and setters 
	public String getID() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public WfTplType getType() {
		return type;
	}

	public void setType(WfTplType type) {
		this.type = type;
	}

	public String getBeforeAction() {
		return beforeAction;
	}

	public void setBeforeAction(String beforeAction) {
		this.beforeAction = beforeAction;
	}

	public String getAfterAction() {
		return afterAction;
	}

	public void setAfterAction(String afterAction) {
		this.afterAction = afterAction;
	}

	public Map<String, WfTemplateNode> getNodes() {
		return nodes;
	}

	public void setNodes(Map<String, WfTemplateNode> nodes) {
		this.nodes = nodes;
	}

	public Map<String, WfTemplateRoute> getRoutes() {
		return routes;
	}

	public void setRoutes(Map<String, WfTemplateRoute> routes) {
		this.routes = routes;
	}
	public JSONObject toJO() throws Exception {
		JSONObject jo = new JSONObject();
		try {
			jo.put("id", this.id);
			jo.put("name", this.name);
			jo.put("description", this.description);
			jo.put("status", this.status);
			jo.put("type", type.toJO());
			jo.put("before_action", this.beforeAction);
			jo.put("after_action", this.afterAction);
			jo.put("after_action", this.afterAction);
			jo.put("nodes", WfTemplateNode.toJSONArray(this.nodes));
			jo.put("routes", WfTemplateRoute.toJSONArray(this.routes));
			jo.put("variables", WfTemplateVar.toJSONArray(this.variables));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo;
	}
	public Map<String, WfTemplateVar> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, WfTemplateVar> variables) {
		this.variables = variables;
	}
}
