package com.programmingtechie.pandevtt.repository;

import com.programmingtechie.pandevtt.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Найти все категории, у которых нет родительской категории (корневые категории) для данного chatId
    List<Category> findByParentIsNullAndChatId(Long chatId);

    // Найти все категории, у которых указанный родитель и указанный chatId
    List<Category> findByParentAndChatId(Category category, Long chatId);

    // Найти все категории для указанного chatId
    List<Category> findByChatId(Long chatId);

    // Найти категорию по имени и chatId
    Optional<Category> findByNameAndChatId(String name, Long chatId);

    // Найти категорию по имени, родительской категории и chatId
    Optional<Category> findByNameAndParentAndChatId(String name, Category parent, Long chatId);
}
