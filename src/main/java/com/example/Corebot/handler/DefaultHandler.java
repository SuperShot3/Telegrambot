package com.example.Corebot.handler;

import com.example.Corebot.Bot.BotLogic;
import com.example.Corebot.Greetings.GreetingClass;

import com.example.Corebot.command.ParsedCommand;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.apache.log4j.Logger;

public class DefaultHandler extends AbstractHandler {

    private static final Logger log = Logger.getLogger(DefaultHandler.class);
    GreetingClass botGreeting = new GreetingClass(bot);

    public DefaultHandler(BotLogic bot) {
        super(bot);
    }
    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) {
        try {
            botGreeting.SayHello(update);
        }catch (Exception e){
            log.info(e + "Greetings exception");
        }

        return "";
    }
}
