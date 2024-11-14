package com.programmingtechie.pandevtt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    // Поле id, которое является уникальным идентификатором категории
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация значения id
    private Long id;

    // Поле для хранения названия категории
    private String name;

    // Связь с родительской категорией (если есть)
    @ManyToOne(fetch = FetchType.LAZY) // LAZY - загрузка родительской категории по требованию
    @JoinColumn(name = "parent_name") // Внешний ключ, указывающий на родительскую категорию
    private Category parent;

    // chatId для хранения ID чата, к которому принадлежит категория
    private Long chatId;

    // Связь с дочерними категориями
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> children = new ArrayList<>();

    // Конструктор для создания категории с родительской категорией
    public Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
    }
}
