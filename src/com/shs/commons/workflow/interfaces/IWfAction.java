package com.shs.commons.workflow.interfaces;
import java.sql.Connection;
import org.apache.log4j.Logger;

import com.shs.commons.workflow.instance.models.WfFlow;
/**
 * @version 0.1
 * @author chyxion
 * @describe: 流程动作，接口
 * @date created: Dec 26, 2012 2:53:41 PM
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public interface IWfAction {
	Logger logger = Logger.getLogger(IWfAction.class);
	/**
	 * 所有结点动作类都要实现该方法。
	 * @param vars
	 */
	public void execute(Connection dbConnection, WfFlow flow) throws Exception;
}
