package com.shs.commons.workflow.template.services;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.shs.commons.workflow.template.dao.WfTemplateDAO;
import com.shs.commons.workflow.template.dao.WfTplTypeDAO;
import com.shs.framework.core.BaseService;
import com.shs.framework.dao.BaseDAO.ConnectionOperator;
import com.shs.framework.utils.UUID;
/**
 * Description: 模板类型
 * User: chyxion
 * Date: 12/23/12
 * Time: 7:10 PM
 * Support: chyxion@163.com
 * Copyright: Shenghang Soft All Right Reserved
 */
public class WfTplTypeService extends BaseService {
    private WfTplTypeDAO typeDAO = new WfTplTypeDAO(dao);
    private WfTemplateDAO tplDAO = new WfTemplateDAO(dao);
    private WfTemplateService tplService = new WfTemplateService();
    public String newType() throws Exception {
    	return dao.executeTransaction(new ConnectionOperator() {
			@Override
			public void run() throws Exception {
		        String id = UUID.get();
		        JSONObject joType =
                params.getJSONObject("TYPE")
                .put("ID", id)
                .put("LEAF", 1)
                .put("STATUS", "Y"); // 新建，状态始终有效
		        // 更新父节点为非叶子节点
		        if (params.getBoolean("UPDATE_PARENT")) {
	               typeDAO.updateToNonleaf(dbConnection, joType.getString("P_ID"));
		        } 
		        // 保存
               typeDAO.save(dbConnection, joType);
               // 返回生成ID
               result = id;
			}
		
		});
    }
    /**
     * 更新模板类型
     * @throws Exception
     */
    public void updateType() throws Exception {
       typeDAO.update(params.getJSONObject("TYPE"), 
    		   params.getJSONObject("CONDITION"));
    }
    /**
     * 删除模板类型
     * @throws Exception
     */
    public void deleteType() throws Exception {
    	dao.executeTransaction(new ConnectionOperator() {
			@Override
			public void run() throws Exception {
		        String tplTypeID = params.get("ID");
                
                // 查询模板类型下的所有模板
                List<String> tplIDs = tplDAO.listIDsByTypeID(dbConnection, tplTypeID);
                if (tplIDs.size() > 0) {
                	tplService.delete(dbConnection, new JSONArray(tplIDs));
                }
		        // 删除流程类型
	            typeDAO.delete(dbConnection, tplTypeID);
	            // 如果需要更新父节点
		        String pID = params.get("P_ID");
		        if (!StringUtils.isBlank(pID)) {
	                // 更新父节点为叶子节点
	                typeDAO.updateToLeaf(dbConnection, pID);
		        } 
			}
		});
    }
    /**
     * 加载模板类型数
     * @return <code>JSONArray</code>
     * @throws Exception
     */
    public JSONArray loadTypesTree() throws Exception {
    	String nodeID = params.get("node");
        return StringUtils.isEmpty(nodeID) ? null : typeDAO.findAllByPID(nodeID);
    }
}
