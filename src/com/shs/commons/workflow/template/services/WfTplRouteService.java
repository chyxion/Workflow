package com.shs.commons.workflow.template.services;
import com.shs.commons.workflow.template.dao.WfTplRouteDAO;
import com.shs.framework.core.BaseService;
import com.shs.framework.utils.UUID;

/**
 * Description:
 * User: chyxion
 * Date: 12/24/12
 * Time: 10:37 AM
 * Support: chyxion@163.com
 * Copyright: Shenghang Soft All Right Reserved
 */
public class WfTplRouteService extends BaseService {
    private WfTplRouteDAO routeDAO = new WfTplRouteDAO(dao);
    public void deleteRoute() throws Exception {
        routeDAO.delete(params.get("ID"));
    }
    public void updateRoute() throws Exception {
        routeDAO.update(params.getJSONObject("ROUTE"), params.getJSONObject("W"));
    }
    public String newRoute() throws Exception {
        String id = UUID.get();
        routeDAO.save(params.getJSONObject("ROUTE").put("ID", id));
        return id;
    }
}
