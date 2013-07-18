package com.shs.commons.workflow.activities;
import java.sql.Connection;
import java.util.List;

import com.shs.commons.workflow.instance.dao.WfNodeDAO;
import com.shs.commons.workflow.instance.models.WfNode;
import com.shs.commons.workflow.instance.models.WfTask;
import com.shs.commons.workflow.interfaces.IWfActivity;
import com.shs.framework.dao.BaseDAO;
/**
 * 人工结点活动
 */
public class ManualActivity extends IWfActivity {
    /**
     * 构造器
     * @param node
     * @param processVO
     */
    public ManualActivity(WfNode node) {
        super(node);
    }
    
    /**
     * 执行活动，分配人工任务。
     * 2011-1-25
     */
    public List<IWfActivity> execute(Connection dbConnection) throws Exception {
        
    	List<IWfActivity> postActivityList = null;
        // 1. 节点正在运行
        if (WfNode.STATUS_RUNNING.equals(wfNode.getStatus())) { // 分配任务，改变节点状态为等待
            //0 执行结点前置动作
            actionService.executeBeforeAction(dbConnection, this);
    		//1.1 分配任务
            List<WfTask> taskList = taskManager.assignTasks(dbConnection, this);
            logger.debug("new tasks");
            //1.2 持久化任务
            taskManager.newTasks(dbConnection, taskList);
            logger.debug("update node status");
            //1.3 改变活动状态并持久化
            wfNode.setStatus(WfNode.STATUS_WAITING);
            new WfNodeDAO(new BaseDAO()).updateNodeStatus(dbConnection, this.getWfNode());
        } else { // 2. 节点已经处理过
	        // 如果节点所有任务都处理过
	        if(wfNode.isAllTasksDealt()) {
		        // 2.0 修改活动状态，完成当前活动
		        nodeService.completeNode(dbConnection, wfNode);
		        // 2.1 执行结点后置动作
		        actionService.executeAfterAction(dbConnection, this);
		        // 该节点执行回滚动作
		        if (rollback) {
		        	postActivityList = postActivities; // 返回回滚后继活动
		        } else {
		        	//2.3 执行后继路由边，获取后继活动列表
		        	postActivityList = flowManager.executePostTransition(dbConnection, this);
		        }
	        }
        }
        return postActivityList;
    }
}
