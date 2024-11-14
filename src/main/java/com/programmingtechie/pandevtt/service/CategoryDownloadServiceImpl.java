package com.programmingtechie.pandevtt.service;

import com.programmingtechie.pandevtt.model.Category;
import com.programmingtechie.pandevtt.repository.CategoryRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class CategoryDownloadServiceImpl implements CategoryDownloadService {

    private final CategoryRepository categoryRepository;

    public CategoryDownloadServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Генерирует Excel-файл с деревом категорий для указанного chatId.
     *
     * @param chatId идентификатор чата.
     * @return массив байтов, представляющий Excel-файл с деревом категорий.
     */
    @Override
    public byte[] generateCategoryTreeExcel(Long chatId) {
        List<Category> categories = categoryRepository.findByChatId(chatId);

        // Если категорий для чата нет, выбрасываем исключение
        if (categories.isEmpty()) {
            throw new RuntimeException("Нет категорий для чата с chatId: " + chatId);
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            // Создаем новый лист Excel и заполняем его данными
            Sheet sheet = workbook.createSheet("Дерево категорий");

            createHeader(sheet);  // Создаем заголовок таблицы
            fillData(sheet, categories);  // Заполняем таблицу данными

            // Записываем данные в ByteArrayOutputStream
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return out.toByteArray();  // Возвращаем байтовый массив
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать Excel файл", e);  // Обработка ошибок записи в файл
        }
    }

    /**
     * Создает заголовок таблицы в Excel файле.
     *
     * @param sheet лист Excel для добавления заголовка.
     */
    private void createHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);  // Создаем первую строку для заголовка
        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        Font headerFont = sheet.getWorkbook().createFont();
        headerFont.setBold(true);  // Делаем шрифт жирным
        headerStyle.setFont(headerFont);

        // Заголовок для первого столбца
        Cell cell = headerRow.createCell(0);
        cell.setCellValue("Категория");
        cell.setCellStyle(headerStyle);

        // Заголовок для второго столбца
        cell = headerRow.createCell(1);
        cell.setCellValue("Родительская категория");
        cell.setCellStyle(headerStyle);
    }

    /**
     * Заполняет данные о категориях в Excel таблице.
     *
     * @param sheet лист Excel для добавления данных.
     * @param categories список категорий для добавления в таблицу.
     */
    private void fillData(Sheet sheet, List<Category> categories) {
        int rowIndex = 1;  // Начинаем с 1, так как 0-й индекс занят заголовком
        for (Category category : categories) {
            Row row = sheet.createRow(rowIndex++);  // Создаем новую строку для каждой категории
            row.createCell(0).setCellValue(category.getName());  // Заполняем название категории
            String parentName = category.getParent() != null ? category.getParent().getName() : " ";  // Получаем имя родительской категории
            row.createCell(1).setCellValue(parentName);  // Заполняем имя родителя
        }
    }
}
