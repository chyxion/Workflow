package com.shs.commons.workflow.expression;

/**
 * User: wangtao
 * Date: 2004-12-17
 * Time: 10:24:05
 */
public class WrapperObject {
	Object container = null;
	Object index = null;
	int indexType = 0; //0-未定义，1-数组或映射表下标，2-JavaBean的成员变量
	Object value = null;

	public Object getContainer()
	{
		return container;
	}

	public void setContainer(Object container)
	{
		this.container = container;
	}

	public Object getIndex()
	{
		return index;
	}

	public void setIndex(Object index)
	{
		this.index = index;
	}

	public int getIndexType()
	{
		return indexType;
	}

	public void setIndexType(int indexType)
	{
		this.indexType = indexType;
	}

	public Object getValue()
	{
		return value;
	}

	public void setValue(Object value)
	{
		this.value = value;
	}
	
	public boolean isSimple()
	{
		return container == null;
	}
}
