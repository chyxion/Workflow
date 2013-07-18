package com.shs.commons.workflow.instance.services;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.shs.commons.workflow.instance.dao.WfRouteDAO;
import com.shs.commons.workflow.instance.models.WfFlow;
import com.shs.commons.workflow.instance.models.WfNode;
import com.shs.commons.workflow.instance.models.WfRoute;
import com.shs.commons.workflow.template.models.WfTemplate;
import com.shs.framework.core.BaseService;
import com.shs.framework.dao.BaseDAO;

/**
 * @version 0.1
 * @author chyxion
 * @describe: 
 * @date created: Dec 29, 2012 10:48:04 AM
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class WfRouteService extends BaseService {
	private WfRouteDAO routeDAO = new WfRouteDAO(dao);
	public void newRoute(Connection dbConnection, WfRoute route) throws Exception {
		routeDAO.newRoute(dbConnection, route.toJO());
	}

	public WfRouteService(BaseDAO dao) {
		this.dao = dao;
	}
	public WfRouteService() {
	}

	/**
	 * 查找流程下所有连接
	 * @param dbConnection
	 * @param flow
	 * @throws Exception
	 */
	public void findAll(Connection dbConnection, WfFlow flow) throws Exception {
		WfTemplate flowTpl = flow.getWfTpl();
		Map<String, WfNode> nodes = flow.getNodes();
    	// 连接
    	JSONArray jaRoutes = routeDAO.findAllByFlowID(dbConnection, flow.getID());
    	Map<String, WfRoute> routes = new HashMap<String, WfRoute>();
    	for (int j = 0; j < jaRoutes.length(); ++j) {
    		JSONObject joRoute = jaRoutes.getJSONObject(j);
    		WfRoute route = routeDAO.getRouteFromJO(joRoute);
    		// 模板连接
    		route.setTplRoute(flowTpl.getRoutes().get(joRoute.getString("TPL_ROUTE_ID")));
    		// from 节点
    		route.setFromNode(nodes.get(joRoute.getString("FROM_NODE_ID")));
    		// to 节点
    		route.setToNode(nodes.get(joRoute.getString("TO_NODE_ID")));
    		routes.put(route.getID(), route);
    	}
    	flow.setRoutes(routes);
	}
}
