package com.shs.commons.workflow.template.services;
import com.shs.commons.workflow.template.dao.WfTplNodeDAO;
import com.shs.commons.workflow.template.models.WfTplNode;
import com.shs.framework.core.BaseService;
import com.shs.framework.utils.UUID;

import org.json.JSONObject;

/**
 * Description:
 * User: chyxion
 * Date: 12/24/12
 * Time: 10:36 AM
 * Support: chyxion@163.com
 * Copyright: Shenghang Soft All Right Reserved
 */
public class WfTplNodeService extends BaseService {
   private WfTplNodeDAO nodeDAO = new WfTplNodeDAO(dao);
    public String newNode() throws Exception {
        String id = UUID.get();
        JSONObject joNode = params.getJSONObject("NODE").put("ID", id);
        // 申请节点，分配策略不一样
        if (joNode.getString("TYPE").equals(WfTplNode.TYPE_APPLICANT)) {
        	joNode.put("ASSIGN_POLICY", WfTplNode.TYPE_APPLICANT);
        }
        nodeDAO.save(joNode);
        return id;
    }
    public void updateNode() throws Exception {
        nodeDAO.update(params.getJSONObject("NODE"),
        		params.getJSONObject("W"));
    }

    /**
     * 删除节点,连同连线
     * @throws Exception
     */
    public void deleteNode() throws Exception {
        nodeDAO.delete(params.get("ID"));
    }
}
