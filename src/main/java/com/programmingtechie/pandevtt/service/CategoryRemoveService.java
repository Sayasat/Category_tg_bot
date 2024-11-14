package com.programmingtechie.pandevtt.service;

import com.programmingtechie.pandevtt.model.Category;
import com.programmingtechie.pandevtt.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryRemoveService {

    private final CategoryRepository categoryRepository;

    /**
     * Удаляет элемент категории и все его дочерние элементы.
     *
     * @param elementName имя элемента для удаления.
     * @param chatId идентификатор чата, в котором находится элемент.
     * @return сообщение об успешности удаления.
     */
    public String removeElement(String elementName, Long chatId) {
        Optional<Category> elementOpt = categoryRepository.findByNameAndChatId(elementName, chatId);

        if (elementOpt.isEmpty()) {
            return "Элемент \"" + elementName + "\" не найден.";
        }

        Category element = elementOpt.get();

        // Проверяем, есть ли у элемента дочерние категории
        if (!element.getChildren().isEmpty()) {
            // Уведомляем пользователя, что у элемента есть дочерние элементы
            return "Элемент \"" + elementName + "\" имеет дочерние категории, которые нужно удалить сначала.";
        }

        // Удаляем элемент (категорию) и её дочерние элементы, если они есть
        categoryRepository.delete(element);
        return "Элемент \"" + elementName + "\" и его дочерние элементы успешно удалены.";
    }
}
