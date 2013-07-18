/**
 * file describe: Route.java
 *
 */
package com.shs.commons.workflow.models.flowchart.templates;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shs.commons.workflow.models.AbstractModel;

/**
 * @class describle: 
 * @version 0.1
 * @date created: 2012-2-24 下午3:46:56
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class WfTemplateRoute extends AbstractModel{
	public final static String TABLE_NAME = "wf_rute";
	public static final String ID_NAME = "f_ruid";
	private String id;
	private String fromNodeId;
	private String toNodeId;
	private String expression;
	private String description;
	private String tplID;
	
	public WfTemplateRoute() {
	}
	
	public WfTemplateRoute(String id, String nodeFromId, String nodeToId,
			String expression, String description, String tplID) {
		this.id = id;
		this.fromNodeId = nodeFromId;
		this.toNodeId = nodeToId;
		this.expression = expression;
		this.description = description;
		this.tplID = tplID;
	}
	//data access
	public static WfTemplateRoute get(String id) throws Exception{
		return AbstractModel.<WfTemplateRoute>findByID (TABLE_NAME, ID_NAME, id, 
					WfTemplateRoute.class);
	}
	public static List<WfTemplateRoute> list() throws Exception{
		return AbstractModel.<WfTemplateRoute>findAllBySQL("select * from " + TABLE_NAME,
                WfTemplateRoute.class);
	}
		
	public static List<WfTemplateRoute> findAllWhere(String whereExpr) throws Exception{
		return AbstractModel.<WfTemplateRoute>findAllBySQL("select * from " + TABLE_NAME + " where " + whereExpr,
                WfTemplateRoute.class);
	}
	@SuppressWarnings("unchecked")
	public static WfTemplateRoute getInstanceFromResultSet(ResultSet rs)
			throws SQLException {
		String id = rs.getString("id");
		String nodeFromId = rs.getString("from_node_id");
		String nodeToId = rs.getString("to_node_id");
		String expressConditions = rs.getString("expression");
		String description = rs.getString("note");
		String workflowTemplateId = rs.getString("tpl_id");
		return new WfTemplateRoute(id, nodeFromId, nodeToId, expressConditions, description, workflowTemplateId);
	}
	
	//tools
	
	/**
	 * 工具方法, 返回route集合中所有from节点Id
	 * @param routesList
	 * @return
	 */
	public static List<String> getFromNodeIds(Collection<WfTemplateRoute> routesList){
		List<String> fromNodesIds = new LinkedList<String>();
		
		for (WfTemplateRoute route : routesList) 
			fromNodesIds.add(route.getFromNodeId());
				
		return fromNodesIds;
	}
	/**
	 * 返回route结合中的全部to节点Id
	 * @param routesList
	 * @return
	 */
	public static List<String> getToNodeIds(Collection<WfTemplateRoute> routesList){
		List<String> toNodesIds = new LinkedList<String>();
		
		for (WfTemplateRoute route : routesList) 
			toNodesIds.add(route.getToNodeId());
				
		return toNodesIds;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFromNodeId() {
		return fromNodeId;
	}

	public void setFromNodeId(String fromNodeId) {
		this.fromNodeId = fromNodeId;
	}

	public String getToNodeId() {
		return toNodeId;
	}

	public void setToNodeId(String toNodeId) {
		this.toNodeId = toNodeId;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String conditionEexpression) {
		this.expression = conditionEexpression;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTplID() {
		return tplID;
	}

	public void setTplID(String tplID) {
		this.tplID = tplID;
	}

	@Override
	public String toString() {
		return "WorkflowTemplateRoute [id=" + id + ", fromNodeId="
				+ fromNodeId + ", toNodeId=" + toNodeId
				+ ", expression=" + expression + ", description="
				+ description + ", tplID=" + tplID
				+ "]";
	}
	public JSONObject toJSONObject(){
		JSONObject jo = new JSONObject();
		try {
			jo.put("id", this.id);
			jo.put("from_node_id", this.fromNodeId);
			jo.put("to_node_id", this.toNodeId);
			jo.put("condition_expression", this.expression);
			jo.put("description", this.description);
			jo.put("workflow_template_id", this.tplID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo;
	}
	public static JSONArray toJSONArray(Map<String, WfTemplateRoute> routes){
		JSONArray routesJSONArray = new JSONArray();
		Collection<WfTemplateRoute> routesCollection = routes.values();
		for (WfTemplateRoute route : routesCollection) 
			routesJSONArray.put(route.toJSONObject());
		return routesJSONArray;
	}
	public static JSONArray toJSONArray(List<WfTemplateRoute> routes){
		JSONArray routesJSONArray = new JSONArray();
		for (WfTemplateRoute route : routes) 
			routesJSONArray.put(route.toJSONObject());
		return routesJSONArray;
	}
}
