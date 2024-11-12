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

    public String addElement(String parentName, String childName, Long chatId) {
        if (parentName != null) {
            return addChildToParent(parentName, childName, chatId);
        }
        return addRootElement(childName, chatId);
    }

    private String addChildToParent(String parentName, String childName, Long chatId) {
        Optional<Category> parentOpt = categoryRepository.findByNameAndChatId(parentName, chatId);
        if (parentOpt.isEmpty()) {
            return "Родительский элемент \"" + parentName + "\" не существует.";
        }

        Category parent = parentOpt.get();
        if (categoryRepository.findByNameAndParentAndChatId(childName, parent, chatId).isPresent()) {
            return "Элемент \"" + childName + "\" уже существует в категории \"" + parentName + "\".";
        }

        Category child = categoryRepository.findByNameAndChatId(childName, chatId).orElse(new Category(childName, parent));
        child.setParent(parent);
        child.setChatId(chatId);
        categoryRepository.save(child);
        return "Элемент \"" + childName + "\" добавлен в категорию \"" + parentName + "\".";
    }

    private String addRootElement(String childName, Long chatId) {
        if (categoryRepository.findByNameAndChatId(childName, chatId).isPresent()) {
            return "Корневой элемент \"" + childName + "\" уже существует.";
        }

        Category rootCategory = new Category(childName, null);
        rootCategory.setChatId(chatId);
        categoryRepository.save(rootCategory);
        return "Корневой элемент \"" + childName + "\" успешно добавлен.";
    }
}
