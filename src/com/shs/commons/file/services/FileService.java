package com.shs.commons.file.services;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.json.JSONArray;
import org.json.JSONObject;
import com.shs.commons.file.dao.FileDAO;
import com.shs.framework.core.BaseService;
import com.shs.framework.dao.BaseDAO.ConnectionOperator;
import com.shs.framework.utils.DateUtils;
import com.shs.framework.utils.UUID;
/**
 * @class describe: 
 * @version 0.1
 * @date created: Apr 2, 2012 4:34:29 PM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
public class FileService extends BaseService {
	/**
	 * 文件服务器，基路径，如：http://192.168.1.168/IDPS
	 */
	public static String FS_BASE_PATH;
	/**
	 * 上传路径，绝对路劲，如：E:/upload/
	 */
	public static String UPLOAD_DIR;
	/**
	 * 下载动作
	 */
	public static String DOWNLOAD_ACTION = "/file/get/";
	/**
	 * 删除动作
	 */
	public final String DO_DELETE_ACTION = "/file/doDelete";
	/**
	 * 根据宿主ID删除
	 */
	public final String DELETE_BY_OWNER_ID_ACTION = "/file/deleteByOwnerID";
	/**
	 * 文件类型，嵌入
	 */
	public static final String TYPE_INLINE = "INLINE";
	/**
	 * 文件类型，附件
	 */
	public static final String TYPE_ATTACH = "ATTACH";
	/**
	 * 最大上传文件
	 */
	public static final long MAX_FILE_SIZE = 1024 * 1024 * 1024; // 10M
	/**
	 * 保存路径
	 * @return
	 */
	public String saveDir() {
		return DateUtils.thisYear() + "/" + DateUtils.thisMonth() + "/" + DateUtils.thisDay() + "/";
	}
	/**
	 * 保存完整路径
	 * @return
	 */
	public String saveFullPath() {
		return UPLOAD_DIR + saveDir();
	}
	private FileDAO fileDAO = new FileDAO(dao);
	
	/**
	 * 追加文件
	 * @param targetFile
	 * @param file
	 */
	public void append(File targetFile, File file) throws IOException{
		FileUtils.openOutputStream(targetFile, true).write(FileUtils.readFileToByteArray(file));
	}
	/**
	 * 转换文件长度问可读数值
	 * @param fileLength
	 * @return
	 */
	public String fileSize(long fileLength) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSize;
		if (fileLength < 1024) {
			fileSize = df.format((double) fileLength) + " B";
		} else if (fileLength < 1048576) {
			fileSize = df.format((double) fileLength / 1024) + " K";
		} else if (fileLength < 1073741824) {
			fileSize = df.format((double) fileLength / 1048576) + " M";
		} else {
			fileSize = df.format((double) fileLength / 1073741824) + " G";
		}
		return fileSize;
	}
	/**
	 * 该方法被Ajax调用，请求删除文件服务器上的文件
	 * 删除文件服务器上的文件
	 * @param jaFileIDs
	 * @throws Exception
	 */
	public void delete() throws Exception {
		validate("IDs", JA);
		httpPost(FS_BASE_PATH + DO_DELETE_ACTION, "IDs", params.get("IDs"));
	}
	/**
	 * 文件服务器执行删除文件
	 * @throws Exception
	 */
	public void doDelete() throws Exception {
		dao.executeTransaction(new ConnectionOperator() {
			@Override
			public void run() throws Exception {
				JSONArray jaID0s = params.getJSONArray("IDs");
				deleteFiles(dbConnection, fileDAO.findAllByID0s(dbConnection, jaID0s));
			}
		});
	}
	/**
	 * 删除文件
	 * @param dbConnection
	 * @param jaFiles
	 * @throws Exception
	 */
	private void deleteFiles(Connection dbConnection, JSONArray jaFiles) throws Exception {
		for (int i = 0; i < jaFiles.length(); ++i) {
			JSONObject joFile = jaFiles.getJSONObject(i);
			fileDAO.delete(dbConnection, joFile.getString("ID"));
			new File(UPLOAD_DIR + joFile.getString("DIRECTORY"), 
					joFile.getString("ID")).delete();
		}
	}
	/**
	 * 请求服务器删除文件
	 * @param ownerID
	 * @throws Exception
	 */
	public void deleteByOwnerID(String ownerID) throws Exception {
		httpPost(FS_BASE_PATH + DELETE_BY_OWNER_ID_ACTION, "OWNER_ID", ownerID);
	}
	/**
	 * http请求
	 * @param url
	 * @param pName
	 * @param pValue
	 * @throws Exception
	 */
	private void httpPost(String url, String pName, String pValue) throws Exception {
		String encoding = "UTF-8";
        String strResponse = new String(
        		Request.Post(url)
                .elementCharset(encoding)
                .bodyForm(Form.form()
                .add(pName, pValue)
                .build(), 
                Charset.forName(encoding))
                .execute()
                .returnContent()
                .asBytes(), 
                encoding);
        JSONObject joResponse = new JSONObject(strResponse);
        if (!joResponse.getBoolean("success")) {
        	throw new Exception(joResponse.getString("message"));
        }
	}
	/**
	 * 根据宿主ID删除文件，执行删除
	 * @throws Exception
	 */
	public void deleteByOwnerID() throws Exception {
		dao.executeTransaction(new ConnectionOperator() {
			@Override
			public void run() throws Exception {
				deleteFiles(dbConnection, fileDAO.findAllByOwnerID(dbConnection, params.get("OWNER_ID")));
			}
		});
	}
	public JSONArray listByOwnerID(final String ownerID) throws Exception{
		return fileDAO.findAllByOwnerID(ownerID);
	}
	/**
	 * 新建文件
	 * @param joFile
	 * @return
	 * @throws Exception
	 */
	public JSONObject newFile(JSONObject joFile) throws Exception {
		joFile.put("ID0", UUID.get())
				.put("USER_ID", params.get("USER_ID"))
				.put("OWNER_ID", params.get("OWNER_ID"));
		if (joFile.isNull("ID")) {
			joFile.put("ID", UUID.get());
		}
		fileDAO.newFile(joFile);
		return joFile;
	}
	/**
	 * 文件名后缀
	 * @param originalName
	 * @return
	 */
	public String fileSuffix(String originalName) {
		Matcher m = Pattern.compile("\\.(\\w+)$").matcher(originalName);
		String sRetun = originalName;
		if (m.find()) {
			sRetun = m.group(1);
		}
		return sRetun;
	}
	public JSONArray list() throws Exception {
		return fileDAO.findAllByOwnerID(params.get("OWNER_ID"));
	}
	/**
	 * 查找一个文件，下载用
	 * @return
	 * @throws Exception
	 */
	public JSONObject get() throws Exception {
		return fileDAO.get(params.get("ID"));
	}
}
