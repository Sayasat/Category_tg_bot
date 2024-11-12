package com.programmingtechie.pandevtt.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface CategoryService {

    String viewCategoryTree(String categoryName, Long chatId);

    String addElement(String parentName, String childName, Long chatId);

    String removeElement(String elementName, Long chatId);

    byte[] generateCategoryTreeExcel(Long chatId);

    String uploadCategoriesFromExcel(InputStream inputStream, Long chatId);
}
