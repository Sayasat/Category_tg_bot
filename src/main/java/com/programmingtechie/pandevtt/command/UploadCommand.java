package com.programmingtechie.pandevtt.command;

import com.programmingtechie.pandevtt.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UploadCommand {

    private final CategoryService categoryService;

    public String handleFileUpload(InputStream inputStream, Long chatId) {
        try {
            categoryService.uploadCategoriesFromExcel(inputStream, chatId);
            return "File successfully processed. Categories have been uploaded to the database.";
        } catch (Exception e) {
            log.error("General error occurred while downloading file: {}", e.getMessage());
            throw new CustomFileDownloadException("An unexpected error occurred", e);
        }
    }
}