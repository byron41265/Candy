package org.lots.candy.utils;

import javax.servlet.http.HttpSession;

import org.lots.candy.config.Constant;
import org.lots.candy.domain.TaskMapper;
import org.lots.candy.domain.UserMapper;
import org.lots.candy.entity.TaskEnum;
import org.lots.candy.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;


@Controller
public class TelegramHelper {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private TaskMapper taskMapper;
	
	@RequestMapping("/tgauth")
	public String auth(NativeWebRequest request, HttpSession session, Model model ){
		User user= (User)session.getAttribute(Constant.USER_SESSION_NAME);
		String userId = user.getUserId();
		String tgId = request.getParameter("id");
		String providerId = "telegram";
		userMapper.updateAccount(userId, providerId, tgId);
		taskMapper.insertOnceTask(userId, TaskEnum.BINDTELEGRAM.toString());
		model.addAttribute("providerId", providerId);
		model.addAttribute("bindmsg", providerId +" Bind Success");
		
		return "connect/connectAll";
	}

}
