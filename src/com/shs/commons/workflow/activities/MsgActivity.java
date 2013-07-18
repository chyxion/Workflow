package com.shs.commons.workflow.activities;
import java.sql.Connection;
import java.util.List;

import com.shs.commons.workflow.instance.models.WfNode;
import com.shs.commons.workflow.interfaces.IWfActivity;
/**
 * @version 0.1
 * @author chyxion
 * @describe: 消息活动
 * @date created: Jan 4, 2013 10:27:12 AM
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public class MsgActivity extends IWfActivity {

	public MsgActivity(WfNode node) {
		super(node);
	}
	@Override
	public List<IWfActivity> execute(Connection dbConnection) throws Exception {
		return null;
	}

}
