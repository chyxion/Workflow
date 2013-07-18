package com.shs.commons.models;
import org.json.JSONObject;
/**
 * Description: 用户角色
 * User: chyxion
 * Date: 12/18/12
 * Time: 11:51 AM
 * Support: chyxion@163.com
 * Copyright: Shenghang Soft All Right Reserved
 */
public class Role {
	private String id;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Role(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public String getID() {
		return id;
	}
	public void setID(String id) {
		this.id = id;
	}
	public JSONObject toJO() throws Exception {
		return new JSONObject()
					.put("id", id)
					.put("name", name);
	}
	
}
