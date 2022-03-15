package com.example.Corebot.service;

import com.example.Corebot.Bot.BotLogic;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MessageSender implements Runnable {
    private static final Logger log = Logger.getLogger(MessageSender.class);
    private BotLogic bot;

    public MessageSender(BotLogic bot) {
        this.bot = bot;
    }
    @Override
    public void run() {
        log.info("[STARTED] MsgSender.  Bot class: " + bot);
        try {
            while (true) {
                for (Object object = bot.sendQueue.poll(); object != null; object = bot.sendQueue.poll()) {
                    log.debug("Get new msg to send " + object);
                    send(object);
                }
                try {
                    int SENDER_SLEEP_TIME = 10000;
                    Thread.sleep(SENDER_SLEEP_TIME);
                } catch (InterruptedException e) {
                    log.error("Take interrupt while operate msg list", e);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
    private void send(Object object) {
        try {
            MessageType messageType = messageType(object);
            switch (messageType) {
                case EXECUTE :
                    @SuppressWarnings("unchecked")
                    BotApiMethod<Message> message = (BotApiMethod<Message>) object;
                    log.debug("User Execute for " + object);
                    bot.execute(message);
                    break;
                case STICKER :
                    SendSticker sendSticker = new SendSticker();
                    sendSticker.setSticker(new InputFile("CAACAgIAAxkBAAINXWIl-i_jvQPWOyMkCW3XTi62EJbqAAKEHAACmibhS2lMEncihqguIwQ"));
                    send(sendSticker);
                    log.debug("Use SendSticker for " + object);
                    break;
                default : log.warn("Cant detect type of object. " + object);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    private MessageType messageType(Object object) {
        if (object instanceof SendSticker) return MessageType.STICKER;
        if (object instanceof BotApiMethod) return MessageType.EXECUTE;
        return MessageType.NOT_DETECTED;


    }
    enum MessageType {
        EXECUTE, STICKER, NOT_DETECTED
    }
}
