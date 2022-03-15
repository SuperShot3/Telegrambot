package com.example.Corebot.Ability;

import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;

enum Stickers {
    ;
    InputFile FUNNY_HUMSTER = new InputFile("CAACAgIAAxkBAAINXWIl-i_jvQPWOyMkCW3XTi62EJbqAAKEHAACmibhS2lMEncihqguIwQ");


    String stickerId;

    Stickers(String stickerId) {
        this.stickerId = stickerId;
    }

    public SendSticker getSendSticker(String chatId) {
        if ("".equals(chatId)) throw new IllegalArgumentException("ChatId cant be null");
        SendSticker sendSticker = getSendSticker();
        sendSticker.setChatId(chatId);
        return sendSticker;
    }
    public SendSticker getSendSticker() {
        SendSticker sendSticker = new SendSticker();
        sendSticker.setSticker(FUNNY_HUMSTER);
        return sendSticker;
    }
}
