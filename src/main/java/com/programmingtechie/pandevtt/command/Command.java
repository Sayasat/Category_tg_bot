package com.programmingtechie.pandevtt.command;

public interface Command {
    String execute(String text, Long chatId);
}
