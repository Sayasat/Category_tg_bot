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

    public String removeElement(String elementName, Long chatId) {
        Optional<Category> elementOpt = categoryRepository.findByNameAndChatId(elementName, chatId);

        if (elementOpt.isEmpty()) {
            return "Элемент \"" + elementName + "\" не найден.";
        }

        categoryRepository.delete(elementOpt.get());
        return "Элемент \"" + elementName + "\" и его дочерние элементы успешно удалены.";
    }
}

