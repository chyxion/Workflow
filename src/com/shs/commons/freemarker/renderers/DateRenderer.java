package com.shs.commons.freemarker.renderers;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class DateRenderer implements TemplateMethodModel {
	public Object exec(List args) throws TemplateModelException {
		  String date = (String)args.get(0);
		  if (StringUtils.isEmpty(date)) {   
			  return "";   
		  }   
		return date.substring(0, 4) + "-" +
			date.substring(4, 6) + "-" + 
			date.substring(6, 8);
	}

}
