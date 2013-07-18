/**
 * file describe: WorkflowTemplateType.java
 *
 */
package com.shs.commons.workflow.models.flowchart.templates;

import com.shs.commons.workflow.models.AbstractModel;

import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @class describe: 模板类型, : wf_tpl_types
 * @version 0.1
 * @date created: Mar 2, 2012 9:24:23 AM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class WfTplType extends AbstractModel {
	public final static String TABLE_NAME = "wf_tpl_types";
	public static final String ID_NAME = "type_id";
	
	private String id;
	private String name;
	private String depth;
	private String leaf;
	private String status;
	public WfTplType(String id, String name, String depth,
                     String leaf, String status) {
		this.id = id;
		this.name = name;
		this.depth = depth;
		this.leaf = leaf;
		this.status = status;
	}
	// data access
			
	public static WfTplType get(String id) throws Exception{
		return AbstractModel.<WfTplType>findBySQL ("select * from " + TABLE_NAME + " where " + ID_NAME + " = '" + id + "'",
					WfTplType.class);
	}
	public static List<WfTplType> list() throws Exception{
		return AbstractModel.<WfTplType>findAllBySQL("select * from " + TABLE_NAME,
                WfTplType.class);
	}
	
	@SuppressWarnings("unchecked")
	public static WfTplType getInstanceFromResultSet(ResultSet rs) throws SQLException {
		String id = rs.getString(ID_NAME);
		String name = rs.getString("f_name");
		String level = rs.getString("f_grad");
		String isDetail = rs.getString("f_islf");
		String status = rs.getString("f_stus");
		
		return new WfTplType(id, name, level, isDetail, status);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepth() {
		return depth;
	}
	public void setDepth(String depth) {
		this.depth = depth;
	}
	public String getIsDetail() {
		return leaf;
	}
	public void setIsDetail(String isDetail) {
		this.leaf = isDetail;
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
                .put("depth", depth)
                .put("is_detail", leaf)
                .put("status", status);
	}
	public static JSONArray toJA(List<WfTplType> types) throws Exception {
		JSONArray jaTypes = new JSONArray();
		for (WfTplType type : types) {
			jaTypes.put(type.toJO());
		}
		return jaTypes;
	}
}
