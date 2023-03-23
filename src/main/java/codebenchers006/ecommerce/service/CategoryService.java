package codebenchers006.ecommerce.service;

import codebenchers006.ecommerce.model.Category;
import codebenchers006.ecommerce.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {

    @Autowired
    CategoryRepo categoryRepo;
    public void createCategory(Category category){
       // System.out.println("adding category");
        categoryRepo.save(category);
    }

    public List<Category> showCategories() {
        return categoryRepo.findAll();
    }

    public Category readCategory(String categoryName) {
        return categoryRepo.findByCategoryName(categoryName);
    }

    public Optional<Category> readCategory(Integer categoryId) {
        return categoryRepo.findById(categoryId);
    }

    public void updateCategory(int id, Category category) {

            Category oldCategory = categoryRepo.findById(id).get();

            oldCategory.setCategoryName(category.getCategoryName());
            oldCategory.setDescription(category.getDescription());
            oldCategory.setProducts(category.getProducts());
            categoryRepo.save(oldCategory);

        }


    public void deleteCategory(int id) {

            categoryRepo.deleteById(id);

    }

    public boolean findById(int id) {
        return categoryRepo.findById(id).isPresent();
    }

    public Category getCategoryUsingId(int id) {
        //System.out.println("list");
        return categoryRepo.findById(id).get();
    }
}
