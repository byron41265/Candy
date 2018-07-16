package org.lots.candy.utils;

import java.util.List;

import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfile;
import org.springframework.social.linkedin.api.LinkedInProfileFull;
import org.springframework.social.linkedin.api.PhoneNumber;
import org.springframework.stereotype.Component;


@Component
public class LinkedinHelper {
	
	public void saveLinkedinUser(String userId , LinkedIn linkedin){
		LinkedInProfileFull profile = linkedin.profileOperations().getUserProfileFull();
		System.out.println(profile.getId());
		List<PhoneNumber> nums = profile.getPhoneNumbers();
//		for(PhoneNumber phone : nums){
//			System.out.println(phone.getPhoneNumber());
//		}
		
	}

}
