package org.RealEstate.web.controller;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.RealEstate.enumerator.LanguageEnum;
import org.RealEstate.utils.Utility;

@SessionScoped
@Named
public class LanguageController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LanguageEnum languageEnum = LanguageEnum.LTR;
	private String bundle_name = "resources.bundle";
	private Locale locale = new Locale("en");

	public void switchLangauge(LanguageEnum lang) {
		if (lang == LanguageEnum.RTL) {
			locale = new Locale("ar");
			FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
//			Utility.changeBundleName("resources.bundle_ar");
			setBundle_name("resources.bundle_ar");
		} else {
			locale = new Locale("en");
			FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
//			Utility.changeBundleName("resources.bundle");
			setBundle_name("resources.bundle") ;
		}

		this.languageEnum = lang;
	}

	public void changeLang(LanguageEnum lang) {
		if (lang == LanguageEnum.RTL) {
			setBundle_name("resources.bundle_ar");
//			Utility.changeBundleName("resources.bundle_ar");
		} else {
			setBundle_name("resources.bundle");
//			Utility.changeBundleName("resources.bundle");
		}
		this.languageEnum = lang;
	}

	public String getMessage(String key) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle(bundle_name);
		MessageFormat msgFormat = new MessageFormat(resourceBundle.getString(key));
		return msgFormat.format(null);
	}

	public LanguageEnum getLanguageEnum() {
		return languageEnum;
	}

	public void setLanguageEnum(LanguageEnum languageEnum) {
		this.languageEnum = languageEnum;
	}

	public String getBundle_name() {
		return bundle_name;
	}

	public void setBundle_name(String bundle_name) {
		this.bundle_name = bundle_name;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
