package com.shs.commons.workflow.activities;
import org.apache.log4j.Logger;

import com.shs.commons.workflow.instance.models.WfNode;
import com.shs.commons.workflow.interfaces.IWfActivity;
import com.shs.commons.workflow.template.models.interfaces.IWfTplNode;
/**
 * @version 0.1
 * @author chyxion
 * @describe: 流程节点活动服务
 * @date created: Jan 5, 2013 1:03:07 PM
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public class ActivityService {
	private Logger logger = Logger.getLogger(getClass());
    /**
     *  分结点类型实例化活动。
     *  2011-1-25。
     * @param node
     * @param wfInstance
     * @return Activity
     */
    public IWfActivity instanceAtivity(WfNode node) throws Exception {
       IWfActivity activity;
       logger.debug(node.getTplNode().getName());
       String tplNodeType = node.getTplNode().getType();
       if(IWfTplNode.TYPE_START.equals(tplNodeType)){
    	   logger.debug("instance start acitivity");
    	   activity = new StartActivity(node);
       } else if(IWfTplNode.TYPE_END.equals(tplNodeType)){
    	   logger.debug("instance end acitivity");
    	   activity = new DoneActivity(node);
       } else if(IWfTplNode.TYPE_APPLICANT.equals(tplNodeType)){
    	   logger.debug("instance applicant acitivity");
    	   activity = new ApplicantActivity(node); // 申请任务
       } else if(IWfTplNode.TYPE_MANUAL.equals(tplNodeType)){
    	   logger.debug("instance manual acitivity");
    	   activity = new ManualActivity(node);
       } else if(IWfTplNode.TYPE_AUTO.equals(tplNodeType)){
    	   logger.debug("instance auto acitivity");
    	   activity = new AutoActivity(node);
       } else if(IWfTplNode.TYPE_EMAIL.equals(tplNodeType)){
    	   logger.debug("instance email acitivity");
    	  activity = new EmailActivity(node); 
       } else if(IWfTplNode.TYPE_MESSAGE.equals(tplNodeType)){
    	   logger.debug("instance message acitivity");
    	   activity = new MsgActivity(node);
       } else {
    	  throw new Exception("非法的节点类型！"); 
       }
       return activity;
    }
}
