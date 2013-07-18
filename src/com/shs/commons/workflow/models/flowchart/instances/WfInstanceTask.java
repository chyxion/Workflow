/**
 * file describe: WorkflowInstanceTask.java
 *
 */
package com.shs.commons.workflow.models.flowchart.instances;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.shs.commons.workflow.models.AbstractModel;
import com.shs.framework.dao.BaseDAO;

/**
 * @class describe: 流程实例任务
 * @version 0.1
 * @date created: Mar 2, 2012 11:49:44 AM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class WfInstanceTask extends AbstractModel{
	public static final String TABLE_NAME = "wf_task";
	public static final String ID_NAME = "f_taid";
	
	private String id;
	private String bizID;
	private String instanceNodeID;
	private String workflowInstanceID;
	private String tplName;
	private String assignTime;
	private String acceptTime;
	private String doneTime;
	private String status;
	private String opinion;
	private String executorID;
	private String executorName;
	private String executorUnit;
	
	public WfInstanceTask(String id, String bizID,
			String instanceNodeID, String workflowInstanceID,
			String templateName, String assignTime, String acceptTime,
			String doneTime, String status, String opinion, String executorID,
			String executorName, String executorUnit) {
		super();
		this.id = id;
		this.bizID = bizID;
		this.instanceNodeID = instanceNodeID;
		this.workflowInstanceID = workflowInstanceID;
		this.tplName = templateName;
		this.assignTime = assignTime;
		this.acceptTime = acceptTime;
		this.doneTime = doneTime;
		this.status = status;
		this.opinion = opinion;
		this.executorID = executorID;
		this.executorName = executorName;
		this.executorUnit = executorUnit;
	}

	//data access
	
	public static WfInstanceTask get(String id) throws Exception{
		return AbstractModel.<WfInstanceTask>findBySQL ("select task.f_taid f_taid, task.f_bid f_bid, task.f_acid f_acid, task.f_ndid f_ndid, task.f_ndname f_ndname, task.f_flid f_flid, task.f_tmname f_tmname, task.f_stme f_stme, task.f_atme f_atme, task.f_etme f_etme, task.f_stus f_stus, task.f_cncls f_cncls, task.f_usrid f_usrid, task.f_usrname f_usrname, idp_unit.dwmc executer_unit from " + 
						TABLE_NAME + " task join idp_unit_user on task.f_usrid = idp_unit_user.yhbh left join idp_unit on idp_unit_user.dwbh = idp_unit.dwbh where task." + ID_NAME + " = '" + id + "'", 
					WfInstanceTask.class);
	}
	public static List<WfInstanceTask> list() throws Exception{
		return AbstractModel.<WfInstanceTask>findAllBySQL("select task.f_taid f_taid, task.f_bid f_bid, task.f_acid f_acid, task.f_ndid f_ndid, task.f_ndname f_ndname, task.f_flid f_flid, task.f_tmname f_tmname, task.f_stme f_stme, task.f_atme f_atme, task.f_etme f_etme, task.f_stus f_stus, task.f_cncls f_cncls, task.f_usrid f_usrid, task.f_usrname f_usrname, idp_unit.dwmc executer_unit from " +
                TABLE_NAME + " task join idp_unit_user on task.f_usrid = idp_unit_user.yhbh left join idp_unit on idp_unit_user.dwbh = idp_unit.dwbh;",
                WfInstanceTask.class);
	}
	
    public static List<WfInstanceTask> findAllByNodeID(Connection dbConnection, String nodeID) throws Exception {
        return new BaseDAO().query(dbConnection,
                new BaseDAO.ResultSetOperator() {
            @Override
            public void run() throws Exception {
                List<WfInstanceTask> listResult = new LinkedList<WfInstanceTask>();
                while (resultSet.next())
                    listResult.add(getInstanceFromResultSet(resultSet));
                result = listResult;
            }
        }, new StringBuffer("select task.f_taid f_taid, task.f_bid f_bid, task.f_acid f_acid, ")
                    .append("task.f_ndid f_ndid, task.f_ndname f_ndname, task.f_flid f_flid, ")
                    .append("task.f_tmname f_tmname, task.f_stme f_stme, task.f_atme f_atme, ")
                    .append("task.f_etme f_etme, task.f_stus f_stus, task.f_cncls f_cncls, ")
                    .append("task.f_usrid f_usrid, task.f_usrname f_usrname, idp_unit.dwmc executer_unit from ")
                    .append(TABLE_NAME)
                    .append(" task join idp_unit_user on task.f_usrid = idp_unit_user.yhbh ")
                    .append("left join idp_unit on idp_unit_user.dwbh = idp_unit.dwbh where f_acid = ?")
                    .toString(), nodeID);
    }
	@SuppressWarnings("unchecked")
	public static WfInstanceTask getInstanceFromResultSet(ResultSet rs) throws SQLException{
		String id = rs.getString(ID_NAME);
		String businessId = rs.getString("f_bid");
		String instanceNodeId = rs.getString("f_acid");
		String instanceId = rs.getString("f_flid");
		String templateName = rs.getString("f_tmname");
		String assignTime = rs.getString("f_stme");
		String acceptTime = rs.getString("f_atme");
		String donmeTime = rs.getString("f_etme");
		String status = rs.getString("f_stus");
		String opinion = rs.getString("f_cncls");
		String executerId = rs.getString("f_usrid");
		String executerName = rs.getString("f_usrname");
		String executerUnit = rs.getString("executer_unit");
		return new WfInstanceTask(id, businessId, 
					instanceNodeId, instanceId, 
					templateName, assignTime, 
					acceptTime, donmeTime, 
					status, opinion, executerId, executerName, executerUnit);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBizID() {
		return bizID;
	}

	public void setBizID(String bizID) {
		this.bizID = bizID;
	}

	public String getInstanceNodeID() {
		return instanceNodeID;
	}

	public void setInstanceNodeID(String instanceNodeID) {
		this.instanceNodeID = instanceNodeID;
	}

	public String getWorkflowInstanceID() {
		return workflowInstanceID;
	}

	public void setWorkflowInstanceID(String instanceId) {
		this.workflowInstanceID = instanceId;
	}

	public String getTplName() {
		return tplName;
	}

	public void setTplName(String tplName) {
		this.tplName = tplName;
	}

	public String getAssignTime() {
		return assignTime;
	}

	public void setAssignTime(String assignTime) {
		this.assignTime = assignTime;
	}

	public String getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(String acceptTime) {
		this.acceptTime = acceptTime;
	}

	public String getDoneTime() {
		return doneTime;
	}

	public void setDoneTime(String doneTime) {
		this.doneTime = doneTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public String getExecutorUnit() {
		return executorUnit;
	}
	public void setExecutorUnit(String executorUnit) {
		this.executorUnit = executorUnit;
	}
	@Override
	public String toString() {
		return "WorkflowInstanceTask [id=" + id + ", bizID=" + bizID
				+ ", instanceNodeID=" + instanceNodeID
				+ ", workflowInstanceID=" + workflowInstanceID
				+ ", tplName=" + tplName + ", assignTime="
				+ assignTime + ", acceptTime=" + acceptTime + ", doneTime="
				+ doneTime + ", status=" + status + ", options=" + opinion
				+ "]";
	}

    public String getExecutorID() {
        return executorID;
    }

    public String getExecutorName() {
        return executorName;
    }
}
