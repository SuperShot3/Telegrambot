package com.example.Corebot.command;

public class ParsedCommand {
    Command command = Command.NONE;
    String text="";

    public ParsedCommand(Command command, String trimText) {
        this.command = command;
        this.text = trimText;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }



}