package com.shs.commons.workflow.template.models;
import org.json.JSONArray;
import org.json.JSONObject;
import com.shs.commons.workflow.template.models.interfaces.IWfTplNode;
import java.util.Map;

/**
 * @class describe:
 * @version 0.1
 * @date created: 2012-2-24 下午3:46:21
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class WfTplNode implements IWfTplNode {
    /**
     * 任务类型
     * 抢占,仅当一个任务完成
     */
    public static final String TASK_TYPE_PREEMPTIVE= "PREEMPTIVE";
    /**
     * 任务类型
     * 会签,所有任务完成
     */
    public static final String TASK_TYPE_COUNTERSIGN = "COUNTERSIGN";
    /**
     * 任务类型
     * 投票,比例（暂不支持）
     */
    public static final String TASK_TYPE_VOTE = "VOTE";
    /**
     * 发出分支类型，互斥
     */
    public static final String OUT_ROUTE_TYPE_MUTEX= "MUTEX";
    /**
     * 发出分支类型，并行
     */
    public static final String OUT_ROUTE_TYPE_PARALLEL = "PARALLEL";
    /**
     * 节点类型，开始
     */
    public static final String NODE_TYPE_START = "START";
    /**
     * 节点类型，终止
     */
    public static final String NODE_TYPE_END = "END";
    /**
     * 节点类型，自动
     */
    public static final String NODE_TYPE_AUTO = "AUTO";
    /**
     * 节点类型，人工
     */
    public static final String NODE_TYPE_MANUAL = "MANUAL";
    /**
     * 节点类型，邮件
     */
    public static final String NODE_TYPE_EMAIL = "EMAIL";
    /**
     * 节点类型，消息
     */
    public static final String NODE_TYPE_MESSAGE = "MESSAGE";
    /**
     * 节点编号
     */
	private String id;
    /**
     * 节点名称
     */
	private String name;
    /**
     * 节点类型
     */
	private String type;
    /**
     * 前置动作
     */
	private String beforeAction;
    /**
     * 节点动作
     */
	private String action;
    /**
     * 后置动作
     */
	private String afterAction;
    /**
     * 任务分配策略
     */
	private String assignPolicy;
    /**
     * 任务类型
     */
	private String taskType;
    /**
     * 发出分支类型
     */
	private String outRouteType;
    /**
     * 时限
     */
	private int timeLimit;
    /**
     * 模板
     */
    private WfTemplate wfTemplate;
    /**
     * 节点在设计图上的位置
     */
	private String offset;

	public String getID() {
		return id;
	}
	public void setID(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBeforeAction() {
		return beforeAction;
	}
	public void setBeforeAction(String beforeAction) {
		this.beforeAction = beforeAction;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getAfterAction() {
		return afterAction;
	}
	public void setAfterAction(String afterAction) {
		this.afterAction = afterAction;
	}
	public String getAssignPolicy() {
		return assignPolicy;
	}
	public void setAssignPolicy(String assignStrategy) {
		this.assignPolicy = assignStrategy;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getOutRouteType() {
		return outRouteType;
	}
	public void setOutRouteType(String outRouteType) {
		this.outRouteType = outRouteType;
	}
	public int getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	public JSONObject toJO() throws Exception {
		return new JSONObject()
			.put("id", this.id)
			.put("name", this.name)
			.put("type", this.type)
			.put("before_action", this.beforeAction)
			.put("action", this.action)
			.put("after_action", this.afterAction)
			.put("assign_strategy", this.assignPolicy)
			.put("task_type", this.taskType)
			.put("out_route_type", this.outRouteType)
			.put("time_limit", this.timeLimit)
			.put("tpl_id", this.wfTemplate.getID())
			.put("offset", new JSONObject(this.offset));
	}
	public String getOffset() {
		return offset;
	}
    public void setWfTemplate(WfTemplate wfTemplate) {
        this.wfTemplate = wfTemplate;
    }
    public WfTemplate getWfTemplate() {
    	return wfTemplate;
    }
    public void setOffset(String offset) {
        this.offset = offset;
    }
	public static JSONArray toJA(Map<String, IWfTplNode> nodes) throws Exception {
		JSONArray jaNodes = new JSONArray();
		for (IWfTplNode node : nodes.values()) {
			jaNodes.put(node.toJO());
		}
		return jaNodes;
	}
}
