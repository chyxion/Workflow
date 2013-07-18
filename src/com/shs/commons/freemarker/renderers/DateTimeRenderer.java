package com.shs.commons.freemarker.renderers;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * @version 0.1
 * @author chyxion
 * @describe: 日期渲染
 * @date created: Feb 1, 2013 5:32:06 PM
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public class DateTimeRenderer implements TemplateMethodModel {
	public Object exec(List args) throws TemplateModelException {
		  String date = (String)args.get(0);
		  if (StringUtils.isEmpty(date)) {   
			  return "";   
		  }   
		return date.substring(0, 4) + "-" +
			date.substring(4, 6) + "-" + 
			date.substring(6, 8) + " " + 
			date.substring(8, 10) + ":" + 
			date.substring(10, 12) + ":" + 
			date.substring(12, 14);
	}
}
