package com.programmingtechie.pandevtt.command;

import com.programmingtechie.pandevtt.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddElementCommand implements Command {

    private final CategoryService categoryService;

    @Override
    public String execute(String text, Long chatId) {
        String[] commandParts = text.split(" ");
        String response;

        if (commandParts.length < 2) {
            return "Некорректный формат команды. Используйте: /addElement <родитель> <элемент> или /addElement <элемент>.";
        }

        if (commandParts.length == 2) {
            String newElement = commandParts[1];
            response = categoryService.addElement(null, newElement, chatId);
        } else if (commandParts.length == 3) {
            String parentElement = commandParts[1];
            String newElement = commandParts[2];
            response = categoryService.addElement(parentElement, newElement, chatId);
        } else {
            response = "Некорректный формат команды. Используйте /addElement <родитель> <элемент> или /addElement <элемент>.";
        }
        return response;
    }
}

