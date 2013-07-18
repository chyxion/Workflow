package com.shs.commons.workflow.services;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import com.shs.commons.workflow.dao.WfVarDAO;
import com.shs.commons.workflow.instance.models.WfFlow;
import com.shs.commons.workflow.models.WfVar;
import com.shs.framework.core.BaseService;
import com.shs.framework.dao.BaseDAO;
/**
 * Description:
 * User: chyxion
 * Date: 12/24/12
 * Time: 10:33 AM
 * Support: chyxion@163.com
 */
public class WfVarService extends BaseService {
    private WfVarDAO varDAO = new WfVarDAO(dao);

	public WfVarService(BaseDAO dao) {
		this.dao = dao;
	}
	public WfVarService() {
	}
	public void newVar() throws Exception {
        varDAO.newVar(params.getJSONObject("v"));
    }
	public void newVars(Connection dbConnection, Collection<WfVar> vars) throws Exception {
		if (!vars.isEmpty()) 
			varDAO.newVars(dbConnection, vars);
    }
    public void updateVar() throws Exception {
        varDAO.updateVar(params.getJSONObject("v"), params.getJSONObject("condition"));
    }
    public void deleteVars() throws Exception {
        varDAO.deleteByNamesAndTplID(params.getJSONArray("ids"), params.get("tpl_id"));
    }
    public void newVar(WfVar var) throws Exception {
    	varDAO.newVar(var);
    }
    /**
     * 查找流程下所有变量
     * @param dbConnection
     * @param flow
     * @throws Exception
     */
    public void findAll(Connection dbConnection, WfFlow flow) throws Exception {
    	// 流程变量
    	JSONArray jaVars = varDAO.findAllByOwnerID(dbConnection, flow.getID());
    	Map<String, WfVar> vars = new HashMap<String, WfVar>();
    	for (int i = 0; i < jaVars.length(); ++i) {
    		WfVar var = varDAO.getWfVarFromJO(jaVars.getJSONObject(i));
    		vars.put(var.getName(), var);
    	}
    	flow.setVars(vars);
    }
}
