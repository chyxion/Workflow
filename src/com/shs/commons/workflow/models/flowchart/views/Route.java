/**
 * file describe: Route.java
 *
 */
package com.shs.commons.workflow.models.flowchart.views;
import com.shs.commons.workflow.models.flowchart.instances.WfInstanceRoute;
import com.shs.commons.workflow.models.flowchart.templates.WfTemplateRoute;

import org.json.JSONObject;

/**
 * @class describe:
 * @version 0.1
 * @date created: Mar 5, 2012 4:47:15 PM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class Route extends AbstractViewComponent {
	public final static String STATUS_DONE = "done";
	public static final String STATUS_UNSTART = "unstart";
	
	private String description;
	private String fromNodeId;
	private String toNodeId;
	private String status;
	
	public Route(WfInstanceRoute instanceRoute){
		if (instanceRoute.getTemplateRoute() != null)
			this.description = instanceRoute.getTemplateRoute().getDescription();
		else this.description = "";
		
		this.fromNodeId = instanceRoute.getFromNodeId();
		this.toNodeId = instanceRoute.getToNodeId();
		this.status = STATUS_DONE;
	}
	
	public Route(WfTemplateRoute templateRoute){
		this.description = templateRoute.getDescription();
		this.fromNodeId = templateRoute.getFromNodeId();
		this.toNodeId = templateRoute.getToNodeId();
		this.status = STATUS_UNSTART;
	}
	public Route(String description, String fromNodeId, String toNodeId,
			String status) {
		super();
		this.description = description;
		this.fromNodeId = fromNodeId;
		this.toNodeId = toNodeId;
		this.status = status;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Route [description=" + description + ", fromNodeId="
				+ fromNodeId + ", toNodeId=" + toNodeId + ", status=" + status
				+ "]";
	}
	public JSONObject toJO() throws Exception {
		//如果description为空白, 使用from -> models 代替
//		routeElement.setAttribute("description", description.trim().equals("") ? fromNodeName + " -> " + toNodeName : description);
        if (description == null) description = "";
        return new JSONObject()
            .put("description", description)
            .put("from_node", fromNodeId)
            .put("to_node", toNodeId)
            .put("status", status)
            .put("style", getStyle())
            .put("css_class", getCssClass());
	}
}

