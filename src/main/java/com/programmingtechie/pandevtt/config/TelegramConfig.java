//package com.programmingtechie.pandevtt.config;
//
//import com.programmingtechie.pandevtt.bot.CategoryBot;
//import com.programmingtechie.pandevtt.command.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.telegram.telegrambots.meta.TelegramBotsApi;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
//
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class TelegramConfig {
//
//    @Value("${bot.name}")
//    private String botName;
//
//    @Value("${bot.token}")
//    private String botToken;
//
//    @Bean
//    public CategoryBot categoryBot(AddElementCommand addElementCommand, RemoveElementCommand removeElementCommand,
//                                   ViewTreeCommand viewTreeCommand, DownloadCommand downloadCommand) {
//        CategoryBot categoryBot = new CategoryBot(botName, botToken, addElementCommand,
//                removeElementCommand, viewTreeCommand, downloadCommand);
//        try {
//            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
//            botsApi.registerBot(categoryBot);  // Регистрация бота
//        } catch (TelegramApiException e) {
//            log.error("Error registering bot: {}", e.getMessage());
//        }
//        return categoryBot;
//    }
//}

package com.programmingtechie.pandevtt.config;

import com.programmingtechie.pandevtt.bot.CategoryBot;
import com.programmingtechie.pandevtt.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TelegramConfig {

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private final CommandHandler commandHandler;

    @Bean
    public CategoryBot categoryBot() {
        CategoryBot categoryBot = new CategoryBot(botName, botToken, commandHandler);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(categoryBot);
        } catch (TelegramApiException e) {
            log.error("Error registering bot: {}", e.getMessage());
        }
        return categoryBot;
    }
}
