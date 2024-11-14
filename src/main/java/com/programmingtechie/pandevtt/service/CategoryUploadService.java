package com.programmingtechie.pandevtt.service;

import com.programmingtechie.pandevtt.model.Category;
import com.programmingtechie.pandevtt.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryUploadService {

    private final CategoryRepository categoryRepository;

    /**
     * Метод для парсинга и сохранения категорий из Excel-файла.
     *
     * @param inputStream поток данных Excel-файла
     * @param chatId идентификатор чата
     * @return сообщение о статусе загрузки категорий
     */
    @Transactional
    public String parseAndSaveCategories(InputStream inputStream, Long chatId) {
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Category> categoryCache = new HashMap<>();
            boolean isFirstRow = true;

            // Процесс чтения строк из Excel файла
            for (Row row : sheet) {
                // Пропускаем заголовок таблицы
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                String categoryName = getCellValue(row, 0).trim();
                String parentCategoryName = getCellValue(row, 1).trim();

                // Обработка корневой категории
                if ("Root".equals(parentCategoryName) || "-".equals(parentCategoryName)) {
                    parentCategoryName = null;
                }

                // Если категория имеет родительскую категорию, находим или создаем её
                Category parentCategory = null;
                if (parentCategoryName != null) {
                    parentCategory = findOrCreateCategory(parentCategoryName, chatId);
                }

                // Создаем или получаем категорию из кэша
                final Category finalParentCategory = parentCategory;
                Category category = categoryCache.computeIfAbsent(categoryName, name -> {
                    Category newCategory = new Category(name, finalParentCategory);
                    newCategory.setChatId(chatId);
                    categoryRepository.save(newCategory);
                    return newCategory;
                });

                // Обновляем родительскую категорию и сохраняем
                category.setParent(finalParentCategory);
                category.setChatId(chatId);
                categoryRepository.save(category);
            }

            return "Категории успешно загружены и сохранены!";
        } catch (Exception e) {
            log.error("Ошибка при парсинге и сохранении категорий: {}", e.getMessage(), e);
            return "Произошла ошибка при обработке Excel-файла.";
        }
    }

    /**
     * Получение значения ячейки с проверкой на пустое значение.
     *
     * @param row строка Excel
     * @param cellIndex индекс ячейки
     * @return строковое значение ячейки или пустая строка, если ячейка пуста
     */
    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return cell.toString().trim();
    }

    /**
     * Метод для поиска или создания новой категории по имени и chatId.
     *
     * @param name имя категории
     * @param chatId идентификатор чата
     * @return найденная или новая категория
     */
    private Category findOrCreateCategory(String name, Long chatId) {
        Optional<Category> existingCategoryOpt = categoryRepository.findByNameAndChatId(name, chatId);
        if (existingCategoryOpt.isPresent()) {
            return existingCategoryOpt.get();
        }

        // Создаем новую категорию, если она не найдена
        Category newCategory = new Category(name, null);
        newCategory.setChatId(chatId);
        return categoryRepository.save(newCategory);
    }
}
