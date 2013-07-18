package com.shs.commons.workflow.instance.controllers;
import com.shs.commons.workflow.instance.services.WfTaskService;
import com.shs.framework.aop.RouteMapping;
import com.shs.framework.core.BaseController;
@RouteMapping(controller="workflow/instance/task")
public class WfTaskController extends BaseController {
	private static final long serialVersionUID = 1L;
	private WfTaskService taskService;
	/**
	 * 待办任务列表
	 * @throws Exception 
	 */
	public void list() throws Exception {
		success(taskService.list());
	}
	/**
	 * 任务历史记录
	 * @throws Exception 
	 */
	public void history() throws Exception {
		success(taskService.tasksHistory());
	}
	/**
	 * 个人工作历史
	 * @throws Exception 
	 */
	public void myTasksHistory() throws Exception {
		success(taskService.myTasksHistory());
	}
	
	/**
	 * 审批任务
	 * @throws Exception 
	 */
	public void approve() throws Exception {
		taskService.approve();
		success();
	}
	/**
	 * 接受任务
	 * @throws Exception 
	 */
	public void accept() throws Exception {
		taskService.accept();
		success();
	}
	/**
	 * 驳回任务
	 * @throws Exception 
	 */
	public void reject() throws Exception {
		taskService.reject();
		success();
	}
}
