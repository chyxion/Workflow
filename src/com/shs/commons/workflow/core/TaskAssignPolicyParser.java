package com.shs.commons.workflow.core;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

import com.shs.commons.models.User;
import com.shs.commons.services.UserService;
import com.shs.commons.workflow.instance.models.WfTask;
import com.shs.commons.workflow.interfaces.IWfActivity;
import com.shs.commons.workflow.interfaces.IWfTaskAssignAction;
import com.shs.framework.utils.DateUtils;
import com.shs.framework.utils.UUID;

/**
 * 人工任务分配解析器。
 * @author admin
 */
public class TaskAssignPolicyParser {
	private Logger logger = Logger.getLogger(getClass());
    /**
     * 常量，流程发起人（即申请人）
     */
    public static final String POLICY_APPLICANT = "APPLICANT";

    /**
     * 按用户分配，直接指定用户列表
     */
    public static final String POLICY_USER = "USER";
    /**
     * 按角色分配，直接指定角色
     */
    public static final String POLICY_ROLE = "ROLE";
    /**
     * 函数动作调用
     */
    public static final String POLICY_ACTION = "ACTION";
    /**
     * 用户ID分隔符
     */
    public static final String POLICY_SEPARATOR = ",";
    /**
     * 按用户分配。
     * USER(001,002,003)
     * @param activity 活动
     * @param policy   分配策略
     * @return
     */
    public List<WfTask> parseTaskForUsers(Connection dbConnection, IWfActivity activity, String policy) throws Exception {
        // 解析出用户列表
        int indexOfLeftBrace = policy.indexOf("(");
        int indexOfRightBrace = policy.indexOf(")");
        //用户ID数组
        String usrIDs = policy.substring(indexOfLeftBrace + 1, indexOfRightBrace);
        List<User> users = new LinkedList<User>();
        UserService userService = new UserService();
        logger.debug("assign tasks for users");
        for (String userID : usrIDs.split(POLICY_SEPARATOR)) {
        	logger.debug(userID);
            users.add(userService.get(dbConnection, userID));
        }
        return assignTasksToUsers(dbConnection, activity, users);
    }

    /**
     * 按角色分配，即ROLE('ZCKZ','ZCCZ')。
     * @param activity 活动
     * @param expression   分配策略
     * @return
     */
    public List<WfTask> parseTasksForRoles(Connection dbConnection, IWfActivity activity, String expression) throws Exception {
    	logger.debug("assign tasks to roles");
    	logger.debug(expression);
        //解析角色
        int indexOfLeftBrace = expression.indexOf("(");
        int indexOfRightBrace = expression.indexOf(")");
        //角色ID列表
        String roleIDs = expression.substring(indexOfLeftBrace + 1, indexOfRightBrace);
        String[] roleIDArray = roleIDs.split(POLICY_SEPARATOR);
        //根据角色列表，获取用户列表
        List<User> userList = findAllUsersByRoleIDs(dbConnection, roleIDArray);
        //根据用户列表构造任务
        return assignTasksToUsers(dbConnection, activity, userList);
    }

    /**
     * 根据角色，解析用户。
     * @param roleIDs
     * @return
     */
    private List<User> findAllUsersByRoleIDs(Connection dbConnections, String[] roleIDs) throws Exception {
        List<User> users = new LinkedList<User>();
        UserService userService = new UserService();
        for (String roleID : roleIDs) {
            users.addAll(userService.findAllByRoleID(dbConnections, roleID));
		}
        return users;

    }

    /**
     * 根据用户ID数组，构造用户任务列表。
     * @param activity
     * @return
     */
    private List<WfTask> assignTasksToUsers(Connection dbConnection, IWfActivity activity, List<User> users) throws Exception {
    	logger.debug("assign tasks to users");
        List<WfTask> tasks = new LinkedList<WfTask>();
        String now14 = DateUtils.now14();
        for (User user : users) {
            WfTask task = new WfTask();
            task.setID(UUID.get());
            task.setAssignTime(now14);
            task.setNode(activity.getWfNode());
            task.setUser(user);
            task.setStatus(WfTask.STATUS_ASSIGNED);
            tasks.add(task);
        }
        return tasks;
    }

	public List<WfTask> parseTasksByAction(Connection dbConnection,
			IWfActivity activity, String assignPolicy) throws Exception {
        //角色ID列表
        //根据角色列表，获取用户列表
        IWfTaskAssignAction aa = (IWfTaskAssignAction) 
        	Class.forName(
        			assignPolicy
        			.substring(assignPolicy.indexOf("(") + 1, 
        					assignPolicy.indexOf(")")))
        		.newInstance();
        List<User> users = new LinkedList<User>();
        for (String userID : aa.execute(dbConnection, activity.getWfNode().getWfFlow())) {
        	users.add(new User().setID(userID));
		}
        //根据用户列表构造任务
        return assignTasksToUsers(dbConnection, activity, users);
	}
}
