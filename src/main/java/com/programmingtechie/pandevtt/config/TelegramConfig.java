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

    // Чтение значений из application.properties для имени бота и его токена
    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    // Ссылка на CommandHandler для обработки команд
    private final CommandHandler commandHandler;

    /**
     * Регистрация бота в Telegram API
     * @return возвращает экземпляр CategoryBot
     */
    @Bean
    public CategoryBot categoryBot() {
        // Создание экземпляра бота с использованием имени и токена
        CategoryBot categoryBot = new CategoryBot(botName, botToken, commandHandler);

        try {
            // Создание объекта для работы с Telegram API
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            // Регистрация бота
            botsApi.registerBot(categoryBot);
        } catch (TelegramApiException e) {
            log.error("Error registering bot: {}", e.getMessage());
        }

        // Возвращаем экземпляр CategoryBot
        return categoryBot;
    }
}
