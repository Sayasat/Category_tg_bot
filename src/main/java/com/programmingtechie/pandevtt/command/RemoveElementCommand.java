package com.programmingtechie.pandevtt.command;

import com.programmingtechie.pandevtt.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemoveElementCommand implements Command {

    // Ссылка на сервис для работы с категориями
    private final CategoryService categoryService;

    /**
     * Метод для обработки команды /removeElement, который удаляет элемент и его дочерние элементы.
     *
     * @param text текст команды
     * @param chatId идентификатор чата
     * @return ответ с результатом удаления или сообщение о некорректной команде
     */
    @Override
    public String execute(String text, Long chatId) {
        // Разделяем текст команды на части
        String[] commandParts = text.split(" ");

        // Проверка на правильность формата команды
        if (commandParts.length == 2) {
            // Получаем название элемента для удаления
            String elementToRemove = commandParts[1];

            // Вызов сервиса для удаления элемента и возврат результата
            return categoryService.removeElement(elementToRemove, chatId);
        } else {
            // Ответ, если команда введена неправильно
            return "Некорректный формат команды. Используйте /removeElement <элемент>.";
        }
    }
}
