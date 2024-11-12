//package com.programmingtechie.pandevtt.service;
//
//import com.programmingtechie.pandevtt.model.Category;
//import com.programmingtechie.pandevtt.repository.CategoryRepository;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.stereotype.Service;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class CategoryDownloadServiceImpl implements CategoryDownloadService {
//
//    private final CategoryRepository categoryRepository;
//
//    public CategoryDownloadServiceImpl(CategoryRepository categoryRepository) {
//        this.categoryRepository = categoryRepository;
//    }
//
//    @Override
//    public byte[] generateCategoryTreeExcel() {
//        try (Workbook workbook = new XSSFWorkbook()) {
//            Sheet sheet = workbook.createSheet("Category Tree");
//            createHeader(sheet);
//            List<Category> categories = categoryRepository.findAll();
//            fillData(sheet, categories);
//
//            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//                workbook.write(out);
//                return out.toByteArray();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to create Excel file", e);
//        }
//    }
//
//    private void createHeader(Sheet sheet) {
//        Row headerRow = sheet.createRow(0);
//        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
//        Font headerFont = sheet.getWorkbook().createFont();
//        headerFont.setBold(true);
//        headerStyle.setFont(headerFont);
//
//        // Create the header cells for "id", "name", and "parent_id"
//        Cell cell = headerRow.createCell(0);
//        cell.setCellValue("id");
//        cell.setCellStyle(headerStyle);
//
//        cell = headerRow.createCell(1);
//        cell.setCellValue("name");
//        cell.setCellStyle(headerStyle);
//
//        cell = headerRow.createCell(2);
//        cell.setCellValue("parent_id");
//        cell.setCellStyle(headerStyle);
//    }
//
//    private void fillData(Sheet sheet, List<Category> categories) {
//        int rowIndex = 1;
//        for (Category category : categories) {
//            Row row = sheet.createRow(rowIndex++);
//
//            // Set the category ID in the first cell
//            row.createCell(0).setCellValue(category.getId());
//
//            // Set the category name in the second cell
//            row.createCell(1).setCellValue(category.getName());
//
//            // Set the parent category ID in the third cell
//            Long parentId = category.getParent() != null ? category.getParent().getId() : null;
//            row.createCell(2).setCellValue(String.valueOf((parentId != null ? parentId : " ")));
//        }
//    }
//}


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

    @Override
    public byte[] generateCategoryTreeExcel(Long chatId) {
        List<Category> categories = categoryRepository.findByChatId(chatId);
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Category Tree");
            createHeader(sheet);
            if (categories.isEmpty()) {
                throw new RuntimeException("Нет категорий для чата с chatId: " + chatId);
            }
            fillData(sheet, categories);

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return out.toByteArray();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create Excel file", e);
        }
    }

    private void createHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        Font headerFont = sheet.getWorkbook().createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        Cell cell = headerRow.createCell(0);
        cell.setCellValue("category");
        cell.setCellStyle(headerStyle);

        cell = headerRow.createCell(1);
        cell.setCellValue("parent_category");
        cell.setCellStyle(headerStyle);
    }

    private void fillData(Sheet sheet, List<Category> categories) {
        int rowIndex = 1;
        for (Category category : categories) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(category.getName());
            String parentName = category.getParent() != null ? category.getParent().getName() : " ";
            row.createCell(1).setCellValue(parentName);
        }
    }
}



//package com.programmingtechie.pandevtt.service;
//
//import com.programmingtechie.pandevtt.model.Category;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.List;
//
//@Service
//public class CategoryDownloadServiceImpl {
//
//    private final CategoryServiceImpl categoryServiceImpl;
//
//    public CategoryDownloadServiceImpl(CategoryServiceImpl categoryServiceImpl) {
//        this.categoryServiceImpl = categoryServiceImpl;
//    }
//
//    public byte[] generateCategoryTreeExcel() {
//        try (Workbook workbook = new XSSFWorkbook()) {
//            Sheet sheet = workbook.createSheet("Category Tree");
//            createHeader(sheet);
//            List<Category> categories = categoryServiceImpl.getAllCategories();
//            fillData(sheet, categories);
//
//            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//                workbook.write(out);
//                return out.toByteArray();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to create Excel file", e);
//        }
//    }
//
//    private void createHeader(Sheet sheet) {
//        Row headerRow = sheet.createRow(0);
//        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
//        Font headerFont = sheet.getWorkbook().createFont();
//        headerFont.setBold(true);
//        headerStyle.setFont(headerFont);
//
//        Cell cell = headerRow.createCell(0);
//        cell.setCellValue("Category");
//        cell.setCellStyle(headerStyle);
//
//        cell = headerRow.createCell(1);
//        cell.setCellValue("Parent Category");
//        cell.setCellStyle(headerStyle);
//    }
//
//    private void fillData(Sheet sheet, List<Category> categories) {
//        int rowIndex = 1;
//        for (Category category : categories) {
//            Row row = sheet.createRow(rowIndex++);
//            row.createCell(0).setCellValue(category.getName());
//            String parentName = category.getParent() != null ? category.getParent().getName() : " ";
//            row.createCell(1).setCellValue(parentName);
//        }
//    }
//}

//package com.programmingtechie.pandevtt.service;
//
//import com.programmingtechie.pandevtt.model.Category;
//import com.programmingtechie.pandevtt.repository.CategoryRepository;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class ExcelExportService {
//
//    private final CategoryRepository categoryRepository;
//
//    public ExcelExportService(CategoryRepository categoryRepository) {
//        this.categoryRepository = categoryRepository;
//    }
//
//    public byte[] generateCategoryTreeExcel() {
//        try (Workbook workbook = new XSSFWorkbook()) {
//            Sheet sheet = workbook.createSheet("Category Tree");
//            createHeader(sheet);
//            List<Category> rootCategories = categoryRepository.findAll()
//                    .stream()
//                    .filter(c -> c.getParent() == null) // Фильтруем корневые категории
//                    .collect(Collectors.toList());
//
//            int[] rowIndex = {1}; // Используем массив для изменения индекса
//            for (Category rootCategory : rootCategories) {
//                fillData(sheet, rootCategory, rowIndex, 1); // Заполняем данные начиная с корневой категории
//            }
//
//            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//                workbook.write(out);
//                return out.toByteArray();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Ошибка при создании Excel файла", e);
//        }
//    }
//
//    private void createHeader(Sheet sheet) {
//        Row headerRow = sheet.createRow(0);
//        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
//        Font headerFont = sheet.getWorkbook().createFont();
//        headerFont.setBold(true);
//        headerStyle.setFont(headerFont);
//
//        Cell cell = headerRow.createCell(0);
//        cell.setCellValue("ID");
//        cell.setCellStyle(headerStyle);
//
//        cell = headerRow.createCell(1);
//        cell.setCellValue("Название");
//        cell.setCellStyle(headerStyle);
//
//        cell = headerRow.createCell(2);
//        cell.setCellValue("Уровень");
//        cell.setCellStyle(headerStyle);
//    }
//
//    /**
//     * Рекурсивно заполняем Excel данные от корневой категории до дочерних
//     *
//     * @param sheet   Лист Excel
//     * @param category Текущая категория
//     * @param rowIndex Текущий индекс строки
//     * @param level   Уровень вложенности категории
//     */
//    private void fillData(Sheet sheet, Category category, int[] rowIndex, int level) {
//        Row row = sheet.createRow(rowIndex[0]++);
//        row.createCell(0).setCellValue(category.getId());
//
//        // Добавляем отступ в зависимости от уровня вложенности
//        StringBuilder nameBuilder = new StringBuilder();
//        nameBuilder.append("  ".repeat(Math.max(0, level - 1))).append("-- ".repeat(Math.max(0, level - 1)));
//        nameBuilder.append(category.getName());
//
//        row.createCell(1).setCellValue(nameBuilder.toString());
//        row.createCell(2).setCellValue(level);
//
//        // Обрабатываем дочерние категории
//        List<Category> childCategories = category.getChildren();
//        for (Category child : childCategories) {
//            fillData(sheet, child, rowIndex, level + 1);
//        }
//    }
//}

//package com.programmingtechie.pandevtt.service;
//
//import com.programmingtechie.pandevtt.model.Category;
//import lombok.RequiredArgsConstructor;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ExcelExportService {
//
//    private final CategoryService categoryService;
//
//    public byte[] generateCategoryTreeExcel() {
//        List<Category> categories = categoryService.getAllCategoriesWithChildren();
//
//        try (Workbook workbook = new XSSFWorkbook()) {
//            Sheet sheet = workbook.createSheet("Category Tree");
//            int rowIndex = 0;
//
//            // Создаем заголовок
//            Row headerRow = sheet.createRow(rowIndex++);
//            headerRow.createCell(0).setCellValue("Category Name");
//            headerRow.createCell(1).setCellValue("Parent Category");
//
//            // Заполняем данные
//            for (Category category : categories) {
//                rowIndex = writeCategoryToSheet(sheet, category, rowIndex, "");
//            }
//
//            // Записываем в ByteArrayOutputStream
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            workbook.write(outputStream);
//            return outputStream.toByteArray();
//
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to generate Excel file", e);
//        }
//    }
//
//    private int writeCategoryToSheet(Sheet sheet, Category category, int rowIndex, String indent) {
//        Row row = sheet.createRow(rowIndex++);
//        row.createCell(0).setCellValue(indent + category.getName());
//
//        if (category.getParent() != null) {
//            row.createCell(1).setCellValue(category.getParent().getName());
//        } else {
//            row.createCell(1).setCellValue("Root");
//        }
//
//        for (Category child : category.getChildren()) {
//            rowIndex = writeCategoryToSheet(sheet, child, rowIndex, indent + "--");
//        }
//
//        return rowIndex;
//    }
//}

//package com.programmingtechie.pandevtt.service;
//
//import com.programmingtechie.pandevtt.model.Category;
//import lombok.RequiredArgsConstructor;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class ExcelExportService {
//
//    private final CategoryService categoryService;
//
//    public byte[] generateCategoryTreeExcel() {
//        List<Category> categories = categoryService.getAllCategoriesWithChildren();
//
//        // Оставляем только корневые категории
//        List<Category> rootCategories = categories.stream()
//                .filter(category -> category.getParent() == null)
//                .collect(Collectors.toList());
//
//        try (Workbook workbook = new XSSFWorkbook()) {
//            Sheet sheet = workbook.createSheet("Category Tree");
//            int rowIndex = 0;
//
//            // Создаем заголовок
//            Row headerRow = sheet.createRow(rowIndex++);
//            headerRow.createCell(0).setCellValue("Category Name");
//            headerRow.createCell(1).setCellValue("Parent Category");
//
//            // Заполняем данные для корневых категорий
//            for (Category rootCategory : rootCategories) {
//                rowIndex = writeCategoryToSheet(sheet, rootCategory, rowIndex, "");
//            }
//
//            // Записываем в ByteArrayOutputStream
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            workbook.write(outputStream);
//            return outputStream.toByteArray();
//
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to generate Excel file", e);
//        }
//    }
//
//    private int writeCategoryToSheet(Sheet sheet, Category category, int rowIndex, String indent) {
//        // Записываем текущую категорию
//        Row row = sheet.createRow(rowIndex++);
//        row.createCell(0).setCellValue(indent + category.getName());
//
//        // Если родительская категория существует, добавляем её название, иначе Root
//        if (category.getParent() != null) {
//            row.createCell(1).setCellValue(category.getParent().getName());
//        } else {
//            row.createCell(1).setCellValue("Root");
//        }
//
//        // Рекурсивно обрабатываем дочерние элементы
//        for (Category child : category.getChildren()) {
//            rowIndex = writeCategoryToSheet(sheet, child, rowIndex, indent + "--");
//        }
//
//        return rowIndex;
//    }
//}
//
//

//package com.programmingtechie.pandevtt.service;
//
//import com.programmingtechie.pandevtt.model.Category;
//import lombok.RequiredArgsConstructor;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class ExcelExportService {
//
//    private final CategoryService categoryService;
//
//    public byte[] generateCategoryTreeExcel() {
//        List<Category> categories = categoryService.getAllCategoriesWithChildren();
//
//        // Оставляем только корневые категории
//        List<Category> rootCategories = categories.stream()
//                .filter(category -> category.getParent() == null)
//                .collect(Collectors.toList());
//
//        try (Workbook workbook = new XSSFWorkbook()) {
//            Sheet sheet = workbook.createSheet("Category Tree");
//            int rowIndex = 0;
//
//            // Создаем заголовок
//            Row headerRow = sheet.createRow(rowIndex++);
//            headerRow.createCell(0).setCellValue("Category Name");
//
//            // Заполняем данные для корневых категорий
//            for (Category rootCategory : rootCategories) {
//                rowIndex = writeCategoryToSheet(sheet, rootCategory, rowIndex, "");
//            }
//
//            // Записываем в ByteArrayOutputStream
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            workbook.write(outputStream);
//            return outputStream.toByteArray();
//
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to generate Excel file", e);
//        }
//    }
//
//    private int writeCategoryToSheet(Sheet sheet, Category category, int rowIndex, String indent) {
//        // Записываем текущую категорию с отступом
//        Row row = sheet.createRow(rowIndex++);
//        row.createCell(0).setCellValue(indent + category.getName());
//
//        // Рекурсивно обрабатываем дочерние элементы
//        for (Category child : category.getChildren()) {
//            rowIndex = writeCategoryToSheet(sheet, child, rowIndex, indent + "--");
//        }
//
//        return rowIndex;
//    }
//}
