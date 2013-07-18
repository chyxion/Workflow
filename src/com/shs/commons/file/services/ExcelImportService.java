package com.shs.commons.file.services;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONObject;
import com.shs.commons.file.models.DbField;
import com.shs.framework.core.BaseService;
import com.shs.framework.utils.JSONUtils;

/**
 * @version 0.1
 * @author chyxion
 * @describe: 
 * @date created: Jan 14, 2013 4:02:23 PM
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public class ExcelImportService extends BaseService {
	private StringBuffer insertSQL;
	private String table;
	/**
	 * 导入
	 * @param fileID
	 * @throws Exception
	 */
	public void doImport(String fileID) throws Exception {
		validate("TABLE", NOT_BLANK);
		File file = new File(FileUtils.getTempDirectory(), fileID);
		try {
			table = params.get("TABLE");
			logger.debug("import excel to table[" + table + "]");
			logger.debug("width import config:");
			// 读取配置文件
			JSONObject joConfig = JSONUtils.newJSONObject(params.getServletContext()
					.getResourceAsStream("/WEB-INF/config/import/" + table + ".json"));
			// 起始行，截止行
			int startRow = joConfig.optInt("start_row"),
					endRow = joConfig.optInt("end_row");
			JSONArray jaFields = joConfig.getJSONArray("fields");
			logger.debug("start row:[" + startRow + "]");
			// 初始化域
			DbField[] fields = initFields(jaFields);
			Workbook wb = new HSSFWorkbook(new FileInputStream(file));
			Sheet sheet = wb.getSheetAt(0);
			List<Object> values = new LinkedList<Object>();
			// 没有指定截止列
			if (endRow == 0) {
				endRow = sheet.getLastRowNum();
			}
			logger.debug("end row:[" + endRow + "]");
			// 遍历行
			for (int r = startRow; r < endRow; ++r) {
				int c = 0;
				// 列值
				List<Object> rowValues = new LinkedList<Object>();
				Row row = sheet.getRow(r);
				// 空的列数
				int numNull = 0;
				// 遍历列
				for (DbField field : fields) {
					Object cv = getCellValue(row.getCell(c++), field);
					if (cv == null) {
						++numNull;
					}
					rowValues.add(cv);
				}
				// 如果整行都是空，不加入
				if (numNull < fields.length)
					values.add(rowValues);
			}
			// 批操作，存数据库
			dao.executeBatch(insertSQL.toString(), values);
		} finally {
			file.delete();
		}
	}
	/**
	 * 只支持两种类型，number，string
	 * @param cell
	 * @param field
	 * @return
	 */
	private Object getCellValue(Cell cell, DbField field) {
		Object v = null;
		if (cell != null) {
			int type = cell.getCellType();
			if (type == Cell.CELL_TYPE_STRING) {
				v = cell.getStringCellValue();
			} else if (type == Cell.CELL_TYPE_NUMERIC) {
				v = cell.getNumericCellValue();
			} else { // 不尝试取值
				v = null;
			}
		} else {
			v = null;
		}
		return field.initValue(v);
	}
	/**
	 * 初始化字段
	 * @param jaFields
	 * @return
	 * @throws Exception
	 */
	private DbField[] initFields(JSONArray jaFields) throws Exception {
		DbField[] fields = new DbField[jaFields.length()];
		insertSQL = 
				new StringBuffer("insert into ")
				.append(table).append("(");
		StringBuffer sbValues = new StringBuffer(") values (");
		for (int i = 0; i < jaFields.length(); ++i) {
			Object oField = jaFields.get(i);
			String name, type;
			int precision = 0; // 精度
			if (oField instanceof JSONObject) {
				JSONObject joField = (JSONObject) oField;
				name = joField.getString("name");
				type = joField.getString("type");
				precision = joField.optInt("precision");
			} else {
				name = (String) oField;
				type = DbField.TYPE_STRING;
			} 
			insertSQL.append(name).append(", ");
			sbValues.append("?, ");
			fields[i] = new DbField(name, type, precision);
		}
		// 拆掉最后一个', '
		insertSQL.setLength(insertSQL.length() - 2);
		// 裁掉最后一个', '
		sbValues.setLength(sbValues.length() - 2);
		insertSQL.append(sbValues.toString())
			.append(")");
		logger.debug("generate insert SQL:");
		logger.debug(insertSQL);
		return fields;
	}
}
