package com.shs.commons.workflow.template.models;
import com.shs.commons.workflow.models.WfVar;
import com.shs.commons.workflow.template.models.interfaces.IWfTplNode;

import org.json.JSONObject;
import java.util.*;
/**
 * @class describe: 流程模板
 * @version 0.1
 * @date created: 2012-2-24 下午3:46:45
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class WfTemplate {

	private String id;
	private String name;
	private String note;
	private String status;
	private WfTplType type;
	private String beforeAction;
	private String afterAction;
	private Map<String, IWfTplNode> nodes;
	private Map<String, WfTplRoute> routes;
	private Map<String, WfVar> vars;
	
	public WfTemplate() {
	}
	//getter and setters
	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public Map<String, IWfTplNode> getNodes() {
		return nodes;
	}

	public void setNodes(Map<String, IWfTplNode> nodes) {
		this.nodes = nodes;
	}

	public Map<String, WfTplRoute> getRoutes() {
		return routes;
	}

	public void setRoutes(Map<String, WfTplRoute> routes) {
		this.routes = routes;
	}
	public JSONObject toJO() throws Exception {
		return new JSONObject()
			.put("id", this.id)
			.put("name", this.name)
			.put("note", this.note)
			.put("status", this.status)
			.put("type", type.toJO())
			.put("before_action", this.beforeAction)
			.put("after_action", this.afterAction)
			.put("after_action", this.afterAction)
			.put("nodes", WfTplNode.toJA(this.nodes))
			.put("routes", WfTplRoute.toJA(this.routes))
			.put("variables", WfVar.toJA(this.vars));
	}
	public Map<String, WfVar> getVars() {
		return vars;
	}

	public void setVars(Map<String, WfVar> vars) {
		this.vars = vars;
	}
}
