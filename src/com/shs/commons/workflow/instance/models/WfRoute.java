package com.shs.commons.workflow.instance.models;
import org.json.JSONObject;

import com.shs.commons.workflow.template.models.WfTplRoute;
/**
 * @class describe: 实例节点边
 * @version 0.1
 * @date created: Mar 1, 2012 2:24:05 PM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class WfRoute {
	/**
	 * 回滚模板连接
	 */
	public static final String TPL_ROUTE_ROLLBACK = "ROLLBACK";
	private String id;
	private WfTplRoute tplRoute;
	private WfNode fromNode;
	private WfNode toNode;
	
	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}
	public WfTplRoute getTplRoute() {
		return tplRoute;
	}
	public void setTplRoute(WfTplRoute tplRoute) {
		this.tplRoute = tplRoute;
	}
	public WfNode getFromNode() {
		return fromNode;
	}
	public void setFromNode(WfNode fromNode) {
		this.fromNode = fromNode;
	}
	public WfNode getToNode() {
		return toNode;
	}
	public void setToNode(WfNode toNode) {
		this.toNode = toNode;
	}

	public JSONObject toJO() throws Exception {
		return new JSONObject()
				.put("ID", id)
				.put("TPL_ROUTE_ID", tplRoute != null ? tplRoute.getID() : TPL_ROUTE_ROLLBACK)
				.put("FROM_NODE_ID", fromNode.getID())
				.put("TO_NODE_ID", toNode.getID());
	}
}
