package com.shs.commons.workflow.instance.services;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import com.shs.commons.services.UserService;
import com.shs.commons.workflow.dao.WfVarDAO;
import com.shs.commons.workflow.instance.dao.WfFlowDAO;
import com.shs.commons.workflow.instance.dao.WfNodeDAO;
import com.shs.commons.workflow.instance.dao.WfRouteDAO;
import com.shs.commons.workflow.instance.dao.WfTaskDAO;
import com.shs.commons.workflow.instance.models.WfFlow;
import com.shs.commons.workflow.instance.models.WfNode;
import com.shs.commons.workflow.models.WfVar;
import com.shs.commons.workflow.services.WfVarService;
import com.shs.commons.workflow.template.models.interfaces.IWfTplNode;
import com.shs.commons.workflow.template.services.WfTemplateService;
import com.shs.framework.core.BaseService;
import com.shs.framework.dao.BaseDAO;
import com.shs.framework.dao.BaseDAO.ConnectionOperator;

/**
 * Description: 流程实例
 * User: chyxion
 * Date: 12/24/12
 * Time: 11:02 AM
 * Support: chyxion@163.com
 * Copyright: Shenghang Soft All Right Reserved
 */
public class WfFlowService extends BaseService {
    private WfFlowDAO flowDAO = new WfFlowDAO(dao);
    private WfVarDAO varDAO = new WfVarDAO(dao);
    private WfNodeDAO nodeDAO = new WfNodeDAO(dao);
    private WfRouteDAO routeDAO = new WfRouteDAO(dao);
    private WfTaskDAO taskDAO = new WfTaskDAO(dao);
    private WfNodeService nodeService = new WfNodeService(dao);
    private WfRouteService routeService = new WfRouteService(dao);
    private WfVarService varService = new WfVarService(dao);
    
    public WfFlowService(BaseDAO dao) {
		super();
		this.dao = dao;
	}
	public WfFlowService() {
		super();
	}

	public void newFlow(Connection dbConnection, WfFlow flow) throws Exception {
    	flowDAO.newFlow(dbConnection, flow);
    }
    /**
     * 添加/更新流程变量
     * @param dbConnection
     * @param newVars
     */
    public void addVars(Connection dbConnection, WfFlow flow, Map<String, WfVar> newVars) throws Exception {
    	if (newVars != null) {
    		Map<String, WfVar> fVars = flow.getVars();
    		for (String name : newVars.keySet()) {
    			WfVar var = fVars.get(name);
    			WfVar newVar = newVars.get(name);
    			if (var == null) {
    				newVar.setOwerID(flow.getID());
    				varDAO.newVar(dbConnection, newVar);
    				fVars.put(name, newVar);
    			} else {
    				var.setValue(newVar.getValue());
    				varDAO.updateValue(dbConnection, var);
    			}
    		}
    	}
    }
    /**
     * 删除流程
     * @param dbConnection
     * @param jaFlowIDs
     * @throws Exception
     */
    public void delete(Connection dbConnection, JSONArray jaFlowIDs) throws Exception {
    	taskDAO.deleteByFlowIDs(dbConnection, jaFlowIDs);
    	routeDAO.deleteByFlowIDs(dbConnection, jaFlowIDs);
    	nodeDAO.deleteByFlowIDs(dbConnection, jaFlowIDs);
    	flowDAO.delete(dbConnection, jaFlowIDs);
    	varDAO.deleteByOwnerIDs(dbConnection, jaFlowIDs);
    }
    /**
     * 根据流程ID查找流程实例
     * @param dbConnection
     * @param flowID
     * @return
     * @throws Exception
     */
    public WfFlow get(Connection dbConnection, String flowID) throws Exception {
    	List<WfFlow> flows = findAll(dbConnection, new JSONArray().put(flowID));
    	WfFlow flow = null;
    	if (flows.size() > 0) {
    		flow = flows.get(0);
    	}
    	return flow;
    }
    /**
     * 查找流程
     * @param dbConnection
     * @param jaFlowIDs
     * @return
     * @throws Exception
     */
    public List<WfFlow> findAll(Connection dbConnection, JSONArray jaFlowIDs) throws Exception {
    	JSONArray jaFlows = flowDAO.findAll(dbConnection, jaFlowIDs);
    	List<WfFlow> flows = new LinkedList<WfFlow>();
    	if (jaFlows.length() > 0) {
    		UserService userService = new UserService();
    		for (int i = 0; i < jaFlows.length(); ++i) {
    			JSONObject joFlow = jaFlows.getJSONObject(i);
    			// 0流程
    			WfFlow flow = flowDAO.getFlowFromJO(joFlow);
		    	// 1用户
		    	flow.setUser(userService.get(dbConnection, joFlow.getString("USER_ID")));
		    	// 2流程模板
		    	flow.setWfTpl(new WfTemplateService().get(dbConnection, joFlow.getString("TPL_ID")));
		    	// 3查找所有节点
		    	nodeService.findAll(dbConnection, flow);
		    	// 4查找所有连接
		    	routeService.findAll(dbConnection, flow);
		    	// 5流程变量
		    	varService.findAll(dbConnection, flow);
		    	// 添加的返回集合
		    	flows.add(flow);
    		}
    	}
    	return flows;
    }
    public JSONArray list() throws Exception {
        return flowDAO.findAllbyTplID(params.get("TPL_ID"));
    }
    public List<WfNode> findNextNodes(WfNode node) {
       return null;
    }
	public JSONObject monitor() throws Exception {
		return dao.execute(new ConnectionOperator() {
			@Override
			public void run() throws Exception {
				WfFlow flow = get(dbConnection, params.get("ID"));
				JSONObject joFlow = flow.toJO();
				joFlow.put("nodes", getFlowNodes(flow));
				result = joFlow;
			}
		});
	}
	private JSONArray getFlowNodes(WfFlow flow) throws Exception {
		JSONArray jaNodes = new JSONArray();
		for (WfNode node : flow.getNodes().values()) {
			jaNodes.put(node.toJO());
		}
		return jaNodes;
	}
	/**
	 * 返回开始节点
	 * @return
	 * @throws Exception
	 */
	public WfNode startNode(WfFlow flow) throws Exception {
		for (WfNode node : flow.getNodes().values()) {
			if (node.getTplNode().getType().equals(IWfTplNode.TYPE_START)) {
				return node;
			}
		}
		throw new Exception("流程[" + flow.getID() + "]没有找到开始节点!");
	}
}
