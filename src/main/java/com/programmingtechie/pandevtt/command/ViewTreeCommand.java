package com.programmingtechie.pandevtt.command;

import com.programmingtechie.pandevtt.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ViewTreeCommand implements Command {

    // Ссылка на сервис для работы с категориями
    private final CategoryService categoryService;

    /**
     * Метод для обработки команды /viewTree, который отображает дерево категорий.
     * При необходимости может отображать подкатегории, если указано имя категории.
     *
     * @param text текст команды
     * @param chatId идентификатор чата
     * @return строка с результатом выполнения команды
     */
    @Override
    public String execute(String text, Long chatId) {
        // Разделяем команду на части по пробелам, чтобы извлечь имя категории, если оно указано
        String[] parts = text.split("\\s+", 2);
        String categoryName = parts.length > 1 ? parts[1].trim() : null;

        // Передаем имя категории (если указано) и идентификатор чата в сервис для отображения дерева
        return categoryService.viewCategoryTree(categoryName, chatId);
    }
}
