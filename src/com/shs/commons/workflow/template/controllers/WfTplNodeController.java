package com.shs.commons.workflow.template.controllers;
import com.shs.commons.workflow.template.services.WfTplNodeService;
import com.shs.framework.aop.RouteMapping;
import com.shs.framework.core.BaseController;

/**
 * Description:
 * User: chyxion
 * Date: 12/23/12
 * Time: 7:17 PM
 * Support: chyxion@163.com
 * Copyright: Shenghang Soft All Right Reserved
 */
@RouteMapping(controller="workflow/template/node")
public class WfTplNodeController extends BaseController {
	private static final long serialVersionUID = 1L;
	
	private WfTplNodeService nodeService;
    public void newNode() throws Exception {
    	success(nodeService.newNode());
    }
    public void updateNode() throws Exception {
        nodeService.updateNode();
        success();
    }
    public void deleteNode() throws Exception {
        nodeService.deleteNode();
        success();
    }
}
