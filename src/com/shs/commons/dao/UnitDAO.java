package com.shs.commons.dao;
import com.shs.commons.models.Unit;
import com.shs.framework.dao.BaseDAO;

import java.sql.Connection;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @class describe: 组织机构DAO
 * @version 0.1
 * @date created: Sep 25, 2012 9:40:15 AM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class UnitDAO {
	public static final String TABLE = "idps_units";
	private BaseDAO dao;
	public UnitDAO(BaseDAO dao) {
		this.dao = dao;
	}
	private StringBuffer selectAllSQL() {
		return new StringBuffer("select u.id, u.name, u.depth, u.leaf, u.status, u.short_name, u.type from idps_units u ");
	}
    public JSONObject findByID(Connection dbConnection, String id) throws Exception {
        return dao.findJSONObject(dbConnection,
        	selectAllSQL().append("where u.id = ?")
        	.toString(), 
        	id);
    }
    public JSONArray findAllByUserID(Connection dbConnection, String userID) throws Exception {
    	return dao.findJSONArray(dbConnection, 
    			selectAllSQL()
    			.append("join idps_user_unit uu on u.id = uu.f_unitid ")
    			.append("where uu.f_userid = ?")
    			.toString(), userID);
    }
    public static Unit getUnitFromResultSet(ResultSet rs) throws Exception {
        return new Unit(rs.getString("ID"),
                rs.getString("NAME"),
                rs.getString("SHORT_NAME"),
                rs.getInt("DEPTH"), 
                rs.getString("LEAF").equals("1"), 
                rs.getString("STATUS")).setType(rs.getString("TYPE"));
    }
    public Unit getUnitFromJO(JSONObject joUnit) throws Exception {
        return new Unit(joUnit.getString("ID"),
                joUnit.getString("NAME"),
                joUnit.optString("SHORT_NAME"),
                joUnit.getInt("DEPTH"), 
                joUnit.getString("LEAF").equals("1"), 
                joUnit.getString("STATUS")).setType(joUnit.getString("TYPE"));
    }
}
