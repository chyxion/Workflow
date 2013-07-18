package com.shs.commons.mail.models;
import org.apache.commons.lang.time.DateUtils;
import javax.mail.*;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.DateTerm;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * @class describe: 邮件收取
 * @version 0.1
 * @date created: Jun 25, 2012 2:21:28 PM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public class MailRetriever {
    private String userID = "";
    private String password = "";
    private String server = "";
    private String provider = "";
    
	public MailRetriever(String userID, String password, String server,
			String provider) {
		super();
		this.userID = userID;
		this.password = password;
		this.server = server;
		this.provider = provider;
	}
	
	public MailRetriever() {
		super();
	}
	/**
	 * 收取所有邮件
	 * @return
	 * @throws Exception
	 */
	public List<Renderable> retrieve() throws Exception {
		List<Renderable> msgs = null;
		// Get system properties
		Properties properties = System.getProperties();
		// Get the default Session object.
		Session session = Session.getInstance(properties);
		// Get a Store object that implements the specified protocol.
		Store store = session.getStore(provider);
		// Connect models the current host using the specified username and
		// password.
		store.connect(server, "wudi@petrochina.com.cn", "w3u0d7i8");
		// Create a Folder object corresponding models the given name.
		Folder folder = store.getFolder("inbox");
		// Open the Folder.
		folder.open(Folder.READ_WRITE);
		Message[] messages = folder.getMessages();
		if (messages != null && messages.length > 0){
			msgs = new LinkedList<Renderable>();
			for (int i = 0; i < messages.length; ++i) {
				if (messages[i].getContent() instanceof Multipart) {
					RenderableMessage msg = new RenderableMessage(messages[i]);
					msgs.add(msg);
				} else {
					Renderable msg = new RenderablePlainText(messages[i]);
					msgs.add(msg);
				}
			}
		}
		if (folder != null)
			folder.close(true);
		if (store != null)
			store.close();
		return msgs;
	}
	/**
	 * 收取比提供日期更新的邮件
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public List<Renderable> retrieve(Date date) throws Exception {
		if (date == null) { // 如果提供日期为空，则收取截至上个月的
			date = DateUtils.addMonths(new Date(), -1);
		}
		List<Renderable> msgs = null;
		// 系统配置属性
		Properties properties = System.getProperties();
		// 创建会话
		Session session = Session.getInstance(properties);
		// 得到会话store
		Store store = session.getStore(provider);
		// 连接邮件服务器
		store.connect(server, userID.substring(0,userID.indexOf("@")), password);
		// 收件箱
		Folder folder = store.getFolder("inbox");
		// 打开收件箱
		folder.open(Folder.READ_WRITE);
		// 接收新邮件
		Message[] messages = folder.search(new DateTerm(ComparisonTerm.GE, date) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean match(Message m) {
				try {
					return this.match(m.getSentDate());
				} catch (MessagingException e) {
					e.printStackTrace();
					return false;
				}
			}
		});
		// 获取消息内容
		if (messages != null && messages.length > 0){
			msgs = new LinkedList<Renderable>();
			for (int i = 0; i < messages.length; ++i) {
				if (messages[i].getContent() instanceof Multipart) {
					RenderableMessage msg = new RenderableMessage(messages[i]);
					msgs.add(msg);
				} else {
					Renderable msg = new RenderablePlainText(messages[i]);
					msgs.add(msg);
				}
			}
		}
		// 关闭文件夹
		if (folder != null)
			folder.close(true);
		// 关闭store
		if (store != null)
			store.close();
		return msgs;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
}
