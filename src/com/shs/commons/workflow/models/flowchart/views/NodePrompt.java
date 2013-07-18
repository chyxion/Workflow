package com.shs.commons.workflow.models.flowchart.views;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.shs.commons.workflow.models.flowchart.instances.WfInstanceNode;
import com.shs.commons.workflow.models.flowchart.templates.WfTemplateNode;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @class describe: 节点提示
 * @version 0.1
 * @date created: Mar 5, 2012 5:11:14 PM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by:
 */
public class NodePrompt extends AbstractViewComponent {
	private String title;
	private Map<String, String> items = new HashMap<String, String>();
	public NodePrompt(WfTemplateNode templateNode) {
		this.title = templateNode.getName();
		this.items.put("prompt 1" , "提示内容1");
		this.items.put("prompt 2" , "提示内容2");
		this.items.put("prompt 3" , "提示内容3");
	}
	public NodePrompt(WfInstanceNode instanceNode){
		this.title = instanceNode.getName();
		this.items.put("prompt 1" , "提示内容1");
		this.items.put("prompt 2" , "提示内容2");
		this.items.put("prompt 3" , "提示内容3");
	}
	public NodePrompt(String title, Map<String, String> items) {
		super();
		this.title = title;
		this.items.put("prompt 1" , "提示内容1");
		this.items.put("prompt 2" , "提示内容2");
		this.items.put("prompt 3" , "提示内容3");
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Map<String, String> getItems() {
		return items;
	}
	public void setItems(Map<String, String> items) {
		this.items = items;
	}
	
	public JSONObject toJO() throws Exception {
        JSONObject joNodePrompt = new JSONObject().put("title", title)
                    .put("style", getStyle())
                    .put("css_class", getCssClass());

		Set<String> keys = items.keySet();
		if (!keys.isEmpty()) {
            JSONArray jaItems = new JSONArray();
            for (String key : keys) {
                jaItems.put(new JSONObject()
                        .put("topic", key)
                        .put("content", items.get(key)));
            }
            joNodePrompt.put("items", jaItems);
        }
		return joNodePrompt;
	}
}
