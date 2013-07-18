package com.shs.commons.workflow.template.models.interfaces;
import org.json.JSONObject;

import com.shs.commons.workflow.template.models.WfTemplate;
public interface IWfTplNode {
	/**
	 * 结点类型
	 */
    public static final String TYPE_START ="START";
    public static final String TYPE_END ="END";
    /**
     * 申请节点
     */
    public static final String TYPE_APPLICANT ="APPLICANT";
    public static final String TYPE_MANUAL ="MANUAL";
    public static final String TYPE_AUTO ="AUTO";
    public static final String TYPE_EMAIL ="EMAIL";
    public static final String TYPE_MESSAGE ="MESSAGE";
	/**
	 * 结点ID
	 */
	public String getID();
	public void setID(String id);
	/**
	 * 结点Name
	 */
	public String getName();
	public void setName(String name);
	/**
	 * 结点类型
	 */
	public String getType();
	public void setType(String type);
	/**
	 * 获取动作服务名,后台注册的一个服务名称
	 */
	public String getAction();
	public void setAction(String service);
	public String getBeforeAction();
	public void setBeforeAction(String service);
	public String getAfterAction();
	public void setAfterAction(String service);
	public String getAssignPolicy();
	public void setAssignPolicy(String policy);
	public JSONObject toJO() throws Exception;
	public void setWfTemplate(WfTemplate template);
	public WfTemplate getWfTemplate();
}
