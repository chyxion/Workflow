package com.shs.commons.services;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.shs.commons.dao.RoleDAO;
import com.shs.commons.dao.UnitDAO;
import com.shs.commons.dao.UserDAO;
import com.shs.commons.models.Role;
import com.shs.commons.models.Unit;
import com.shs.commons.models.User;
import com.shs.framework.core.BaseService;
import com.shs.framework.core.ExtStore;

/**
 * @class describe: 用户服务类
 * @version 0.1
 * @date created: Apr 17, 2012 5:35:48 PM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public class UserService extends BaseService {
	private UserDAO userDAO = new UserDAO(dao);
	private UnitDAO unitDAO = new UnitDAO(dao);
	private RoleDAO roleDAO = new RoleDAO(dao);
	
    public ExtStore list() throws Exception {
        return new ExtStore(dao, params) {
            @Override
            protected void run() throws Exception {
                StringBuffer sbSQL = new StringBuffer("select * from (")
				        .append("select t.login_id, t.user_name, t.gender, t.email, ")
				        .append("t.unit_id, t.phone,, c.role_id, ")
				        .append("row_number() over (order by t.login_id) rn ")
				        .append("from idps_users) ")
				        .append("where rn between ? and ? ");
                // 如果传入查询过滤参数
                String p = params.get("user_name");
                if (p != null && !p.equals("")) {
                    addValue(0).addValue(100); // 返回100条符合条件
                    and("t.user_name", "like",
                            "user_name"); // 注意，这里是参数的名字，不是值
                } else { // 翻页参数
                    int start = params.getInt("start");
                    addValue(start);
                    addValue(start + params.getInt("limit"));
                }
                sbSQL.append("order by rn");
                setSQL(sbSQL);
            }
        };
    }
    public User get(Connection dbConnection, String userID) throws Exception {
    	JSONObject joUser = userDAO.findByUserID(dbConnection, userID);
    	User user = userDAO.getUserFromJO(joUser);
    	JSONArray jaUnits = unitDAO.findAllByUserID(dbConnection, userID);
    	List<Unit> units = new LinkedList<Unit>();
    	for (int i = 0; i < jaUnits.length(); ++i) {
    		units.add(unitDAO.getUnitFromJO(jaUnits.getJSONObject(i)));
    	}
    	user.setUnits(units);
    	JSONArray jaRoles = roleDAO.findAllByUserID(dbConnection, userID);
    	List<Role> roles = new LinkedList<Role>();
    	for (int i = 0; i < jaRoles.length(); ++i) {
    		roles.add(roleDAO.getRoleFromJO(jaRoles.getJSONObject(i)));
    	}
    	user.setRoles(roles);
    	// 功能
    	user.setFns(userDAO.findAllFns(dbConnection, userID));
    	return user;
    }
	public List<User> findAllByRoleID(Connection dbConnections, String roleID) {
		return null;
	}
	public JSONArray findAll() throws Exception {
		return dao.findJSONArray("select * from idps_users");
	}
}
