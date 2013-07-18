package com.shs.commons.workflow.activities;
import java.sql.Connection;
import java.util.List;

import com.shs.commons.workflow.instance.dao.WfNodeDAO;
import com.shs.commons.workflow.instance.models.WfNode;
import com.shs.commons.workflow.instance.models.WfTask;
import com.shs.commons.workflow.instance.services.WfTaskService;
import com.shs.commons.workflow.interfaces.IWfActivity;
import com.shs.commons.workflow.template.models.interfaces.IWfTplNode;
import com.shs.framework.dao.BaseDAO;
/**
 * @version 0.1
 * @author chyxion
 * @describe: 
 * @date created: Jan 4, 2013 5:28:23 PM
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public class ApplicantActivity extends IWfActivity {

	public ApplicantActivity(WfNode node) {
		super(node);
	}

	@Override
	public List<IWfActivity> execute(Connection dbConnection) throws Exception {
    	List<IWfActivity> postActivityList = null;
        // 1. 节点正在运行，分配任务
        if (WfNode.STATUS_RUNNING.equals(wfNode.getStatus())) {
            //0 执行结点前置动作
            actionService.executeBeforeAction(dbConnection, this);
    		//1.1 分配任务
            WfTask task = taskManager.createTaskForApplicant(this);
            //1.2 持久化任务
            logger.debug("new task for applicant");
            taskManager.newTask(dbConnection, task);
            //1.3 改变活动状态并持久化
            wfNode.setStatus(WfNode.STATUS_WAITING);
            new WfNodeDAO(new BaseDAO()).updateNodeStatus(dbConnection, this.getWfNode());
            // 如果前置节点是开始节点，自动完成任务
            if(wfNode.getBeforeNode().getTplNode().getType().equals(IWfTplNode.TYPE_START)) {
		        //自动接受并提交申请任务
		    	WfTaskService taskService = new WfTaskService();
		        //接受任务
            	logger.debug("Auto Accept Apply Task");
		        taskService.accept(dbConnection, task);
		        //提交任务
		        task.setOpinion("提出申请");
		        logger.debug("Auto Complete Apply Task");
		        taskService.commit(dbConnection, task); // 没有流程变量
            }
        } else { // 节点已经处理过
	        //2.1 执行结点后置动作
	        actionService.executeAfterAction(dbConnection, this);
	        //2.2 修改活动状态，完成当前活动
	        logger.debug("Applicant Complete Node");
	        nodeService.completeNode(dbConnection, wfNode);
	        //2.3 执行后继路由边，获取后继活动列表
	        logger.debug("Execute Apply Post Nodes");
	        postActivityList = flowManager.executePostTransition(dbConnection, this);
        }
        return postActivityList;
	}
}
