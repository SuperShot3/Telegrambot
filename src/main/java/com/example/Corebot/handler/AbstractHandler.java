package com.example.Corebot.handler;

import com.example.Corebot.Bot.BotLogic;
import com.example.Corebot.command.ParsedCommand;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class AbstractHandler {
        BotLogic bot;

        AbstractHandler(BotLogic bot) {
        this.bot = bot;
        }

public abstract String operate(String chatId, ParsedCommand parsedCommand, Update update);
        }

