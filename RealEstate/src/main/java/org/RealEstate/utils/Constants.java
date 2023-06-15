package org.RealEstate.utils;

public interface Constants {

	public final String DATE_FORMAT_TO_JSON = "yyyy-MM-dd";
	public final String FILE_ENCODING_UTF_8 = "UTF-8";
	public final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public final String DATE_FORMAT_TO_USER = "dd-MM-yyyy";
	public final String DATE_FORMAT_CARD = "yy-M-dd HH:mm:ss";

	public final String JSON_EMPTY_ARRAY = "[]";
	public final String JSON_EMPTY_OBJECT = "{}";

	public final String UPLOAD_DIR = determineUploadDir();

	static String determineUploadDir() {
		String os = System.getProperty("os.name").toLowerCase();

		if (os.contains("win")) {
			return "C:\\var\\webapp\\images\\";
		} else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
			return "var/webapp/images/";
		} else {
			throw new UnsupportedOperationException("Unsupported operating system.");
		}
	}

	final String POST_IMAGE_DIR_NAME = "POST";
	final String PROFILE_IMAGE_DIR_NAME = "PROFILE";

	public static final String EMPTY_REQUEST_DONT_CONTAIN_DATA = "EMPTY_REQUEST_DONT_CONTAIN_DATA";
	public static final String POST_TYPE_NOT_SUPPORTED = "POST_TYPE_NOT_SUPPORTED";

	public static final String NB_OF_IMAGE_GREATER_NUMBER_OF_IMAGE_ALLOWED = "NB_OF_IMAGE_GREATER_NUMBER_OF_IMAGE_ALLOWED";
	public static final String AT_LAST_ONE_IMAGE_REQUIRED = "AT_LAST_ONE_IMAGE_REQUIRED";

	public static final int NB_IMAGE_IN_POST_ALLOWED = 5;
}
