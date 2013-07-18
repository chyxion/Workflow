package com.shs.commons.workflow.instance.models;
import org.json.JSONObject;

import com.shs.commons.models.User;

public class WfTask {
    /**
     * 任务分配,尚未接受
     */
    public static final String STATUS_ASSIGNED = "ASSIGNED";
    /**
     * 接受任务
     */
    public static final String STATUS_ACCEPTED = "ACCEPTED";
    /**
     * 完成任务
     */
    public static final String STATUS_COMPLETED = "COMPLETED";
    /**
     * 任务被他人抢占, 针对于抢占式（有一个接受即可）任务
     */
    public final static String STUTAS_DEAD = "DEAD";
    /**
     * 任务被忽略,针对于投票式（按照比例投票）任务，达到比例后，该任务被忽略
     */
    public static final String STATUS_VOTE_COMPLETED = "VOTE_COMPLETED";
    
    /**
     * task id
     */
    private String id;
    /**
     * 所属节点
     */
    private WfNode node;
    /**
     * 任务执行人
     */
    private User user;
    /**
     * 任务状态
     */
    private String status;
    /**
     * 任务创建时间
     */
    private String assignTime;
    /**
     * 任务接受时间
     */
    private String acceptTime;
    /**
     * 任务完成时间
     */
    private String doneTime;
    /**
     * 描述
     */
    private String note;
    /**
     * 办理结果，意见
     */
    private String opinion;
    
    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public WfNode getNode() {
        return node;
    }

    public void setNode(WfNode node) {
        this.node = node;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignTime() {
        return assignTime;
    }

    public void setAssignTime(String assignTime) {
        this.assignTime = assignTime;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getDoneTime() {
        return doneTime;
    }

    public void setDoneTime(String doneTime) {
        this.doneTime = doneTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public String getOpinion() {
		return opinion;
	}
	public JSONObject toJO() throws Exception {
		return new JSONObject().put("ID", id)
			.put("USER_ID", user.getID())
			.put("NODE_ID", node.getID())
			.put("ACCEPT_TIME", acceptTime)
			.put("ASSIGN_TIME", assignTime)
			.put("DONE_TIME", doneTime)
			.put("OPINION", opinion)
			.put("STATUS", status);
	}
}
