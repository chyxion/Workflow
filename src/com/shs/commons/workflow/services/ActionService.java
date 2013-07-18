package com.shs.commons.workflow.services;
import java.sql.Connection;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.shs.commons.workflow.instance.models.WfFlow;
import com.shs.commons.workflow.interfaces.IWfAction;
import com.shs.commons.workflow.interfaces.IWfActivity;

/**
 * <p>Title: </p>
 * <p>Description: 动作执行器,执行流程或者结点上配置的自动动作。</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: ShengHangSoft </p>
 * <p>Date: 2011-5-18</p>
 * <p>Time: 17:09:26</p>
 * <p>@version 1.0</p>
 */
public class ActionService {
	private Logger logger = Logger.getLogger(getClass());
	/**
	 * 执行结点前置动作。
	 * @param activity
	 * @throws Exception
	 */
    public void executeBeforeAction(Connection dbConnection, IWfActivity activity) throws Exception {
    	executeAction(dbConnection, activity.getWfNode().getWfFlow(), activity.getWfNode().getTplNode().getBeforeAction());
    }
    
	/**
	 * 执行结点动作。
	 * @param activity
	 * @throws Exception
	 */
    public void executeAction(Connection dbConnection, IWfActivity activity) throws Exception {
        executeAction(dbConnection, activity.getWfNode().getWfFlow(), 
        		activity.getWfNode().getTplNode().getAction());
    }
    
	/**
	 * 执行结点后置动作。
	 * @param activity
	 * @throws Exception
	 */
    public void executeAfterAction(Connection dbConnection, IWfActivity activity) throws Exception {
        	executeAction(dbConnection, activity.getWfNode().getWfFlow(), 
        			activity.getWfNode().getTplNode().getAfterAction());
    }
    

    /**
     * 执行流程前置动作。
     * @param flow
     * @throws Exception
     */
    public void executeBeforeAction(Connection dbConnection, WfFlow flow) throws Exception {
    	executeAction(dbConnection, flow, flow.getWfTpl().getBeforeAction());
    }
    
    /**
     * 执行流程后置动作。
     * @param flow
     * @throws Exception
     */
    public void executeAfterAction(Connection dbConnection, WfFlow flow) throws Exception {
    	executeAction(dbConnection, flow, flow.getWfTpl().getAfterAction());
    }
    
    /**
     * 执行动作。
     * @throws Exception
     */
    public void executeAction(Connection dbConnection, WfFlow flow, String actionName) throws Exception {
    	
        if (!StringUtils.isBlank(actionName)) {
        	logger.debug("execute action");
        	logger.debug(actionName);
			IWfAction action = (IWfAction) Class.forName(actionName).newInstance();
			//取出流程变量，传给动作方法
			action.execute(dbConnection, flow);
        }
    }
}

