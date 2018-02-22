package com.quantech.service.CategoryService;

import com.quantech.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    /**
     * Saves the given category object into the repository.
     * @param category The category object to save.
     * @throws NullPointerException If the name has not been set, that is, is null.
     */
    public void saveCategory(Category category) throws NullPointerException;

    /**
     * @return A list of all categories stored in the category repository.
     */
    public List<Category> getAllCategories();

    /**
     * Finds a category corresponding to the unique id, stored in the repository.
     * @param id The id for which a category is a associated with.
     * @return A category corresponding to the id if one exists, null otherwise.
     */
    public Category getCategory(Long id);

    /**
     * Deletes the category with the corresponding id from the repository.
     * @param id The id corresponding to the category to be deleted.
     */
    public void deleteCategory(Long id);
}
