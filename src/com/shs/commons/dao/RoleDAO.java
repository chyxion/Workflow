package com.shs.commons.dao;
import java.sql.Connection;
import org.json.JSONArray;
import org.json.JSONObject;

import com.shs.commons.models.Role;
import com.shs.framework.dao.BaseDAO;

/**
 * @version 0.1
 * @author chyxion
 * @describe: 
 * @date created: Dec 29, 2012 3:34:47 PM
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class RoleDAO {
	public final static String TABLE = "IDPS_ROLES";
	private BaseDAO dao;
	public RoleDAO(BaseDAO dao) {
		this.dao = dao;
	}
	public Role getRoleFromJO(JSONObject joRole) throws Exception {
		return new Role(joRole.getString("ID"),
				joRole.getString("NAME"));
	}
	public JSONArray findAllByUserID(Connection dbConnection, String userID) throws Exception {
		return dao.findJSONArray(dbConnection, 
				new StringBuffer("select r.f_id id, r.f_name name from idps_role r ")
				.append("join idps_user_role ur on r.f_id = ur.f_roleid ")
				.append("where ur.f_userid = ?")
				.toString(), userID);
	}
	public JSONObject findByID(Connection dbConnection, String id) throws Exception {
		return dao.findJSONObject(dbConnection, 
				"select id, name from idps_roles where id = ?", id);
	}
}
