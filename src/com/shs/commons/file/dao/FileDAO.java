package com.shs.commons.file.dao;
import java.sql.Connection;

import com.shs.commons.file.services.FileService;
import com.shs.framework.dao.BaseDAO;
import com.shs.framework.dao.BaseDAO.ConnectionOperator;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Description: 附件DAO
 * User: chyxion
 * Date: 12/24/12
 * Time: 5:13 PM
 * Support: chyxion@163.com
 * Copyright: Shenghang Soft All Right Reserved
 */
public class FileDAO {
	private BaseDAO dao;
	public FileDAO(BaseDAO dao) {
		super();
		this.dao = dao;
	}
	public final static String TABLE = "IDPS_FILES";

	private StringBuffer selectAllSQL() {
		return new StringBuffer("select id, id0, name, owner_id, type, ")
					.append("content_type, file_size, directory from idps_files where ");
	}
    public JSONObject get(final String id) throws Exception {
    	return dao.execute(new ConnectionOperator() {
			@Override
			public void run() throws Exception {
				result = get(dbConnection, id);
			}
		});
    }
    public JSONObject get(Connection dbConnection, String id) throws Exception {
        return dao.findJSONObject(dbConnection, selectAllSQL().append("id = ?").toString(), id);
    }
    public JSONArray findAll(Connection dbConnection, JSONArray jaIDs) throws Exception {
        return dao.findJSONArray(dbConnection, selectAllSQL().append("id in (?)").toString(), jaIDs);
    }
    public JSONArray findAllByID0s(Connection dbConnection, JSONArray jaID0s) throws Exception {
        return dao.findJSONArray(dbConnection, selectAllSQL().append("id0 in (?)").toString(), jaID0s);
    }
    public int delete(Connection dbConnection, String fileID) throws Exception {
    	return dao.update(dbConnection, "delete from idps_files where id = ?", fileID);
    }
	public void newFile(JSONObject joFile) throws Exception {
		dao.insert(TABLE, joFile);
	}
	public JSONArray findAllByOwnerID(String ownerID) throws Exception {
		return dao.findJSONArray(selectAllSQL()
				.append("owner_id = ?")
				.toString(), ownerID);
	}
	public JSONArray findAllByOwnerID(Connection dbConnection, String ownerID) throws Exception {
		return dao.findJSONArray(dbConnection, 
				selectAllSQL()
				.append("owner_id = ?")
				.append("and type = ?")
				.toString(), ownerID, FileService.TYPE_ATTACH);
	}
}
