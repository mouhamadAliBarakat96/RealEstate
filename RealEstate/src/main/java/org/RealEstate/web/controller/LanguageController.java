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
		this.languageEnum = lang;
	}
}
