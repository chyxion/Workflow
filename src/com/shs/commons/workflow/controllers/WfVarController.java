package com.shs.commons.workflow.controllers;
import com.shs.commons.workflow.services.WfVarService;
import com.shs.framework.aop.RouteMapping;
import com.shs.framework.core.BaseController;

/**
 * @version 0.1
 * @author chyxion
 * @describe: 
 * @date created: Dec 26, 2012 4:31:23 PM
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
@RouteMapping(controller="/workflow/var")
public class WfVarController extends BaseController {
	WfVarService varService;
    public void newVar() throws Exception {
        varService.newVar();
        success();
    }
    public void updateVar() throws Exception {
        varService.updateVar();
        success();
    }
    public void deleteVars() throws Exception {
        varService.deleteVars();
        success();
    }
}
