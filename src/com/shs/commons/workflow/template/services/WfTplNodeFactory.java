package com.shs.commons.workflow.template.services;

import com.shs.commons.workflow.template.models.derived.WfTplNodeAuto;
import com.shs.commons.workflow.template.models.derived.WfTplNodeEnd;
import com.shs.commons.workflow.template.models.derived.WfTplNodeManual;
import com.shs.commons.workflow.template.models.derived.WfTplNodeStart;
import com.shs.commons.workflow.template.models.interfaces.IWfTplNode;

/**
 * 流程模板结点工程类，负责实例化流程模板结点。
 * @author admin
 */
public class WfTplNodeFactory {

	/**
	 * 实例化模板结点。
	 * @param pv
	 * @return
	 */
	public static IWfTplNode instanceNode(String type) {
		IWfTplNode node = null;
		if (IWfTplNode.TYPE_START.equals(type)) {
			IWfTplNode startNode = new WfTplNodeStart();
			node=startNode;
		}
		if (IWfTplNode.TYPE_END.equals(type)) {
			WfTplNodeEnd downNode = new WfTplNodeEnd();
			node=downNode;
		}
		if (IWfTplNode.TYPE_MANUAL.equals(type)) {
			//实例化个性结点对象，目的是实现个性化赋值
			WfTplNodeManual manulNode = new WfTplNodeManual();
			//为个性化结点对象赋值,分配策略
//			manulNode.setAssignStratige(pv.get("F_ASGN"));
//			manulNode.setTaskType(pv.get("F_TASK"));
//			manulNode.setLimitTime(pv.get("F_TIME"));
			//将个性化结点对象赋给通用接口
			node=manulNode;
		}
		if (IWfTplNode.TYPE_AUTO.equals(type)) {
			WfTplNodeAuto autoNode = new WfTplNodeAuto();
			node=autoNode;
		}
		if (IWfTplNode.TYPE_EMAIL.equals(type)) {
			// TODO
		}
		if (IWfTplNode.TYPE_MESSAGE.equals(type)) {
			// TODO
		}
		//公共属性赋值
//		node.setId(pv.get("F_NDID"));
//		node.setName(pv.get("F_NAME"));
//		node.setBeforeAction(pv.get("F_PACTN"));
//		node.setAction(pv.get("F_ACTN"));
//		node.setAfterAction(pv.get("F_EACTN"));
//		node.setNodeType(pv.get("F_TYPE"));
		return node;
	}
}
