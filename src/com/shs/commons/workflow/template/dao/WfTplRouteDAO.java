package com.shs.commons.workflow.template.dao;
import com.shs.commons.workflow.template.models.WfTplRoute;
import com.shs.framework.dao.BaseDAO;
import com.shs.framework.dao.BaseDAO.ConnectionOperator;

import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Description: 模板连线
 * User: chyxion
 * Date: 12/19/12
 * Time: 2:35 PM
 * Support: chyxion@163.com
 * Copyright: Shenghang Soft All Right Reserved
 */
public class WfTplRouteDAO {
    public static final String TABLE = "wf_tpl_routes";
    private BaseDAO dao;
    public WfTplRouteDAO(BaseDAO dao) {
		this.dao = dao;
	}
	public void delete(final String id) throws Exception {
       dao.execute(new ConnectionOperator() {
           @Override
           public void run() throws Exception {
               delete(dbConnection, id);
           }
       });
    }
    public void delete(Connection dbConnection, String id) throws Exception {
        dao.update(dbConnection, "delete from wf_tpl_routes where id = ?", id);
    }

    public WfTplRoute getTplRouteFromResultSet(ResultSet rs)
            throws Exception {
    	WfTplRoute route = new WfTplRoute();
    	route.setID(rs.getString("ID"));
    	route.setNote(rs.getString("NOTE"));
    	route.setExpression(rs.getString("EXPRESSION"));
    	return route;
    }
    public WfTplRoute getTplRouteFromJO(JSONObject joRoute)
            throws Exception {
    	WfTplRoute route = new WfTplRoute();
    	route.setID(joRoute.getString("ID"));
    	route.setNote(joRoute.optString("NOTE"));
    	route.setExpression(joRoute.optString("EXPRESSION"));
    	return route;
    }
    public void deleteByNodeID(Connection dbConnection, String nodeID) throws Exception {
        dao.update(dbConnection, "delete from wf_tpl_routes where from_node_id = ? or to_node_id = ?", new Object[]{nodeID, nodeID});
    }
    public void deleteByNodeIDs(Connection dbConnection, JSONArray jaNodeIDs) throws Exception {
        dao.update(dbConnection, "delete from wf_tpl_routes where from_node_id in (?) or to_node_id  (?)", new Object[]{jaNodeIDs, jaNodeIDs});
    }
    /**
     * 查找模板ID下的所有连接
     * @param dbConnection
     * @param tplID
     * @return
     * @throws Exception
     */
    public JSONArray findAllByTplID(Connection dbConnection, String tplID) throws Exception {
        return dao.findJSONArray(dbConnection, 
        		new StringBuffer("select r.id id, r.from_node_id from_node_id, r.to_node_id to_node_id, ")
        				.append("r.expression expression, r.note note ")
        				.append("from wf_tpl_routes r ")
        				.append("join wf_tpl_nodes fn on r.from_node_id = fn.id ")
        				.append("join wf_tpl_nodes ft on r.to_node_id = ft.id ")
        				.append("where fn.tpl_id = ? or ft.tpl_id = ?")
        				.toString(), tplID, tplID);
    }

    public void save(JSONObject joRoute) throws Exception {
        dao.insert(TABLE, joRoute);
    }
    public void update(JSONObject joRoute, JSONObject joCondition) throws Exception {
        dao.update(TABLE, joRoute, joCondition);
    }
	public void deleteByTplIDs(Connection dbConnection, JSONArray jaIDs) throws Exception {
        dao.update(dbConnection, 
        		new StringBuffer("delete from wf_tpl_routes where from_node_id in (")
        		.append("select id from wf_tpl_nodes where tpl_id in (?)) ")
        		.append("or to_node_id in ( ")
        		.append("select id from wf_tpl_nodes where tpl_id in (?))")
        		.toString(), new Object[]{jaIDs, jaIDs});
	}
}
