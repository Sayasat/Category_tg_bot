package com.programmingtechie.pandevtt.command;

import com.programmingtechie.pandevtt.service.CategoryService;
import com.programmingtechie.pandevtt.service.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemoveElementCommand implements Command {

    private final CategoryService categoryService;

    @Override
    public String execute(String text, Long chatId) {
        String[] commandParts = text.split(" ");

        if (commandParts.length == 2) {
            String elementToRemove = commandParts[1];
            return categoryService.removeElement(elementToRemove, chatId);
        } else {
            return "Некорректный формат команды. Используйте /removeElement <элемент>.";
        }

    }
}
