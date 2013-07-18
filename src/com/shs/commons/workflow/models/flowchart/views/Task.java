/**
 * file describe: Task.java
 *
 */
package com.shs.commons.workflow.models.flowchart.views;

import com.shs.commons.workflow.models.flowchart.instances.WfInstanceTask;
import com.shs.framework.utils.DateUtils;

import org.json.JSONObject;

/**
 * @class describe: 节点任务
 * @version 0.1
 * @date created: Mar 5, 2012 4:38:45 PM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class Task extends AbstractViewComponent {
	private String timeAssign;
	private String timeAccept;
	private String timeDone;
	private String status;
	private String executorID;
	private String executorName;
	private String executorUnit;
	private String opinion;
	
	public Task(WfInstanceTask instanceTask) {
		super();
		this.timeAssign = instanceTask.getAssignTime();
		this.timeAccept = instanceTask.getAcceptTime();
		this.timeDone = instanceTask.getDoneTime();
		this.status = instanceTask.getStatus();
		this.executorID = instanceTask.getExecutorID();
		this.executorName = instanceTask.getExecutorName();
		this.opinion = instanceTask.getOpinion();
		this.executorUnit = instanceTask.getExecutorUnit();
	}
	public Task() {
		super();
	}
	public Task(String style, String cssClass) {
		super(style, cssClass);
	}
	public Task(String timeAssign, String timeAccept, String timeDone,
			String status, String executorID, String executorName, String option, String executorUnit) {
		super();
		this.timeAssign = timeAssign;
		this.timeAccept = timeAccept;
		this.timeDone = timeDone;
		this.status = status;
		this.executorID = executorID;
		this.executorName = executorName;
		this.opinion = option;
		this.executorUnit = executorUnit;
	}
	//getters and setters 
	public String getStatus() {
		return status;
	}
	public String getTimeAssign() {
		return timeAssign;
	}
	public void setTimeAssign(String timeAssign) {
		this.timeAssign = timeAssign;
	}
	public String getTimeAccept() {
		return timeAccept;
	}
	public void setTimeAccept(String timeAccept) {
		this.timeAccept = timeAccept;
	}
	public String getTimeDone() {
		return timeDone;
	}
	public void setTimeDone(String timeDone) {
		this.timeDone = timeDone;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getExecutorUnit() {
		return executorUnit;
	}
	public void setExecutorUnit(String executorUnit) {
		this.executorUnit = executorUnit;
	}
	public String getExecutorID() {
		return executorID;
	}
	public void setExecutorID(String executorID) {
		this.executorID = executorID;
	}
	public String getExecutorName() {
		return executorName;
	}
	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}
	public String getExecutor() {
		return executorID;
	}
	public void setExecutor(String executor) {
		this.executorID = executor;
	}
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	@Override
	public String toString() {
		return "Task [assignTime=" + timeAssign + ", acceptTime=" + timeAccept
				+ ", doneTime=" + timeDone + ", status=" + status
				+ ", executorID=" + executorID + ", executorName="
				+ executorName + ", opinion=" + opinion + "]";
	}
	public JSONObject toJO() throws Exception {
        return new JSONObject()
                .put("time_assign", timeAssign.equals("") ? timeAssign : DateUtils.formatDefault(timeAssign))
                .put("time_accept", timeAccept.equals("") ? timeAccept : DateUtils.formatDefault(timeAccept))
                .put("time_done", timeDone.equals("") ? timeDone : DateUtils.formatDefault(timeDone))
                .put("status", status)
                .put("executor", executorName + "[" + executorID + "]")
                .put("executor_unit", executorUnit)
                .put("opinion", opinion)
                .put("style", getStyle())
                .put("css_class", getCssClass());
	}
}
