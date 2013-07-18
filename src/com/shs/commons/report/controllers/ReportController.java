package com.shs.commons.report.controllers;
import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;
import net.sf.jasperreports.j2ee.servlets.DocxServlet;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;
import net.sf.jasperreports.j2ee.servlets.JExcelApiServlet;
import net.sf.jasperreports.j2ee.servlets.PdfServlet;
import org.json.JSONArray;
import com.shs.commons.report.models.ReportDataSource;
import com.shs.commons.report.services.ReportService;
import com.shs.framework.aop.RouteMapping;
import com.shs.framework.core.BaseController;

/**
 * @version 0.1
 * @author chyxion
 * @describe: 
 * @date created: Mar 26, 2013 5:56:29 PM
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
@RouteMapping(controller="/system/report")
public class ReportController extends BaseController {
	private ReportService service;
	public void index() {
		success();
	}
	public void list() {
		JSONArray jaList = new JSONArray(service.list());
		success(jaList);
	}
	public void html() throws Exception {
		compile();
		fill();
		response.setContentType(CONTENT_TYPE_HTML);
		PrintWriter out = response.getWriter();
		JRXhtmlExporter exporter = new JRXhtmlExporter();
		Object jasperPrint = getSessionAttr(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE);
		request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, out);
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "image?image=");
		exporter.exportReport();
		renderNull();
	}
	public void excel() throws Exception {
		compile();
		fill();
		new JExcelApiServlet().service(request, response);
		renderNull();
	}
	public void docx() throws Exception {
		compile();
		fill();
		new DocxServlet().service(request, response);
		renderNull();
	}
	public void image() throws Exception {
		new ImageServlet().service(request, response);
		renderNull();
	}
	public void compile() throws Exception {
		JasperCompileManager.compileReportToFile(getServletContext().getRealPath("/WEB-INF/views/reports/table.jrxml"));
	}
	public void fill() throws Exception {
		String reportFileName = getServletContext().getRealPath("/WEB-INF/views/reports/table.jasper");
		File reportFile = new File(reportFileName);
		if (!reportFile.exists())
			throw new JRRuntimeException("File WebappReport.jasper not found. The report design must be compiled first.");

		Map<String, Object> p = new HashMap<String, Object>();
		p.put("ReportTitle", "网络监控平台配置信息报表");
		p.put("BaseDir", reportFile.getParentFile());
					
		JasperPrint jasperPrint = 
			JasperFillManager.fillReport(
				reportFileName, 
				p, 
				new ReportDataSource(service.list()));
		setSessionAttr(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
	}
	public void pdf() throws Exception {
		compile();
		fill();
		new PdfServlet().service(request, response);
		renderNull();
	}
}
