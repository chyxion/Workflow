package com.shs.commons.report.services;
import java.util.List;
import java.util.Map;
import com.shs.framework.core.BaseService;
import com.shs.framework.dao.BaseDAO;
public class ReportService extends BaseService {
	public List<Map<String, Object>> list() {
		return dao.findMapList("select f_key ID, f_value NAME, f_note VALUE from ExtConfig");
	}
}
