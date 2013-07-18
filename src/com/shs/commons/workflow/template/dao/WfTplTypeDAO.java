package com.shs.commons.workflow.template.dao;
import com.shs.commons.workflow.template.models.WfTplType;
import com.shs.framework.dao.BaseDAO;
import com.shs.framework.dao.BaseDAO.ConnectionOperator;
import com.shs.framework.dao.BaseDAO.ResultSetOperator;

import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Description: 流程模板类型
 * User: chyxion
 * Date: 12/19/12
 * Time: 2:35 PM
 * Support: chyxion@163.com
 * Copyright: Shenghang Soft All Right Reserved
 */
public class WfTplTypeDAO {
    private static final String TABLE = "wf_tpl_types";
    private BaseDAO dao;
    public WfTplTypeDAO(BaseDAO dao) {
		this.dao = dao;
	}

	/**
     * 找到顶级类型
     * @return
     * @throws Exception
     */
    public JSONArray findTopLevelTypes() throws Exception {
        return dao.execute(new BaseDAO.ConnectionOperator() {
            @Override
            public void run() throws Exception {
                 result = findTopLevelTypes(dbConnection);
            }
        });
    }

    /**
     * 查找顶级类型
     * @param dbConnection
     * @return
     * @throws Exception
     */
    public JSONArray findTopLevelTypes(Connection dbConnection) throws Exception {
        return dao.findJSONArray(true, dbConnection, "select id, name text, status, depth, leaf from wf_tpl_types where p_id is null");
    }

    /**
     * 根据父编号查找孩子类别
     * @param pID 父编号
     * @return
     * @throws Exception
     */
    public JSONArray findAllByPID(final String pID) throws Exception {
        return dao.execute(new ConnectionOperator() {
            @Override
            public void run() throws Exception {
                result = findAllByPID(dbConnection, pID);
            }
        });
    }
    public JSONArray findAllByPID(Connection dbConnection, String pID) throws Exception {
        return dao.findJSONArray(true, dbConnection, "select id, name text, depth, leaf, status from wf_tpl_types where p_id = ?", pID);
    }
    public void save(Connection dbConnection, final JSONObject joType) throws Exception {
        dao.insert(dbConnection, TABLE, joType);
    }
    public void update(JSONObject joType, JSONObject joCondition) throws Exception {
        dao.update(TABLE, joType, joCondition);
    }

    public void delete(final String id) throws Exception {
        dao.execute(new ConnectionOperator() {
            @Override
            public void run() throws Exception {
                delete(dbConnection, id);
            }
        });
    }

    /**
     * 删除模板类别，连同旗下模板
     * @param dbConnection
     * @param id
     * @throws Exception
     */
    public void delete(Connection dbConnection, String id) throws Exception {
        dao.update(dbConnection, "delete from wf_tpl_types where id = ?", id);
    }

    /**
     * 删除类型，更新父类型为叶子
     * @param id
     * @param pID
     * @throws Exception
     */
    public void deleteAndLeafParent(final String id, final String pID) throws Exception {
        dao.executeTransaction(new ConnectionOperator() {
            @Override
            public void run() throws Exception {
                delete(dbConnection, id);
                updateToLeaf(dbConnection, pID);
            }
        });
    }

    /**
     * 更新类型为叶子
     * @param dbConnection
     * @param id
     * @throws Exception
     */
    public void updateToLeaf(Connection dbConnection, String id) throws Exception {
        dao.update(dbConnection, "update wf_tpl_types set leaf = 1 where id = ?", id);
    }
    /**
     * 更新模板类型为非叶
     * @param dbConnection
     * @param id
     */
    public void updateToNonleaf(Connection dbConnection, String id) throws Exception {
        dao.update(dbConnection, "update wf_tpl_types set leaf = 0 where id = ?", id);
    }
    public WfTplType get(Connection dbConnection, String typeID) throws Exception {
        return dao.query(dbConnection, new ResultSetOperator() {
            @Override
            public void run() throws Exception {
                if (resultSet.next())
                    result = getWfTplTypeFromResultSet(resultSet);
            }
        }, "select id, name, depth, leaf, status from wf_tpl_types where id =?", typeID);
    }
    private WfTplType getWfTplTypeFromResultSet(ResultSet rs) throws Exception {
       WfTplType typeReturn = new WfTplType();
        typeReturn.setID(rs.getString("ID"));
        typeReturn.setName(rs.getString("NAME"));
        typeReturn.setDepth(rs.getInt("DEPTH"));
        typeReturn.setLeaf("1".equals(rs.getString("LEAF")));
        typeReturn.setStatus(rs.getString("STATUS"));
        return typeReturn;
    }
}
