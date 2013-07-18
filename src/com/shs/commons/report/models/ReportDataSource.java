package com.shs.commons.report.models;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * @version 0.1
 * @author chyxion
 * @describe: 
 * @date created: Mar 26, 2013 5:50:29 PM
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public class ReportDataSource implements JRDataSource {

	private List<?> data;
	private Iterator<?> it;
	private Map<String, ?> currentItem;
	/**
	 * 格式化日期
	 */
	private boolean formatDateTime = true;
	/**
	 * 日期格式
	 */
	private String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 格式化数值
	 */
	private boolean formatNumeric = true;
	/**
	 * 数值格式
	 */
	private String numericFormat = "0.00";
	/**
	 * 格式化日期
	 */
	private boolean formatDate = true;
	/**
	 * 日期格式
	 */
	private String dateFormat = "yyyy-MM-dd";
	
	public boolean isFormatDateTime() {
		return formatDateTime;
	}
	/**
	 * 设置格式化日期时间
	 * @param formatDateTime
	 * @return 
	 */
	public ReportDataSource setFormatDateTime(boolean formatDateTime) {
		this.formatDateTime = formatDateTime;
		return this;
	}
	/**
	 * 日期时间格式
	 * @return
	 */
	public String getDateTimeFormat() {
		return dateTimeFormat;
	}
	/**
	 * 设置日期时间格式
	 * @param dateTimeFormat
	 * @return 
	 */
	public ReportDataSource setDateTimeFormat(String dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat;
		return this;
	}
	/**
	 * 日期格式
	 * @return
	 */
	public String getDateFormat() {
		return dateFormat;
	}
	/**
	 * 日期格式
	 * @param dateFormat
	 * @return 
	 */
	public ReportDataSource setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
		return this;
	}
	/**
	 * 是否格式化日期
	 * @return
	 */
	public boolean isFormatDate() {
		return formatDateTime;
	}
	/**
	 * 设置格式化日期
	 * @param formatDate
	 * @return 
	 */
	public ReportDataSource setFormatDate(boolean formatDate) {
		this.formatDateTime = formatDate;
		return this;
	}
	/**
	 * 是否格式化数值
	 * @return
	 */
	public boolean isFormatNumeric() {
		return formatNumeric;
	}
	/**
	 * 设置格式化数值
	 * @param formatNumeric
	 * @return 
	 */
	public ReportDataSource setFormatNumeric(boolean formatNumeric) {
		this.formatNumeric = formatNumeric;
		return this;
	}
	/**
	 * 数值格式
	 * @return
	 */
	public String getNumericFormat() {
		return numericFormat;
	}
	/**
	 * 设置数值格式，默认保留2未小数
	 * @param numericFormat
	 */
	public  ReportDataSource setNumericFormat(String numericFormat) {
		this.numericFormat = numericFormat;
		return this;
	}

	
	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
		this.it = this.data.iterator();
	}
	/**
	 * @param data 数据源数据，默认格式化日期
	 */
	public ReportDataSource(List<?> data) {
		super();
		this.data = data;
		it = this.data.iterator();
	}
	/**
	 * @param data 数据源数据
	 * @param isFormat 是否格式化日期
	 */
	public ReportDataSource(List<Map<String, Object>> data, boolean isFormat) {
		this(data);
		this.formatDate = isFormat;
		this.formatDateTime = isFormat;
		this.formatNumeric = isFormat;
	}
	/**
	 *
	 */
	public boolean next() throws JRException {
		boolean hasNext = false;
		if (it.hasNext()) {
			currentItem = (Map<String, ?>) it.next();
			hasNext = true;
		} else {
			currentItem = null;
		}
		return hasNext;
	}

	/**
	 *
	 */
	public Object getFieldValue(JRField field) throws JRException {
		Object v = currentItem.get(field.getName());
		// 格式化数值
		if (formatNumeric && (v instanceof Double || v instanceof Float)) {
			v = new DecimalFormat(numericFormat).format(v);
		// 格式化日期时间
		} else if (formatDateTime && v instanceof Timestamp) {
			v = new SimpleDateFormat(dateTimeFormat).format((Timestamp) v);
		// 格式化日期
		} else if (formatDate && v instanceof java.sql.Date) {
			v = new SimpleDateFormat(dateFormat).format((java.sql.Date) v);
		// 将BigDecimal转换成Double
		} else if (v instanceof BigDecimal) {
			v = ((BigDecimal) v).doubleValue();
			// 如果需要格式化，格式化
			if (formatNumeric) v = new DecimalFormat(numericFormat).format(v);
		}
		return v;
	}

}