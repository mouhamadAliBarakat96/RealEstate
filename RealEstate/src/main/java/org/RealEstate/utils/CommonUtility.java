package org.RealEstate.utils;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public class CommonUtility {
	public static void addMessageToFacesContextFromBundle(String message, String bundle, String severity) {
		FacesMessage facesMessage = new FacesMessage();
		facesMessage.setSummary(CommonUtility.getMessage(message, bundle));
		facesMessage.setDetail(severity);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
		ex.getFlash().setKeepMessages(true);
	}

	public static void addMessageToFacesContext(String message, String severity) {
		FacesMessage facesMessage = new FacesMessage();
		facesMessage.setSummary(message);
		facesMessage.setDetail(severity);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public static String getMessage(String key, String bundle) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle(bundle);
		MessageFormat msgFormat = new MessageFormat(resourceBundle.getString(key));
		return msgFormat.format(null);
	}
}