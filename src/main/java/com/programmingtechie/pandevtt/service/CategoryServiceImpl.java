package com.programmingtechie.pandevtt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    // Сервисы для обработки разных операций с категориями
    private final CategoryViewService viewService;
    private final CategoryAddService addService;
    private final CategoryRemoveService removeService;
    private final CategoryDownloadServiceImpl downloadService;
    private final CategoryUploadService uploadService;

    /**
     * Отображает дерево категорий для заданного чата.
     *
     * @param categoryName имя категории для отображения.
     * @param chatId идентификатор чата.
     * @return строковое представление дерева категорий.
     */
    @Override
    public String viewCategoryTree(String categoryName, Long chatId) {
        return viewService.viewCategoryTree(categoryName, chatId);
    }

    /**
     * Добавляет новый элемент в категорию.
     *
     * @param parentName имя родительской категории.
     * @param childName имя дочерней категории.
     * @param chatId идентификатор чата, для которого нужно добавить элемент.
     * @return сообщение об успешности добавления.
     */
    @Override
    public String addElement(String parentName, String childName, Long chatId) {
        return addService.addElement(parentName, childName, chatId);
    }

    /**
     * Удаляет элемент категории по имени.
     *
     * @param elementName имя элемента категории.
     * @param chatId идентификатор чата, для которого нужно удалить элемент.
     * @return сообщение об успешности удаления.
     */
    @Override
    public String removeElement(String elementName, Long chatId) {
        return removeService.removeElement(elementName, chatId);
    }

    /**
     * Генерирует Excel файл, представляющий дерево категорий для чата.
     *
     * @param chatId идентификатор чата, для которого нужно создать Excel файл.
     * @return байтовый массив, содержащий Excel файл.
     */
    @Override
    public byte[] generateCategoryTreeExcel(Long chatId) {
        return downloadService.generateCategoryTreeExcel(chatId);
    }

    /**
     * Загружает категории из Excel файла.
     *
     * @param inputStream поток данных Excel файла.
     * @param chatId идентификатор чата, для которого нужно загрузить категории.
     * @return сообщение об успешности загрузки.
     */
    @Override
    public String uploadCategoriesFromExcel(InputStream inputStream, Long chatId) {
        return uploadService.parseAndSaveCategories(inputStream, chatId);
    }
}
