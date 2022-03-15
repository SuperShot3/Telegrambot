package com.example.Corebot;

import com.example.Corebot.Bot.BotLogic;
import com.example.Corebot.service.MessageReceiver;
import com.example.Corebot.service.MessageSender;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import static java.lang.System.getenv;

@SpringBootApplication
public class Main {

	private static final Logger log = Logger.getLogger(Main.class.getName());
	private static final int PRIORITY_FOR_SENDER = 1;
	private static final int PRIORITY_FOR_RECEIVER = 3;
	private static final String BOT_ADMIN = "961020144"; // my ID Mr.K

	public static void main(String[] args) throws TelegramApiException {
		SpringApplication.run(Main.class, args);

		BotLogic botLogic = new BotLogic(getenv("BOT_USERNAME"), getenv("TOKEN"));
		final int RECONNECT_PAUSE = 10000;

		MessageReceiver messageReceiver = new MessageReceiver(botLogic);
		MessageSender messageSender = new MessageSender(botLogic);
		botLogic.botConnect();


		Thread receiver = new Thread(messageReceiver);
		receiver.setDaemon(true);
		receiver.setName("MsgReceiver");
		receiver.setPriority(PRIORITY_FOR_RECEIVER);
		receiver.start();

		Thread sender = new Thread(messageSender);
		sender.setDaemon(true);
		sender.setName("MsgSender");
		sender.setPriority(PRIORITY_FOR_SENDER);
		sender.start();
		sendStartReport(botLogic);
	}
	private static void sendStartReport(BotLogic bot) throws TelegramApiException {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(BOT_ADMIN);
		sendMessage.setText("Запустился");
		bot.execute(sendMessage);

	}
}

