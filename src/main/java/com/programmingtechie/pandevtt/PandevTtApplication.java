package com.programmingtechie.pandevtt;

import com.programmingtechie.pandevtt.bot.CategoryBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@SpringBootApplication
public class PandevTtApplication {

    public static void main(String[] args) {
        SpringApplication.run(PandevTtApplication.class, args);
    }

}
