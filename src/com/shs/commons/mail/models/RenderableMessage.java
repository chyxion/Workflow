package com.shs.commons.mail.models;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
/**
 * @class describe: 邮件内容，带附件的
 * @version 0.1
 * @date created: Jun 25, 2012 10:15:56 AM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public class RenderableMessage implements Renderable {

	private String subject;
	private String bodyText;
	List<Attachment> attachments;
    private String sender;
    private String recipients;
    private String recipientsCC = "";
    private Message rawMessage; // 原生消息
	/** Creates a new instance of RenderableMessage */
	public RenderableMessage(Message m) throws MessagingException, IOException {
		subject = m.getSubject();
		attachments = new LinkedList<Attachment>();
        sender = MimeUtility.decodeText(InternetAddress.toString(m.getFrom()));
        recipients = MailUtils.decodeReceipients(InternetAddress.toString(m.getRecipients(Message.RecipientType.TO)));
        recipientsCC = MailUtils.decodeReceipients(InternetAddress.toString(m.getRecipients(Message.RecipientType.CC)));
        this.rawMessage = m;
		extractPart(m);
	}
	/**
	 * 提取邮件内容
	 * @param part
	 * @throws javax.mail.MessagingException
	 * @throws java.io.IOException
	 */
	private void extractPart(final Part part) throws MessagingException,
			IOException {
		if (part.getContent() instanceof Multipart) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				extractPart(mp.getBodyPart(i));
			}
			return;
		}
		if (part.getFileName() == null) { //邮件内容
			bodyText = part.getContent().toString();
		} else { // 附件
			Attachment attachment = new Attachment();
			String contentType = part.getContentType();
			attachment.setContentType(contentType.substring(0, contentType.indexOf(";")));
			attachment.setFileName(MimeUtility.decodeText(part.getFileName()));
			InputStream in = part.getInputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			byte[] buffer = new byte[8192];
			int count = 0;
			while ((count = in.read(buffer)) >= 0)
				bos.write(buffer, 0, count);
			in.close();
			attachment.setContent(bos.toByteArray());
			attachments.add(attachment);
		}
	}
	public String getSubject() {
		return subject;
	}
	public String getBodyText() {
		return bodyText;
	}
	public int getAttachmentCount() {
		return attachments == null ? 0 : attachments.size();
	}
	public Attachment getAttachment(int i) {
		return attachments.get(i);
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
