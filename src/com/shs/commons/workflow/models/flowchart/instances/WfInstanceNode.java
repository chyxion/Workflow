package com.shs.commons.workflow.models.flowchart.instances;

import java.sql.Connection;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import com.shs.commons.workflow.models.AbstractModel;
import com.shs.commons.workflow.models.flowchart.templates.WfTemplateNode;
import com.shs.framework.dao.BaseDAO;

/**
 * @class describe: 流程节点实例, 活动
 * @version 0.1
 * @date created: 2012-2-24 下午5:34:40
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class WfInstanceNode extends AbstractModel{
	//流程
	public final static String TABLE_NAME = "wf_actn";
	public final static String ID_NAME = "f_acid";
	private String id;
	private String name;
	private String workflowInstanceId;
	private WfTemplateNode templateNode;
	private String templateId;
	private String templateName; //流程模板name
	private String timeStart;
	private String timeDone;
	private String status;
	private String rollback;
	private List<WfInstanceTask> tasks = new LinkedList<WfInstanceTask>();
	
	public WfInstanceNode(String id, String name,
			String workflowInstanceId, WfTemplateNode templateNode,
			String templateId, String templateName, String timeStart,
			String timeDone, String status, String rollback,
			List<WfInstanceTask> tasks) {
		super();
		this.id = id;
		this.name = name;
		this.workflowInstanceId = workflowInstanceId;
		this.templateId = templateId;
		this.templateName = templateName;
		this.templateNode = templateNode;
		this.timeStart = timeStart;
		this.timeDone = timeDone;
		this.status = status;
		this.rollback = rollback;
		this.tasks = tasks;
	}

	//data access
	public static WfInstanceNode get(String id) throws Exception{
		return AbstractModel.<WfInstanceNode>findByID (TABLE_NAME, ID_NAME, id, 
					WfInstanceNode.class);
	}
	public static List<WfInstanceNode> list() throws Exception{
		return AbstractModel.<WfInstanceNode>findAllBySQL("select * from " + TABLE_NAME,
                WfInstanceNode.class);
	}

    public static List<WfInstanceNode> findAllByFlowID(Connection dbConnection, String flowID) throws Exception {
        return new BaseDAO().query(dbConnection, new BaseDAO.ResultSetOperator() {
            @Override
            public void run() throws Exception {
                List<WfInstanceNode> listResult = new LinkedList<WfInstanceNode>();
                while (resultSet.next()) {
                    listResult.add(getInstanceFromResultSet(dbConnection, resultSet));
                }
                result = listResult;
            }
        }, "select * from wf_actn where f_flid = ?", flowID);
    }

	public WfTemplateNode getTemplateNode() {
		return templateNode;
	}
	public List<WfInstanceTask> getTasks() {
		return tasks;
	}
	public static WfInstanceNode getInstanceFromResultSet(Connection dbConnection, ResultSet rs)
			throws Exception {
		String id = rs.getString("f_acid");
		String name = rs.getString("f_ndname");
		String workflowInstanceId = rs.getString("f_flid");
		WfTemplateNode templateNode = WfTemplateNode.get(rs.getString("f_ndid"));
		String templateId = rs.getString("f_tmid");
		String templateName = rs.getString("f_tmname");
		String timeStart = rs.getString("f_stme");
		String timeDone = rs.getString("f_etme");
		String status = rs.getString("f_stus");
		String rollback = rs.getString("f_sfth");
		return new WfInstanceNode(id, name,
                workflowInstanceId,
                templateNode,
                templateId,
                templateName,
                timeStart,
                timeDone,
                status,
                rollback,
                WfInstanceTask.findAllByNodeID(dbConnection, id));
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "WorkflowInstanceNode [id=" + id + ", name=" + name
				+ ", workflowInstanceId=" + workflowInstanceId
				+ ", templateNode=" + templateNode + ", templateId="
				+ templateId + ", templateName=" + templateName
				+ ", timeStart=" + timeStart + ", timeDone=" + timeDone
				+ ", status=" + status + ", rollback=" + rollback + ", tasks="
				+ tasks + "]";
	}
	
}
