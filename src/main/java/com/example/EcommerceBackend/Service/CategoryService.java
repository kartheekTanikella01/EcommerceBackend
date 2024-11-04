package com.example.EcommerceBackend.Service;

import com.example.EcommerceBackend.DTO.CategoryDto;
import com.example.EcommerceBackend.DTO.ProductDto;
import com.example.EcommerceBackend.Entity.Category;
import com.example.EcommerceBackend.Entity.Product;
import com.example.EcommerceBackend.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void createCategory(CategoryDto category) {
        Category categoryEntity = new Category();
        categoryEntity.setCategoryName(category.getCategoryName());
        categoryEntity.setDescription(category.getDescription());
        categoryRepository.save(categoryEntity);
    }

    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categories) {
            categoryDtos.add(getCategoryDto(category));
        }
        return categoryDtos;
    }

    public CategoryDto getCategoryById(int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category with this ID not found"));
        return getCategoryDto(category);
    }

    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }

    public void updateCategory(int id, CategoryDto category) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (!existingCategory.isPresent()) {
            throw new RuntimeException("No category found");
        }
        Category categoryEntity = existingCategory.get();
        categoryEntity.setCategoryName(category.getCategoryName());
        categoryEntity.setDescription(category.getDescription());
        // Set other fields if needed
        categoryRepository.save(categoryEntity);
    }

    public CategoryDto getCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setCategoryName(category.getCategoryName());
        categoryDto.setDescription(category.getDescription());

        // Convert Product entities to ProductDto
        List<ProductDto> productDtos = category.getProducts().stream().map(product -> {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setPrice(product.getPrice());
            // Set other fields as necessary
            return productDto;
        }).collect(Collectors.toList());

        categoryDto.setProducts(productDtos);
        return categoryDto;
    }
}
