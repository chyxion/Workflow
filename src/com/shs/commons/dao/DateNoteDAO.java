package com.shs.commons.dao;

import java.sql.Connection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.shs.framework.dao.BaseDAO;
import com.shs.framework.dao.BaseDAO.ConnectionOperator;

/**
 * @version 0.1
 * @author chyxion
 * @describe: 
 * @date created: Jan 20, 2013 9:59:05 AM
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public class DateNoteDAO {
	public static final String TABLE = "idps_datenotes";
	private BaseDAO dao;
	public DateNoteDAO(BaseDAO dao) {
		this.dao = dao;
	}
	public void newNote(JSONObject joNote) throws Exception {
		dao.insert(TABLE, joNote);
	}
	public void updateNote(JSONObject joNote, JSONObject joCondition) throws Exception {
		dao.update(TABLE, joNote, joCondition);
	}
	public void deleteNote(Connection dbConnnection, String userID, String day) throws Exception {
		dao.update(dbConnnection, "delete from idps_datenotes where user_id = ? and day = ?", userID, day);
	}
	public JSONArray list(final String userID, final String startDay, final String endDay) throws Exception {
		return dao.execute(new ConnectionOperator() {
			@Override
			public void run() throws Exception {
				result = list(dbConnection, userID, startDay, endDay);
			}
		});
	}
	public List<String> list(Connection dbConnection, String userID, String startDay, String endDay) throws Exception {
		return dao.findStrList(dbConnection, 
				"select day from idps_datenotes where user_id = ? and day between ? and ?", 
				userID, startDay, endDay);
	}
	public String load(String userID, String day) throws Exception {
		return dao.findStr(
				"select note from idps_datenotes where user_id = ? and day = ?", 
				userID, day);
	}
}
