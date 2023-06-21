package org.RealEstate.utils;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.RealEstate.enumerator.PostType;
import org.RealEstate.enumerator.RealEstateTypeEnum;
import org.RealEstate.model.AppratmentRent;
import org.RealEstate.model.AppratmentSell;
import org.RealEstate.model.Land;
import org.RealEstate.model.OfficeRent;
import org.RealEstate.model.OfficeSell;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.ShopRent;
import org.RealEstate.model.ShopSell;

public class Utility {

	public final static String BUNDLE_FILE_NAME_AR = "resources.Bundle-ar";
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
	
	public static RealEstate createObject(RealEstate item) {
		switch (item.getPostType()) {
		
		case APPRATMENT_RENT:
			return (AppratmentRent) item;

		case APPRATMENT_SELL:
			return (AppratmentSell) item;

		case OFFICE_RENT:
			return (OfficeRent) item;

		case OFFICE_SELL:
			return (OfficeSell) item;

		case SHOP_RENT:
			return (ShopRent) item;
		case SHOP_SELL:
			return (ShopSell) item;
		case LAND:
			return (Land) item;
		default:
			return null;
		}
	}
}
