package com.shs.commons.workflow.models.flowchart.views;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.shs.commons.workflow.models.flowchart.instances.WfInstanceNode;
import com.shs.commons.workflow.models.flowchart.instances.WfInstanceTask;
import com.shs.commons.workflow.models.flowchart.templates.WfTemplateNode;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @class describe: 流程图表节点任务
 * @version 0.1
 * @date created: Mar 5, 2012 4:38:31 PM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class Node extends AbstractViewComponent {
	public final static Map<String, String> TYPE_MAPPING = new HashMap<String, String>();
	public final static Map<String, String> STATUS_MAPPING = new HashMap<String, String>();
	//执行状态
	public final static String STATUS_UNSTART = "WAT";
	public final static String STATUS_DONE = "CMP";
	public final static String STATUS_CURRENT = "RUN";
	// 图表节点类型
	public final static String TYPE_ORIGIN = "origin";
	public final static String TYPE_TERMINAL = "terminal";
	public final static String TYPE_DONE = "done";
	public final static String TYPE_UNSTART = "unstart";
	public final static String TYPE_CURRENT = "current";
	static {
		TYPE_MAPPING.put("MNU", "人工节点");
		TYPE_MAPPING.put("AUT", "自动节点");
		TYPE_MAPPING.put("EML", "发送邮件");
		TYPE_MAPPING.put("MSG", "发送消息");
		TYPE_MAPPING.put("STR", "开始节点");
		TYPE_MAPPING.put("END", "结束节点");
		STATUS_MAPPING.put(STATUS_CURRENT, "运行中");
		STATUS_MAPPING.put(STATUS_DONE, "已完成");
		STATUS_MAPPING.put(STATUS_UNSTART, "未启动");
	}
	
	private String id;
	private String title;
	private String type; //origin, terminal, done, unstart, current
	private String templateNodeType; // 模板节点类型,[自动节点, 人工节点...], 后面添加的, so, 上面的type是标识节点(颜色, 形状)样式的
	private int timeLimit;
	private String status; //done, current, unstart
	private NodePrompt prompt;
	private List<Task> tasks = new LinkedList<Task>();

	/**
	 * 根据流程节点构造
	 * @param instanceNode
	 */
	public Node(WfInstanceNode instanceNode) {
		this.id = instanceNode.getId();
		this.title = instanceNode.getName();
		this.type = TYPE_DONE;
        WfTemplateNode tplNode = instanceNode.getTemplateNode();
        if (tplNode != null) {
            this.templateNodeType =tplNode.getType();
            this.timeLimit = tplNode.getTimeLimit();
        }
		this.status = instanceNode.getStatus();
		this.prompt = new NodePrompt(instanceNode);
		for (WfInstanceTask task : instanceNode.getTasks())
			this.tasks.add(new Task(task));
	}
	/**
	 * 根据模板节点构造
	 * @param templateNode
	 */
	public Node(WfTemplateNode templateNode){
		this.id = templateNode.getId();
		this.title = templateNode.getName();
		this.type = TYPE_UNSTART; // 未启动
		this.templateNodeType = templateNode.getType();
		this.timeLimit = templateNode.getTimeLimit();
		this.status = STATUS_UNSTART; // 未启动
		this.prompt = new NodePrompt(templateNode);
	}
	
	public Node(String id, String title, String type, int timeLimit,
			String status, NodePrompt prompt, List<Task> tasks) {
		super();
		this.id = id;
		this.title = title;
		this.type = type;
		this.timeLimit = timeLimit;
		this.status = status;
		this.prompt = prompt;
		this.tasks = tasks;
	}
	//getters and setters
	public String getId() {
		return id;
	}
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public NodePrompt getPrompt() {
		return prompt;
	}
	public void setPrompt(NodePrompt prompt) {
		this.prompt = prompt;
	}
	@Override
	public String toString() {
		return "Node [id=" + id + ", title=" + title + ", type=" + type
				+ ", timeLimit=" + timeLimit + ", status=" + status
				+ ", prompt=" + prompt + ", tasks=" + tasks + "]";
	}
	
	public JSONObject toJO() throws Exception {
        JSONObject joNode = new JSONObject()
		.put("id", id)
		.put("title", title)
		.put("type", type)
		.put("template_node_type", TYPE_MAPPING.get(templateNodeType))
		.put("time_limit", timeLimit + "")
		.put("status", STATUS_MAPPING.get(status == null || status.equals("") ? STATUS_UNSTART : status))
		.put("style", getStyle())
		.put("css_class", getCssClass())
		.put("prompt", prompt.toJO());
        JSONArray jaTasks = new JSONArray();
		for (Task task : this.tasks)
			jaTasks.put(task.toJO());
        joNode.put("tasks", jaTasks);

        return joNode;
	}
}
