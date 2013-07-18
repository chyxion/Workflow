package com.shs.commons.workflow.template.controllers;
import com.shs.commons.workflow.template.services.WfTplRouteService;
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
@RouteMapping(controller="workflow/template/route")
public class WfTplRouteController extends BaseController {
    private WfTplRouteService routeService;
    public void newRoute() throws Exception{
    	success(routeService.newRoute());
    }
    public void updateRoute() throws Exception {
        routeService.updateRoute();
        success();
    }
    public void deleteRoute() throws Exception {
        routeService.deleteRoute();
        success();
    }
}
