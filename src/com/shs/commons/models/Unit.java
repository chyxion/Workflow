package com.shs.commons.models;
import org.json.JSONObject;
/**
 * Description: 用户单位
 * User: chyxion
 * Date: 12/18/12
 * Time: 11:51 AM
 * Support: chyxion@163.com
 * Copyright: Shenghang Soft All Right Reserved
 */
public class Unit {
	/**
	 * 教学单位
	 */
	public static final String TYPE_TEACH = "teach";
	/**
	 * 管理单位
	 */
	public static final String TYPE_MANAGE = "manage";
	
    private String id;
    private String name;
    private String shortName;
    private int depth;
    private boolean leaf; // 0|1
    private String status;
    private String type;
    
    public Unit(String id, String name, String shortName, int depth, 
    		boolean leaf, String status) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.depth = depth;
        this.leaf = leaf;
        this.status = status;
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public boolean getLeaf() {
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

	public JSONObject toJO() throws Exception {
		return new JSONObject()
				.put("id", id)
				.put("name", name)
				.put("type", getType())
				.put("short_name", shortName);
	}

	public Unit setType(String type) {
		this.type = type;
		return this;
	}

	public String getType() {
		return type;
	}
}
