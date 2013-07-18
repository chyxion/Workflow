package com.shs.commons.workflow.template.controllers;
import com.shs.commons.workflow.template.services.WfTplTypeService;
import com.shs.framework.aop.RouteMapping;
import com.shs.framework.core.BaseController;

/**
 * Description:
 * User: chyxion
 * Date: 12/23/12
 * Time: 7:09 PM
 * Support: chyxion@163.com
 * Copyright: Shenghang Soft All Right Reserved
 */
@RouteMapping(controller="workflow/template/type")
public class WfTplTypeController extends BaseController {
    private WfTplTypeService typeService;
    public void newType() throws Exception {
    	success(typeService.newType());
    }
    public void updateType() throws Exception {
        typeService.updateType();
        success();
    }
    public void deleteType() throws Exception {
        typeService.deleteType();
        success();
    }
    public void loadTypesTree() throws Exception {
    	success(typeService.loadTypesTree());
    }
}
