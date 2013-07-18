package com.shs.commons.models;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
/**
 * @class describe: 用户model,
 * @version 0.1
 * @date created: Apr 17, 2012 2:32:43 PM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public class User {
	/**
	 * 操作员
	 */
	public static final String TYPE_TESTER = "001";
	/**
	 * 管理员
	 */
	public static final String TYPE_MANAGER = "002";
	/**
	 * 指导老师
	 */
	public static final String TYPE_TEACHER = "003";
	/**
	 * 学生
	 */
	public static final String TYPE_STUDENT = "004";
	public static final Map<String, String> MAP_HOME_VIEW = new HashMap<String, String>();
	static {
		MAP_HOME_VIEW.put(TYPE_TESTER, "HomeOperator");
		MAP_HOME_VIEW.put(TYPE_MANAGER, "HomeAdmin");
		MAP_HOME_VIEW.put(TYPE_TEACHER, "HomeTeacher");
	}
    /**
     * 登陆号
     */
    private String id;
    /**
     * 登陆密码
     */
    private String password;
    /**
     * 姓名
     */
    private String name;
    /**
     * 角色
     */
    private List<Role> roles;
    /**
     * 单位
     */
    private List<Unit> units;
    /**
     * 单位
     */
    private Unit unit;
    /**
     * 用户类型
     */
    private String type;
    /**
     * 状态
     */
    private String status;
    /**
     * 功能
     */
    private List<String> fns;
    
    public User(String userID, String userName, 
    		List<Role> roles, List<Unit> units) {
        this.id = userID;
        this.name = userName;
        this.roles = roles;
        this.units = units;
        this.unit = units.get(0);
    }
    public User(String userID, 
    		String password,
    		String userName) {
        this.id = userID;
        this.setPassword(password);
        this.name = userName;
    }
    public User() {
    }
    public String getID() {
        return id;
    }

    public User setID(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Role> getRoles() {
        return roles;
    }
    public Role getRole() {
    	return roles.get(0);
    }
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Unit> getUnits() {
        return units;
    }
    public void setUnits(List<Unit> units) throws Exception {
    	if (units.size() < 1) {
    		throw new Exception("用户没有组织机构！");
    	}
        this.units = units;
        this.unit = units.get(0);
    }
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	public JSONObject toJO() throws Exception {
		JSONObject joUser = new JSONObject()
			.put("id", id)
			.put("type", type)
			.put("name", name);
			joUser.put("unit", unit.toJO());
		return joUser;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public String getHomeView() {
		return MAP_HOME_VIEW.get(type);
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return status;
	}
	public void setFns(List<String> fns) {
		this.fns = fns;
	}
	public List<String> getFns() {
		return fns;
	}
    public Unit getUnit() {
        return unit;
    }
	public Unit setUnitID(String unitID) {
		for (Unit u : units) {
			if (u.getID().equals(unitID)) {
				unit = u;
				break;
			}
		}
		return unit;
	}
}
