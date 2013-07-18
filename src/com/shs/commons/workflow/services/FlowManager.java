package com.shs.commons.workflow.services;
import java.sql.Connection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.shs.commons.models.User;
import com.shs.commons.workflow.activities.ActivityService;
import com.shs.commons.workflow.core.WfEngine;
import com.shs.commons.workflow.expression.ExpressionParser;
import com.shs.commons.workflow.expression.ParseException;
import com.shs.commons.workflow.instance.models.WfFlow;
import com.shs.commons.workflow.instance.models.WfNode;
import com.shs.commons.workflow.instance.models.WfRoute;
import com.shs.commons.workflow.instance.services.WfFlowService;
import com.shs.commons.workflow.instance.services.WfNodeService;
import com.shs.commons.workflow.instance.services.WfRouteService;
import com.shs.commons.workflow.interfaces.IWfActivity;
import com.shs.commons.workflow.models.WfVar;
import com.shs.commons.workflow.template.models.WfTemplate;
import com.shs.commons.workflow.template.models.WfTplRoute;
import com.shs.commons.workflow.template.models.interfaces.IWfTplNode;
import com.shs.commons.workflow.template.services.WfTemplateService;
import com.shs.framework.utils.DateUtils;
import com.shs.framework.utils.UUID;

/**
 * <p>Title: </p>
 * <p>Description:流程实例管理器 </p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: ShengHangSoft </p>
 * <p>Date: 2011-5-16</p>
 * <p>Time: 10:07:32</p>
 * <p>@version 1.0</p>
 */
public class FlowManager {
	private Logger logger = Logger.getLogger(getClass());
	
	private ActionService actionService = new ActionService();
	private ActivityService activityService = new ActivityService();
	private WfTemplateService tplService = new WfTemplateService();
	private WfFlowService flowService = new WfFlowService();
	private WfNodeService nodeService = new WfNodeService();
	private WfVarService varService = new WfVarService();
	private WfRouteService routeService = new WfRouteService();
	/**
	 * 启动流程
	 * @param tplID 流程模板ID
	 * @param bizID 业务ID
	 * @param user 发起人
	 * @param vars 流程变量
	 * @throws Exception
	 */
    public void start(Connection dbConnection, final String tplID, 
				    		final String bizID, 
				    		final User user, 
				    		final Map<String, WfVar> vars) throws Exception {
	        //0 查找指定流程模板
			WfTemplate wfTpl = 
				tplService.get(dbConnection, tplID);
	        
	        //1 创建流程实例，持久化
	        WfFlow flow = initWfFlow(dbConnection, wfTpl, bizID, user);
	        //2 执行流程前置动作
	        actionService.executeBeforeAction(dbConnection, flow);
	        //3 初始化流程变量
	        initVars(dbConnection, flow, vars);
	        //4 初始化开始节点
	        WfNode startNode = initStartNode(dbConnection, flow);
	        //5 把开始活动放入引擎的活动队列
	        WfEngine.getInstance().getActivityQueue().enqueue(activityService.instanceAtivity(startNode));
    }
    /**
     * 启动流程，木有流程变量
     * @param dbConnection
     * @param tplID
     * @param bizID
     * @param user
     * @throws Exception
     */
    public void start(Connection dbConnection, final String tplID, 
				    		final String bizID, 
				    		final User user) throws Exception {
    	start(dbConnection, tplID, bizID, user, null);
    }
    /**
     * 实例化流程
     * 2011-1-25
     * @param wfTpl 流程模板
     * @return  流程实例
     */
    public WfFlow initWfFlow(Connection dbConnection, 
    		WfTemplate wfTpl, 
    		String bizID,
    		User user) throws Exception {
    	
    	// 创建流程实例
        WfFlow wfFlow = new WfFlow();
        wfFlow.setID(UUID.get());
        wfFlow.setWfTpl(wfTpl);
        wfFlow.setBizID(bizID);
        // 发起人
        wfFlow.setUser(user);
        // 设置开始时间
        wfFlow.setStartTime(DateUtils.now14());
        // 设置状态
        wfFlow.setStatus(WfFlow.STATUS_RUNNING);
        // 持久化流程
        flowService.newFlow(dbConnection, wfFlow);
        
        return wfFlow;
    }
    public WfNode initStartNode(Connection dbConnection, WfFlow flow) throws Exception {
        // 创建开始节点，持久化
        WfNode startNode = new WfNode();
        startNode.setStartTime(DateUtils.now14());
        // 找到开始模板节点
        startNode.setTplNode(tplService.getStartNode(flow.getWfTpl()));
        startNode.setID(UUID.get());
        startNode.setStatus(WfNode.STATUS_RUNNING);
        startNode.setWfFlow(flow);
        // 添加到流程节点
        flow.addNode(startNode);
        // 持久化
        nodeService.newNode(dbConnection, startNode);
        return startNode;
    }
    public void initVars(Connection dbConnection, WfFlow flow, Map<String, WfVar> vars) throws Exception {
    	String flowID = flow.getID();
    	Map<String, WfVar> flowVars = flow.getVars();
    	// 克隆模板中的变量到流程实例
    	for(WfVar var : flow.getWfTpl().getVars().values()) {
    		WfVar flowVar = new WfVar(var);
    		flowVar.setOwerID(flowID);
    		flowVars.put(flowVar.getName(), flowVar);
    	}
    	// 添加初始变量到流程实例
    	if (vars != null) {
	    	for (WfVar wfVar : vars.values()) {
	    		wfVar.setOwerID(flowID);
	    		flowVars.put(wfVar.getName(), wfVar);
			}
    	}
    	varService.newVars(dbConnection, flowVars.values());
    }
    /**
     * 执行结点的后继迁移,获取后继活动列表
     * 2011-1-25
     * @param fromActivity
     * @return 后继活动列表
     */
    public List<IWfActivity> executePostTransition(Connection dbConnection, IWfActivity fromActivity) throws Exception {
    	logger.debug("Execute Post Actions");
    	WfFlow flow = fromActivity.getWfNode().getWfFlow();
        //1.根据Activity，获取Node
        WfTemplate flowTpl = flow.getWfTpl();
        
        //2.获取后继路由边
        List<WfTplRoute> outRoutes = 
        	tplService.getNodeOutRoutes(flowTpl, 
        	fromActivity.getWfNode().getTplNode());
        
        //3.执行路由
        //3.3.1 判断条件是否满足
        Map<String, WfVar> vars = flow.getVars();
        for (Iterator<WfTplRoute> it = outRoutes.iterator(); it.hasNext();) {
        	WfTplRoute tplRoute = it.next();
            //3.3.1 判断条件是否满足
            //3.3.2 删除不满足条件的路由边
            if (!evalExpression(tplRoute.getExpression(), vars)) {
            	logger.debug("remove route");
            	logger.debug(tplRoute.toJO());
                it.remove();
            }
        }
        
        //4. 判断是否存在满足条件的后继路由边
        if (outRoutes.isEmpty()) {
            throw new Exception(new StringBuffer("[")
            		.append(flowTpl.getName())
            		.append("][")
            		.append(fromActivity.getWfNode().getTplNode().getName())
            		.append("]找不到符合条件的后继连接").toString());
        }
        
        //5. 取得后继活动并且把新的变迁写入数据库
        List<IWfActivity> toActivities = new LinkedList<IWfActivity>();
        // 遍历出边，创建流程出边
        for (Iterator<WfTplRoute> it = outRoutes.iterator(); it.hasNext();) {
            WfTplRoute tplRoute = it.next();
            //0 取后继结点
            IWfTplNode postNode = tplRoute.getToNode();
            WfNode toNode = new WfNode();
            toNode.setID(UUID.get());
            toNode.setStartTime(DateUtils.now14());
            toNode.setStatus(WfNode.STATUS_RUNNING);
            toNode.setTplNode(postNode);
            toNode.setWfFlow(flow);
            flow.addNode(toNode);
            //1 持久化后继活动
            nodeService.newNode(dbConnection, toNode);
            //2 实例化后继活动
            IWfActivity toActivity = activityService.instanceAtivity(toNode);
            //3 实例化变迁
            WfRoute route = new WfRoute();
            route.setID(UUID.get());
            route.setTplRoute(tplRoute);
            route.setFromNode(fromActivity.getWfNode());
            route.setToNode(toActivity.getWfNode());
            //4 持久化连接
            routeService.newRoute(dbConnection, route);
            // 增加连接到流程
            flow.addRoute(route);
            toActivities.add(toActivity);
        }
        return toActivities;
    }

    /**
     * 解析条件表达式。
     * @param expression	条件表达式
     * @param varSet		流程变量
     * @return
     * @throws ParseException
     */
    public static boolean evalExpression(String expression, Map<String, WfVar> varSet) throws Exception {
    	// 没有提供表达式
        if (StringUtils.isBlank(expression)) return true;
        
        //定义表达式解析器
        ExpressionParser expParser = new ExpressionParser();
        Object result = expParser.parseExpr(expression, varSet);
        return ((Boolean) result).booleanValue();
    }
}
