//package com.programmingtechie.pandevtt.service;
//
//import com.programmingtechie.pandevtt.model.Category;
//import com.programmingtechie.pandevtt.repository.CategoryRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class CategoryService {
//
//    private final CategoryRepository categoryRepository;
//
//    public List<Category> getAllCategories() {
//        return categoryRepository.findAll();
//    }
//
//    public String viewCategoryTree(String categoryName) {
//        StringBuilder response = new StringBuilder();
//        response.append("Categories Tree:\n");
//
//        if (categoryName != null && !categoryName.isEmpty()) {
//            var categoryOpt = categoryRepository.findByName(categoryName);
//            if (categoryOpt.isEmpty()) {
//                return "Категория с именем \"" + categoryName + "\" не найдена.";
//            }
//
//            Category category = categoryOpt.get();
//            response.append(viewCategory(category, 0));
//        } else {
//            List<Category> rootCategories = categoryRepository.findByParentIsNull();
//            if (rootCategories.isEmpty()) {
//                return "Корневые категории отсутствуют.";
//            }
//
//            for (Category root : rootCategories) {
//                response.append(viewCategory(root, 0));
//            }
//        }
//
//        return response.toString();
//    }
//
//    private String viewCategory(Category category, int level) {
//        StringBuilder builder = new StringBuilder();
//        builder.append(" ".repeat(level * 2)).append("- ").append(category.getName()).append("\n");
//
//        List<Category> children = categoryRepository.findByParent(category);
//        for (Category child : children) {
//            builder.append(viewCategory(child, level + 1));
//        }
//
//        return builder.toString();
//    }
//
//    public String addElement(String parentName, String childName) {
//        if (parentName != null) {
//            Optional<Category> parentFind = categoryRepository.findByName(parentName);
//            if (parentFind.isEmpty()) {
//                return "Родительский элемент \"" + parentName + "\" не существует.";
//            }
//
//            Category parent = parentFind.get();
//
//            Optional<Category> existingChild = categoryRepository.findByNameAndParent(childName, parent);
//            if (existingChild.isPresent()) {
//                return "Элемент с именем \"" + childName + "\" уже существует в категории \"" + parentName + "\".";
//            }
//
//            Category childToCheck = categoryRepository.findByName(childName).orElse(null);
//            if (childToCheck != null && childToCheck.getParent() == null) {
//                childToCheck.setParent(parent);
//                categoryRepository.save(childToCheck);
//                return "Элемент \"" + childName + "\" успешно добавлен в категорию \"" + parentName + "\".";
//            }
//
//            Category child = new Category();
//            child.setName(childName);
//            child.setParent(parent);
//            categoryRepository.save(child);
//
//            return "Элемент \"" + childName + "\" успешно добавлен в категорию \"" + parentName + "\".";
//        } else {
//            Optional<Category> existingRootCategory = categoryRepository.findByName(childName);
//
//            if (existingRootCategory.isPresent() && existingRootCategory.get().getParent() == null) {
//                return "Корневой элемент \"" + childName + "\" уже существует.";
//            }
//
//            Category rootCategory = new Category();
//            rootCategory.setName(childName);
//            rootCategory.setParent(null);
//            categoryRepository.save(rootCategory);
//
//            return "Корневой элемент \"" + childName + "\" успешно добавлен.";
//        }
//    }
//
//    public String removeElement(String elementName) {
//        Optional<Category> elemFind = categoryRepository.findByName(elementName);
//
//        if(elemFind.isPresent()) {
//            categoryRepository.delete(elemFind.get());
//            return "Element \"" + elementName + "\" and his child elements successfully deleted.";
//        } else {
//            return "Element \"" + elementName + "\" does not exist";
//        }
//    }
//}

//package com.programmingtechie.pandevtt.service;
//
//import com.programmingtechie.pandevtt.model.Category;
//import com.programmingtechie.pandevtt.repository.CategoryRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class CategoryServiceImpl {
//
//    private final CategoryRepository categoryRepository;
//
//    /**
//     * Получение всех категорий из базы данных.
//     */
//    public List<Category> getAllCategories() {
//        return categoryRepository.findAll();
//    }
//
//    /**
//     * Просмотр дерева категорий. Если передано имя категории, то отображает дерево от этой категории.
//     */
//    public String viewCategoryTree(String categoryName) {
//        StringBuilder response = new StringBuilder("Categories Tree:\n");
//
//        if (categoryName != null && !categoryName.isEmpty()) {
//            return viewFromCategoryName(categoryName, response);
//        }
//
//        List<Category> rootCategories = categoryRepository.findByParentIsNull();
//        if (rootCategories.isEmpty()) {
//            return "Корневые категории отсутствуют.";
//        }
//
//        for (Category root : rootCategories) {
//            appendCategoryToResponse(root, response, 0);
//        }
//
//        return response.toString();
//    }
//
//    private String viewFromCategoryName(String categoryName, StringBuilder response) {
//        var categoryOpt = categoryRepository.findByName(categoryName);
//        if (categoryOpt.isEmpty()) {
//            return "Категория с именем \"" + categoryName + "\" не найдена.";
//        }
//        appendCategoryToResponse(categoryOpt.get(), response, 0);
//        return response.toString();
//    }
//
//    /**
//     * Добавление элемента в дерево категорий. Если parentName не указан, то добавляется корневой элемент.
//     */
//    public String addElement(String parentName, String childName) {
//        if (parentName != null) {
//            return addChildToParent(parentName, childName);
//        }
//        return addRootElement(childName);
//    }
//
//    private String addChildToParent(String parentName, String childName) {
//        Optional<Category> parentOpt = categoryRepository.findByName(parentName);
//        if (parentOpt.isEmpty()) {
//            return "Родительский элемент \"" + parentName + "\" не существует.";
//        }
//
//        Category parent = parentOpt.get();
//        if (categoryRepository.findByNameAndParent(childName, parent).isPresent()) {
//            return "Элемент \"" + childName + "\" уже существует в категории \"" + parentName + "\".";
//        }
//
//        Optional<Category> existingCategoryOpt = categoryRepository.findByName(childName);
//        if (existingCategoryOpt.isPresent() && existingCategoryOpt.get().getParent() == null) {
//            Category existingCategory = existingCategoryOpt.get();
//            existingCategory.setParent(parent);
//            categoryRepository.save(existingCategory);
//            return "Элемент \"" + childName + "\" добавлен в категорию \"" + parentName + "\".";
//        }
//
//        Category child = new Category(childName, parent);
//        categoryRepository.save(child);
//        return "Элемент \"" + childName + "\" успешно добавлен в категорию \"" + parentName + "\".";
//    }
//
//    private String addRootElement(String childName) {
//        if (categoryRepository.findByName(childName).isPresent()) {
//            return "Корневой элемент \"" + childName + "\" уже существует.";
//        }
//
//        Category rootCategory = new Category(childName, null);
//        categoryRepository.save(rootCategory);
//        return "Корневой элемент \"" + childName + "\" успешно добавлен.";
//    }
//
//    /**
//     * Удаление элемента и его дочерних элементов из дерева категорий.
//     */
//    public String removeElement(String elementName) {
//        Optional<Category> elementOpt = categoryRepository.findByName(elementName);
//
//        if (elementOpt.isEmpty()) {
//            return "Элемент \"" + elementName + "\" не найден.";
//        }
//
//        categoryRepository.delete(elementOpt.get());
//        return "Элемент \"" + elementName + "\" и его дочерние элементы успешно удалены.";
//    }
//
//    /**
//     * Рекурсивное добавление категории в ответ с отступами.
//     */
//    private void appendCategoryToResponse(Category category, StringBuilder response, int level) {
//        response.append(" ".repeat(level * 2))
//                .append("- ")
//                .append(category.getName())
//                .append("\n");
//
//        List<Category> children = categoryRepository.findByParent(category);
//        children.forEach(child -> appendCategoryToResponse(child, response, level + 1));
//    }
//}


package com.programmingtechie.pandevtt.service;

import com.programmingtechie.pandevtt.model.Category;
import com.programmingtechie.pandevtt.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryViewService viewService;
    private final CategoryAddService addService;
    private final CategoryRemoveService removeService;
    private final CategoryDownloadServiceImpl downloadService;
    private final CategoryUploadService uploadService;
    private final CategoryRepository categoryRepository;

    @Override
    public String viewCategoryTree(String categoryName, Long chatId) {
        return viewService.viewCategoryTree(categoryName, chatId);
    }

    @Override
    public String addElement(String parentName, String childName, Long chatId) {
        return addService.addElement(parentName, childName, chatId);
    }

    @Override
    public String removeElement(String elementName, Long chatId) {
        return removeService.removeElement(elementName, chatId);
    }

    @Override
    public byte[] generateCategoryTreeExcel(Long chatId) {
        return downloadService.generateCategoryTreeExcel(chatId);
    }

    @Override
    public String uploadCategoriesFromExcel(InputStream inputStream, Long chatId) {
        return uploadService.parseAndSaveCategories(inputStream, chatId);
    }

//    @Override
//    public String uploadCategoriesFromExcel(MultipartFile file) {
//        return uploadService.handleFileUpload(file);
//    }
//    @Override
//    public String uploadCategoriesFromExcel(MultipartFile file) {
//        if (CategoryUploadService.isValidExcelFile(file)) {
//            try {
//                // Извлекаем категории из файла
//                List<Category> categories = uploadService.extractCategoriesFromExcel(file.getInputStream());
//
//                // Сохраняем категории в базу данных
//                // Сохранение в БД
//                categoryRepository.saveAll(categories);
//
//                return "Категории успешно загружены.";
//            } catch (Exception e) {
//                e.printStackTrace();
//                return "Ошибка при загрузке категорий. Пожалуйста, попробуйте снова.";
//            }
//        } else {
//            return "Неверный формат Excel файла. Убедитесь, что файл в правильном формате.";
//        }
//    }


//    @Override
//    public String uploadCategoriesFromExcel(InputStream inputStream) {
//        try {
//            // Проверка на валидность Excel файла, если нужна
//            if (CategoryUploadService.isValidExcelFile(inputStream)) {
//                // Извлекаем категории из файла
//                List<Category> categories = uploadService.extractCategoriesFromExcel(inputStream);
//
//                // Сохраняем категории в базу данных
//                categoryRepository.saveAll(categories);
//
//                return "Категории успешно загружены.";
//            } else {
//                return "Неверный формат Excel файла.";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Ошибка при загрузке категорий. Пожалуйста, попробуйте снова.";
//        }
//    }


}
