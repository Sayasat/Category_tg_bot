package com.programmingtechie.pandevtt.command;

import com.programmingtechie.pandevtt.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddElementCommand implements Command {

    // Сервис для работы с категориями, внедряется через конструктор
    private final CategoryService categoryService;

    /**
     * Метод выполнения команды /addElement.
     * Он разбивает текст команды на части и вызывает соответствующий метод сервиса для добавления элемента.
     *
     * @param text    текст команды, например "/addElement <родитель> <элемент>"
     * @param chatId  идентификатор чата, для отправки ответа пользователю
     * @return строка с ответом на команду
     */
    @Override
    public String execute(String text, Long chatId) {
        // Разделяем команду на части
        String[] commandParts = text.split(" ");
        String response;

        // Проверка на минимальный формат команды, если количество частей меньше двух
        if (commandParts.length < 2) {
            return "Некорректный формат команды. Используйте: /addElement <родитель> <элемент> или /addElement <элемент>.";
        }

        // Если команда состоит из двух частей, то добавляется новый элемент без родителя
        if (commandParts.length == 2) {
            String newElement = commandParts[1];
            response = categoryService.addElement(null, newElement, chatId);
        }
        // Если команда состоит из трех частей, то добавляется новый элемент с родителем
        else if (commandParts.length == 3) {
            String parentElement = commandParts[1];
            String newElement = commandParts[2];
            response = categoryService.addElement(parentElement, newElement, chatId);
        }
        // Если количество частей команды больше трех, возвращаем ошибку
        else {
            response = "Некорректный формат команды. Используйте /addElement <родитель> <элемент> или /addElement <элемент>.";
        }

        return response;
    }
}
