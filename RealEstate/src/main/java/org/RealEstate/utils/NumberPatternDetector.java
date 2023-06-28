package org.RealEstate.utils;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberPatternDetector implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static boolean checkTextContainNumber(String text) {
		return detectPattern1(text) || detectPattern2(text) || detectPattern3(text) || detectPattern4(text);
	}

	// pattern 71006196
	public static boolean detectPattern1(String text) {
		// Matches 8-digit numbers
		Pattern pattern = Pattern.compile("\\b\\d{8}\\b");
		Matcher matcher = pattern.matcher(text);
		return matcher.find();
	}

//01-543128	
	public static boolean detectPattern2(String text) {
		// Matches 2-digit numbers separated by a hyphen, followed by 6-digit numbers
		Pattern pattern = Pattern.compile("\\b\\d{2}-\\d{6}\\b");
		Matcher matcher = pattern.matcher(text);
		return matcher.find();
	}

//34567890 
	public static boolean detectPattern3(String text) {
		// Matches any 2-digit numbers separated by any word, followed by an 8-digit
		// number
		Pattern pattern = Pattern.compile("\\b\\d{2}\\S+\\d{8}\\b");
		Matcher matcher = pattern.matcher(text);
		return matcher.find();
	}

	// Matches any 2 numbers, any character, followed by 6 digits
	public static boolean detectPattern4(String text) {
		// Matches any 2 numbers, any character, followed by 6 digits
		Pattern pattern = Pattern.compile("\\b\\d{2}.\\d{6}\\b");
		Matcher matcher = pattern.matcher(text);
		return matcher.find();
	}
}
