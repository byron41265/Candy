package org.lots.candy.utils;

import org.lots.candy.domain.TaskMapper;
import org.lots.candy.domain.UserMapper;
import org.lots.candy.entity.TaskEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Component;

@Component
public class FacebookHelper {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private TaskMapper taskMapper;
	
	public  void saveFacebookUser(String userId , Facebook facebook){
		
		String userProviderId = facebook.userOperations().getUserProfile().getId();
		userMapper.updateAccount(userId, "facebook", userProviderId);
		taskMapper.insertOnceTask(userId, TaskEnum.BINDFACEBOOK.toString());
		System.out.println(facebook.userOperations().getUserProfile().getName());
		
	
	}

}
