package org.RealEstate.enumerator;

public enum PostStatus {
	ACCEPTED, PENDING, TO_REVIEUX_BY_USER, REFFUSED, EXPIRED;

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

}
