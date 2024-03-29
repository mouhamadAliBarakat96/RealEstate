package org.RealEstate.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.RealEstate.enumerator.Unit;
import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Utils {

	private Utils() {
		throw new IllegalStateException("Utility class");
	}

	/***
	 * transform object to string format json of any type
	 * 
	 * @param <T>
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static <T> String objectToString(T data) throws Exception {
		Gson gs = new GsonBuilder().setDateFormat(Constants.DATE_FORMAT).excludeFieldsWithoutExposeAnnotation()
				.create();
		String json = gs.toJson(data);
		return new String(json.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);

	}

	/***
	 * convert String data to object of any type
	 * 
	 * @param <T>
	 * @param type
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static <T> T getDataJsonAsObj(Class<T> type, String content) throws Exception {

		T data = null;
		JsonParser jsonParser = new JsonParser();
		if (!(content == null)) {

			if ((content.toString().equals(Constants.JSON_EMPTY_ARRAY)
					|| content.toString().endsWith(Constants.JSON_EMPTY_OBJECT))) {
				return null;
			}

			Gson gson = new GsonBuilder().setDateFormat(Constants.DATE_FORMAT).excludeFieldsWithoutExposeAnnotation()
					.create();

			JsonObject obj = (JsonObject) jsonParser.parse(content.toString());
			data = gson.fromJson(obj, type);
		}

		return data;

	}

	/***
	 * convert input stream to list of any type
	 * 
	 * @param <T>
	 * @param is
	 * @param Classtype
	 * @return
	 * @throws IOException
	 */
	public static <T> ArrayList<T> convertFromStreamDataToList(InputStream is, Class<T> Classtype) throws IOException {

		StringBuilder sb = new StringBuilder();
		ObjectMapper mapper = new ObjectMapper();

		/*
		 * 
		 * used to check if sb (json String is empty)
		 * 
		 * without check can cause excption on parse object
		 * 
		 */

		if (sb.toString().equals(Constants.JSON_EMPTY_ARRAY) || sb.toString().endsWith(Constants.JSON_EMPTY_OBJECT)) {

			return null;

		}

		sb.append(IOUtils.toString(IOUtils.toByteArray(is), Constants.FILE_ENCODING_UTF_8));
		mapper.setTimeZone(TimeZone.getDefault());

		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		TypeFactory t = TypeFactory.defaultInstance();
		return mapper.readValue(sb.toString(), t.constructCollectionType(ArrayList.class, Classtype));

	}

	public static <T> T getObjectFromStream(InputStream is, Class<T> Classtype) throws IOException {

		if (is == null) {
			return null;
		} else {
			JsonParser jsonParser = new JsonParser();
			Gson gson = new GsonBuilder().setDateFormat(Constants.DATE_FORMAT).excludeFieldsWithoutExposeAnnotation()
					.create();
			StringBuilder sb = new StringBuilder();

			sb.append(new String(IOUtils.toByteArray(is), Constants.FILE_ENCODING_UTF_8));

			if (sb.toString().equals(Constants.JSON_EMPTY_ARRAY)
					|| sb.toString().endsWith(Constants.JSON_EMPTY_OBJECT)) {

				return null;

			}

			JsonObject obj = (JsonObject) jsonParser.parse(sb.toString());

			return gson.fromJson(obj, Classtype);
		}

	}

	public static <T> T getObjectFromString(String json, Class<T> Classtype) throws IOException {

		JsonParser jsonParser = new JsonParser();
		Gson gson = new GsonBuilder().setDateFormat(Constants.DATE_FORMAT).excludeFieldsWithoutExposeAnnotation()
				.create();
		StringBuilder sb = new StringBuilder();

		if (json.equals(Constants.JSON_EMPTY_ARRAY) || sb.toString().endsWith(Constants.JSON_EMPTY_OBJECT)) {

			return null;

		}

		JsonObject obj = (JsonObject) jsonParser.parse(json);

		return gson.fromJson(obj, Classtype);

	}

	public static <T> ArrayList<T> convertFromStringJsonToList(String data, Class<T> Classtype) throws IOException {

		ArrayList<T> List = new ArrayList<T>();
		ObjectMapper mapper = new ObjectMapper();

		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setTimeZone(TimeZone.getDefault());

		TypeFactory t = TypeFactory.defaultInstance();

		List = mapper.readValue(data, t.constructCollectionType(ArrayList.class, Classtype));

		return List;
	}

	/***
	 * convert list of data( object) of any type to string format json
	 * 
	 * @param <T>
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static <T> String listToString(List<T> data) throws Exception {

		Gson gson2 = new GsonBuilder().setDateFormat(Constants.DATE_FORMAT).excludeFieldsWithoutExposeAnnotation()
				.create();
		String json = gson2.toJson(data);
		return new String(json.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);

	}
//
//	public static <T> String ObjectToString(T data) throws Exception {
//
//		Gson gs = new Gson();
//		return gs.toJson(data);
//
//	}

	public static <T> String listToString(List<T> data, String dateFormat) throws Exception {

		Gson gson2 = new GsonBuilder().setDateFormat(dateFormat).excludeFieldsWithoutExposeAnnotation().create();
		return gson2.toJson(data);

	}

	public static byte[] convertFromStringToBytes(String data) throws UnsupportedEncodingException {
		return data.getBytes(Constants.FILE_ENCODING_UTF_8);
	}

	public static String convertFromBytesToString(byte[] data) throws IOException {
		return IOUtils.toString(data, Constants.FILE_ENCODING_UTF_8);
	}

	// ------------------------------------------- begin --------------//

	// dateExceededCurrentDateByMonths
	public static boolean dateExceededCurrentDateByDays(Date date, int days) {

		if (date != null) {
			return ChronoUnit.DAYS.between(convertToLocalDateViaInstant(date),
					convertToLocalDateViaInstant(new Date())) > days;
		}

		return false;
	}

	// convertToLocalDateViaInstant
	public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	// ------------------------------------------- end --------------//

	public static int differenceBtweenDate(Unit unitDuration, Date start, Date end) {

		if (unitDuration.getKey().equals(Unit.MONTHLY.getKey())) {
			return (int) ChronoUnit.MONTHS.between(convertToLocalDateViaInstant(start),
					convertToLocalDateViaInstant(end));

		} else if (unitDuration.getKey().equals(Unit.YEARLY.getKey())) {
			return (int) ChronoUnit.YEARS.between(convertToLocalDateViaInstant(start),
					convertToLocalDateViaInstant(end));

		} else {
			return 0;
		}

	}

	public static Date getFirstDayFromMonth() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}

	public static int radnomIntBasedToDate() {
		Date currentDate = new Date();
		long timestamp = currentDate.getTime(); // Get the current timestamp in milliseconds

		Random random = new Random(timestamp); // Seed the Random object with the timestamp
		int randomNumber = random.nextInt();

		return randomNumber;
	}

	// add days
	public static Date addDaysToCurrentDate(int numberOfDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, numberOfDays); //
		return calendar.getTime();
	}

	public static Date getThresholdDate(int numberOfDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -numberOfDays); // Subtract 10 days
		return calendar.getTime();
	}
	// -------------------------- begin ------------------- //

	public static Date dateWithoutMilliseconds(Date date) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

		if (date != null) {

			String dateAsString = simpleDateFormat.format(date);
			return simpleDateFormat.parse(dateAsString);
		} else {
			return null;
		}

	}

	public static long minuteDifferenceBtw2Date(Date start, Date end) throws Exception {
		if (start != null && end != null) {
			long diff = start.getTime() - end.getTime();

			return (diff / (60 * 1000));
		} else {
			throw new Exception();
		}

	}

	public static String sha256(String base) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(base.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static boolean validatePassword(String password) {
		// Define a regular expression pattern for password validation
		String result = password.replaceAll("\\s+", "");
		String pattern = "^(?=.*[A-Z]).{8,}$";

		// Create a Pattern object
		Pattern regex = Pattern.compile(pattern);

		// Create a Matcher object
		Matcher matcher = regex.matcher(result);

		// Check if the password matches the pattern
		return matcher.matches();
	}

	public static boolean validatePhoneNumber(String input) {
		String result = input.replaceAll("\\s+", "");
		String regex = "^(\\+?961|0)?([1-9][0-9]{6}|70[0-9]{6}|71[0-9]{6}|73[0-9]{6}|76[0-9]{6}|77[0-9]{6}|78[0-9]{6}|79[0-9]{6}|81[0-9]{6}|82[0-9]{6}|88[0-9]{6})$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(result);

		return matcher.matches();
	}

	public static String replaceHost(String originalUrl, String newHost ,String  mode ) {
	    int startIndex = originalUrl.indexOf("://") + 3; // Find the start of the host
	    int endIndex = originalUrl.indexOf("/", startIndex); // Find the end of the host or the start of the path

	    if (startIndex >= 0 && endIndex >= 0) {
	        String oldHostAndPort = originalUrl.substring(startIndex, endIndex);
	        String[] parts = oldHostAndPort.split(":", 2); // Split host and port
	        String oldHost = parts[0];

	        
	        
	        if (  mode.equals(Constants.PRODUCATION) && parts.length > 1) {
	            // Check if the part after colon is a valid port
	            String portPart = parts[1];
	            if (portPart.matches("\\d{1,4}")) {
	                // If valid port, replace host and port with new host
	                return originalUrl.replace(oldHostAndPort, newHost);
	            }
	        } else {
	            // If no port, replace only host with new host
	            return originalUrl.replace(oldHost, newHost);
	        }
	    }

	    return originalUrl;
	}
	
	public static String generateRandomStringBasedOnTime() {
		// Get the current timestamp
		long timestamp = System.currentTimeMillis();

		// Initialize a random number generator with the timestamp as the seed
		Random random = new Random(timestamp);

		// Define the characters that can be used in the random string
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

		// Set the length of the random string you want to generate
		int length = 10; // Change this to your desired length

		// Generate the random string
		StringBuilder randomString = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(characters.length());
			char randomChar = characters.charAt(randomIndex);
			randomString.append(randomChar);
		}

		return randomString.toString();
	}
}
