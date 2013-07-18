package com.shs.commons.workflow.instance.dao;
import java.sql.Connection;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.shs.commons.workflow.instance.models.WfFlow;
import com.shs.framework.dao.BaseDAO;
import com.shs.framework.dao.BaseDAO.ConnectionOperator;

/**
 * Description:
 * User: chyxion
 * Date: 12/24/12
 * Time: 11:03 AM
 * Support: chyxion@163.com
 * Copyright: Shenghang Soft All Right Reserved
 */
public class WfFlowDAO {
	private BaseDAO dao;
	public WfFlowDAO(BaseDAO dao) {
		this.dao = dao;
	}
	public static final String TABLE = "WF_FLOWS";
		
	private StringBuffer selectAllSQL() {
		return new StringBuffer("select id, biz_id, user_id, tpl_id, start_time, done_time, status, note from wf_flows where ");
	}
    public WfFlow getFlowFromJO(JSONObject joFlow) throws Exception {
        WfFlow flow = new WfFlow();
        flow.setID(joFlow.getString("ID"));
        flow.setBizID(joFlow.getString("BIZ_ID"));
        flow.setStartTime(joFlow.getString("START_TIME"));
        flow.setDoneTime(joFlow.optString("DONE_TIME"));
        flow.setStatus(joFlow.getString("STATUS"));
        flow.setNote(joFlow.optString("NOTE"));
        return flow;
    }
    /**
     * 完成流程
     * @param dbConnection
     * @param flow
     * @throws Exception
     */
    public void complete(Connection dbConnection, WfFlow flow) throws Exception {
    	dao.update(dbConnection, 
    			"update wf_flows set done_time = ?, status = ?, note = ? where id = ?", 
    			flow.getDoneTime(), 
    			flow.getStatus(), 
    			flow.getNote(),
    			flow.getID());
    }
    public void complete(final WfFlow flow) throws Exception {
    	dao.execute(new ConnectionOperator() {
			@Override
			public void run() throws Exception {
				complete(dbConnection, flow);
			}
		});
    }
    public List<String> listIDsByTplIDs(Connection dbConnection, JSONArray jaTplIDs) throws Exception {
    	return dao.findStrList("select id from wf_flows where tpl_id in (?)", jaTplIDs);
    }
	/**
	 * 新建流程
	 * @param dbConnection
	 * @param flow
	 * @throws Exception
	 */
	public void newFlow(Connection dbConnection, WfFlow flow) throws Exception {
		dao.update(dbConnection, 
			new StringBuffer("insert into wf_flows (id, user_id, tpl_id, biz_id, start_time, status) ")
			.append("values (?, ?, ?, ?, ?, ?)")
			.toString(), 
				flow.getID(), 
				flow.getUser().getID(), 
				flow.getWfTpl().getID(), 
				flow.getBizID(), 
				flow.getStartTime(),
				flow.getStatus());
	}
	public JSONObject get(Connection dbConnection, String id) throws Exception {
		return dao.findJSONObject(dbConnection, 
				selectAllSQL().append("id = ?")
				.toString(), id);
	}
	public JSONArray findAll(Connection dbConnection, JSONArray jaFlowIDs) throws Exception {
		return dao.findJSONArray(dbConnection, 
				selectAllSQL().append("id in (?)")
				.toString(), jaFlowIDs);
	}
	public void delete(Connection dbConnection, JSONArray jaFlowIDs) throws Exception {
		dao.update(dbConnection, "delete from wf_flows where id in (?)", jaFlowIDs);
	}
	public JSONArray findAllbyTplID(String tplID) throws Exception {
		return dao.findJSONArray(
				new StringBuffer("select f.id, f.start_time, f.done_time, ")
					.append("f.status, u.name user_name, f.note from wf_flows f ")
					.append("join idps_users u on f.user_id = u.id ")
					.append("where f.tpl_id = ?")
					.toString(), tplID);
	}
}
