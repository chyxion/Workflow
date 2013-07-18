package com.shs.commons.workflow.template.services;
import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.shs.commons.workflow.dao.WfVarDAO;
import com.shs.commons.workflow.instance.dao.WfFlowDAO;
import com.shs.commons.workflow.instance.services.WfFlowService;
import com.shs.commons.workflow.models.WfVar;
import com.shs.commons.workflow.template.dao.WfTemplateDAO;
import com.shs.commons.workflow.template.dao.WfTplNodeDAO;
import com.shs.commons.workflow.template.dao.WfTplRouteDAO;
import com.shs.commons.workflow.template.dao.WfTplTypeDAO;
import com.shs.commons.workflow.template.models.WfTemplate;
import com.shs.commons.workflow.template.models.WfTplRoute;
import com.shs.commons.workflow.template.models.interfaces.IWfTplNode;
import com.shs.framework.core.BaseService;
import com.shs.framework.core.ExtStore;
import com.shs.framework.dao.BaseDAO.ConnectionOperator;
import com.shs.framework.utils.UUID;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Description: 流程模板服务类
 * User: chyxion
 * Date: 12/24/12
 * Time: 10:39 AM
 * Support: chyxion@163.com
 * Copyright: Shenghang Soft All Right Reserved
 */
public class WfTemplateService extends BaseService {
	
    private WfTemplateDAO templateDAO = new WfTemplateDAO(dao);
    private WfTplTypeDAO tplTypeDAO = new WfTplTypeDAO(dao);
    private WfTplNodeDAO nodeDAO = new WfTplNodeDAO(dao);
    private WfTplRouteDAO routeDAO = new WfTplRouteDAO(dao);
    private WfVarDAO varDAO = new WfVarDAO(dao);
    private WfFlowService flowService = new WfFlowService(dao);
    private WfFlowDAO flowDAO = new WfFlowDAO(dao);
    /**
     * 流程模板列表
     * @return Store
     * @throws Exception
     */
    public ExtStore list() throws Exception {
        return null;
    }
    /**
     * 查看模板
     * @return
     * @throws Exception
     */
    public JSONObject show() throws Exception {
        return null;
    }


    /**
     * 根据模板类型ID查找所有模板
     * @return JSONArray
     * @throws Exception
     */
    public JSONArray listByTypeID() throws Exception {
        return templateDAO.findAllByTypeID(params.get("TYPE_ID"));
    }

    /**
     * 新建模板
     * @return
     * @throws Exception
     */
    public String newTemplate() throws Exception {
        String id = UUID.get();
        templateDAO.save(params.getJSONObject("TPL").put("ID", id));
        return id;
    }

    /**
     * 更新模板
     * @throws Exception
     */
    public void updateTemplate() throws Exception {
        templateDAO.update(params.getJSONObject("TPL"),
        		params.getJSONObject("W"));
    }

    /**
     * 删除模板
     * @throws Exception
     */
    public void delete() throws Exception {
    	dao.executeTransaction(new ConnectionOperator() {
			@Override
			public void run() throws Exception {
				delete(dbConnection, params.getJSONArray("IDs"));
			}
		});
    }

    public void delete(Connection dbConnection, JSONArray jaIDs) throws Exception {
    	if (jaIDs.length() > 0) {
	    	List<String> flowIDs = 
	    		flowDAO.listIDsByTplIDs(dbConnection, jaIDs);
	    	if (flowIDs.size() > 0) {
	    		flowService.delete(dbConnection, 
	    			new JSONArray(flowIDs));
	    	}
	        varDAO.deleteByOwnerIDs(dbConnection, jaIDs);
	        routeDAO.deleteByTplIDs(dbConnection, jaIDs);
	        nodeDAO.deleteByTplIDs(dbConnection, jaIDs);
	        templateDAO.delete(dbConnection, jaIDs);
    	}
    }
    /**
     * 加载流程设计图数据
     * @return
     * @throws Exception
     */
    public JSONObject load() throws Exception {
        return dao.execute(new ConnectionOperator() {
            @Override
            public void run() throws Exception {
            	String tplID = params.get("ID");
                result = new JSONObject()
                        .put("vars",
                                varDAO.findAllByOwnerID(dbConnection, tplID))
                        .put("nodes",
                                nodeDAO.findAllByTplID(dbConnection, tplID))
                        .put("routes",
                                routeDAO.findAllByTplID(dbConnection, tplID));
            }
        });
    }
    
    /**
     * 从ResultSet中创建流程模板Java对象
     * @param dbConnection
     * @param joTpl
     * @return
     * @throws Exception
     */
    public WfTemplate get(Connection dbConnection, String tplID) throws Exception {
    	JSONObject joTpl = templateDAO.get(dbConnection, tplID);
    	WfTemplate tpl = templateDAO.getTplFromJO(joTpl);
    	tpl.setType(tplTypeDAO.get(dbConnection, joTpl.getString("TYPE_ID")));
    	
        // 1,查询模板下节点
        JSONArray jaNodes = nodeDAO.findAllByTplID(dbConnection, tplID);
        IWfTplNode node;
        Map<String, IWfTplNode> nodes = new HashMap<String, IWfTplNode>();
        for (int i = 0; i < jaNodes.length(); ++i) {
        	node = nodeDAO.getTplNodeFromJO(jaNodes.getJSONObject(i));
        	// 设置节点所属模板
        	node.setWfTemplate(tpl);
        	nodes.put(node.getID(), node);
        }
        tpl.setNodes(nodes);
        // 3, 流程变量
        JSONArray jaVars = varDAO.findAllByOwnerID(dbConnection, tplID);
        Map<String, WfVar> vars = new HashMap<String, WfVar>();
        WfVar var;
        for (int i = 0; i < jaVars.length(); ++i) {
        	var = varDAO.getWfVarFromJO(jaVars.getJSONObject(i));
        	vars.put(var.getName(), var);
        }
        tpl.setVars(vars);
        // 4,流程连接
        JSONArray jaRoutes = routeDAO.findAllByTplID(dbConnection, tplID);
        Map<String, WfTplRoute> routes = new HashMap<String, WfTplRoute>();
        WfTplRoute route;
        for (int i = 0; i < jaRoutes.length(); ++i) {
        	JSONObject joRoute = jaRoutes.getJSONObject(i);
        	route = routeDAO.getTplRouteFromJO(joRoute);
        	route.setFromNode(nodes.get(joRoute.getString("FROM_NODE_ID")));
        	route.setToNode(nodes.get(joRoute.getString("TO_NODE_ID")));
        	routes.put(route.getID(), route);
        }
        tpl.setRoutes(routes);
    	return tpl;
    }

    /**
     * 查找申请节点,
     * 开始节点只有一条出边，并且紧跟申请节点
     * @param flowTpl
     * @return
     * @throws Exception
     */
    public IWfTplNode getApplicantNode(WfTemplate flowTpl) throws Exception {
		return getNodeOutRoutes(flowTpl,  // 查找开始节点的所有出边
			getStartNode(flowTpl)) // 开始节点
			.get(0) // 只有一条出边
			.getToNode(); // 到达节点
    }
    
    /**
     * 获取结点出边
     * 2011-1-25
     * @param flowId
     * @param nodeId
     * @return 后继路由边集
     */
    public List<WfTplRoute> getNodeOutRoutes(WfTemplate tpl, IWfTplNode node) throws Exception{
        List<WfTplRoute> routes = new LinkedList<WfTplRoute>();
        for (WfTplRoute route : tpl.getRoutes().values()) {
        	if (route.getFromNode().getID().equals(node.getID())) {
                routes.add(route);
        	}
		}
        return routes;
    }
    /**
     * 查找流程模板开始节点
     */
    public IWfTplNode getStartNode(WfTemplate tpl) throws Exception {
    	for (IWfTplNode node : tpl.getNodes().values()) {
    		if (IWfTplNode.TYPE_START.equals(node.getType()))
    			return node;
		}
    	throw new Exception("流程模板[" + tpl.getName() + "]没有找到开始节点.");
    }
	/**
	 * 查找一组节点后紧跟的节点组
	 * @param nodes
	 * @return
	 */
	public List<IWfTplNode> nextNodes(WfTemplate tpl, List<IWfTplNode> nodes) {
		List<IWfTplNode> nextNodes = new LinkedList<IWfTplNode>();
		for (IWfTplNode node : nodes)
			for (WfTplRoute it : tpl.getRoutes().values())
				if (node.getID().equals(it.getFromNode().getID())) {
					IWfTplNode toNode = tpl.getNodes().get(it.getToNode().getID());
					if (!nextNodes.contains(toNode)) //如果多个节点指向一个节点, 只取一个
						nextNodes.add(toNode);
				}
		return nextNodes;
	}
	/**
	 * 返回节点nodes之后的所有连接边
	 * @param nodes
	 * @return
	 */
	public void routesAfter(WfTemplate tpl, List<IWfTplNode> nodes, List<WfTplRoute> afterRoutes) {
		List<IWfTplNode> nextNodes = nextNodes(tpl, nodes);
		if (nextNodes.size() > 0) {
			afterRoutes.addAll(inRoutes(tpl, nextNodes));
			routesAfter(tpl, nextNodes, afterRoutes);
		}
	}
	/**
	 * 找出节点的入边
	 * @param nodes
	 * @return
	 */
	public List<WfTplRoute> inRoutes(WfTemplate tpl, List<IWfTplNode> nodes) {
		List<WfTplRoute> inRoutesList = new LinkedList<WfTplRoute>();
		for (IWfTplNode node : nodes)
			for (WfTplRoute route : tpl.getRoutes().values())
				if (route.getToNode().getID().equals(node.getID()))
					inRoutesList.add(route);
		return inRoutesList;
	}
}
