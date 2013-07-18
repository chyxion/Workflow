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
 * <p>Time: 11:20:13</p>
 * <p>@version 1.0</p>
 */
public class StartActivity extends IWfActivity {
    /**
     * 构造器
     * @param node
     * @param processVO
     */
    public StartActivity(WfNode node){
        super(node);
    }

	@Override
	public List<IWfActivity> execute(Connection dbConnection) throws Exception {
        // 0. 前置动作
        actionService.executeBeforeAction(dbConnection, this);
        // 1. 节点动作 
        actionService.executeAction(dbConnection, this);
        //2. 修改活动状态，完成当前活动（并实现持久化）
        nodeService.completeNode(dbConnection, wfNode);
        // 后置动作
        actionService.executeAfterAction(dbConnection, this);
        
        //3. 执行后继路由边，获取后继活动列表
        return new FlowManager().executePostTransition(dbConnection, this);
	}
}
