package com.shs.commons.workflow.models;
import com.shs.framework.dao.BaseDAO;
import com.shs.framework.dao.BaseDAO.ResultSetOperator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @class describe: 
 * @version 0.1
 * @date created: Mar 1, 2012 2:34:34 PM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public abstract class AbstractModel {
	protected String style = "";
	protected String cssClass = "";
	
    protected static <T extends AbstractModel> T getInstanceFromResultSet(ResultSet rs) throws Exception{
        throw new Exception("must implement.");
    }
	protected static <T extends AbstractModel> T findBySQL(String sqlString, final Class<T> modelClass) throws Exception {
		return new BaseDAO().query(new ResultSetOperator() {
            @Override
            public void run() throws Exception {
                if (resultSet.next())
                    result = modelClass.getDeclaredMethod("getTplNodeFromResultSet", ResultSet.class).invoke(null, resultSet);
            }
        }, sqlString);
    }
    @SuppressWarnings("unchecked")
	protected static <T extends AbstractModel> T findByID(final String tableName, final String idName, final String idValue, final Class<T> modelClass) throws Exception {
        return (T) new BaseDAO().execute(new BaseDAO.ConnectionOperator() {
            @Override
            public void run() throws Exception {
                result = findByID(dbConnection, tableName, idName, idValue, modelClass);
            }
        });
    }
    @SuppressWarnings("unchecked")
    protected static <T extends AbstractModel> T findByID(Connection dbConnection, String tableName, String idName, String idValue, final Class<T> modelClass) throws Exception {
        return (T) new BaseDAO().query(dbConnection, new ResultSetOperator() {
            @Override
            public void run() throws Exception {
                if (resultSet.next())
                    result = modelClass.getDeclaredMethod("getTplNodeFromResultSet", ResultSet.class).invoke(null, resultSet);
            }
        }, "select * from " + tableName + " where " + idName + " = ?", idValue);
    }
    @SuppressWarnings("unchecked")
	protected static <T extends AbstractModel> List<T> findAllBySQL(String sqlString, final Class<T> modelClass) throws Exception {
    	System.out.println("Find All By SQL: " + sqlString);
        return new BaseDAO().query(new ResultSetOperator() {
			@Override
			public void run() throws Exception {
				List<T> list = new LinkedList<T>();
				while (resultSet.next())
					list.add((T) modelClass.getDeclaredMethod("getTplNodeFromResultSet", ResultSet.class).invoke(null, resultSet));
				result = list;
			}
		}, sqlString);
    }
	/**
	 * css鏍峰紡
	 * @return
	 */
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getCssClass() {
		return cssClass;
	}
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
}
