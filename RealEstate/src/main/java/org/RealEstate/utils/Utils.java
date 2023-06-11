package org.RealEstate.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
		return gs.toJson(data);

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


			if (json.equals(Constants.JSON_EMPTY_ARRAY)
					|| sb.toString().endsWith(Constants.JSON_EMPTY_OBJECT)) {

				return null;

			}

			JsonObject obj = (JsonObject) jsonParser.parse(sb.toString());

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
		return gson2.toJson(data);

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
}
