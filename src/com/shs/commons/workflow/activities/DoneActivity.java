package com.shs.commons.workflow.activities;
import java.sql.Connection;
import java.util.List;

import com.shs.commons.workflow.instance.dao.WfFlowDAO;
import com.shs.commons.workflow.instance.models.WfFlow;
import com.shs.commons.workflow.instance.models.WfNode;
import com.shs.commons.workflow.interfaces.IWfActivity;
import com.shs.framework.dao.BaseDAO;
import com.shs.framework.utils.DateUtils;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: ShengHangSoft </p>
 * <p>Date: 2011-5-16</p>
 * <p>Time: 11:20:29</p>
 * <p>@version 1.0</p>
 */
public class DoneActivity  extends IWfActivity {
    /**
     * 构造器
     * @param node
     * @param processVO
     */
    public DoneActivity (WfNode node){
        super(node);
    }
    
    /**
     * 执行活动，结束流程。
     */
    public List<IWfActivity> execute(Connection dbConnection) throws Exception {
        // 0. 前置动作
        actionService.executeBeforeAction(dbConnection, this);
        // 1. 节点动作
        actionService.executeAction(dbConnection, this);
        // 2. 修改活动状态，完成当前活动
        nodeService.completeNode(dbConnection, wfNode);
        // 3. 后置动作
        actionService.executeAfterAction(dbConnection, this);
        // 4. 结束流程,并持久化
        WfFlow flow = wfNode.getWfFlow();
        flow.setDoneTime(DateUtils.now14());
        flow.setStatus(WfFlow.STATUS_COMPLETED);
        flow.setNote("正常完成");
        new WfFlowDAO(new BaseDAO()).complete(dbConnection, flow);
        // 5. 执行流程结束回调类
        actionService.executeAfterAction(dbConnection, flow);
        // 6. 给申请人发消息
        //systemMsgService.newMsg(dbConnection, flow.getUser().getID(), "流程消息", "您提交的申请[" + flow.getWfTpl().getName() + "]已经完成流程.");
        // 7. 结束结点无后继结点，返回 Null
        return null;
    }
}
