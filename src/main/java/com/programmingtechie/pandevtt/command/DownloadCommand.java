//package com.programmingtechie.pandevtt.command;
//
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
//import org.telegram.telegrambots.meta.api.objects.Message;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.bots.AbsSender;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//
//@Component
//public class DownloadCommand implements Command {
//
//    private final ExcelExportService excelExportService;
//
//    public DownloadCommand(ExcelExportService excelExportService) {
//        this.excelExportService = excelExportService;
//    }
//
//    @Override
//    public String getCommandIdentifier() {
//        return "download";
//    }
//
//    @Override
//    public String getDescription() {
//        return "Скачать Excel документ с деревом категорий";
//    }
//
//    @Override
//    public void processMessage(AbsSender sender, Message message, String[] arguments) {
//        byte[] excelFile = excelExportService.generateCategoryTreeExcel();
//
//        SendDocument sendDocument = new SendDocument();
//        sendDocument.setChatId(message.getChatId().toString());
//        sendDocument.setDocument("category_tree.xlsx", new ByteArrayInputStream(excelFile));
//        sendDocument.setCaption("Дерево категорий");
//
//        try {
//            sender.execute(sendDocument);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public String execute(String text) {
//        return processMessage();
//    }
//}

package com.programmingtechie.pandevtt.command;

import com.programmingtechie.pandevtt.service.CategoryDownloadService;
import com.programmingtechie.pandevtt.service.CategoryDownloadServiceImpl;
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

    public DownloadCommand(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void execute(AbsSender sender, Message message) {
//        byte[] excelFile = categoryService.generateCategoryTreeExcel();
//        InputFile file = new InputFile(new ByteArrayInputStream(excelFile), "category_tree.xlsx");
//
//        SendDocument sendDocument = new SendDocument();
//        sendDocument.setChatId(message.getChatId().toString());
//        sendDocument.setDocument(file);
//        sendDocument.setCaption("Дерево категорий в Excel");
//
//        try {
//            sender.execute(sendDocument);
//        } catch (TelegramApiException e) {
//            log.error("Ошибка при отправке Excel файла: {}", e.getMessage());
//        }
        Long chatId = message.getChatId();  // Получаем chatId
        log.info("Получен запрос на скачивание от пользователя с chatId: {}", chatId);

        try {
            // Генерируем Excel файл для конкретного чата
            byte[] excelFile = categoryService.generateCategoryTreeExcel(chatId);

            // Создаем InputFile из массива байтов
            InputFile file = new InputFile(new ByteArrayInputStream(excelFile), "category_tree_" + chatId + ".xlsx");

            // Создаем SendDocument для отправки документа
            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(chatId.toString());  // Передаем chatId
            sendDocument.setDocument(file);
            sendDocument.setCaption("Дерево категорий для чата " + chatId);

            // Отправляем документ
            sender.execute(sendDocument);
            log.info("Файл успешно отправлен пользователю с chatId: {}", chatId);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке Excel файла пользователю с chatId {}: {}", chatId, e.getMessage());
        }
    }
}

