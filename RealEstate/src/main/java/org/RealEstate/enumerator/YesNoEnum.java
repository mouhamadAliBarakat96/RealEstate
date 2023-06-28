package org.RealEstate.enumerator;

import org.RealEstate.utils.Utility;

public enum YesNoEnum {

	YES(Utility.getMessage("answer_yes")), NO(Utility.getMessage("answer_no"));

	private String answer;

	private YesNoEnum(String answer) {
		// TODO Auto-generated constructor stub
		this.answer = answer;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
