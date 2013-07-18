package com.shs.commons.workflow;
import java.util.HashMap;
import java.util.Map;

/**
 * @class describle: 流程配置
 * @version 0.1
 * @date created: 2012-12-24 下午3:46:45
 * @author chyxion
 * @support: chyxion@163.com
 * @date modified:
 * @modified by:
 */
public final class Config {
    /**
     * 节点类型，开始
     */
    public static final String NODE_TYPE_START = "START";
    /**
     * 节点类型，终止
     */
    public static final String NODE_TYPE_END = "END";
    /**
     * 节点类型，自动
     */
    public static final String NODE_TYPE_AUTO = "AUTO";
    /**
     * 节点类型，人工
     */
    public static final String NODE_TYPE_MANUAL = "MANUAL";
    /**
     * 节点类型，邮件
     */
    public static final String NODE_TYPE_EMAIL = "EMAIL";
    /**
     * 节点类型，消息
     */
    public static final String NODE_TYPE_MESSAGE = "MESSAGE";
    /**
     * 节点分支类型，互斥
     */
    public static final String NODE_OUT_ROUTE_TYPE_MUTEX = "MUTEX";
    /**
     * 节点发出分支类型，平行
     */
    public static final String NODE_OUT_ROUTE_TYPE_PARALLEL = "PARALLEL";
    /**
     * 任务类型，抢占
     */
    public static final String TASK_TYPE_PREEMPTIVE = "PREEMPTIVE";
    /**
     * 任务类型，会签
     */
    public static final String TASK_TYPE_COUNTERSIGN = "COUNTERSIGN";
    /**
     * 任务类型，投票
     */
    public static final String TASK_TYPE_VOTE = "VOTE";
    /**
     * 名称映射
     */
    public static final Map<String, String> NAME_MAPPING;
    static {
       NAME_MAPPING = new HashMap<String, String>();
       NAME_MAPPING.put("START", "开始");
       NAME_MAPPING.put("END", "终止");
       NAME_MAPPING.put("AUTO", "自动");
       NAME_MAPPING.put("MANUAL", "人工");
       NAME_MAPPING.put("EMAIL", "邮件");
       NAME_MAPPING.put("MESSAGE", "消息");
       NAME_MAPPING.put("MUTEX", "互斥");
       NAME_MAPPING.put("PARALLEL", "平行");
       NAME_MAPPING.put("PREEMPTIVE", "抢占");
       NAME_MAPPING.put("COUNTERSIGN", "会签");
       NAME_MAPPING.put("VOTE", "投票");
    }
}
