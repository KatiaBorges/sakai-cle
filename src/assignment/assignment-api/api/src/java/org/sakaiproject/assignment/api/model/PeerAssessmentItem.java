package org.sakaiproject.assignment.api.model;

import java.io.Serializable;

public class PeerAssessmentItem implements Serializable{

	private static final long serialVersionUID = -8376570648172966170L;
	private String assignmentId;
	private String submissionId;
	private String assessorUserId;
	private Integer score;
	private String comment;
	private boolean removed;
	//submitted is only a flag to help with the UI show/hide reviews
	//that the user still needs to complete (more of a hide flag than a submit)
	private boolean submitted;
	//transient variables for displaying information in the UI
	private String assessorDisplayName;
	
	public String getSubmissionId() {
		return submissionId;
	}
	public void setSubmissionId(String submissionId) {
		this.submissionId = submissionId;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public boolean isRemoved() {
		return removed;
	}
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}
	public String getAssessorUserId() {
		return assessorUserId;
	}
	public void setAssessorUserId(String assessorUserId) {
		this.assessorUserId = assessorUserId;
	}
	public String getAssignmentId() {
		return assignmentId;
	}
	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}
	//score is stored as a integer value in the DB, but is really a decimal value (divide by 10)
	public String getScoreDisplay(){
		return getScore() == null ? "" : "" + score/10.0;
	}
	//transient variable that is only set for UI
	public String getAssessorDisplayName(){
		return assessorDisplayName;
	}
	//transient variable that is only set for UI
	public void setAssessorDisplayName(String assessorDisplayName) {
		this.assessorDisplayName = assessorDisplayName;
	}
	public boolean isSubmitted() {
		return submitted;
	}
	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}
}
