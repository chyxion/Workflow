package com.shs.commons.workflow.instance.dao;
import java.sql.Connection;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.shs.commons.workflow.instance.models.WfNode;
import com.shs.framework.dao.BaseDAO;

/**
 * Description:
 * User: chyxion
 * Date: 12/24/12
 * Time: 11:04 AM
 * Support: chyxion@163.com
 * Copyright: Shenghang Soft All Right Reserved
 */
public class WfNodeDAO {
    private BaseDAO dao;
    public WfNodeDAO(BaseDAO dao) {
		this.dao = dao;
	}
	public static final String TABLE = "wf_nodes";
	private StringBuffer selectAllSQL() {
		return new StringBuffer("select id, flow_id, start_time, done_time, ")
			.append("tpl_node_id, status from wf_nodes where ");
	}
    public WfNode getNodeFromJO(JSONObject joNode) throws Exception {
       WfNode node = new WfNode();
       node.setID(joNode.getString("ID"));
       node.setStartTime(joNode.getString("START_TIME"));
       node.setDoneTime(joNode.optString("DONE_TIME"));
       node.setStatus(joNode.getString("STATUS"));
       return node;
    }
    
    
    /**
     * 更新节点状态
     */
    public void updateNodeStatus(Connection dbConnection, WfNode node) throws Exception{
        dao.update("update wf_nodes set status = ? where id =?",
                node.getStatus(), 
        		node.getID());
    }
    /**
     * 新建节点
     * @param dbConnection
     * @param node
     * @throws Exception
     */
    public void newNode(Connection dbConnection, JSONObject joNode) throws Exception {
    	dao.insert(dbConnection, TABLE, joNode);
    }
	public JSONObject get(Connection dbConnection, String id) throws Exception {
		return dao.findJSONObject(dbConnection, 
				selectAllSQL().append("id = ?").toString(), id);
	}
	public List<String> listIDsByFlowIDs(Connection dbConnection,
			JSONArray jaFlowIDs) throws Exception {
		return dao.findStrList("select id from wf_nodes where flow_id in (?)", jaFlowIDs);
	}
	public JSONArray findAllByFlowID(Connection dbConnection, String flowID) throws Exception {
		return dao.findJSONArray(dbConnection, 
				selectAllSQL().append("flow_id = ?")
				.toString(), flowID);
	}
	public void deleteByFlowIDs(Connection dbConnection, JSONArray jaFlowIDs) throws Exception {
        dao.update(dbConnection, "delete from wf_nodes where id in (?)", jaFlowIDs);
	}
	public JSONArray findAll(Connection dbConnection, JSONArray jaNodeIDs) throws Exception {
		return dao.findJSONArray(dbConnection, 
				selectAllSQL()
				.append("id in (?)")
				.toString(), 
				jaNodeIDs);
	}

	/**
	 * 完成节点
	 * @param dbConnection
	 * @param node
	 * @throws Exception
	 */
	public void completeNode(Connection dbConnection, WfNode node) throws Exception {
        dao.update("update wf_nodes set status = ?, done_time = ? where id = ?",
                node.getStatus(), 
        		node.getDoneTime(),
        		node.getID());
	}
}
