package org.RealEstate.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.RealEstate.enumerator.PostType;
import org.RealEstate.model.AppratmentRent;
import org.RealEstate.model.AppratmentSell;
import org.RealEstate.model.Land;
import org.RealEstate.model.OfficeRent;
import org.RealEstate.model.OfficeSell;
import org.RealEstate.model.RealEstate;
import org.RealEstate.model.ShopRent;
import org.RealEstate.model.ShopSell;
import org.RealEstate.model.StoreHouseSell;

public class Utility {

	public final static String BUNDLE_FILE_NAME_AR = "resources.bundle_ar";
	public final static String BUNDLE_FILE_NAME = "resources.bundle";
	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");


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

	public static String getMessage_ar(String key) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_FILE_NAME_AR);
		MessageFormat msgFormat = new MessageFormat(resourceBundle.getString(key));
		return msgFormat.format(null);
	}

	public static void addSuccessMessage(String key, Locale local) {
		try {
			if (local.getLanguage().equals("en"))
				key = getMessage(key, BUNDLE_FILE_NAME);
			else if (local.getLanguage().equals("ar"))
				key = getMessage(key, BUNDLE_FILE_NAME_AR);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			FacesMessage msg = new FacesMessage(key, "success");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public static void addErrorMessage(String key,Locale local) {
		try {
			if (local.getLanguage().equals("en"))
				key = getMessage(key, BUNDLE_FILE_NAME);
			else if (local.getLanguage().equals("ar"))
				key = getMessage(key, BUNDLE_FILE_NAME_AR);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			FacesMessage msg = new FacesMessage(key, "error");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public static void addWarningMessage(String key,Locale local) {
		try {
			if (local.getLanguage().equals("en"))
				key = getMessage(key, BUNDLE_FILE_NAME);
			else if (local.getLanguage().equals("ar"))
				key = getMessage(key, BUNDLE_FILE_NAME_AR);
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

	public static RealEstate castingObject(RealEstate item) {
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

	public static RealEstate initializeRealEstate(PostType postType) {
		switch (postType) {
		case APPRATMENT_RENT:
			return new AppratmentRent();
		case APPRATMENT_SELL:
			return new AppratmentSell();
		case OFFICE_RENT:
			return new OfficeRent();
		case OFFICE_SELL:
			return new OfficeSell();
		case SHOP_RENT:
			return new ShopRent();
		case SHOP_SELL:
			return new ShopSell();
		case LAND:
			return new Land();
		case STORE_HOUSE_SELL:
			return new StoreHouseSell();
		case STORE_HOUSE_RENT:
			return new StoreHouseSell();
		default:
			return null;
		}
	}

	public static String hashPassword(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			StringBuilder hexString = new StringBuilder(2 * hash.length);
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			// handle the exception
			return null;
		}
	}
}
