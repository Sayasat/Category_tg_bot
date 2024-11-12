package com.programmingtechie.pandevtt.service;//package com.programmingtechie.pandevtt.service;
//
//import com.programmingtechie.pandevtt.model.Category;
//import com.programmingtechie.pandevtt.repository.CategoryRepository;
//import lombok.RequiredArgsConstructor;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class CategoryUploadService {
//
//    private final CategoryRepository categoryRepository;
//
//    public void importCategoryTreeFromExcel(InputStream inputStream) {
//        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
//            Sheet sheet = workbook.getSheetAt(0);
//            Map<String, Category> categoryMap = new HashMap<>();
//
//            // Пропускаем заголовок
//            int rowIndex = 1;
//            while (rowIndex <= sheet.getLastRowNum()) {
//                Row row = sheet.getRow(rowIndex++);
//                if (row == null) continue;
//
//                String name = getCellValue(row.getCell(0));
//                String parentName = getCellValue(row.getCell(1));
//
//                // Найти родительскую категорию
//                Category parentCategory = null;
//                if (!"Root".equalsIgnoreCase(parentName)) {
//                    parentCategory = categoryMap.getOrDefault(parentName,
//                            categoryRepository.findByName(parentName)
//                                    .orElse(null));
//                }
//
//                Category category = new Category();
//                category.setName(name);
//                category.setParent(parentCategory);
//
//                // Сохраняем в базе
//                categoryRepository.save(category);
//                categoryMap.put(name, category);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Error reading Excel file", e);
//        }
//    }
//
//    private String getCellValue(Cell cell) {
//        if (cell == null) return "";
//        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : "";
//    }
//}
//
//package com.programmingtechie.pandevtt.service;
//
//import com.programmingtechie.pandevtt.model.Category;
//import com.programmingtechie.pandevtt.repository.CategoryRepository;
//import lombok.RequiredArgsConstructor;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class CategoryUploadService {
//
//    private final CategoryRepository categoryRepository;
//
//    /**
//     * Метод загружает категории из Excel файла и сохраняет их в базу данных.
//     *
//     * @param file загружаемый Excel файл
//     * @return сообщение об успехе или ошибке
//     */
//    public String uploadCategoriesFromExcel(MultipartFile file) {
//        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
//            Sheet sheet = workbook.getSheetAt(0);
//            Map<String, Category> categoriesMap = new HashMap<>();
//
//            // Проходим по строкам начиная со второй (индекс 1)
//            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//                Row row = sheet.getRow(i);
//                if (row == null) continue;
//
//                String categoryName = getCellValue(row.getCell(0));
//                String parentName = getCellValue(row.getCell(1));
//
//                if (categoryName == null || categoryName.isEmpty()) continue;
//
//                // Получаем или создаем родительскую категорию
//                final Category parentCategory = (parentName != null && !parentName.isEmpty())
//                        ? categoriesMap.computeIfAbsent(parentName, name ->
//                        categoryRepository.findByName(name).orElseGet(() -> {
//                            Category newParent = new Category();
//                            newParent.setName(name);
//                            categoryRepository.save(newParent);
//                            return newParent;
//                        })
//                ) : null;
//
//                // Получаем или создаем категорию
//                categoriesMap.computeIfAbsent(categoryName, name ->
//                        categoryRepository.findByName(name).orElseGet(() -> {
//                            Category newCategory = new Category();
//                            newCategory.setName(name);
//                            newCategory.setParent(parentCategory);
//                            categoryRepository.save(newCategory);
//                            return newCategory;
//                        })
//                );
//            }
//
//            return "Загрузка категорий из Excel файла успешно выполнена.";
//
//        } catch (IOException e) {
//            throw new RuntimeException("Ошибка при чтении Excel файла", e);
//        }
//    }
//
//    private String getCellValue(Cell cell) {
//        if (cell == null) return null;
//        if (cell.getCellType() == CellType.STRING) {
//            return cell.getStringCellValue().trim();
//        } else if (cell.getCellType() == CellType.NUMERIC) {
//            return String.valueOf((int) cell.getNumericCellValue());
//        }
//        return null;
//    }
//}

//package com.programmingtechie.pandevtt.service;
//
//import com.programmingtechie.pandevtt.model.Category;
//import com.programmingtechie.pandevtt.repository.CategoryRepository;
//import lombok.RequiredArgsConstructor;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Objects;
//
//@Service
//@RequiredArgsConstructor
//public class CategoryUploadService {
//
//    private final CategoryRepository categoryRepository;
//
//    /**
//     * Проверка, является ли файл валидным Excel файлом.
//     */
//    public static boolean isValidExcelFile(MultipartFile file) {
//        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//    }
//
//    /**
//     * Получение данных категорий из Excel файла.
//     */
//    public List<Category> getCategoriesDataFromExcel(InputStream inputStream) throws IOException {
//        List<Category> categories = new ArrayList<>();
//        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
//        XSSFSheet sheet = workbook.getSheetAt(0); // Предполагаем, что данные находятся на первом листе
//        int rowIndex = 0;
//
//        for (Row row : sheet) {
//            if (rowIndex == 0) {
//                rowIndex++;
//                continue; // Пропускаем заголовок
//            }
//
//            Iterator<Cell> cellIterator = row.iterator();
//            int cellIndex = 0;
//            Category category = new Category();
//            Category parentCategory = null;
//
//            while (cellIterator.hasNext()) {
//                Cell cell = cellIterator.next();
//                switch (cellIndex) {
//                    case 0 -> category.setName(cell.getStringCellValue()); // Название категории
//                    case 1 -> {
//                        // Если существует родительская категория
//                        String parentName = cell.getStringCellValue();
//                        if (!parentName.isEmpty()) {
//                            parentCategory = new Category();
//                            parentCategory.setName(parentName);  // Устанавливаем родительскую категорию
//                        }
//                    }
//                    default -> {}
//                }
//                cellIndex++;
//            }
//
//            category.setParent(parentCategory);  // Устанавливаем родительскую категорию
//            categories.add(category);
//        }
//
//        workbook.close();
//        return categories;
//    }
//
//    /**
//     * Метод для сохранения категорий в базу данных.
//     */
//    @Transactional
//    public void saveCategoriesToDatabase(List<Category> categories) {
//        categoryRepository.saveAll(categories);
//    }
//}
//package com.programmingtechie.pandevtt.service;
//
//import com.programmingtechie.pandevtt.model.Category;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class CategoryUploadService {
//
//    // Метод для проверки формата Excel файла
//    public static boolean isValidExcelFile(MultipartFile file) {
//        return file.getOriginalFilename().endsWith(".xlsx");
//    }
//
//    // Метод для извлечения категорий из Excel
//    public List<Category> extractCategoriesFromExcel(InputStream inputStream) {
//        List<Category> categories = new ArrayList<>();
//
//        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
//            Sheet sheet = workbook.getSheetAt(0); // Получаем первый лист
//            for (Row row : sheet) {
//                // Пропускаем заголовок
//                if (row.getRowNum() == 0) {
//                    continue;
//                }
//
//                String categoryName = row.getCell(0).getStringCellValue();
//                String parentCategoryName = row.getCell(1).getStringCellValue();
//
//                // Здесь можно добавить логику для поиска родительской категории и построения иерархии
//                Category parentCategory = findCategoryByName(parentCategoryName);
//
//                Category category = new Category(categoryName, parentCategory);
//                categories.add(category);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return categories;
//    }
//
//    // Метод для поиска категории по имени (для построения иерархии)
//    private Category findCategoryByName(String parentCategoryName) {
//        // Логика поиска родительской категории в базе данных или в памяти
//        // Например, вы можете запросить базу данных для получения категории по имени.
//        return new Category(parentCategoryName, null); // Просто пример
//    }
//
//}

//package com.programmingtechie.pandevtt.service;

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

    @Transactional
    public String parseAndSaveCategories(InputStream inputStream, Long chatId) {
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            // Кэш для хранения уже сохраненных категорий
            Map<String, Category> categoryCache = new HashMap<>();

            for (Row row : sheet) {
                String categoryName = row.getCell(0).getStringCellValue().trim();
                String parentCategoryName = row.getCell(1) != null ? row.getCell(1).getStringCellValue().trim() : null;

                // Обработка исключения для родительской категории, если она равна "Root"
                if ("Root".equals(parentCategoryName)) {
                    parentCategoryName = null;
                }

                // Ищем родительскую категорию, если она указана
                Category parentCategory = null;
                if (parentCategoryName != null && !parentCategoryName.isEmpty()) {
                    parentCategory = findOrCreateCategory(parentCategoryName, chatId);
                }

                // Создаем или обновляем текущую категорию
                final Category finalParentCategory = parentCategory;  // Сделать родителя final
                Category category = categoryCache.computeIfAbsent(categoryName, name -> {
                    Category newCategory = new Category(name, finalParentCategory);  // Используем final переменную
                    newCategory.setChatId(chatId);  // Устанавливаем chatId для категории
                    categoryRepository.save(newCategory); // Сохраняем категорию сразу при создании
                    return newCategory;
                });

                // Если родитель существует, обновляем родителя у текущей категории
                category.setParent(finalParentCategory);

                // Обновляем chatId и сохраняем категорию
                category.setChatId(chatId);
                categoryRepository.save(category);
            }

            workbook.close();
            return "Categories uploaded and saved successfully!";
        } catch (Exception e) {
            log.error("Error during parsing and saving categories: {}", e.getMessage());
            return "An error occurred while processing the Excel file.";
        }
    }


    private Category findOrCreateCategory(String name, Long chatId) {
        // Ищем категорию по имени и chatId
        Optional<Category> existingCategoryOpt = categoryRepository.findByNameAndChatId(name, chatId);
        if (existingCategoryOpt.isPresent()) {
            return existingCategoryOpt.get();
        }

        // Если категория не найдена, создаем новую
        Category newCategory = new Category(name, null);  // Родителя пока нет
        newCategory.setChatId(chatId);
        return categoryRepository.save(newCategory);
    }

}


//import com.programmingtechie.pandevtt.model.Category;
//import com.programmingtechie.pandevtt.repository.CategoryRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.stereotype.Service;
//
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class CategoryUploadService {
//
//    private final CategoryRepository categoryRepository;
//
//    public String parseAndSaveCategories(InputStream inputStream, Long chatId) {
//        System.out.println("Check");
//        try {
//            Workbook workbook = new XSSFWorkbook(inputStream);
//            // Получаем первую страницу (лист)
//            Sheet sheet = workbook.getSheetAt(0);
//
//            // Создаем список для хранения категорий
//            List<Category> categories = new ArrayList<>();
//
//            Iterator<Row> rows = sheet.iterator();
//            // Пропускаем первую строку, если там заголовки
//            if (rows.hasNext()) {
//                rows.next();
//            }
//
//            while (rows.hasNext()) {
//                Row currentRow = rows.next();
//
//                // Проверка на наличие данных в ячейках
//                Cell nameCell = currentRow.getCell(0);
//                Cell parentNameCell = currentRow.getCell(1);
//
//                if (nameCell == null || nameCell.getCellType() == CellType.BLANK) {
//                    log.warn("Пропуск строки из-за пустого значения имени категории");
//                    continue; // Переход к следующей строке
//                }
//
//                String categoryName = nameCell.getStringCellValue().trim();
//
//                // Обработка пустого значения родительской категории
//                String parentCategoryName = null;
//                if (parentNameCell != null && parentNameCell.getCellType() != CellType.BLANK) {
//                    parentCategoryName = parentNameCell.getStringCellValue().trim();
//                }
//
//                // Создаем категорию
//                Category category = new Category();
//                category.setName(categoryName);
//
//                // Если родительская категория указана, ищем её в базе
//                if (parentCategoryName != null && !parentCategoryName.isEmpty()) {
//                    try {
//                        Category parent = categoryRepository
//                                .findByNameAndChatId(parentCategoryName, chatId)
//                                .orElseThrow(() -> new IllegalArgumentException("Parent category not found: " + parentCategoryName));
//                        category.setParent(parent);
//                    } catch (IllegalArgumentException ex) {
//                        log.warn("Родительская категория '{}' не найдена для chatId {}. Пропуск категории '{}'.", parentCategoryName, chatId, categoryName);
//                        continue; // Пропускаем, если родительская категория не найдена
//                    }
//                }
//
//                category.setChatId(chatId);
//                categories.add(category);
//            }
//
//            // Сохраняем все категории
//            categoryRepository.saveAll(categories);
//
//            // Закрываем Workbook
//            workbook.close();
//
//            log.info("Categories successfully uploaded for chatId: {}", chatId);
//            return "Categories successfully uploaded";
//        } catch (Exception e) {
//            log.error("Failed to parse Excel file: {}", e.getMessage(), e);
//            return "Failed to upload categories";
//        }
//    }
//
//}