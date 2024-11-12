package com.programmingtechie.pandevtt.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.InputStream;

@Component
public class CommandHandler {

    private final AddElementCommand addElementCommand;
    private final RemoveElementCommand removeElementCommand;
    private final ViewTreeCommand viewTreeCommand;
    private final DownloadCommand downloadCommand;
    private final HelpCommand helpCommand;
    @Autowired
    private final UploadCommand uploadCommand;

    public CommandHandler(AddElementCommand addElementCommand, RemoveElementCommand removeElementCommand,
                          ViewTreeCommand viewTreeCommand, DownloadCommand downloadCommand,
                          HelpCommand helpCommand, UploadCommand uploadCommand) {
        this.addElementCommand = addElementCommand;
        this.removeElementCommand = removeElementCommand;
        this.viewTreeCommand = viewTreeCommand;
        this.downloadCommand = downloadCommand;
        this.helpCommand = helpCommand;
        this.uploadCommand = uploadCommand;
    }

    public String handleCommand(AbsSender sender, String messageText, Message message, Long chatId) {
        if (messageText.startsWith("/start")) {
            return helpCommand.welcome();
        }

        if (messageText.startsWith("/help")) {
            return helpCommand.execute(messageText, chatId);
        }

        if (messageText.startsWith("/addElement")) {
            return addElementCommand.execute(messageText, chatId);
        }

        if (messageText.startsWith("/removeElement")) {
            return removeElementCommand.execute(messageText, chatId);
        }

        if (messageText.startsWith("/viewTree")) {
            return viewTreeCommand.execute(messageText, chatId);
        }

        if (messageText.startsWith("/download")) {
            downloadCommand.execute(sender, message);
            return "The category tree is being downloaded.";
        }


        return "Unknown command. Type /help for a list of commands.";
    }

    public String handleFileUpload(AbsSender sender, InputStream inputStream, Document document, Long chatId) {
        if (!document.getFileName().endsWith(".xlsx")) {
            return "Invalid file format. Please upload a .xlsx file.";
        }
        return uploadCommand.handleFileUpload(inputStream, chatId);
    }
}

//
//        if (messageText.startsWith("/upload")) {
//            return "Please upload an Excel file (with .xlsx extension) after this message.";
//        }
//
//        // Проверяем наличие файла
//        if (message.hasDocument()) {
//            return uploadCommand.handleFileUpload(sender, message, message.getDocument());
//        }
