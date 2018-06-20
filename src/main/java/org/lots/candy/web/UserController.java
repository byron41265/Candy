package org.lots.candy.web;

import org.lots.candy.domain.UserMapper;
import javax.inject.Inject;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;

@Controller
public class UserController {
	@Autowired
	private Twitter twitter;
	@Autowired
	private ConnectionRepository connectionRepository;
	
	
//	public UserController(Twitter twitter,ConnectionRepository connectionRepository) {
//		this.twitter = twitter;
//		this.connectionRepository = connectionRepository;
//	}
	
	@Autowired
	private UserMapper userMapper;
	
	@RequestMapping("/")
	public String index(Model model){
		if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
	            return "redirect:/connect/twitter";
	        }

	        model.addAttribute("name",twitter.userOperations().getUserProfile().getName());
	        model.addAttribute("id", twitter.userOperations().getUserProfile().getId());
	        CursoredList<TwitterProfile> friends = twitter.friendOperations().getFriends();
	        model.addAttribute("friends", friends.size());
	        
		return "index";
	}
	@RequestMapping("/callback")
	public void callback(){
		System.out.println("callback called");
	}
	
	

}
