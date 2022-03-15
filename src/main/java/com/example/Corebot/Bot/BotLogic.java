package com.example.Corebot.Bot;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BotLogic extends TelegramLongPollingBot {

    Logger log = Logger.getLogger(BotLogic.class.getName());
    final int RECONNECT_PAUSE = 10000;

    private static final String TOKEN = System.getenv("TOKEN");
    private static final String BOT_USERNAME = System.getenv("BOT_USERNAME");

    public BotLogic(String botName,String botToken) {}

    @Override
    public String getBotUsername() {
        return System.getenv("BOT_USERNAME");
    }

    @Override
    public String getBotToken() {
        return System.getenv("TOKEN");
    }



    public Queue<Object> sendQueue = new ConcurrentLinkedQueue<>();
    public Queue<Object> receiveQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void onUpdateReceived(Update update) {

        receiveQueue.add(update);
        log.debug("New update : updateId" + update.getUpdateId());

    }
    public void botConnect() throws TelegramApiException {

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(this);
            log.info("[STARTED] TelegramAPI. Bot Connected. Bot class: " + this);
        } catch (TelegramApiException e) {
            log.error("Cant Connect. Pause " + RECONNECT_PAUSE / 1000 + "sec and try again. Error: " + e.getMessage());
            try {
                Thread.sleep(RECONNECT_PAUSE);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                return;
            }
            botConnect();
        }
    }
}
