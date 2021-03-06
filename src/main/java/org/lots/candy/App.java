package org.lots.candy;
import org.lots.candy.utils.LotsBot;
import org.lots.candy.web.SendEmailUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

@SpringBootApplication
@EnableScheduling
@ImportResource(locations={"classpath:myKaptcha.xml"})
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
			e.printStackTrace();
		}

	}
}
