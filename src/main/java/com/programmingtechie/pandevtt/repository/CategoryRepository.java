package com.programmingtechie.pandevtt.repository;

import com.programmingtechie.pandevtt.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    List<Category> findByParentIsNullAndChatId(Long chatId);
    Optional<Category> findByNameAndParentName(String name, String parentName);
    Optional<Category> findByNameAndParent(String name, Category parent);
    List<Category> findByParentAndChatId(Category category, Long chatId);
    List<Category> findByChatId(Long chatId);
    Optional<Category> findByNameAndChatId(String name, Long chatId);
    Optional<Category> findByNameAndParentAndChatId(String name, Category parent, Long chatId);
}
