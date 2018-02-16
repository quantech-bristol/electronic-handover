package com.quantech.service.CategoryService;

import com.quantech.model.Category;
import com.quantech.repo.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("categoryService")
public class CategoryServiceImpl {
    @Autowired
    CategoryRepository categoryRepository;

    public void saveCategory(Category category) throws NullPointerException, IllegalArgumentException {
        categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        List<Category> cs = new ArrayList<>();
        categoryRepository.findAll().forEach(c -> cs.add(c));
        return cs;
    }
}
