package com.shs.commons.workflow.template.models.derived;

class WfTplNodeDefault {
	/**
	 * ID
	 */
	protected String id = null;
	/**
	 * Name
	 */
	protected String name = null;
	/**
	 * Kind
	 */
	protected String type = null;
	/**
	 * 结点动作
	 */
	protected String action = null;
	
	/**
	 * 结点前置动作
	 */
	
	protected String beforeAction = null;
	/**
	 * 结点后置动作
	 */
	protected String afterAction = null;
	
	public String getBeforeAction() {
		return beforeAction;
	}

	public void setBeforeAction(String beforeAction) {
		this.beforeAction = beforeAction;
	}

	public String getAfterAction() {
		return afterAction;
	}

	public void setAfterAction(String afterAction) {
		this.afterAction = afterAction;
	}

	public WfTplNodeDefault() {
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
    public String getType() {
        return this.type;
    }

    public void setNodeType(String kind) {
		this.type = kind;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}
