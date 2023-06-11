package org.RealEstate.utils;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Utility {

	public final static String BUNDLE_FILE_NAME_EN = "resources.Bundle-en";
	public final static String BUNDLE_FILE_NAME = "resources.Bundle";
	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

	public static void addMessageToFacesContext(String key) {

	}

	public static void addSuccessMessage(String key) {
		try {
			key = getMessage(key, BUNDLE_FILE_NAME);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			FacesMessage msg = new FacesMessage(key, "success");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public static String getMessage(String key, String bundle) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle(bundle);
		MessageFormat msgFormat = new MessageFormat(resourceBundle.getString(key));
		return msgFormat.format(null);
	}
	
	public static String getMessage(String key) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_FILE_NAME);
		MessageFormat msgFormat = new MessageFormat(resourceBundle.getString(key));
		return msgFormat.format(null);
	}

	public static void addErrorMessage(String key) {
		try {
			key = getMessage(key, BUNDLE_FILE_NAME);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			FacesMessage msg = new FacesMessage(key, "error");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public static void addWarningMessage(String key) {
		try {
			key = getMessage(key, BUNDLE_FILE_NAME);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			FacesMessage msg = new FacesMessage(key, "warn");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public static void addMessage(String msg) {
		FacesMessage facesMessage = new FacesMessage(msg, "warn");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}
}
