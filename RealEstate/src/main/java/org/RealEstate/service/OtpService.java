package org.RealEstate.service;

import javax.ejb.Stateless;

import com.unimtx.Uni;
import com.unimtx.UniException;
import com.unimtx.UniResponse;
import com.unimtx.model.UniMessage;
import com.unimtx.model.UniOtp;

@Stateless
public class OtpService {
	

	private static String ACCESS_KEY_ID = "EEWLNGdpyA3CBYr4p8ewCK";
	private static String ACCESS_KEY_SECRET = "";

	public void sendSms (String phoneNumber ) {
		Uni.init(ACCESS_KEY_ID); // if using simple auth mode, just pass in the first parameter

		 UniMessage message = UniMessage.build()
		            .setTo(phoneNumber) // in E.164 format
		            .setText("Your verification code is 2048.");
		

	        try {
	            UniResponse res = message.send();
	            System.out.println(res.data);
	        } catch (UniException e) {
	            System.out.println("Error: " + e);
	            System.out.println("RequestId: " + e.requestId);
	        }


	}
	
	public void sendOtp(String phoneNumber) throws Exception {
		if (phoneNumber != null) {

			Uni.init(ACCESS_KEY_ID); // if using simple auth mode, just pass in the first parameter
			UniResponse ui = UniOtp.build().setTo(phoneNumber).send();
			System.out.println(ui.data);
		} else {
			throw new Exception("USER_PHONE_NUMBER_DOESNT_EXIST");
		}

	}

	public boolean validOtp(String phoneNumber, String otp) {

		Uni.init(ACCESS_KEY_ID, ACCESS_KEY_SECRET); // if using simple auth mode, just pass in the first parameter

		UniResponse res = UniOtp.build().setTo(phoneNumber).setCode(otp) // the code user provided
				.verify();

		System.out.println(res.valid);
		return res.valid;
	}
	
	

}
