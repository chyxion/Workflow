package com.shs.commons.workflow.template.models.derived;
import com.shs.commons.workflow.template.models.WfTplNode;
public class WfTplNodeManual extends WfTplNode {
    /**
     * 分派策略脚本
     */
    private String assignStrategy;
    
    /**
     * 任务控制类型，RAC:抢占,仅当一个任务完成 ；RSG:会签,所有任务完成
     */
    private String taskType;
    
    /**
     * 任务完成限时，小时为单位
     */
    private String limitTime;
    
    public WfTplNodeManual() {
    }
	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getAssignStratige() {
		return assignStrategy;
	}

	public void setAssignStratige(String assignStratige) {
		this.assignStrategy = assignStratige;
	}

	public String getLimitTime() {
		return limitTime;
	}

	public void setLimitTime(String limitTime) {
		this.limitTime = limitTime;
	}
}
