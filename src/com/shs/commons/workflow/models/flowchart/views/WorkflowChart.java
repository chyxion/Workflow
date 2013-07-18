/**
 * file describe: WorkflowChart.java
 *
 */
package com.shs.commons.workflow.models.flowchart.views;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.shs.commons.workflow.instance.models.WfNode;
import com.shs.commons.workflow.models.flowchart.instances.WfInstance;
import com.shs.commons.workflow.models.flowchart.instances.WfInstanceNode;
import com.shs.commons.workflow.models.flowchart.instances.WfInstanceRoute;
import com.shs.commons.workflow.models.flowchart.templates.WfTemplateNode;
import com.shs.commons.workflow.models.flowchart.templates.WfTemplateRoute;
import com.shs.framework.utils.DateUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @class describe: 流程图
 * @version 0.1
 * @date created: Mar 5, 2012 2:29:20 PM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class WorkflowChart extends AbstractViewComponent {
    /**
     * 状态映射
     */
	public static final Map<String, String> STATUS_MAPPING = new HashMap<String, String>();
	static {
		WorkflowChart.STATUS_MAPPING.put(WfNode.STATUS_RUNNING, "运行中");
		WorkflowChart.STATUS_MAPPING.put(WfNode.STATUS_COMPLETED, "已完成");
	}
	private String id;
	private String bizID;
	private String name;
	private String timeStart;
	private String timeDone;
	private String status;
	private String userId;
	private String userName;
	private String userUnitID;
	private String userUnitName;
	private String description;
	private LinkedList<NodeGroup> nodeGroups;
	private List<Route> routes;
	private WfInstance workflowInstance;

	public WorkflowChart(WfInstance wfInstance) {
		super();
		this.id = wfInstance.getID();
		this.bizID = wfInstance.getBizID();
		this.name = wfInstance.getName();
		this.timeStart = wfInstance.getTimeStart();
		this.timeDone = wfInstance.getTimeDone();
		this.status = wfInstance.getStatus();
		this.userId = wfInstance.getUserID();
		this.userName = wfInstance.getUserName();
		this.description = wfInstance.getWfTpl().getDescription();
		this.workflowInstance = wfInstance;
		//构造节点组
		this.nodeGroups = new LinkedList<NodeGroup>();
		//组织流程实例节点到节点组
		organizeInstanceNodes(wfInstance.startNodes(), this.nodeGroups);
		//将流程实例中最后节点设置为当前节点,  如果后面还有结点
		List<Node> currentNodes = this.nodeGroups.getLast().getNodes();
				
		//流程实例的末节点
		List<WfInstanceNode> instanceLastNodes = wfInstance.lastNodes();
		//流程实例末节点后模板节点
		List<WfTemplateNode> nodesAfterInstanceLast = wfInstance.nodesAfterLast();
		if (nodesAfterInstanceLast.size() > 0){
			for (Node node : currentNodes){ 
				node.setStatus(Node.STATUS_CURRENT);
				node.setType(Node.TYPE_CURRENT);
			}
		}
		//组织模板节点到节点组
		organizeTemplateNodes(nodesAfterInstanceLast, this.nodeGroups);
		//将第一个节点和最后一个节点标识为origin和terminal
		List<Node> originNodes = this.nodeGroups.getFirst().getNodes();
		for (Node node : originNodes) 
			node.setType(Node.TYPE_ORIGIN);
		List<Node> terminalNodes = this.nodeGroups.getLast().getNodes();	
		for (Node node : terminalNodes) 
			node.setType(Node.TYPE_TERMINAL);
				
		//构造连接边
		this.routes = new LinkedList<Route>();
		//取出实例中全部边
		Collection<WfInstanceRoute> instanceRoutes = wfInstance.getRoutes().values();
		for (WfInstanceRoute instanceRoute : instanceRoutes) 
			this.routes.add(new Route(instanceRoute));
		//流程模板连接边 
		Collection<WfTemplateRoute> templateRoutes = wfInstance.getWfTpl().getRoutes().values();
		//连接实例节点到模板节点, 实例末节点, 模板实例节点对应的下一个节点
		for (WfInstanceNode lastInstanceNode : instanceLastNodes) // 实例末尾节点
			for (WfTemplateNode nextTemplateNode : nodesAfterInstanceLast) // 实例与模板交接处模板节点
				for (WfTemplateRoute templateRoute : templateRoutes) { // 在模板连线中寻找连接点 
					String templateFromNodeId = lastInstanceNode.getTemplateNode().getId();
					String templateToNodeId = nextTemplateNode.getId();
					if (templateFromNodeId.equals(templateRoute.getFromNodeId()) &&
							templateToNodeId.equals(templateRoute.getToNodeId()))
						this.routes.add(new Route(templateRoute.getDescription(), 
								lastInstanceNode.getId(), //实例节点
								templateToNodeId,  //模板节点
								Route.STATUS_UNSTART));
				}
		//添加模板中剩下的route
		LinkedList<WfTemplateRoute> afterTemplateRoutes = new LinkedList<WfTemplateRoute>();
				wfInstance.getWfTpl().routesAfter(nodesAfterInstanceLast, afterTemplateRoutes);
		for (WfTemplateRoute workflowTemplateRoute : afterTemplateRoutes) {
			this.routes.add(new Route(workflowTemplateRoute));
		}
	}
	//tools
	public JSONObject toJO() throws Exception {
        JSONObject joWf = new JSONObject()
            .put("id", id)
            .put("business_id", bizID)
            .put("name", name)
            .put("time_start", DateUtils.formatDefault(timeStart))
            .put("time_done", DateUtils.formatDefault(timeDone))
            .put("status", STATUS_MAPPING.get(status))
            .put("user_id", userId)
            .put("user_name", userName)
            .put("user_unit", userUnitName + "(" + userUnitID + ")")
            .put("description", description)
            .put("style", this.getStyle())
            .put("css_class", this.getCssClass());
        JSONArray jaNodeGroups = new JSONArray();
		//构建node_groups
		for (NodeGroup nodeGroup : this.nodeGroups) 
			jaNodeGroups.put(nodeGroup.toJA());
				
		joWf.put("node_groups", jaNodeGroups);
		//构建routes
        JSONArray jaRoutes = new JSONArray();
		for (Route route : routes) 
			jaRoutes.put(route.toJO());
				
		joWf.put("routes", jaRoutes);
        return joWf;
	}
	//tools
	/**
	 * 组织流程实例节点
	 */
	public void organizeInstanceNodes(List<WfInstanceNode> startNodes, 
					List<NodeGroup> nodeGroups){
		if (startNodes.size() > 0) {
			//将节点添加到图表节点组
			NodeGroup ng = new NodeGroup();
			for (WfInstanceNode node : startNodes)
				ng.getNodes().add(new Node(node));
			nodeGroups.add(ng);
			//递归添加下一组
			organizeInstanceNodes(workflowInstance.nextNodes(startNodes), nodeGroups);
		}
		
	}
	/**
	 * 组织模板节点到图表节点
	 * @param startNodes
	 * @param nodeGroups
	 */
	public void organizeTemplateNodes(List<WfTemplateNode> startNodes, 
				List<NodeGroup> nodeGroups){
		if (startNodes.size() > 0){
			//将节点添加到图表组节点
			NodeGroup ng = new NodeGroup();
			for (WfTemplateNode node : startNodes)
				ng.getNodes().add(new Node(node));
			nodeGroups.add(ng);
			//递归添加下一组
			organizeTemplateNodes(workflowInstance.getWfTpl().nextNodes(startNodes), nodeGroups);
		}
		
	}
	//getters and setters
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBizID() {
		return bizID;
	}
	public void setBizID(String bizID) {
		this.bizID = bizID;
	}
	public String getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}
	public String getTimeDone() {
		return timeDone;
	}
	public void setTimeDone(String timeDone) {
		this.timeDone = timeDone;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserUnitID() {
		return userUnitID;
	}
	public void setUserUnitID(String userUnitID) {
		this.userUnitID = userUnitID;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserUnitName() {
		return userUnitName;
	}

	public void setUserUnitName(String userUnitName) {
		this.userUnitName = userUnitName;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LinkedList<NodeGroup> getNodeGroups() {
		return nodeGroups;
	}
	public void setNodeGroups(LinkedList<NodeGroup> nodeGroups) {
		this.nodeGroups = nodeGroups;
	}
	public List<Route> getRoutes() {
		return routes;
	}
	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}
	
}
