package org.RealEstate.utils;

public interface Constants {

	public final String DATE_FORMAT_TO_JSON = "yyyy-MM-dd";
	public final String FILE_ENCODING_UTF_8 = "UTF-8";
	public final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public final String DATE_FORMAT_TO_USER = "dd-MM-yyyy";
	public final String DATE_FORMAT_CARD = "yy-M-dd HH:mm:ss";
	public final String USER_SESSION = "USER_SESSION";
	public final String CURRENT_URL = "current_url";
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

	final String IMAGES = "images";
	final String POST_IMAGE_DIR_NAME = "POST";
	final String PROFILE_IMAGE_DIR_NAME = "PROFILE";
	final String ADS_IMAGE_DIR_NAME = "ADS";

	
	
	public static final String EMPTY_REQUEST_DONT_CONTAIN_DATA = "EMPTY_REQUEST_DONT_CONTAIN_DATA";
	public static final String POST_TYPE_NOT_SUPPORTED = "POST_TYPE_NOT_SUPPORTED";

	public static final String NB_OF_IMAGE_GREATER_NUMBER_OF_IMAGE_ALLOWED = "NB_OF_IMAGE_GREATER_NUMBER_OF_IMAGE_ALLOWED";
	public static final String ONLY_ONE_IMAGE_ALLOWED_FOR_USER = "ONLY_ONE_IMAGE_ALLOWED_FOR_USER";

	public static final String AT_LAST_ONE_IMAGE_REQUIRED = "AT_LAST_ONE_IMAGE_REQUIRED";
	public static final String PHONE_NUMBER_NOT_CORRECT = "PHONE_NUMBER_NOT_CORRECT";
	public static final String USER_NAME_FIRST_NAME_MIDDLE_NAME_LAST_NAME_SHOULD_NOT_BE_EMPTY = "USER_NAME_FIRST_NAME_MIDDLE_NAME_LAST_NAME_PASSWORD_SHOULD_NOT_BE_EMPTY";
	public static final String USER_ID_SHOULD_BE_GREATER_THAN_ZERO = "USER_ID_SHOULD_BE_GREATER_THAN_ZERO";

	
	public static final String FACEBOOK_ID_SHOUD_NOT_BE_NULL = "FACEBOOK_ID_SHOUD_NOT_BE_NULL";

	
	public static final String USER_NAME_SHOULD_BE_UNIQUE = "USER_NAME_SHOULD_BE_UNIQUE";
	public static final String FACEBOOK_USER_HAVE_ACCOUNT = "FACEBOOK_USER_HAVE_ALREADY_ACCOUNT";

	
	public static final int NB_IMAGE_IN_POST_ALLOWED = 5;

	public static final String USER_NOT_EXISTS = "USER_NOT_EXISTS";
	public static final String VILLAGE_NOT_EXISTS = "VILLAGE_NOT_EXISTS";
	public static final String DISTRICT_NOT_EXISTS = "DISTRICT_NOT_EXISTS";
	public static final String GOVERNORTE_NOT_EXISTS = "GOVERNORTE_NOT_EXISTS";
	public static final String USER_NAME_OR_PASSWORD_INVALID = "USER_NAME_OR_PASSWORD_INVALID";
	public static final String EXCEEDED_POST_LIMIT_FOR_THIS_USER = "EXCEEDED_POST_LIMIT_for_this_user" ;

	public static final String FACEBOOK_ID_INVALID = "FACEBOOK_ID_INVALID";


	
}
