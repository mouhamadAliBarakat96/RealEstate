package org.RealEstate.enumerator;

import org.RealEstate.utils.Utility;

public enum LanguageEnum {
	LTR(Utility.getMessage("lang_en"),Utility.getMessage("lang_en")),
	RTL(Utility.getMessage("lang_ar"),Utility.getMessage("lang_en"));

	private String lang;
	private String lang_ar;

	private LanguageEnum(String lang,String lang_ar) {
		this.lang = lang;
		this.lang_ar = lang_ar;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getLang_ar() {
		return lang_ar;
	}

	public void setLang_ar(String lang_ar) {
		this.lang_ar = lang_ar;
	}

}
