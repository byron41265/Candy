package org.lots.candy.utils;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.lots.candy.domain.TelegramMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.api.objects.User;

@Component
public class LotsBot extends TelegramLongPollingBot  {
	
	@Autowired
	private TelegramMapper  telegramMapper;

	@Override
	public String getBotUsername() {
		return "lotsglobal_bot";
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage()) {
			Message msg = update.getMessage();
			List<User> users = msg.getNewChatMembers();
			if (users != null){
				for(User user : users){
					User invitor = msg.getFrom();
					System.out.println(invitor.getUserName() +"invited" + user.getUserName());
					telegramMapper.insertInvitation(String.valueOf(invitor.getId()), String.valueOf(user.getId()));
				}
			}
		}

	}

	@Override
	public String getBotToken() {
		return "573152760:AAEx_v-tlAovY_yLMgBiFJv87zASE8jK-l4";
	}



}
