package com.shs.commons.dao;
import com.shs.commons.models.User;
import com.shs.framework.dao.BaseDAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import org.json.JSONObject;

/**
 * @class describe: 用户DAO
 * @version 0.1
 * @date created: Apr 17, 2012 3:06:52 PM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class UserDAO {
	public final static String TABLE = "IDPS_USERS";
	private BaseDAO dao;
	public UserDAO(BaseDAO dao) {
		this.dao = dao;
	}
	private StringBuffer selectAllSQL() {
		return new StringBuffer("select id, name, password, status from idps_users where ");
	}
    public static User getUserFromResultSet(ResultSet rs) throws Exception {
    	User user = new User(rs.getString("ID"), 
    		   rs.getString("PASSWORD"),
    		   rs.getString("NAME"));
    	user.setStatus(rs.getString("STATUS"));
    	return user;
    }
    public User getUserFromJO(JSONObject joUser) throws Exception {
    	if (joUser == null) throw new Exception("用户不存在！");
    	User user = new User(joUser.getString("ID"), 
    		   joUser.getString("PASSWORD"),
    		   joUser.getString("NAME"));
    	user.setStatus(joUser.getString("STATUS"));
    	return user;
    }
    public JSONObject findByUserID(Connection dbConnection, String userID) throws Exception {
    	return dao.findJSONObject(dbConnection, 
    			selectAllSQL().append("id = ?")
    			.toString(), userID);
    }
    /**
     * 查询用户功能
     * @param dbConnection
     * @param userID
     * @return
     * @throws Exception
     */
    public List<String> findAllFns(Connection dbConnection, String userID) throws Exception {
    	return dao.findStrList(dbConnection, 
    			new StringBuffer("select distinct f.f_value from idps_user_role ur ")
    				.append("join idps_role r on ur.f_roleid = r.f_id ")
    				.append("join idps_role_function rf on r.f_id = rf.f_roleid ")
    				.append("join idps_function f on rf.f_fnid = f.f_id ")
    				.append("where ur.f_userid = ?")
    				.toString(), userID);
    }
}
