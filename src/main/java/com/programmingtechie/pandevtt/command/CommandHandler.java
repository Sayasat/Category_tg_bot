package com.programmingtechie.pandevtt.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CommandHandler {

    // Команды, которые обрабатывает этот класс
    private final AddElementCommand addElementCommand;
    private final RemoveElementCommand removeElementCommand;
    private final ViewTreeCommand viewTreeCommand;
    private final DownloadCommand downloadCommand;
    private final HelpCommand helpCommand;
    private final UploadCommand uploadCommand;

    // Карта для отслеживания состояния загрузки для каждого чата
    private final Map<Long, Boolean> uploadStateMap = new HashMap<>();

    /**
     * Обрабатывает команду, пришедшую от пользователя.
     * В зависимости от текста сообщения, вызывается соответствующий метод для выполнения команды.
     *
     * @param sender    объект, используемый для отправки сообщений
     * @param messageText текст команды, полученный от пользователя
     * @param message   объект сообщения, содержащий дополнительные данные
     * @param chatId    идентификатор чата пользователя
     * @return ответ на команду, который будет отправлен пользователю
     */
    public String handleCommand(AbsSender sender, String messageText, Message message, Long chatId) {
        // Обработка команды /start
        if (messageText.startsWith("/start")) {
            return helpCommand.welcome();
        }

        // Обработка команды /help
        if (messageText.startsWith("/help")) {
            return helpCommand.execute(messageText, chatId);
        }

        // Обработка команды /addElement
        if (messageText.startsWith("/addElement")) {
            return addElementCommand.execute(messageText, chatId);
        }

        // Обработка команды /removeElement
        if (messageText.startsWith("/removeElement")) {
            return removeElementCommand.execute(messageText, chatId);
        }

        // Обработка команды /viewTree
        if (messageText.startsWith("/viewTree")) {
            return viewTreeCommand.execute(messageText, chatId);
        }

        // Обработка команды /download
        if (messageText.startsWith("/download")) {
            downloadCommand.execute(sender, message);
            return "Категорийное дерево загружается.";
        }

        // Обработка команды /upload
        if (messageText.startsWith("/upload")) {
            // Устанавливаем состояние загрузки для данного чата
            uploadStateMap.put(chatId, true);
            return """
            Пожалуйста, загрузите файл .xlsx с данными категорий.
            Формат файла должен быть следующим:
            - Первая колонка: **Category** (название категории)
            - Вторая колонка: **Parent** (название родительской категории или пусто, если это корневая категория)
            Пример:
            ```
            Category   | Parent
            Электроника| -
            Телефоны   | Электроника
            Наушники   | Электроника
            ```
            """;
        }

        // Обработка команды /cancel
        if (messageText.startsWith("/cancel")) {
            uploadStateMap.remove(chatId);
            return "Процесс загрузки файла был отменен.";
        }

        // Возвращаем сообщение по умолчанию, если команда не распознана
        return "Неизвестная команда. Введите /help для получения списка команд.";
    }

    /**
     * Обрабатывает загрузку файла .xlsx, если пользователь начал процесс загрузки.
     *
     * @param inputStream входной поток данных файла
     * @param document    документ, который был загружен пользователем
     * @param chatId      идентификатор чата пользователя
     * @return сообщение, которое будет отправлено пользователю после обработки файла
     */
    public String handleFileUpload(InputStream inputStream, Document document, Long chatId) {
        // Проверка, был ли активирован процесс загрузки для текущего чата
        if (uploadStateMap.getOrDefault(chatId, false)) {
            // Проверка на правильный формат файла
            if (!document.getFileName().endsWith(".xlsx")) {
                return "Неверный формат файла. Пожалуйста, загрузите файл с расширением .xlsx.";
            }

            // Удаляем состояние загрузки после обработки файла
            uploadStateMap.remove(chatId);
            return uploadCommand.handleFileUpload(inputStream, chatId);
        }

        // Если команда /upload не была вызвана, возвращаем сообщение о необходимости вызвать эту команду
        return "Пожалуйста, сначала введите команду /upload перед загрузкой файла.";
    }
}
