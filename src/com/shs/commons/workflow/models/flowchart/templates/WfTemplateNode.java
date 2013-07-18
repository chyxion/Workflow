/**
 * file describe: WorkflowNode.java
 *
 */
package com.shs.commons.workflow.models.flowchart.templates;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shs.commons.workflow.models.AbstractModel;

/**
 * @class describle: //table name: wf_node
 * @version 0.1
 * @date created: 2012-2-24 下午3:46:21
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class WfTemplateNode extends AbstractModel{
	public final static String TABLE_NAME = "wf_node";
	public static final String ID_NAME = "f_ndid";
	private String id;
	private String name;
	/**
	 * 	MNU:人工结点
		AUT:自动结点
		EML:发送邮件结点
		MSG:发送消息结点
		STR;开始结点
		END:结束结点
	 */
	private String type;
	private String beforeAction;
	private String currentAction;
	private String afterAction;
	private String assignStrategy;
	private String taskType; 
	private String outRouteType;
	private int timeLimit;
	private String workflowTemplateId;
	private String offset;
	
	public WfTemplateNode(String id, String name, String type,
			String beforeAction, String currentAction, String afterAction,
			String assignStrategy, String taskType, String outRouteType,
			int timeLimit, String workflowTemplateId, String offset) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.beforeAction = beforeAction;
		this.currentAction = currentAction;
		this.afterAction = afterAction;
		this.assignStrategy = assignStrategy;
		this.taskType = taskType;
		this.outRouteType = outRouteType;
		this.timeLimit = timeLimit;
		this.workflowTemplateId = workflowTemplateId;
		this.offset = offset;
	}

	@SuppressWarnings("unchecked")
	public static WfTemplateNode getInstanceFromResultSet(ResultSet rs)
			throws SQLException {
		String id = rs.getString("f_ndid");
		String name = rs.getString("f_name");
		String type = rs.getString("f_type");
		String beforeAction = rs.getString("f_pactn");
		String currentAction = rs.getString("f_actn");
		String afterAction = rs.getString("f_eactn");
		String assignStrategy = rs.getString("f_asgn");
		String taskType = rs.getString("f_task"); 
		String outRouteType = rs.getString("f_out");
		int timeLimit = rs.getInt("f_time");
		String workflowTemplateId = rs.getString("f_tmid");
		String offset = rs.getString("offset");
		return new WfTemplateNode(id, name, type, beforeAction, currentAction, afterAction, assignStrategy, taskType, outRouteType, timeLimit, workflowTemplateId, offset);
	}
	//data access
	public static WfTemplateNode get(String id) throws Exception{
		return AbstractModel.<WfTemplateNode>findBySQL ("select * from " + TABLE_NAME + " where " + ID_NAME + " = '" + id + "'", 
					WfTemplateNode.class);
	}
	public static List<WfTemplateNode> list() throws Exception{
		return AbstractModel.<WfTemplateNode>findAllBySQL("select * from " + TABLE_NAME,
                WfTemplateNode.class);
	}
	
	public static List<WfTemplateNode> findAllWhere(String whareExpr) throws Exception{
		return AbstractModel.<WfTemplateNode>findAllBySQL("select * from " + TABLE_NAME + " where " + whareExpr,
                WfTemplateNode.class);
	}
	
	public String getId() {
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBeforeAction() {
		return beforeAction;
	}
	public void setBeforeAction(String beforeAction) {
		this.beforeAction = beforeAction;
	}
	public String getAction() {
		return currentAction;
	}
	public void setCurrentAction(String currentAction) {
		this.currentAction = currentAction;
	}
	public String getAfterAction() {
		return afterAction;
	}
	public void setAfterAction(String afterAction) {
		this.afterAction = afterAction;
	}
	public String getAssignStrategy() {
		return assignStrategy;
	}
	public void setAssignStrategy(String assignStrategy) {
		this.assignStrategy = assignStrategy;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getOutRouteType() {
		return outRouteType;
	}
	public void setOutRouteType(String outRouteType) {
		this.outRouteType = outRouteType;
	}
	public int getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	public String getWorkflowTemplateId() {
		return workflowTemplateId;
	}
	public void setWorkflowTemplateId(String workflowTemplateId) {
		this.workflowTemplateId = workflowTemplateId;
	}
	@Override
	public String toString() {
		return "WorkflowTemplateNode [id=" + id + ", name=" + name + ", type="
				+ type + ", beforeAction=" + beforeAction + ", currentAction="
				+ currentAction + ", afterAction=" + afterAction
				+ ", assignStrategy=" + assignStrategy + ", taskType="
				+ taskType + ", outRouteType=" + outRouteType + ", timeLimit="
				+ timeLimit + ", workflowTemplateId=" + workflowTemplateId
				+ "]";
	}
	public JSONObject toJSONObject(){
		JSONObject jo = new JSONObject();
		try {
			jo.put("id", this.id);
			jo.put("name", this.name);
			jo.put("type", this.type);
			jo.put("before_action", this.beforeAction);
			jo.put("action", this.currentAction);
			jo.put("after_action", this.afterAction);
			jo.put("assign_strategy", this.assignStrategy);
			jo.put("task_type", this.taskType);
			jo.put("out_route_type", this.outRouteType);
			jo.put("time_limit", this.timeLimit);
			jo.put("workflow_template_id", this.workflowTemplateId);
			jo.put("offset", new JSONObject(this.offset));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo;
	}
	public static JSONArray toJSONArray(List<WfTemplateNode> nodes){
		JSONArray nodesJSONArray = new JSONArray();
		for (WfTemplateNode node : nodes) {
			nodesJSONArray.put(node.toJSONObject());
		}
		return nodesJSONArray;
	}
	public static JSONArray toJSONArray(Map<String, WfTemplateNode> nodes){
		Collection<WfTemplateNode> nodesCollection = nodes.values();
		JSONArray nodesJSONArray = new JSONArray();
		for (WfTemplateNode node : nodesCollection) {
			nodesJSONArray.put(node.toJSONObject());
		}
		return nodesJSONArray;
	}
	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}
	
}
