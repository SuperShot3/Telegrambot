package com.example.Corebot.service;

import com.example.Corebot.Bot.BotLogic;
import com.example.Corebot.command.Command;
import com.example.Corebot.command.ParsedCommand;
import com.example.Corebot.command.Parser;
import com.example.Corebot.handler.AbstractHandler;
import com.example.Corebot.handler.DefaultHandler;
import com.example.Corebot.handler.NotifyHandler;
import com.example.Corebot.handler.SystemHandler;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageReceiver implements Runnable {
    private static final Logger log = Logger.getLogger(MessageReceiver.class);
    private final int WAIT_FOR_NEW_MESSAGE_DELAY = 1000;

    private BotLogic bot;
    private final Parser parser;

    public MessageReceiver(BotLogic bot) {
        this.bot = bot;
        parser = new Parser(bot);
    }

    @Override
    public void run() {

        log.info("[STARTED] MsgReciever.  Bot class: " + bot);
        while (true) {
            for (Object object = bot.receiveQueue.poll(); object != null; object = bot.receiveQueue.poll()) {
                log.debug("New object for analyze in queue " + object.toString());
                try {
                    analyze(object);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(WAIT_FOR_NEW_MESSAGE_DELAY);
            } catch (InterruptedException e) {
                log.error("Catch interrupt. Exit", e);
                return;
            }
        }
    }
    private void analyze(Object object) throws TelegramApiException {
        if (object instanceof Update) {
            Update update = (Update) object;
            log.debug("Update received: " + update);
            analyzeForUpdateType(update);
        } else log.warn("Cant operate type of object: " + object.toString());
    }

    private void analyzeForUpdateType (Update update) {
        Message message1 = update.getMessage();
        Long chatId = update.getMessage().getChatId();

        ParsedCommand parsedCommand = new ParsedCommand(Command.NONE,"");
        if(message1.hasText()) {
            parsedCommand = parser.getParsedCommand(message1.getText());
        }else {
            Sticker sticker = message1.getSticker();
            if(sticker != null){
                parsedCommand = new ParsedCommand(Command.STICKER,sticker.getFileId());
            }
        }
        AbstractHandler handlerForCommand = getHandlerForCommand(parsedCommand.getCommand());
        String operationResult = handlerForCommand.operate(chatId.toString(), parsedCommand, update);

        if (!"".equals(operationResult)) {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(operationResult);
            bot.sendQueue.add(message);
        }
    }
    private AbstractHandler getHandlerForCommand(Command command) {
        if (command == null) {
            log.warn("Null command accepted. This is not good scenario.");
            return new DefaultHandler(bot);
        }
        switch (command) {
            case START:
            case HELP:
            case ID:
            case STICKER:
                SystemHandler systemHandler = new SystemHandler(bot);
                log.info("Handler for command[" + command + "] is: " + systemHandler);
                return systemHandler;
            case NOTIFY:
                NotifyHandler notifyHandler = new NotifyHandler(bot);
                log.info("Handler for command[" + command + "] is: " + notifyHandler);
                return notifyHandler;
            default:
                log.info("Handler for command[" + command + "] not Set. Return DefaultHandler");
                return new DefaultHandler(bot);
        }
    }
}
