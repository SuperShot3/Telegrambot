package com.example.Corebot.Ability;
import com.example.Corebot.Bot.BotLogic;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;


public class Notify implements Runnable {
    private static final Logger log = Logger.getLogger(Notify.class);
    private static final int MILLISEC_IN_SEC = 1000;

    BotLogic bot;
    long delayInMillisec;
    String chatID;
    public Notify(BotLogic bot, String chatID, long delayInMillisec) {
        this.bot = bot;
        this.chatID = chatID;
        this.delayInMillisec = delayInMillisec;
        log.debug("CREATE. " + toString());
    }
    @Override
    public void run() {
        log.info("RUN. " + toString());
        bot.sendQueue.add(getFirstMessage());
        try {
            Thread.sleep(delayInMillisec);
            bot.sendQueue.add(getSecondMessage());
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        log.info("FIHISH. " + toString());
    }
    private SendMessage getFirstMessage() {
        return new SendMessage(chatID, "I will send you notify after " + delayInMillisec / MILLISEC_IN_SEC + " sec");
    }
   /* private SendSticker getSecondSticker() {
        SendSticker sendSticker = new SendSticker();
        sendSticker.setSticker(new InputFile("CAADBQADiQMAAukKyAPZH7wCI2BwFxYE"));
        sendSticker.setChatId(chatID);
        return sendSticker;
    }*/

    private SendMessage getSecondMessage() {
        return new SendMessage(chatID, "This is notify message. Thanks for using :)");
    }
}
