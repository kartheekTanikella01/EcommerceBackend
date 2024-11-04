package com.example.EcommerceBackend.Controller;

import com.example.EcommerceBackend.Common.APIResponse;
import com.example.EcommerceBackend.DTO.CategoryDto;
import com.example.EcommerceBackend.Entity.Category;
import com.example.EcommerceBackend.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/admin/create")
    public void createCategory(@RequestBody CategoryDto category){
         categoryService.createCategory(category);
    }
    @GetMapping("/admin")
    public List<CategoryDto> allCategories(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/admin/{id}")
    public CategoryDto categoryById(@PathVariable int id){
        return categoryService.getCategoryById(id);
    }

    @PutMapping ("/admin/update/{categoryId}")
    public ResponseEntity<APIResponse> updateCategory(@PathVariable("categoryId") int categoryId, @RequestBody CategoryDto category ) {
        System.out.println("category id " + categoryId);
        CategoryDto category1= categoryService.getCategoryById(categoryId);
        categoryService.updateCategory(categoryId, category);
        return new ResponseEntity<APIResponse>(new APIResponse(true, "category has been updated"), HttpStatus.OK);
    }

    @DeleteMapping("/admin/{id}")
    public void deleteById(@PathVariable int id){
        categoryService.deleteCategory(id);

    }



}
