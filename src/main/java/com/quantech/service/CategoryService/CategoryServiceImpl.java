package com.quantech.service.CategoryService;

import com.quantech.model.Category;
import com.quantech.model.Ward;
import com.quantech.repo.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public void saveCategory(Category category) throws NullPointerException {
        categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> cs = new ArrayList<>();
        categoryRepository.findAll().forEach(c -> cs.add(c));
        return cs;
    }

    @Override
    public Category getCategory(Long id) {
        return categoryRepository.findOne(id);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.delete(id);
    }
}
