package com.shs.commons.workflow.models.flowchart.views;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;

/**
 * @class describe: 节点组
 * @version 0.1
 * @date created: Mar 5, 2012 4:37:37 PM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class NodeGroup {
	private List<Node> nodes = new LinkedList<Node>();
	
	public NodeGroup() {
		super();
	}
	public NodeGroup(List<Node> nodes) {
		super();
		this.nodes = nodes;
	}
	public List<Node> getNodes() {
		return nodes;
	}
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}
	@Override
	public String toString() {
		return "NodeGroup [nodes=" + nodes + "]";
	}
	public JSONArray toJA() throws Exception {
        JSONArray jaNodeGroup = new JSONArray();
		for (Node node : nodes)
			jaNodeGroup.put(node.toJO());
		return jaNodeGroup;
	}
}
