package com.shs.commons.workflow.interfaces;

import java.sql.Connection;
import java.util.List;

import com.shs.commons.workflow.instance.models.WfFlow;

/**
 * @version 0.1
 * @author chyxion
 * @describe: 任务分配Action 
 * @date created: Jan 27, 2013 9:20:55 PM
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public interface IWfTaskAssignAction {
	List<String> execute(Connection dbConnection, WfFlow flow) throws Exception; 
}
