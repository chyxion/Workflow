package com.shs.commons.workflow.activities;

import java.sql.Connection;
import java.util.List;

import com.shs.commons.workflow.instance.models.WfNode;
import com.shs.commons.workflow.interfaces.IWfActivity;

public class EmailActivity extends IWfActivity {
	public EmailActivity(WfNode node) {
		super(node);
	}
	@Override
	public List<IWfActivity> execute(Connection dbConnection) throws Exception {
		return null;
	}

}
