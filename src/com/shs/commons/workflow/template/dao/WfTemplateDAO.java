package com.shs.commons.workflow.template.dao;
import java.sql.Connection;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.shs.commons.workflow.template.models.WfTemplate;
import com.shs.framework.dao.BaseDAO;
import com.shs.framework.dao.BaseDAO.ConnectionOperator;

/**
 * Description:
 * User: chyxion
 * Date: 12/19/12
 * Time: 2:34 PM
 * Support: chyxion@163.com
 * Copyright: Shenghang Soft All Right Reserved
 */
public class WfTemplateDAO {
    public final static String TABLE = "WF_TEMPLATES";
    private BaseDAO dao;
    public WfTemplateDAO(BaseDAO dao) {
		this.dao = dao;
	}
	private StringBuffer selectAllSQL() {
    	return new StringBuffer("select id, name, status, before_action, after_action, type_id from wf_templates where ");
    }
    public JSONArray findAllByTypeID(final String typeID) throws Exception {
       return  dao.execute(new ConnectionOperator() {
           @Override
           public void run() throws Exception {
               result = findAllByTypeID(dbConnection, typeID);
           }
       });
    }
    public JSONArray findAllByTypeID(Connection dbConnection, String typeID) throws Exception {
        return dao.findJSONArray(dbConnection, 
        		selectAllSQL().append("type_id = ?")
        		.toString(), typeID);
    }
    public List<String> listIDsByTypeID(Connection dbConnection, String typeID) throws Exception {
    	return new BaseDAO().findStrList(dbConnection, "select id from wf_templates where type_id = ?", typeID);
    }
    public void save(JSONObject joTpl) throws Exception {
    	dao.insert(TABLE, joTpl);
    }
    public void save(Connection dbConnection, JSONObject joTpl) throws Exception {
    	dao.insert(dbConnection, TABLE, joTpl);
    }
    public void update(Connection dbConnection, JSONObject joTpl, JSONObject joCondition) throws Exception {
        dao.update(dbConnection, TABLE, joTpl, joCondition);
    }
    /**
     * 更新模板
     * @param joTpl
     * @param joCondition
     * @throws Exception
     */
    public void update(JSONObject joTpl, JSONObject joCondition) throws Exception {
        dao.update(TABLE, joTpl, joCondition);
    }

    /**
     * 删除模板
     * @param dbConnection 事务连接
     * @param jaIDs 模板IDs(JSONArray)
     * @throws Exception
     */
    public void delete(Connection dbConnection, JSONArray jaIDs) throws Exception {
        dao.update(dbConnection, "delete from wf_templates where id in (?)", jaIDs);
    }

    public WfTemplate getTplFromJO(JSONObject joTpl) throws Exception {
        WfTemplate tplReturn = new WfTemplate();
        tplReturn.setID(joTpl.getString("ID"));
        tplReturn.setName(joTpl.getString("NAME"));
        tplReturn.setNote(joTpl.optString("NOTE"));
        tplReturn.setBeforeAction(joTpl.optString("BEFORE_ACTION"));
        tplReturn.setAfterAction(joTpl.optString("AFTER_ACTION"));
        tplReturn.setStatus(joTpl.optString("STATUS"));
       return tplReturn;
    }
    /**
     * 根据模板编号，查找模板
     * @param dbConnection
     * @param tplID
     * @return
     * @throws Exception
     */
    public JSONObject get(Connection dbConnection, String tplID) throws Exception {
        return dao.findJSONObject(dbConnection, 
        	selectAllSQL().append("id = ?").toString(), 
        	tplID);
    }
}
