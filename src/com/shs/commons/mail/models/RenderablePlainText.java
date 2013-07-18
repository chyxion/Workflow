package com.shs.commons.mail.models;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
/**
 * @class describe: 纯文本邮件内容
 * @version 0.1
 * @date created: Jun 25, 2012 2:22:49 PM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public class RenderablePlainText implements Renderable {
    
    private String bodyText;
    private String subject;
    private String sender;
    private String recipients;
    private String recipientsCC = "";
    private Message rawMessage;
    /**
     * 创建纯文本邮件
     * @param message
     * @throws javax.mail.MessagingException
     * @throws java.io.IOException
     */
    public RenderablePlainText(Message message) throws MessagingException, IOException {
        subject = message.getSubject();
        bodyText = message.getContent().toString();
        sender = MimeUtility.decodeText(InternetAddress.toString(message.getFrom()));
        recipients = MailUtils.decodeReceipients(InternetAddress.toString(message.getRecipients(Message.RecipientType.TO)));
        recipientsCC = MailUtils.decodeReceipients(InternetAddress.toString(message.getRecipients(Message.RecipientType.CC)));
        rawMessage = message;
    }
    public Attachment getAttachment(int i) {
        return null;
    }
    public int getAttachmentCount() {
        return 0;
    }
    public String getBodyText() {
        return "<pre>"+bodyText+"</pre>";
    }
    public String getSubject() {
        return subject;
    }
	public String getRecipients() throws UnsupportedEncodingException {
		return recipients;
	}
	public String getSender() throws UnsupportedEncodingException {
		return sender;
	}
	public String getRecipientsCC() throws UnsupportedEncodingException {
		return recipientsCC;
	}
	public Message getRawMessage(){
		return rawMessage;
	}
}
