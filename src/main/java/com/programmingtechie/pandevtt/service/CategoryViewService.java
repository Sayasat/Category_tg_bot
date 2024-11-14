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

    /**
     * Метод для просмотра категорийного дерева, начиная с указанной категории.
     *
     * @param categoryName имя категории, с которой нужно начать просмотр (может быть null)
     * @param chatId идентификатор чата
     * @return строка с деревом категорий
     */
    public String viewCategoryTree(String categoryName, Long chatId) {
        StringBuilder response = new StringBuilder("Категорийное дерево:\n");

        // Если категория задана, то ищем её и показываем дерево с этой категории
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            return viewFromCategoryName(categoryName.trim(), response, chatId);
        }

        // Ищем корневые категории, если категория не задана
        List<Category> rootCategories = categoryRepository.findByParentIsNullAndChatId(chatId);
        if (rootCategories.isEmpty()) {
            return "Корневые категории отсутствуют.";
        }

        // Для каждой корневой категории рекурсивно добавляем дочерние категории
        rootCategories.forEach(root -> appendCategoryToResponse(root, response, 0, chatId));

        return response.toString();
    }

    /**
     * Метод для отображения категорийного дерева с конкретной категории по имени.
     *
     * @param categoryName имя категории, с которой начинается просмотр
     * @param response строка с деревом категорий
     * @param chatId идентификатор чата
     * @return строка с деревом категорий
     */
    private String viewFromCategoryName(String categoryName, StringBuilder response, Long chatId) {
        Optional<Category> categoryOpt = categoryRepository.findByNameAndChatId(categoryName, chatId);
        if (categoryOpt.isEmpty()) {
            return "Категория с именем \"" + categoryName + "\" не найдена.";
        }
        appendCategoryToResponse(categoryOpt.get(), response, 0, chatId);
        return response.toString();
    }

    /**
     * Рекурсивный метод для добавления категорий и их подкатегорий в строку ответа.
     *
     * @param category текущая категория
     * @param response строка с деревом категорий
     * @param level уровень вложенности категории
     * @param chatId идентификатор чата
     */
    private void appendCategoryToResponse(Category category, StringBuilder response, int level, Long chatId) {
        // Формируем строку для текущей категории с соответствующим уровнем отступа
        response.append(" ".repeat(level * 2))
                .append("- ")
                .append(category.getName())
                .append("\n");

        // Ищем дочерние категории и добавляем их рекурсивно
        List<Category> children = categoryRepository.findByParentAndChatId(category, chatId);
        children.forEach(child -> appendCategoryToResponse(child, response, level + 1, chatId));
    }
}
