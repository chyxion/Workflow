/**
 * file describe: AbstractViewComponent.java
 *
 */
package com.shs.commons.workflow.models.flowchart.views;

/**
 * @class describe: 抽象视图组件
 * @version 0.1
 * @date created: Mar 5, 2012 4:41:45 PM
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified: 
 * @modified by: 
 */
public abstract class AbstractViewComponent {
	private String style = ""; //css样式
	private String cssClass = "";
	
	public AbstractViewComponent() {
		super();
	}
	public AbstractViewComponent(String style, String cssClass) {
		super();
		this.style = style;
		this.cssClass = cssClass;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getCssClass() {
		return cssClass;
	}
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	} 
}
