package com.shs.commons.workflow.instance.controllers;
import com.shs.commons.workflow.instance.services.WfFlowService;
import com.shs.framework.aop.RouteMapping;
import com.shs.framework.core.BaseController;
/**
 * Description: 流程实例控制器
 * User: chyxion
 * Date: 12/18/12
 * Time: 10:28 AM
 * Support: chyxion@163.com
 * Copyright: Shenghang Soft All Right Reserved
 */
@RouteMapping(controller="workflow/instance/flow")
public class WfFlowController extends BaseController {
	private static final long serialVersionUID = 1L;
	private WfFlowService flowService;
    public void list() throws Exception {
    	success(flowService.list());
    }
    public void monitor() throws Exception {
    	success(flowService.monitor());
    }
}
