package com.shs.commons.workflow.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.shs.commons.workflow.models.WfVar;
import com.shs.framework.dao.BaseDAO;
import com.shs.framework.dao.BaseDAO.ConnectionOperator;

/**
 * @version 0.1
 * @author chyxion
 * @describe: 流程变量DAO
 * @date created: Dec 26, 2012 4:16:50 PM
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public class WfVarDAO {
	private BaseDAO dao;
    public WfVarDAO(BaseDAO dao) {
		this.dao = dao;
	}
	public final static String TABLE = "wf_vars";
	public void newVar(final WfVar var) throws Exception {
		dao.execute(new ConnectionOperator() {
			@Override
			public void run() throws Exception {
				newVar(var);
			}
		});
	}
	public void newVar(Connection dbConnection, WfVar wfVar) throws Exception {
		dao.update(dbConnection, 
			"insert into wf_vars (owner_id, name, value, precision, type, note) values (?, ?, ?, ?, ?, ?)", 
			new Object[]{wfVar.getOwerID(), wfVar.getName(), wfVar.getValue(), wfVar.getPrecision(),
				wfVar.getType(), wfVar.getNote()});
	}
    public void newVar(JSONObject joVar) throws Exception {
       dao.insert(TABLE, joVar);
    }
    public void newVar(Connection dbConnection, JSONObject joTplVar) throws Exception {
        dao.insert(dbConnection, TABLE, joTplVar);
    }
    public void updateVar(JSONObject joTplVar, JSONObject joCondition) throws Exception {
        dao.update(TABLE, joTplVar, joCondition);
    }
    public void delete(String id) throws Exception {
        dao.update("delete from wf_vars where id = ?", id);
    }
    public void delete(Connection dbConnection, String id) throws Exception {
       dao.update(dbConnection, "delete from wf_vars where name = ?", id);
    }
    public void deleteByNamesAndOwnerID(Connection dbConnection, JSONArray jaNames, String ownerID) throws Exception {
        dao.update(dbConnection, "delete from wf_vars where owner_id = ? and name in (?)", new Object[]{ownerID, jaNames});
    }
    public void deleteByNamesAndTplID(final JSONArray jaNames, final String tplID) throws Exception {
        dao.execute(new ConnectionOperator() {
            @Override
            public void run() throws Exception {
                deleteByNamesAndOwnerID(dbConnection, jaNames, tplID);
            }
        });
    }
    public void deleteByOwnerID(String ownerID) throws Exception {
        dao.update("delete from wf_vars where owner_id = ?", ownerID);
    }
    public void deleteByOwnerID(Connection dbConnection, String ownerID) throws Exception {
        dao.update(dbConnection, "delete from wf_vars where owner_id = ?", ownerID);
    }
    public void deleteByOwnerIDs(Connection dbConnection, JSONArray jaOwnerIDs) throws Exception {
        dao.update(dbConnection, "delete from wf_vars where owner_id in (?)", jaOwnerIDs);
    }
    /**
     * 从ResultSet中创建读取生成var对象
     * @param rs
     * @return
     * @throws Exception
     */
    public WfVar getWfVarFromResultSet(ResultSet rs) throws Exception {
        return new WfVar(rs.getString("OWNER_ID"), 
        		rs.getString("NAME"), 
        		rs.getString("VALUE"), 
        		rs.getString("TYPE"), 
        		rs.getInt("PRECISION"), 
        		rs.getString("NOTE"));
    }

    public WfVar getWfVarFromJO(JSONObject joVar) throws Exception {
        return new WfVar(joVar.getString("OWNER_ID"), 
        		joVar.getString("NAME"), 
        		joVar.getString("VALUE"), 
        		joVar.getString("TYPE"), 
        		joVar.optInt("PRECISION"), 
        		joVar.optString("NOTE"));
    }
    /**
     * 根据模板ID查找全部
     * @param dbConnection
     * @param ownerID
     * @return
     * @throws Exception
     */
    public JSONArray findAllByOwnerID(Connection dbConnection, String ownerID) throws Exception {
        return dao.findJSONArray(dbConnection, 
        		"select owner_id, name, value, type, precision, note from wf_vars where owner_id = ?", ownerID);
    }
	public void newVars(Connection dbConnection, Collection<WfVar> vars) throws Exception {
		for (WfVar var : vars) {
			newVar(dbConnection, var);
		}
	}
	public void updateValue(Connection dbConnection, WfVar var) throws Exception {
		dao.update(dbConnection, 
				"update wf_vars set value = ? where name = ? and owner_id = ?", 
				new Object[]{var.getValue(), var.getName(), var.getOwerID()});
	}
}
