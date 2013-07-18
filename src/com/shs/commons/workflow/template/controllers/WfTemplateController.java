package com.shs.commons.workflow.template.controllers;
import com.shs.commons.workflow.template.services.WfTemplateService;
import com.shs.framework.aop.RouteMapping;
import com.shs.framework.core.BaseController;

/**
 * Description: 流程模板控制器
 * User: chyxion
 * Date: 12/18/12
 * Time: 10:28 AM
 * Support: chyxion@163.com
 * Copyright: Shenghang Soft All Right Reserved
 */
@RouteMapping(controller="workflow/template/template")
public class WfTemplateController extends BaseController {
	private static final long serialVersionUID = 1L;
	
	private WfTemplateService templateService;
    public void show() throws Exception {
    	success(templateService.show());
    }
    public void load() throws Exception {
    	success(templateService.load());
    }
    public void newTemplate() throws Exception {
    	success(templateService.newTemplate());
    }
    public void updateTemplate() throws Exception {
        templateService.updateTemplate();
        success();
    }
    public void deleteTemplates() throws Exception {
        templateService.delete();
        success();
    }
    public void listByTypeID() throws Exception {
    	success(templateService.listByTypeID());
    }
}
