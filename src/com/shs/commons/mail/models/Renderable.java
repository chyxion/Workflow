package com.shs.commons.mail.models;
import javax.mail.Message;
import java.io.UnsupportedEncodingException;

/**
 * @class describe: 邮件渲染接口
 * @version 0.1
 * @date created: Jun 25, 2012 8:48:51 AM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public interface Renderable {
	/**
	 * 获取附件
	 * @param i
	 * @return
	 */
    Attachment getAttachment(int i);
    /**
     * 返回附件数目
     * @return
     */
    int getAttachmentCount();
    /**
     * 返回邮件内容
     * @return
     */
    String getBodyText();
    /**
     * 邮件主题
     * @return
     */
    String getSubject();
    /**
     * 发件人
     * @return
     */
    String getSender() throws UnsupportedEncodingException ;
    /**
     * 收件人
     * @return
     */
    String getRecipients() throws UnsupportedEncodingException;
    String getRecipientsCC() throws UnsupportedEncodingException;
    Message getRawMessage();
}
