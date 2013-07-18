/**
 * file describe: Route.java
 *
 */
package com.shs.commons.workflow.template.models;
import org.json.JSONArray;
import org.json.JSONObject;

import com.shs.commons.workflow.template.models.interfaces.IWfTplNode;

import java.util.Collection;
import java.util.Map;
/**
 * @class describe: 流程模板边
 * @version 0.1
 * @date created: 2012-2-24 下午3:46:56
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class WfTplRoute {
	private String id;
    private IWfTplNode fromNode;
    private IWfTplNode toNode;
	private String expression;
	private String note;
	private WfTemplate wfTemplate;

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}


	public String getExpression() {
		return expression;
	}

	public void setExpression(String conditionEexpression) {
		this.expression = conditionEexpression;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public JSONObject toJO() throws Exception {
		return new JSONObject()
			.put("id", this.id)
			.put("from_node_id", this.fromNode.getID())
			.put("to_node_id", this.toNode.getID())
			.put("condition_expression", this.expression)
			.put("note", this.note)
			.put("workflow_template_id", this.wfTemplate.getID());
	}
	public static JSONArray toJA(Map<String, WfTplRoute> routes) throws Exception {
		JSONArray routesJSONArray = new JSONArray();
		Collection<WfTplRoute> routesCollection = routes.values();
		for (WfTplRoute route : routesCollection)
			routesJSONArray.put(route.toJO());
		return routesJSONArray;
	}

    public IWfTplNode getFromNode() {
        return fromNode;
    }

   public IWfTplNode getToNode() {
        return toNode;
    }

    public void setFromNode(IWfTplNode fromNode) {
        this.fromNode = fromNode;
    }

    public void setToNode(IWfTplNode toNode) {
        this.toNode = toNode;
    }

    public void setWfTemplate(WfTemplate wfTemplate) {
        this.wfTemplate = wfTemplate;
    }
}
