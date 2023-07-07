package org.RealEstate.web.controller;

import java.io.Serializable;
import java.util.Locale;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.RealEstate.enumerator.LanguageEnum;

@SessionScoped
@Named
public class LanguageController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LanguageEnum languageEnum = LanguageEnum.LTR;
	private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
	private String bundle_name = "resources.Bundle";

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
	}

	public LanguageEnum getLanguageEnum() {
		return languageEnum;
	}

	public void setLanguageEnum(LanguageEnum languageEnum) {
		this.languageEnum = languageEnum;
	}

	public void changeLang(LanguageEnum lang) {
		if (lang == LanguageEnum.RTL) {
			setBundle_name("resources.Bundle-ar");
		} else {
			setBundle_name("resources.Bundle");
		}
		this.languageEnum = lang;
	}

	public String getBundle_name() {
		return bundle_name;
	}

	public void setBundle_name(String bundle_name) {
		this.bundle_name = bundle_name;
	}

}
