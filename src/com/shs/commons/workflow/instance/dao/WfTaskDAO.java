package com.shs.commons.workflow.instance.dao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import com.shs.commons.workflow.instance.models.WfTask;
import com.shs.commons.workflow.template.models.interfaces.IWfTplNode;
import com.shs.framework.dao.BaseDAO;
import com.shs.framework.dao.BaseDAO.ConnectionOperator;

public class WfTaskDAO {
	private Logger logger = Logger.getLogger(getClass());
	private BaseDAO dao;
    public WfTaskDAO(BaseDAO dao) {
		this.dao = dao;
	}
	public static final String TABLE = "WF_TASKS";
    private StringBuffer selectAllSQL() {
    	return new StringBuffer("select id, node_id, user_id, assign_time, accept_time, ")
    		.append("done_time, status, opinion from wf_tasks where ");
    }
    public WfTask getTaskFromResultSet(ResultSet rs) throws Exception {
        WfTask task = new WfTask();
        task.setID(rs.getString("ID"));
        task.setAcceptTime(rs.getString("ACCEPT_TIME"));
        task.setDoneTime(rs.getString("DONE_TIME"));
        task.setAssignTime(rs.getString("ASSIGN_TIME"));
        task.setStatus(rs.getString("STATUS"));
        task.setOpinion(rs.getString("OPINION"));
        return task;
    }
    public WfTask getTaskFromJO(JSONObject joTask) throws Exception {
        WfTask task = new WfTask();
        task.setID(joTask.getString("ID"));
        task.setAssignTime(joTask.getString("ASSIGN_TIME"));
        task.setAcceptTime(joTask.optString("ACCEPT_TIME"));
        task.setDoneTime(joTask.optString("DONE_TIME"));
        task.setStatus(joTask.getString("STATUS"));
        task.setOpinion(joTask.optString("OPINION"));
        return task;
    }
	/**
	 * 接受任务。
	 * 更新接收状态，接收时间
	 * @param taskID
	 * @param status
	 */
	public void acceptTask(Connection dbConnection, WfTask task) throws Exception {
		dao.update(dbConnection, 
				"update wf_tasks set accept_time = ?, status = ? where id = ?",
				task.getAcceptTime(), 
				task.getStatus(), 
				task.getID());
	}
	/**
	 * 提交任务，更新状态，完成时间
	 * @param dbConnection
	 * @param task
	 * @throws Exception
	 */
	public void complete(Connection dbConnection, WfTask task) throws Exception {
		dao.update(dbConnection, "update wf_tasks set status = ?, done_time = ?, opinion = ? where id = ?", 
				new Object[]{task.getStatus(), 
				task.getDoneTime(),
				task.getOpinion(), task.getID()});
	}
    /**
     * 查找未完成的用户任务
     * @param usrID
     * @return
     * @throws Exception
     */
	public JSONArray findNotCompletedTasksByUserID(Connection dbConnection, String usrID) throws Exception {
		return dao.findJSONArray(dbConnection, 
				selectAllSQL().append("status <> ?")
				.toString(), 
				WfTask.STATUS_COMPLETED);
	}

	/**
	 * 插入一条任务数据.
	 */
	public void newTask(Connection dbConnection, JSONObject task) throws Exception {
		dao.insert(dbConnection, TABLE, task);
	}
	/**
	 * 删除一条任务数据
	 */
	public void deleteTask(Connection dbConnection, String id) throws Exception {
		dao.update(dbConnection, "delete from wf_tasks where id = ?", id);
	}

	/**
	 * 终止其他兄弟任务
	 * @param dbConnection
	 * @param task
	 * @throws Exception
	 */
    public void dieBrotherTasks(Connection dbConnection, WfTask task) throws Exception {
    	logger.debug("die brother tasks");
    	logger.debug(task.toJO());
        dao.update(dbConnection, "update wf_tasks set status = ? where node_id = ? and id <> ?",
                WfTask.STUTAS_DEAD, task.getNode().getID(), task.getID());
    }
	/**
	 * 获取指定活动所有分配出去的任务状态。
	 * @param nodeID
	 * @return
	 */
	public List<String> findAllStatusByNodeID(Connection dbConnection, String nodeID) throws Exception {
        return dao.findStrList(dbConnection, "select status from wf_tasks where node_id = ?", nodeID);
	}
    public JSONArray findAllByNodeID(Connection dbConnection, String nodeID) throws Exception {
    	return dao.findJSONArray(dbConnection, 
    			selectAllSQL().append("node_id = ?")
    			.toString(), nodeID);
    }
    public void deleteByNodeIDs(Connection dbConnection, JSONArray jaNodeIDs) throws Exception {
    	dao.update(dbConnection, "delete from wf_tasks where node_id in (?)", jaNodeIDs);
    }
	public void newTasks(Connection dbConnection, List<JSONObject> taskList) throws Exception {
		dao.insert(dbConnection, TABLE, new JSONArray(taskList));
	}
	public JSONArray findAllByNodeIDs(Connection dbConnection, JSONArray jaNodeIDs) throws Exception {
		return dao.findJSONArray(dbConnection, 
				selectAllSQL().append("node_id in (?)")
				.toString(),
				jaNodeIDs);
	}
	public void deleteByFlowIDs(Connection dbConnection, JSONArray jaFlowIDs) throws Exception {
		dao.update(dbConnection, 
				"delete from wf_tasks where node_id in (select id from wf_nodes where flow_id in (?))", 
				jaFlowIDs);
	}
	public JSONArray findAllTodoByUserID(Connection dbConnection, String userID) throws Exception {
		return dao.findJSONArray(dbConnection, 
							selectAllSQL().append("user_id = ? and status in (?, ?)")
							.toString(), new Object[]{userID, WfTask.STATUS_ASSIGNED, WfTask.STATUS_ACCEPTED});
	}
	public JSONArray listAllTodo(Connection dbConnection, String userID, int start, int limit) throws Exception {
		return dao.findJSONArrayPage(dbConnection,
				"t.assign_time", 
				"desc", 
				start, 
				limit, 
				new StringBuffer("select t.id id, tf.name || '-' || tn.name name, f.biz_id, ")
				.append("t.assign_time, t.accept_time, au.name applicant, u.name a_unit, ")
				.append("tn.action, f.id flow_id from wf_tasks t ")
				.append("join wf_nodes n on t.node_id = n.id ")
				.append("join wf_tpl_nodes tn on n.tpl_node_id = tn.id ")
				.append("join wf_flows f on n.flow_id = f.id ")
				.append("join wf_templates tf on f.tpl_id = tf.id ")
				.append("join idps_users au on f.user_id = au.id ")
				.append("join idps_user_unit uu on au.id = uu.f_userid ")
				.append("join idps_units u on uu.f_unitid = u.id ")
				.append("where t.status <> ? and t.user_id = ?").toString(),
				WfTask.STATUS_COMPLETED, userID);
	}
	public int tasksCount(Connection dbConnection, String userID) throws Exception {
		return dao.findInt(dbConnection, 
			"select count(1) from wf_tasks where status <> ? and user_id = ?", 
			WfTask.STATUS_COMPLETED, userID);
	}
	/**
	 * 查询一个任务
	 * @param dbConnection
	 * @param taskID
	 * @return
	 * @throws Exception
	 */
	public JSONObject get(Connection dbConnection, String taskID) throws Exception {
		return dao.findJSONObject(dbConnection, 
				selectAllSQL()
				.append("id = ?")
				.toString(), taskID);
	}
	/**
	 * 查询所属流程ID
	 * @param dbConnection
	 * @param taskID
	 * @return
	 * @throws Exception
	 */
	public String findOwnerFlowID(Connection dbConnection, String taskID) throws Exception {
		return dao.findStr(dbConnection, 
				new StringBuffer("select f.id from wf_flows f ")
				.append("join wf_nodes n on f.id = n.flow_id ")
				.append("join wf_tasks t on n.id = t.node_id ")
				.append(" where t.id = ?")
				.toString(), taskID);
	}
	/**
	 * 查询所属流程ID
	 * @param dbConnection
	 * @param joTaskIDs
	 * @return
	 * @throws Exception
	 */
	public List<String> findOwnerFlowIDs(Connection dbConnection, JSONArray joTaskIDs) throws Exception {
		return dao.findStrList(dbConnection, 
				new StringBuffer("select f.id from wf_flows f ")
				.append("join wf_nodes n on f.id = n.flow_id ")
				.append("join wf_tasks t on n.id = t.node_id ")
				.append(" where t.id in (?)")
				.toString(), joTaskIDs);
	}
	public JSONArray listHistory(final String flowID) throws Exception {
		return dao.execute(new ConnectionOperator() {
			@Override
			protected void run() throws Exception {
				result = listHistory(dbConnection, flowID);
			}
		});
	}
	public JSONArray listHistory(Connection dbConnection, String flowID) throws Exception {
		return dao.findJSONArray(dbConnection,
				new StringBuffer("select t.id, tn.name task_name, u.name user_name,  ")
					.append("t.assign_time, t.accept_time, t.opinion, ")
					.append("t.done_time from wf_tasks t ")
					.append("join wf_nodes n on t.node_id = n.id ")
					.append("join wf_tpl_nodes tn on n.tpl_node_id = tn.id ")
					.append("join idps_users u on t.user_id = u.id ")
					.append("join wf_flows f on n.flow_id = f.id ")
					.append("where t.status = ? and f.id = ? order by t.assign_time ")
					.toString(), 
					WfTask.STATUS_COMPLETED, flowID);
	}
	public List<Map<String, Object>> listHistoryByBizID(Connection dbConnection, String bizID) throws Exception {
		return dao.findMapList(dbConnection,
				new StringBuffer("select t.id, tn.name task_name, u.name user_name,  ")
					.append("t.assign_time, t.accept_time, t.opinion, ")
					.append("t.done_time from wf_tasks t ")
					.append("join wf_nodes n on t.node_id = n.id ")
					.append("join wf_tpl_nodes tn on n.tpl_node_id = tn.id ")
					.append("join idps_users u on t.user_id = u.id ")
					.append("join wf_flows f on n.flow_id = f.id ")
					.append("where t.status = ? and f.biz_id = ? order by t.assign_time ")
					.toString(), 
					WfTask.STATUS_COMPLETED, bizID);
	}
	public JSONArray findTasksByUserID(Connection dbConnection, String userID, int start, int limit) throws Exception {
		return dao.findJSONArrayPage(
				dbConnection, 
				"t.assign_time", 
				"desc", start, limit, 
				new StringBuffer("select t.id id, tf.name || '-' || tn.name name, f.biz_id, ")
				.append("t.assign_time, t.accept_time, t.done_time, ")
				.append("t.opinion, au.name applicant, u.name a_unit, ")
				.append("tn.action, f.id flow_id from wf_tasks t ")
				.append("join wf_nodes n on t.node_id = n.id ")
				.append("join wf_tpl_nodes tn on n.tpl_node_id = tn.id ")
				.append("join wf_flows f on n.flow_id = f.id ")
				.append("join wf_templates tf on f.tpl_id = tf.id ")
				.append("join idps_users au on f.user_id = au.id ")
				.append("join idps_user_unit uu on au.id = uu.f_userid ")
				.append("join idps_units u on uu.f_unitid = u.id ")
				.append("where tn.type <> ? and t.status = ? and t.user_id = ?").toString(),
				IWfTplNode.TYPE_APPLICANT,
				WfTask.STATUS_COMPLETED, 
				userID);
	}
	public int tasksHistoryCount(Connection dbConnection, String userID) throws Exception {
		return dao.findInt(dbConnection, 
			new StringBuffer("select count(1) from wf_tasks ")
			.append("join wf_nodes n on t.node_id = n.id ")
			.append("join wf_tpl_nodes tn on n.tpl_node_id = tn.id ")
			.append("where tn.type <> ? and status <> ? and user_id = ?")
			.toString(), 
			IWfTplNode.TYPE_APPLICANT,
			WfTask.STATUS_COMPLETED, 
			userID);
	}
}
