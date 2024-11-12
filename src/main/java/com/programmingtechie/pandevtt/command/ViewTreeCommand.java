package com.programmingtechie.pandevtt.command;

import com.programmingtechie.pandevtt.service.CategoryService;
import com.programmingtechie.pandevtt.service.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ViewTreeCommand implements Command {

    private final CategoryService categoryService;

    @Override
    public String execute(String text, Long chatId) {
        String[] parts = text.split("\\s+", 2);
        String categoryName = parts.length > 1 ? parts[1].trim() : null;

        return categoryService.viewCategoryTree(categoryName, chatId);
    }
}
