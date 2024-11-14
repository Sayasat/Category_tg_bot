package com.programmingtechie.pandevtt.command;

import com.programmingtechie.pandevtt.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.ByteArrayInputStream;

@Slf4j
@Component
public class DownloadCommand {

    private final CategoryService categoryService;

    /**
     * Конструктор класса, инжектящий зависимость для работы с категориями.
     *
     * @param categoryService сервис для работы с категориями
     */
    public DownloadCommand(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Метод для обработки команды скачивания дерева категорий и отправки файла пользователю.
     *
     * @param sender объект для отправки сообщений в Telegram
     * @param message сообщение от пользователя, содержащее запрос
     */
    public void execute(AbsSender sender, Message message) {
        Long chatId = message.getChatId();
        log.info("Получен запрос на скачивание от пользователя с chatId: {}", chatId);

        try {
            // Получение байтового массива Excel файла с деревом категорий
            byte[] excelFile = categoryService.generateCategoryTreeExcel(chatId);

            // Создание объекта InputFile для отправки в Telegram
            InputFile file = new InputFile(new ByteArrayInputStream(excelFile), "category_tree_" + chatId + ".xlsx");

            // Создание объекта SendDocument для отправки файла
            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(chatId.toString());
            sendDocument.setDocument(file);
            sendDocument.setCaption("Дерево категорий для чата " + chatId);

            // Отправка файла пользователю
            sender.execute(sendDocument);
            log.info("Файл успешно отправлен пользователю с chatId: {}", chatId);
        } catch (TelegramApiException e) {
            // Логирование ошибки при отправке файла
            log.error("Ошибка при отправке Excel файла пользователю с chatId {}: {}", chatId, e.getMessage());
        }
    }
}
