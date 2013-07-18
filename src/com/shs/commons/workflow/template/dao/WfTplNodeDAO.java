package com.shs.commons.workflow.template.dao;
import java.sql.Connection;
import java.sql.ResultSet;
import org.json.JSONArray;
import org.json.JSONObject;

import com.shs.commons.workflow.template.models.WfTplNode;
import com.shs.commons.workflow.template.models.interfaces.IWfTplNode;
import com.shs.framework.dao.BaseDAO;
import com.shs.framework.dao.BaseDAO.ResultSetOperator;

/**
 * Description: 流程模板节点
 * User: chyxion
 * Date: 12/19/12
 * Time: 2:35 PM
 * Support: chyxion@163.com
 * Copyright: Shenghang Soft All Right Reserved
 */
public class WfTplNodeDAO {
	private BaseDAO dao;
    private WfTplRouteDAO routeDAO;
    public WfTplNodeDAO(BaseDAO dao) {
		this.dao = dao;
		this.routeDAO = new WfTplRouteDAO(dao);
	}
	public final static String TABLE = "wf_tpl_nodes";
    // 查询全部字段SQL
    private final StringBuffer SELELCT_ALL_SQL = 
    			new StringBuffer("select id, name, type, before_action, action, after_action, ")
                .append("assign_policy, task_type, out_route_type, time_limit, tpl_id, offset ")
                .append("from wf_tpl_nodes where ");
    
    public void save(JSONObject joNode) throws Exception {
       dao.insert(TABLE, joNode);
    }
    public void update(JSONObject joNode, JSONObject joCondition) throws Exception {
       dao.update(TABLE, joNode, joCondition);
    }
    public void deleteByTplID(Connection dbConnection, String tplID) throws Exception {
       dao.update(dbConnection, "delete from wf_tpl_nodes where tpl_id = ?", tplID);
    }
    public void delete(Connection dbConnection, String id) throws Exception {
        dao.update(dbConnection, "delete from wf_tpl_nodes where id = ?", id);
        routeDAO.deleteByNodeID(dbConnection, id);
    }
    public void delete(final String id) throws Exception {
        dao.execute(new BaseDAO.ConnectionOperator() {
            @Override
            public void run() throws Exception {
                delete(dbConnection, id);
            }
        });
    }
    /**
     * 根据模板ID(s)删除
     * @param dbConnection
     * @param tplIDs
     * @throws Exception
     */
    public void deleteByTplIDs(Connection dbConnection, JSONArray tplIDs) throws Exception {
        dao.update(dbConnection, "delete from wf_tpl_nodes where tpl_id in (?)", tplIDs);
    }
    public IWfTplNode getTplNodeFromResultSet(ResultSet rs)
            throws Exception {
        WfTplNode node = new WfTplNode();
        node.setID(rs.getString("ID"));
        node.setName(rs.getString("NAME"));
        node.setType(rs.getString("TYPE"));
        node.setAction(rs.getString("ACTION"));
        node.setBeforeAction(rs.getString("BEFORE_ACTION"));
        node.setAfterAction(rs.getString("AFTER_ACTION"));
        node.setAssignPolicy(rs.getString("ASSIGN_POLICY"));
        node.setTaskType(rs.getString("TASK_TYPE"));
        node.setTimeLimit(rs.getInt("TIME_LIMIT"));
        node.setOutRouteType(rs.getString("OUT_ROUTE_TYPE"));
        node.setOffset(rs.getString("OFFSET"));
        return node;
    }
    public IWfTplNode getTplNodeFromJO(JSONObject joTplNode)
            throws Exception {
        WfTplNode node = new WfTplNode();
        node.setID(joTplNode.getString("ID"));
        node.setName(joTplNode.getString("NAME"));
        node.setType(joTplNode.getString("TYPE"));
        node.setAction(joTplNode.optString("ACTION"));
        node.setBeforeAction(joTplNode.optString("BEFORE_ACTION"));
        node.setAfterAction(joTplNode.optString("AFTER_ACTION"));
        node.setAssignPolicy(joTplNode.optString("ASSIGN_POLICY"));
        node.setTaskType(joTplNode.optString("TASK_TYPE"));
        node.setTimeLimit(joTplNode.optInt("TIME_LIMIT"));
        node.setOutRouteType(joTplNode.optString("OUT_ROUTE_TYPE"));
        node.setOffset(joTplNode.getString("OFFSET"));
        return node;
    }
    /**
     * 根据ID查找生成Java对象
     * @param dbConnection
     * @param id
     * @return
     * @throws Exception
     */
    public IWfTplNode get(Connection dbConnection, String id) throws Exception {
        return dao.query(dbConnection, 
        		new ResultSetOperator() {
            @Override
            public void run() throws Exception {
                result = getTplNodeFromResultSet(resultSet);
            }
        }, 
		SELELCT_ALL_SQL.append("id = ?").toString(), id);
    }
    public JSONArray findAllByTplID(Connection dbConnection, String tplID) throws Exception {
       return dao.findJSONArray(dbConnection, 
    		   SELELCT_ALL_SQL.append("tpl_id = ?")
    		   .toString(), tplID);
    }
}
