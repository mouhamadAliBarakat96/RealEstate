package org.RealEstate.enumerator;

import org.RealEstate.utils.Utility;

public enum PostStatus {
	ACCEPTED(Utility.getMessage("post_status_accepted"),Utility.getMessage("post_status_accepted",Utility.BUNDLE_FILE_NAME_AR)),
	PENDING(Utility.getMessage("post_status_pending"),Utility.getMessage("post_status_pending",Utility.BUNDLE_FILE_NAME_AR)),
	TO_REVIEUX_BY_USER(Utility.getMessage("post_status_to_review"),Utility.getMessage("post_status_to_review",Utility.BUNDLE_FILE_NAME_AR)),
	REFFUSED(Utility.getMessage("post_status_refused"),Utility.getMessage("post_status_refused",Utility.BUNDLE_FILE_NAME_AR)),
	EXPIRED(Utility.getMessage("post_status_expired"),Utility.getMessage("post_status_expired",Utility.BUNDLE_FILE_NAME_AR));

	 
	private String status;
	private String status_ar;

	private PostStatus(String status,String status_ar) {
		// TODO Auto-generated constructor stub
		this.status = status;
		this.status_ar = status_ar;
	}
	
	
	public static PostStatus findEnum(String postStatus) {
		switch (postStatus) {
		case "ACCEPTED":
			return ACCEPTED;
			
		case "PENDING":
			return PENDING;
			
		case "TO_REVIEUX_BY_USER":
			return TO_REVIEUX_BY_USER;

		case "REFFUSED":
			return REFFUSED;

		case "EXPIRED":
			return EXPIRED;

		default:
			return null;
		}
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getStatus_ar() {
		return status_ar;
	}


	public void setStatus_ar(String status_ar) {
		this.status_ar = status_ar;
	}

}
