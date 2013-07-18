package com.shs.commons.workflow.template.models;
import org.json.JSONObject;
/**
 * @class describe: 模板类型, : wf_tpl_types
 * @version 0.1
 * @date created: Mar 2, 2012 9:24:23 AM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class WfTplType {
	private String id;
	private String name;
	private int depth;
	private boolean leaf;
	private String status;
	public JSONObject toJO() throws Exception {
        return new JSONObject()
                .put("id", id)
                .put("name", name)
                .put("depth", depth)
                .put("leaf", leaf)
                .put("status", status);
	}

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

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
