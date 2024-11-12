//package com.programmingtechie.pandevtt.bot;
//
//import com.programmingtechie.pandevtt.command.*;
//import lombok.extern.slf4j.Slf4j;
//import org.telegram.telegrambots.bots.TelegramLongPollingBot;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Document;
//import org.telegram.telegrambots.meta.api.objects.Message;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//
//import java.io.InputStream;
//
//@Slf4j
//public class CategoryBot extends TelegramLongPollingBot {
//
//    private final String botName;
//    private final CommandHandler commandHandler;
//
//    public CategoryBot(String botName, String botToken, CommandHandler commandHandler) {
//        super(botToken);
//        this.botName = botName;
//        this.commandHandler = commandHandler;
//    }
//
//    @Override
//    public String getBotUsername() {
//        return this.botName;
//    }
//
//    @Override
//    public void onUpdateReceived(Update update) {
//        if (update.hasMessage()) {
//            Message message = update.getMessage();
//            Long chatId = message.getChatId();
//
//            try {
//                String responseMessage;
//
//                // Проверка на текст или документ
//                if (message.hasText()) {
//                    String messageText = message.getText().trim();
//                    responseMessage = commandHandler.handleCommand(this, messageText, message, chatId);
//                } else if (message.hasDocument()) {
//                    Document document = message.getDocument();
//                    InputStream inputStream = getFileAsStream(document);
//                    responseMessage = commandHandler.handleFileUpload(this, inputStream, document, chatId);
//                } else {
//                    responseMessage = "Please send a valid command or upload a .xlsx file.";
//                }
//
//                sendMessage(chatId, responseMessage);
//            } catch (Exception e) {
//                log.error("Exception: {}", e.getMessage());
//                sendMessage(chatId, "An error occurred while processing your request.");
//            }
//        }
//    }
//
//    private InputStream getFileAsStream(Document document) throws TelegramApiException {
//        return downloadFileAsStream(document.getFileId());
//    }
//
//    private void sendMessage(Long chatId, String message) {
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(String.valueOf(chatId));
//        sendMessage.setText(message);
//
//        try {
//            execute(sendMessage);
//        } catch (TelegramApiException e) {
//            log.error("Error sending message: {}", e.getMessage());
//        }
//    }
//}
//
//
////    @Override
////    public void onUpdateReceived(Update update) {
////        if (update.hasMessage() && update.getMessage().hasText()) {
////            Message message = update.getMessage();
////            String messageText = message.getText().trim();
////            Long chatId = message.getChatId();
////            log.info("Message received: {}", messageText);
////
////            try {
////                String responseMessage = commandHandler.handleCommand(this, messageText, message);
////
////                sendMessage(chatId, responseMessage);
////            } catch (Exception e) {
////                log.error("Exception: {}", e.getMessage());
////                sendMessage(chatId, "An error occurred while processing your request.");
////            }
////        }
////    }


package com.programmingtechie.pandevtt.bot;

import com.programmingtechie.pandevtt.command.CommandHandler;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;

@Slf4j
public class CategoryBot extends TelegramLongPollingBot {

    private final String botName;
    private final CommandHandler commandHandler;

    public CategoryBot(String botName, String botToken, CommandHandler commandHandler) {
        super(botToken);
        this.botName = botName;
        this.commandHandler = commandHandler;
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();

            try {
                String responseMessage;

                // Проверка на текст или документ
                if (message.hasText()) {
                    String messageText = message.getText().trim();
                    responseMessage = commandHandler.handleCommand(this, messageText, message, chatId);
                } else if (message.hasDocument()) {
                    Document document = message.getDocument();
                    log.info("Получен документ: {}", document.getFileName());

                    try (InputStream inputStream = getFileAsStream(document)) {
                        responseMessage = commandHandler.handleFileUpload(this, inputStream, document, chatId);
                    } catch (Exception e) {
                        log.error("Ошибка при загрузке файла: {}", e.getMessage(), e);
                        responseMessage = "Ошибка при обработке файла. Пожалуйста, попробуйте снова.";
                    }
                } else {
                    responseMessage = "Отправьте команду или загрузите файл .xlsx.";
                }

                sendMessage(chatId, responseMessage);
            } catch (Exception e) {
                log.error("Ошибка при обработке сообщения: {}", e.getMessage(), e);
                sendMessage(chatId, "Произошла ошибка при обработке вашего запроса.");
            }
        }
    }

    /**
     * Метод для получения InputStream файла.
     */
    private InputStream getFileAsStream(Document document) {
        try {
            log.info("Получаем информацию о файле: {}", document.getFileName());

            // Получаем информацию о файле
            var file = execute(org.telegram.telegrambots.meta.api.methods.GetFile.builder()
                    .fileId(document.getFileId())
                    .build());

            log.info("Скачивание файла по ссылке: {}", file.getFilePath());

            // Скачиваем файл
            return downloadFileAsStream(file.getFilePath());
        } catch (TelegramApiException e) {
            log.error("Ошибка при загрузке файла: {}", e.getMessage());
            throw new RuntimeException("Не удалось скачать файл из Telegram", e);
        }
    }


    /**
     * Метод для отправки сообщения пользователю.
     */
    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(message);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения: {}", e.getMessage());
        }
    }
}
