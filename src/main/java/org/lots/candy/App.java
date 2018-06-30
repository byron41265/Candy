package org.lots.candy;
import org.lots.candy.utils.LotsBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

@SpringBootApplication
public class App 
{
	public static void main(String[] args) {
		
		ApiContextInitializer.init();
		TelegramBotsApi botsApi = new TelegramBotsApi();
		
		ApplicationContext ctx = SpringApplication.run(App.class, args);
		LotsBot bot = ctx.getBean(LotsBot.class);
		
		try {
			botsApi.registerBot(bot);
		} catch (TelegramApiRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
