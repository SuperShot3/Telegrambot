package com.example.Corebot.Greetings;


import com.example.Corebot.Bot.BotLogic;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Locale;

public class GreetingClass {
    private static final Logger log = Logger.getLogger(GreetingClass.class);
    String [] my = {"hello","hi","hello","привет","ghbdtn"};
    BotLogic bot;
    public GreetingClass(BotLogic bot) {
        this.bot = bot;
    }

    public void SayHello (Update update)  {
        log.info("Say Hello Method ");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        if(update.getMessage().hasText()){
            String userInput = update.getMessage().getText().toLowerCase(Locale.ROOT).trim();
            for (String s : my) {
                if (userInput.equals(s)) {
                    sendMessage.setText("Hello " + update.getMessage().getFrom().getUserName());
                    break;
                } else {
                    sendMessage.setText("I don't understand you yet, please try command /start first");
                }
            }
        }
        try
        {
            bot.execute(sendMessage);
        }catch (Exception e){
            log.error(e);
        }
    }

}