package com.shs.commons.workflow.exceptions;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
public class WfException extends Exception {
	private static final long serialVersionUID = 1L;
	/**
     * 系统异常
     */
    public final static int EXCEPTION_CODE_UNKNOW_ERROR = 0;
    public final static int EXCEPTION_CODE_SYSTEM_ERROR = 1;
    /**
     * 引擎异常
     */
    public final static int ENGINE_STARTED = 10;
    public final static int ENGINE_STOPED = 11;
    /**
     * 流程异常
     */
    public final static int FLOW_NOT_EXIST = 100;
    /**
     * 后继路由不存在
     */
    public final static int ROUTE_NOT_EXIST = 120;
    /**
     * 表达式解析异常
     */
    public final static int PARSE_CONDITION_ERROR = 150;
    /**
     * 分派策略异常
     */
    public final static int ASSIGN_NOT_EXIST = 180;

    /**
     * 错误代码
     */
    private int code;
    /**
     * 错误消息
     */
    private String message;

    private static Properties msgProps;

    /**
     * 构造器
     * @param code  异常代码
     */
    public WfException(int code) {
        this.code = code;
        try {
            String msg = msgProps.getProperty("SYSTEM_" + code);
            if (msg != null) {
                this.message = new String(msg.trim().getBytes("ISO8859_1"), "gb2312");
            } else {
                this.message = "异常信息未定义";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 构造器
     * @param message  异常消息
     */
    public WfException(String message) {
        this.code = EXCEPTION_CODE_UNKNOW_ERROR;
        this.message = message;
    }

    /**
     * 构造器
     * @param code
     * @param message
     */
    public WfException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 构造器
     * @param ex  系统异常
     */
    public WfException(Exception ex) {
        this.code = EXCEPTION_CODE_SYSTEM_ERROR;
        this.message = ex.getMessage();
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
