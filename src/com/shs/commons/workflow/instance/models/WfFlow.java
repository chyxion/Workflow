package com.shs.commons.workflow.instance.models;
import com.shs.commons.models.User;
import com.shs.commons.workflow.models.WfVar;
import com.shs.commons.workflow.template.models.WfTemplate;

import java.util.*;
import org.json.JSONObject;

/**
 * @class describe: 流程实例
 * @version 0.1
 * @date created: Mar 1, 2012 2:23:37 PM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class WfFlow {
	/**
	 * 运行中
	 */
    public static final String STATUS_RUNNING = "RUNNING";
    /**
     * 完成
     */
    public static final String STATUS_COMPLETED = "COMPLETED";
    /**
     * 流程进入异常
     */
    public static final String STATUS_EXCEPTIONAL = "EXCEPTIONAL";
    /**
     * 流程被取消
     */
    public static final String STATUS_CANCELED = "CANCELED";
    /**
     * 业务新建状态
     */
    public static final String BIZ_STATUS_NEW = "N";
    /**
     * 业务运行状态
     */
    public static final String BIZ_STATUS_RUNNING = "R";
    /**
     * 业务完成状态
     */
    public static final String BIZ_STATUS_COMPLETED = "S";
    /**
     * 业务取消状态
     */
    public static final String BIZ_STATUS_CANCELD = "C";
    /**
     * 流程编号
     */
	private String id;
	/**
	 * 业务编号
	 */
	private String bizID;
	/**
	 * 启动时间
	 */
	private String startTime;
	/**
	 * 完成时间
	 */
	private String doneTime;
	/**
	 * 状态
	 */
	private String status;
	/**
	 * 发起人
	 */
    private User user;
    /**
     * 流程节点
     */
	private Map<String, WfNode> nodes = new HashMap<String, WfNode>();
	/**
	 * 流程连接
	 */
	private Map<String, WfRoute> routes = new HashMap<String, WfRoute>();
	/**
	 * 流程变量
	 */
	private Map<String, WfVar> vars = new HashMap<String, WfVar>();
	/**
	 * 流程模板
	 */
	private WfTemplate wfTpl;
	/**
	 * 备注
	 */
	private String note;
	
	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getStartTime() {
		return startTime;
	}


	public String getDoneTime() {
		return doneTime;
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
	public WfFlow setBizID(String bizID) {
		this.bizID = bizID;
		return this;
	}
	public Map<String, WfRoute> getRoutes() {
		return routes;
	}

	public WfTemplate getWfTpl() {
		return wfTpl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Map<String, WfNode> getNodes() {
		return nodes;
	}

	public void setNodes(Map<String, WfNode> nodes) {
		this.nodes = nodes;
	}

	public void setStartTime(String timeStart) {
		this.startTime = timeStart;
	}

	public void setDoneTime(String timeDone) {
		this.doneTime = timeDone;
	}

	public void setRoutes(Map<String, WfRoute> routes) {
		this.routes = routes;
	}

	public void setWfTpl(WfTemplate wfTpl) {
		this.wfTpl = wfTpl;
	}

	public void setVars(Map<String, WfVar> vars) {
		this.vars = vars;
	}

	public Map<String, WfVar> getVars() {
		return vars;
	}
	public void addVar(WfVar var) {
		
	}
	public WfVar getVar(String name) {
		return vars.get(name);
	}
	public void modifyVar(WfVar var) {
        if (var == null) return;
        WfVar thisVar = this.vars.get(var.getName());
        if (thisVar == null) return;;
        if (!(thisVar.getType().equals(var.getType())))
        	return;
        thisVar.setValue(var.getValue());
	}
	public void addNode(WfNode node) {
		nodes.put(node.getID(), node);
	}
	public void addRoute(WfRoute route) {
		routes.put(route.getID(), route);
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getNote() {
		return note;
	}
	public JSONObject toJO() throws Exception {
		return new JSONObject()
			.put("ID", id)
			.put("BIZ_ID", bizID)
			.put("START_TIME", startTime)
			.put("DONE_TIME", doneTime)
			.put("STATUS", status)
			.put("NOTE", note);
	}
}
