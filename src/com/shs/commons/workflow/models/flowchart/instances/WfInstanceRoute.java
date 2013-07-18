package com.shs.commons.workflow.models.flowchart.instances;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.shs.commons.workflow.models.AbstractModel;
import com.shs.commons.workflow.models.flowchart.templates.WfTemplateRoute;
import com.shs.framework.dao.BaseDAO;

/**
 * @class describe: 实例节点边
 * @version 0.1
 * @date created: Mar 1, 2012 2:24:05 PM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class WfInstanceRoute extends AbstractModel{
	public final static String TABLE_NAME = "wf_trst";
	public final static String ID_NAME = "f_trid";
	//fields
	private String id;
	private WfTemplateRoute tplRoute;
	private String instanceID;
	private String fromNodeID;
	private String toNodeID;
	
	
	public WfInstanceRoute() {
	}
	//data access
	public WfInstanceRoute(String id, WfTemplateRoute tplRoute,
			String instanceID, String fromNodeID, String toNodeID) {
		super();
		this.id = id;
		this.tplRoute = tplRoute;
		this.instanceID = instanceID;
		this.fromNodeID = fromNodeID;
		this.toNodeID = toNodeID;
	}

	public static WfInstanceRoute get(String id) throws Exception{
		return AbstractModel.<WfInstanceRoute>findByID (TABLE_NAME, ID_NAME, id, 
					WfInstanceRoute.class);
	}

	public static List<WfInstanceRoute> list() throws Exception{
		return AbstractModel.findAllBySQL("select * from " + TABLE_NAME,
                WfInstanceRoute.class);
	}
    public static List<WfInstanceRoute> findAllByFlowID(Connection dbConnection, String flowID) throws Exception{
        return new BaseDAO().query(dbConnection,  new BaseDAO.ResultSetOperator() {
            @Override
            public void run() throws Exception {
                List<WfInstanceRoute> listResult = new LinkedList<WfInstanceRoute>();
                while (resultSet.next())
                    listResult.add(getInstanceFromResultSet(resultSet));
                result = listResult;
            }
        }, "select * from wf_trst where f_flid = ?", flowID);
    }
	@SuppressWarnings("unchecked")
	public static WfInstanceRoute getInstanceFromResultSet(ResultSet rs)
			throws Exception {
		String id = rs.getString(ID_NAME);
		WfTemplateRoute templateRoute = WfTemplateRoute.get(rs.getString("f_ruid"));
		String workflowInstanceId = rs.getString("f_flid");
		String fromInstanceNodeId = rs.getString("f_facid");
		String toInstanceNodeId = rs.getString("f_tacid");
		return new WfInstanceRoute(id, templateRoute, workflowInstanceId, fromInstanceNodeId, toInstanceNodeId);
	}
	//tools
	/**
	 * 工具方法, 返回route集合中所有from节点Id
	 * @param routesList
	 * @return
	 */
	public static List<String> getFromNodeIDs(Collection<WfInstanceRoute> routesList){
		List<String> fromNodesIds = new LinkedList<String>();
		
		for (WfInstanceRoute route : routesList) 
			fromNodesIds.add(route.getFromNodeId());
				
		return fromNodesIds;
	}
	/**
	 * 返回route结合中的全部to节点Id
	 * @param routesList
	 * @return
	 */
	public static List<String> getToNodeIDs(Collection<WfInstanceRoute> routesList){
		List<String> toNodesIds = new LinkedList<String>();
		
		for (WfInstanceRoute route : routesList) 
			toNodesIds.add(route.getToNodeId());
				
		return toNodesIds;
	}
	//getters and setters

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public WfTemplateRoute getTemplateRoute() {
		return tplRoute;
	}

	public void setTemplateRoute(WfTemplateRoute templateRoute) {
		this.tplRoute = templateRoute;
	}

	public String getWorkflowInstanceId() {
		return instanceID;
	}

	public void setWorkflowInstanceId(String workflowInstanceId) {
		this.instanceID = workflowInstanceId;
	}

	public String getFromNodeId() {
		return fromNodeID;
	}

	public void setFromInstanceNodeId(String fromInstanceNodeId) {
		this.fromNodeID = fromInstanceNodeId;
	}

	public String getToNodeId() {
		return toNodeID;
	}

	public void setToInstanceNodeId(String toInstanceNodeId) {
		this.toNodeID = toInstanceNodeId;
	}

	@Override
	public String toString() {
		return "WorkflowInstanceRoute [id=" + id + ", templateRoute="
				+ tplRoute + ", workflowInstanceId=" + instanceID
				+ ", fromInstanceNodeId=" + fromNodeID
				+ ", toInstanceNodeId=" + toNodeID + "]";
	}
	
}
