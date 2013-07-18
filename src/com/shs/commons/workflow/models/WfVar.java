package com.shs.commons.workflow.models;
import java.math.BigDecimal;
import java.util.Map;

import org.json.JSONArray;

/**
 * @version 0.1
 * @author chyxion
 * @describe: 流程变量
 * @date created: Dec 26, 2012 4:17:49 PM
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public class WfVar {
	/**
	 * String类型
	 */
	public final static String TYPE_STRING = "S";
	/**
	 * int类型
	 * 数值型（在做表达式运算时转化为 double）
	 */
	public final static String TYPE_NUMBER = "N";
	/**
	 * 宿主ID
	 */
	private String owerID;
	/**
	 * 变量名
	 */
	private String name;
	/**
	 * 变量值
	 */
	private Object value;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 精度
	 */
	private int precision;
	/**
	 * 备注
	 */
	private String note;
	
	public WfVar(String owerID, String name, String value, String type,
			int precision, String note) {
		super();
		this.owerID = owerID;
		this.name = name;
		this.value = value;
		this.type = type;
		this.precision = precision;
		this.note = note;
	}
	public WfVar() {
	}
	/**
	 * 复制构造函数，流程模板变量迁移给实例变量时候用
	 * @param var
	 */
	public WfVar(WfVar var) {
		name = var.name;
		value = var.value;
		type = var.type;
		precision = var.precision;
		note = var.note;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		//根据变量类型，构造类型对象
		if(getType().equals(TYPE_NUMBER)){
			this.value = new BigDecimal(value.toString());
		} else {
			this.value = value;
		}
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public void setOwerID(String owerID) {
		this.owerID = owerID;
	}
	public String getOwerID() {
		return owerID;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public static JSONArray toJA(Map<String, WfVar> vars) throws Exception {
		return null;
	}
}
