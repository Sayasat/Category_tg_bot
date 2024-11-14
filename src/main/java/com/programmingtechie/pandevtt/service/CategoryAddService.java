package com.programmingtechie.pandevtt.service;

import com.programmingtechie.pandevtt.model.Category;
import com.programmingtechie.pandevtt.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryAddService {

    private final CategoryRepository categoryRepository;

    // Метод для добавления элемента. Если parentName не null, это дочерний элемент, иначе - корневой элемент
    public String addElement(String parentName, String childName, Long chatId) {
        if (parentName != null) {
            return addChildToParent(parentName, childName, chatId);
        }
        return addRootElement(childName, chatId);
    }

    // Метод для добавления дочернего элемента в родительскую категорию
    private String addChildToParent(String parentName, String childName, Long chatId) {
        // Ищем родительскую категорию по имени и chatId
        Optional<Category> parentOpt = categoryRepository.findByNameAndChatId(parentName, chatId);
        if (parentOpt.isEmpty()) {
            return "Родительский элемент \"" + parentName + "\" не существует.";
        }

        Category parent = parentOpt.get();

        // Проверяем, не существует ли уже дочерний элемент в данной родительской категории
        if (categoryRepository.findByNameAndParentAndChatId(childName, parent, chatId).isPresent()) {
            return "Элемент \"" + childName + "\" уже существует в категории \"" + parentName + "\".";
        }

        // Если элемент уже существует, обновляем его, иначе создаем новый
        Category child = categoryRepository.findByNameAndChatId(childName, chatId).orElse(new Category(childName, parent));
        child.setParent(parent); // Устанавливаем родителя
        child.setChatId(chatId); // Устанавливаем chatId
        categoryRepository.save(child); // Сохраняем дочерний элемент
        return "Элемент \"" + childName + "\" добавлен в категорию \"" + parentName + "\".";
    }

    // Метод для добавления корневого элемента
    private String addRootElement(String childName, Long chatId) {
        // Проверяем, существует ли уже корневой элемент с таким именем
        if (categoryRepository.findByNameAndChatId(childName, chatId).isPresent()) {
            return "Корневой элемент \"" + childName + "\" уже существует.";
        }

        // Создаем новый корневой элемент
        Category rootCategory = new Category(childName, null);
        rootCategory.setChatId(chatId); // Устанавливаем chatId
        categoryRepository.save(rootCategory); // Сохраняем корневой элемент
        return "Корневой элемент \"" + childName + "\" успешно добавлен.";
    }
}
