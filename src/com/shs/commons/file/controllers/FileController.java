package com.shs.commons.file.controllers;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import com.shs.commons.file.services.ExcelImportService;
import com.shs.commons.file.services.FileService;
import com.shs.framework.aop.ClearInterceptor;
import com.shs.framework.aop.ClearLayer;
import com.shs.framework.aop.RouteMapping;
import com.shs.framework.core.BaseController;
import com.shs.framework.dao.BaseDAO;
import com.shs.framework.upload.UploadFile;
import com.shs.framework.utils.UUID;

/**
 * @class describe: 文件控制器
 * @version 0.1
 * @date created: Apr 2, 2012 8:33:11 AM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified:
 * @modified by:
 * @copyright: Shenghang Soft All Right Reserved.
 */
@ClearInterceptor(ClearLayer.ALL)
@RouteMapping(controller="/commons/file")
public class FileController extends BaseController {
	private FileService fileService;
	private ExcelImportService excelImportService;
	private static final long serialVersionUID = 1L;
	/**
	 * 文件
	 */
	private File file;
	/**
	 *  文件类型，KE的文件类型，image，flash
	 */
	private String fileType;
	/**
	 * 初始名字
	 */
	private String fileName;
	/**
	 * 文件块
	 */
	private int chunk;
	/**
	 * 文件总块
	 */
	private int chunks;
	/**
	 * 内容类型
	 */
	private String contentType;
	/**
	 * plupload上传,分块上传 
	 * @return
	 * @throws Exception 
	 */
	public void plUpload() throws Exception {
			// 文件UUID
			String fileID;
			// 一开始, 创建文件
			if (chunk == 0) {
				fileID = UUID.get();
				// 保存完整路径
				String saveFullPath = fileService.saveFullPath();
				// session记录，文件完整路径
				setSessionAttr("PL_SAVE_FULL_PATH", saveFullPath);
				// 记录文件保存相对路径
				setSessionAttr("PL_SAVE_PATH", fileService.saveDir());
				// 记录文件UUID
				setSessionAttr("PL_FILE_ID", fileID);
				// 复制临时文件到目标文件
				FileUtils.copyFile(file, new File(saveFullPath, fileID));
			} else {
				fileID = getSessionAttr("PL_FILE_ID");
				// 追加文件
				fileService.append(new File((String) getSessionAttr("PL_SAVE_FULL_PATH"), fileID), file);
			}
			
			//文件上传完成
			if (chunk == chunks || chunk == chunks - 1) {
				JSONObject joFile = genJOFile();
				// 放入文件UUID
				joFile.put("ID", fileID);
				joFile.put("TYPE", FileService.TYPE_ATTACH); // 附件
				// 保存路径，相对路径
				joFile.put("DIRECTORY", getSessionAttr("PL_SAVE_PATH"));
				// 保存数据库
				fileService.newFile(joFile);
				// 清除session记录,文件完整路径
				removeSessionAttr("PL_SAVE_FULL_PATH");
				// 清除文件保存相对路径
				removeSessionAttr("PL_SAVE_PATH");
				// 清除文件UUID
				removeSessionAttr("PL_FILE_ID");
				// 返回文件信息
				success(joFile);
			}
	}
	/**
	 * 导入Excel
	 */
	public void excelImport() throws Exception {
			// 文件UUID
			String fileID;
			// 一开始, 创建文件
			if (chunk == 0) {
				fileID = UUID.get();
				// 记录文件UUID
				setSessionAttr("IMPORT_FILE_ID", fileID);
				// 复制临时文件到目标文件
				FileUtils.copyFile(file, new File(FileUtils.getTempDirectory(), fileID));
			} else {
				fileID = getSessionAttr("IMPORT_FILE_ID");
				// 追加文件
				fileService.append(new File(FileUtils.getTempDirectory(), fileID), file);
			}
			//文件上传完成
			if (chunk == chunks || chunk == chunks - 1) {
				excelImportService.doImport(fileID);
				// 清除文件UUID
				removeSessionAttr("IMPORT_FILE_ID");
				// 返回文件信息
				success();
			}
	}
	public void excelExport () throws Exception {
		outFile(params.get("FILE_NAME") + ".xls", params.get("CONTENT"), CONTENT_TYPE_EXCEL);
	}
	/**
	 * 输出文件
	 * @param fileName
	 * @param content
	 * @param contentType
	 * @throws Exception
	 */
	protected void outFile(String fileName, String content, String contentType) throws Exception {
		response.addHeader("Content-Disposition", 
				"attachment;filename=" + 
				URLEncoder.encode(fileName, "UTF-8"));
        response.setContentLength(content.length());
        out(content, contentType);
	}
	public void wordExport() throws Exception {
		outFile(params.get("FILE_NAME") + ".doc", params.get("CONTENT"), CONTENT_TYPE_WORD);
	}
//	public void upload() throws Exception {
//		try {
//			JSONObject joFile = null;
//			// 保存数据库
//			success(fileService.newFile(joFile), "text/html;charset=utf-8");
//		} catch (Exception e) {
//			fail(e, "text/html;charset=utf-8");
//			e.printStackTrace();
//		} 
//	}
	public void upload() throws Exception {
		jsp("/file/upload");
	}
	public void list() {
		setAttr("files", new BaseDAO().findMapList("select id, owner_id, name from ExtFiles"));
		freeMarker("/file/list.html");
	}
	/**
	 * 构建文件JSONObject
	 * @param file
	 * @return
	 * @throws org.json.JSONException
	 */
	private JSONObject genJOFile() throws JSONException{
		JSONObject joFile = new JSONObject()
				.put("NAME", fileName)
				.put("FILE_SIZE", fileService.fileSize(file.length()))
				.put("CONTENT_TYPE", contentType);
		return joFile;
	}
	/**
	 * 输出JSON信息, ke上传使用
	 * @param message
	 * @throws Exception 
	 * @throws JSONException 
	 */
	private void keError(String message) throws Exception {
		out(new JSONObject()
			.put("error", 1)
			.put("message", message).toString());
	}
	private void keSuccess(String url) throws Exception {
		out(new JSONObject()
			.put("error", 0)
			.put("url", url).toString());
	}
	/**
	 * 编辑器文件上传
	 * @return
	 * @throws Exception 
	 */
	public void keUpload() throws Exception {
		try {
			// 定义允许上传的文件扩展名
			Map<String, String> extMap = new HashMap<String, String>();
			extMap.put("image", "gif,jpg,jpeg,png,bmp");
			extMap.put("flash", "swf,flv");
			extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
	
			if (file == null) {
				keError("请选择文件");
				return;
			}
	
			if (!Arrays.<String>asList(extMap.get(fileType).split(","))
					.contains(fileService.fileSuffix(fileName))) {
				String fileTypes = extMap.get(fileType);
				keError("上传文件格式必须是 [" + fileTypes + "]");
				return;
			}
			/**
			 * 判断文件大小
			 */
			if (file.length() > FileService.MAX_FILE_SIZE){
				keError("文件大小不能超过[" + fileService.fileSize(FileService.MAX_FILE_SIZE) + "]");
				return;
			}
			//文件上传完成
			JSONObject joFile = genJOFile();
			joFile.put("TYPE", "INLINE"); // 文件类型, 内嵌 
			joFile.put("DIRECTORY", fileService.saveDir());
			fileService.newFile(joFile);
			String fileID = joFile.getString("ID");
			FileUtils.copyFile(file, new File(fileService.saveFullPath(), fileID));
			// 保存数据库
			keSuccess(FileService.FS_BASE_PATH + FileService.DOWNLOAD_ACTION + fileID);
		} catch (Exception e) {
			keError(e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * 文件流输出
	 * @return
	 */
	public InputStream getFileStream() throws Exception {
		InputStream fin = null;
			JSONObject joFile = fileService.get();
			if (joFile != null){
				fileName = URLEncoder.encode(joFile.getString("NAME"), "UTF-8");
				fin = new FileInputStream(FileService.UPLOAD_DIR + 
							joFile.getString("DIRECTORY") + joFile.getString("ID"));
			} else {
				fileName = "missing.png";
				fin = getServletContext().getResourceAsStream("/assets/commons/images/missing.png");
			}
			
		return fin;
	}
	/**
	 * 获取文件, 文件下载
	 * @return
	 */
	public void get() throws Exception {
		String fileID = params.get();
		Map<String, Object> joFile = 
			new BaseDAO().findMap("select name, content from ExtFiles where id = ?", 
				fileID);
		file((File) joFile.get("CONTENT"), (String) joFile.get("NAME"));
	}
	public void uploadFile() throws Exception {
		UploadFile f = params.getFile();
		JSONObject joFile = 
			new JSONObject()
			.put("ID", UUID.get())
			.put("OWNER_ID", UUID.get())
			.put("NAME", f.getOriginalFileName())
			.put("CONTENT", f.getFile());
		
		new BaseDAO().insert("ExtFiles", joFile);
		joFile.remove("CONTENT");
		setAttr("data", joFile);
		jsp("/file/success");
	}
	/**
	 * Ajax请求删除文件
	 * @return
	 * @throws Exception 
	 */
	public void delete() throws Exception {
		fileService.delete();
		success();
	}
	/**
	 * 执行删除文件，该方法为文件服务器方法，Ajax无法直接调用，JavaScript请求上一个方法（delete），
	 * 然后再调用此方法删除文件
	 * @throws Exception 
	 */
	public void doDelete() throws Exception {
		fileService.doDelete();
		success();
	}
	/**
	 * 根据宿主ID删除
	 * @throws Exception 
	 */
	public void deleteByOwnerID () throws Exception {
		fileService.deleteByOwnerID();
		success();
	}
	//填充上传的文件
	public void setFile(File file) {
		this.file = file;
	}
	/**
	 * 文件内容类型
	 * @param contentType
	 */
	public void setFileContentType(String contentType){
		this.contentType = contentType;
	}
	/**
	 * 文件初始名, struts2约定
	 * @param originalName
	 */
	public void setFileFileName(String originalName) {
		this.fileName = originalName;
	}
	/**
	 * 获取文件原始名称, 下载时候使用, 在struts.action.xml中有配置
	 * @return
	 */
	public String getFileName(){
		return this.fileName;
	}
	/**
	 * plupload的文件块
	 * @param chunk
	 */
	public void setChunk(int chunk) {
		this.chunk = chunk;
	}
	/**
	 * plupload传递过来的文件块数目
	 * @param chunks
	 */
	public void setChunks(int chunks) {
		this.chunks = chunks;
	}
	/**
	 * 上传的文件类型类型目录,image、flash、media、file, KE这货使用其来判断上传文件类型
	 */
	public void setDir(String fileType) {
		if (StringUtils.isEmpty(fileType)) {
			this.fileType = "image";
		} else {
			this.fileType = fileType;
		}
	}
	/**
	 * 获得文件原始名称 
	 * @param fileName
	 */
	public void setImgFileFileName(String fileName){
		this.fileName = fileName;
	}
	public void setImgFileContentType(String ct) {
		this.contentType = ct;
	}
}
