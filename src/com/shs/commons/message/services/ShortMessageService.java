package com.shs.commons.message.services;

import java.sql.Connection;
import java.util.List;

import com.shs.framework.core.BaseService;
import com.shs.framework.dao.BaseDAO;

import org.apache.commons.lang.StringUtils;

/**
 * @version 0.1
 * @author chyxion
 * @describe: 短信服务
 * @date created: Dec 10, 2012 11:11:28 AM
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public class ShortMessageService extends BaseService {

	/**
	 * 发送短信
	 * @param msg String 短信内容
	 * @param phones List<String> 号码列表 
	 * @return 发送结果
	 * @throws Exception
	 */
	public boolean send(String msg, List<String> phones) throws Exception {
		Connection dbConnection = null;
        try{
            send(dbConnection, msg, StringUtils.join(phones, ";"));
        } finally {
            BaseDAO.close(dbConnection);
        }
		return true;
	}
    public boolean send(String msg, String[] phones) throws Exception {
        Connection dbConnection = null; //SQLServerDbConnection.get();
        try{
            send(dbConnection, msg, StringUtils.join(phones, ";"));
        } finally {
            BaseDAO.close(dbConnection);
        }
        return true;
    }
    public void send(String msg, String phones) throws Exception {
        Connection dbConnection = null; // SQLServerDbConnection.get();
        try{
            send(dbConnection, msg, phones);
        } finally {
            BaseDAO.close(dbConnection);
        }
    }
    public void send(Connection dbConnection, String msg, String phones) throws Exception {
        dao.update(dbConnection,
                new StringBuffer("insert into SM_MtToSend ")
                        .append("(SISMSID, Msisdns, ReceiveTime, SendTime, ExtCode, ")
                        .append("RequireReport, Content, WapPushUri, MessageFormat, ")
                        .append("SendMethod, ApplicaionID, UserID, Reserve3) values (")
                        .append("newid(), ?, getdate(), getdate(), ?, ?, ?, ?, ?, ?, ?, ?, ?)")
                        .toString(),
                new Object[]{
                        phones,
                        "", 0, msg, "", 8, 0,
                        "A000000000000002",
                        0, ""
                });
    }
}
