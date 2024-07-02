package com.axonivy.utils.bpmnstatistic.bo;

import java.io.Serializable;
import java.time.Duration;
import java.util.Date;

public class WorkflowProgress implements Serializable {
	private static final long serialVersionUID = 932880898772989547L;
	private String processRawPid;
	private String arrowId;
	private String originElementId;
	private String targetElementId;
	private Date startTimeStamp;
	private Date endTimeStamp;
	private Duration duration;
	private Long caseId;

	public WorkflowProgress(String processRawPid, String arrowId, String originElementId, String targetElementId,
			Long caseId) {
		this.processRawPid = processRawPid;
		this.arrowId = arrowId;
		this.originElementId = originElementId;
		this.targetElementId = targetElementId;
		this.caseId = caseId;
		this.startTimeStamp = new Date();
	};

	public WorkflowProgress() {
	}

	public Long getCaseId() {
		return caseId;
	}

	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}

	public Date getStartTimeStamp() {
		return startTimeStamp;
	}

	public void setStartTimeStamp(Date startTimeStamp) {
		this.startTimeStamp = startTimeStamp;
	}

	public Date getEndTimeStamp() {
		return endTimeStamp;
	}

	public void setEndTimeStamp(Date endTimeStamp) {
		this.endTimeStamp = endTimeStamp;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public String getProcessRawPid() {
		return processRawPid;
	}

	public void setProcessRawPid(String processRawPid) {
		this.processRawPid = processRawPid;
	}

	public String getArrowId() {
		return arrowId;
	}

	public void setArrowId(String arrowId) {
		this.arrowId = arrowId;
	}

	public String getOriginElementId() {
		return originElementId;
	}

	public void setOriginElementId(String originElementId) {
		this.originElementId = originElementId;
	}

	public String getTargetElementId() {
		return targetElementId;
	}

	public void setTargetElementId(String targetElementId) {
		this.targetElementId = targetElementId;
	}
}
