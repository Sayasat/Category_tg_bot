package com.programmingtechie.pandevtt.service;

import com.programmingtechie.pandevtt.model.Category;
import com.programmingtechie.pandevtt.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryViewService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public String viewCategoryTree(String categoryName, Long chatId) {
        StringBuilder response = new StringBuilder("Categories Tree:\n");

        if (categoryName != null && !categoryName.isEmpty()) {
            return viewFromCategoryName(categoryName, response, chatId);
        }

        List<Category> rootCategories = categoryRepository.findByParentIsNullAndChatId(chatId);
        if (rootCategories.isEmpty()) {
            return "Корневые категории отсутствуют.";
        }

        for (Category root : rootCategories) {
            appendCategoryToResponse(root, response, 0, chatId);
        }

        return response.toString();
    }

    private String viewFromCategoryName(String categoryName, StringBuilder response, Long chatId) {
        Optional<Category> categoryOpt = categoryRepository.findByNameAndChatId(categoryName, chatId);
        if (categoryOpt.isEmpty()) {
            return "Категория с именем \"" + categoryName + "\" не найдена.";
        }
        appendCategoryToResponse(categoryOpt.get(), response, 0, chatId);
        return response.toString();
    }

    private void appendCategoryToResponse(Category category, StringBuilder response, int level, Long chatId) {
        response.append(" ".repeat(level * 2))
                .append("- ")
                .append(category.getName())
                .append("\n");

        List<Category> children = categoryRepository.findByParentAndChatId(category, chatId);
        children.forEach(child -> appendCategoryToResponse(child, response, level + 1, chatId));
    }
}

