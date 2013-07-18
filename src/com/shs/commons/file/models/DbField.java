package com.shs.commons.file.models;
/**
 * 数据库列名
 * @version 0.1
 * @author chyxion
 * @describe: 
 * @date created: Jan 14, 2013 5:25:26 PM
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public class DbField {
	public final static String TYPE_INT = "int";
	public final static String TYPE_STRING = "string";
	public final static String TYPE_DOUBLE = "double";
	/**
	 * 精度
	 */
	private int precision;
	/**
	 * 列名
	 */
	private String name;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 构造方法
	 * @param name
	 * @param type
	 * @param precision
	 */
	public DbField(String name, String type, int precision) {
		this.name = name;
		this.type = type;
		this.precision = precision;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 初始化值
	 * @param v
	 * @return
	 */
	public Object initValue(Object v) {
		if (TYPE_DOUBLE.equals(type)) {
			if (v instanceof String) {
				v = Double.parseDouble((String) v);
			}
		} else if (TYPE_INT.equals(type)) {
			if (v instanceof String) {
				v = Integer.parseInt((String) v);
			} else if (v instanceof Double) {
				v = ((Double) v).intValue();
			} 
		} 
		return v;
	}
}
