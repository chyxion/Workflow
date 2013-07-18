package com.shs.commons.mail.models;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;

public class MailUtils {

	/**
	 * 字符串的编码
	 * @param str
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 */
	public static String encodeType(String str)
			throws UnsupportedEncodingException {
		String[] encoddings = new String[] { "gb2312", "iso-8859-1", "gbk",
				"utf-8" };
		for (String it : encoddings) {
			if (str.equals(new String(str.getBytes(it), it)))
				return it;
		}
		return "";
	}

	public static String encodeStr(String str)
			throws UnsupportedEncodingException {
		String[] encoddings = new String[] { 
				"gb2312", "iso-8859-1", "gbk",
				"utf-8" };
		for (String it : encoddings) {
			if (str.equals(new String(str.getBytes(it), it)))
				return new String(str.getBytes(it));
		}
		return str;
	}
	/**
	 * 解码接收人
	 * @param str
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 */
	public static String decodeReceipients(String str) throws UnsupportedEncodingException{
        if (str != null){
	        if (str.startsWith("=?GBK") || str.startsWith("=?gbk")){
	        	return MimeUtility.decodeText(str);
	        } else{
	        	//return new String(MimeUtility.decodeText(str).getBytes(), "gbk");
	        	return encodeStr(str);
	        }
        }
        return null;
	}
}
