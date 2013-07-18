package com.shs.commons.mail.models;
/**
 * @class describe: 邮件附件
 * @version 0.1
 * @date created: Jun 25, 2012 10:31:03 AM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public class Attachment {
        private String contentType;
        private String fileName;
        private byte[] content;
        private String contentID;
        
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentID() {
        return contentID;
    }
    public void setContentID(String contentID) {
        this.contentID = contentID;
    }
}
