package com.shs.commons.workflow.activities;
import com.shs.commons.workflow.instance.models.WfNode;
import com.shs.commons.workflow.interfaces.IWfActivity;
import com.shs.commons.workflow.services.FlowManager;

import java.sql.Connection;
import java.util.List;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: ShengHangSoft </p>
 * <p>Date: 2011-5-16</p>
 * <p>Time: 11:21:29</p>
 * <p>@version 1.0</p>
 */
public class AutoActivity extends IWfActivity {
    /**
     * 构造器
     * @param node
     * @param processVO
     */
    public AutoActivity(WfNode node){
        super(node);
    }
    
    /**
     * 执行活动，返回后继活动列表。
     */
    public List<IWfActivity> execute(Connection dbConnection) throws Exception{
        //1. 执行节点动作
        actionService.executeBeforeAction(dbConnection, this);
        actionService.executeAction(dbConnection, this);
        actionService.executeAfterAction(dbConnection, this);
        
        //2. 完成当前活动
        nodeService.completeNode(dbConnection, wfNode);
        //3. 执行后继路由边，获取后继活动列表
        return new FlowManager().executePostTransition(dbConnection, this);
    }
}
