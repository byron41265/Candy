package org.lots.candy.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {
	@Autowired
	private ConnectionRepository connectionRepository;
	
	
//	public UserController(Twitter twitter,ConnectionRepository connectionRepository) {
//		this.twitter = twitter;
//		this.connectionRepository = connectionRepository;
//	}
	
	
	@RequestMapping(value="/{providerId}", method=RequestMethod.GET)
	public String bindcallback(@PathVariable String providerId, Model model){
		
		if("twitter".equals(providerId)){
			Connection<Twitter> connection = connectionRepository.findPrimaryConnection(Twitter.class);
			if(connection != null){
				Twitter twitter = connection.getApi();
				System.out.println(twitter.userOperations().getProfileId());
				System.out.println(twitter.userOperations().getUserProfile().getId());
				System.out.println(twitter.userOperations().getUserProfile().getName());
				System.out.println(twitter.userOperations().getUserProfile().getScreenName());
				model.addAttribute("bindmsg", "Twitter Bind Success");
			}
		}
		

//	        model.addAttribute("name",twitter.userOperations().getUserProfile().getName());
//	        model.addAttribute("id", twitter.userOperations().getUserProfile().getId());
//	        CursoredList<TwitterProfile> friends = twitter.friendOperations().getFriends();
//	        model.addAttribute("friends", friends.size());
	        
		return "index";
	}
	@RequestMapping("/")
	public String init(){
		return "index";
	}

}
