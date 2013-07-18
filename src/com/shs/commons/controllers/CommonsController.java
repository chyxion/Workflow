package com.shs.commons.controllers;
import com.shs.framework.aop.RouteMapping;
import com.shs.framework.core.BaseController;
import com.shs.framework.utils.UUID;

/**
 * @version 0.1
 * @author chyxion
 * @describe: 
 * @date created: Jan 15, 2013 4:45:09 PM
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 * @copyright: Shenghang Soft All Right Reserved.
 */
@RouteMapping(controller="/commons")
public class CommonsController extends BaseController {
	private static final long serialVersionUID = 1L;
	/**
	 * 返回一个uuid
	 * @throws Exception
	 */
	public void uuid() throws Exception {
		success(UUID.get());
	}
}
