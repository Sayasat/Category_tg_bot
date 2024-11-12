package com.programmingtechie.pandevtt;

import com.programmingtechie.pandevtt.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PandevTtApplicationTests {

    private CategoryRepository categoryRepository;

    @Test
    void contextLoads() {
    }

    @Test
    public void testCategoryRepository() {
        System.out.println("test started");
        if(categoryRepository == null) {
            System.out.println("fine");
        }else
            categoryRepository.findAll().forEach(ca -> System.out.println(ca.getName()));
    }
}
