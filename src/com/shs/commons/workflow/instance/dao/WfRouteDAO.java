package com.shs.commons.workflow.instance.dao;
import java.sql.Connection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.shs.commons.workflow.instance.models.WfRoute;
import com.shs.framework.dao.BaseDAO;

/**
 * Description:
 * User: chyxion
 * Date: 12/24/12
 * Time: 11:04 AM
 * Support: chyxion@163.com
 * Copyright: Shenghang Soft All Right Reserved
 */
public class WfRouteDAO {
	public final String TABLE = "WF_ROUTES";
	private BaseDAO dao;
	public WfRouteDAO(BaseDAO dao) {
		this.dao = dao;
	}
	public WfRoute getRouteFromJO(JSONObject joRoute) throws Exception {
		WfRoute route = new WfRoute();
		route.setID(joRoute.getString("ID"));
		return route;
	}
	public JSONArray findAllByFlowID(Connection dbConnection, String flowID) throws Exception {
        return dao.findJSONArray(dbConnection, 
        		new StringBuffer("select r.id id, r.from_node_id from_node_id, ")
        				.append("r.to_node_id to_node_id, r.tpl_route_id tpl_route_id ")
        				.append("from wf_routes r ")
        				.append("join wf_nodes fn on r.from_node_id = fn.id ")
        				.append("join wf_nodes ft on r.to_node_id = ft.id ")
        				.append("where fn.flow_id = ? or ft.flow_id = ?")
        				.toString(), 
        				new Object[]{flowID, flowID});
	}
	public void deleteByFlowIDs(Connection dbConnection, JSONArray jaFlowIDs) throws Exception {
        dao.update(dbConnection, 
        		new StringBuffer("delete from wf_routes where from_node_id in (")
        		.append("select id from wf_nodes where flow_id in (?)) ")
        		.append("or to_node_id in ( ")
        		.append("select id from wf_nodes where flow_id in (?))")
        		.toString(), jaFlowIDs, jaFlowIDs);
	}
	public void newRoute(Connection dbConnection, JSONObject joRoute) throws Exception {
		dao.insert(TABLE, joRoute);
	}
}
