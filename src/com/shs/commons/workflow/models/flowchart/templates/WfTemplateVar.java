package com.shs.commons.workflow.models.flowchart.templates;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shs.commons.workflow.models.AbstractModel;
/**
 * 流程模板变量
 * @class describe: 
 * @version 0.1
 * @date created: Mar 19, 2012 7:46:52 PM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by:
 */
public class WfTemplateVar extends AbstractModel{

	public final static String TABLE_NAME = "wf_tmvr";
	
	private String tplID;
	private String name;
	private String initValue;
	private String type;
	private int decimal;
	private String description;
	public WfTemplateVar(String tplID, String name, String initValue,
			String type, int decimal, String description) {
		this.tplID = tplID;
		this.name = name;
		this.initValue = initValue;
		this.type = type;
		this.decimal = decimal;
		this.description = description;
	}
	// data access
			
	public static List<WfTemplateVar> findByTemplateId(String templateId) throws Exception{
		return AbstractModel.<WfTemplateVar>findAllBySQL("select * from " + TABLE_NAME + " where  f_tmid = '" + templateId + "'",
                WfTemplateVar.class);
	}
	public static List<WfTemplateVar> list() throws Exception{
		return AbstractModel.<WfTemplateVar>findAllBySQL("select * from " + TABLE_NAME,
                WfTemplateVar.class);
	}
	
	@SuppressWarnings("unchecked")
	public static WfTemplateVar getInstanceFromResultSet(ResultSet rs) throws SQLException {
		String templateId = rs.getString("f_tmid");
		String name = rs.getString("f_key");
		String initValue = rs.getString("f_val");
		String type = rs.getString("f_type");
		int decimal = rs.getInt("f_dec");
		String description = rs.getString("f_note");
		
		return new WfTemplateVar(templateId, name, initValue, type, decimal, description);
	}
	
	public String getTplID() {
		return tplID;
	}

	public void setTplID(String tplID) {
		this.tplID = tplID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInitValue() {
		return initValue;
	}

	public void setInitValue(String initValue) {
		this.initValue = initValue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getDecimal() {
		return decimal;
	}

	public void setDecimal(int decimal) {
		this.decimal = decimal;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public JSONObject toJSONObject() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("workflow_template_id",
                    tplID)
			.put("name", name)
			.put("init_value", initValue)
			.put("type", type)
			.put("decimal", decimal)
			.put("description", description);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo;
	}
	public static JSONArray toJSONArray(List<WfTemplateVar> variables){
		JSONArray variablesJSONArray = new JSONArray();
		
		for (WfTemplateVar var : variables) 
			variablesJSONArray.put(var.toJSONObject());
				
		return variablesJSONArray;
	}
	public static JSONArray toJSONArray(Map<String, WfTemplateVar> variables){
		JSONArray variablesJSONArray = new JSONArray();
		Collection<WfTemplateVar> variablesCollection = variables.values();
		for (WfTemplateVar var : variablesCollection) 
			variablesJSONArray.put(var.toJSONObject());
				
		return variablesJSONArray;
	}
}
