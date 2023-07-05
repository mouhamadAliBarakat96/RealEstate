package org.RealEstate.enumerator;

import org.RealEstate.utils.Utility;

public enum LanguageEnum {
	LTR(Utility.getMessage("lang_en")), RTL(Utility.getMessage("lang_ar"));

	private String lang;

	private LanguageEnum(String lang) {
		this.lang = lang;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

}
