package org.RealEstate.enumerator;

import org.RealEstate.utils.Utility;

public enum YesNoEnum {

	YES(Utility.getMessage("answer_yes"),Utility.getMessage_ar("answer_yes")),
	NO(Utility.getMessage("answer_no"),Utility.getMessage_ar("answer_no"));

	private String answer;
	private String answer_ar;
	private YesNoEnum(String answer,String answer_ar) {
		// TODO Auto-generated constructor stub
		this.answer = answer;
		this.answer_ar=answer_ar;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getAnswer_ar() {
		return answer_ar;
	}

	public void setAnswer_ar(String answer_ar) {
		this.answer_ar = answer_ar;
	}

}
