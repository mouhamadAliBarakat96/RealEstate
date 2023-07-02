package org.RealEstate.enumerator;

import org.RealEstate.utils.Utility;

public enum PostStatus {
	ACCEPTED(Utility.getMessage("post_status_accepted")),
	PENDING(Utility.getMessage("post_status_pending")),
	TO_REVIEUX_BY_USER(Utility.getMessage("post_status_to_review")),
	REFFUSED(Utility.getMessage("post_status_refused")),
	EXPIRED(Utility.getMessage("post_status_expired"));

	 
	private String status;

	private PostStatus(String status) {
		// TODO Auto-generated constructor stub
		this.status = status;
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

}
