package com.example.EcommerceBackend.Controller;

import com.example.EcommerceBackend.Common.APIResponse;
import com.example.EcommerceBackend.DTO.ProductDto;
import com.example.EcommerceBackend.Entity.Category;
import com.example.EcommerceBackend.Repository.CategoryRepository;
import com.example.EcommerceBackend.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("/sellar")
    public ResponseEntity<APIResponse> createProduct(@RequestBody ProductDto productDto) {
        // Check if the category exists using categoryId from productDto
        Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());

        // If the category is not found, return a Bad Request response
        if (!optionalCategory.isPresent()) {
            return new ResponseEntity<>(new APIResponse(false, "Category does not exist"), HttpStatus.BAD_REQUEST);
        }

        // Call the service to create the product
        productService.createProduct(productDto, optionalCategory.get());

        // Return a successful response with HttpStatus.CREATED
        return new ResponseEntity<>(new APIResponse(true, "Product has been added"), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductbyId(@PathVariable int id){
        ProductDto productDto=productService.getProductByid(id);

        return  new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @PutMapping("/sellar/{productId}")
    public ResponseEntity<APIResponse> updateProduct(@PathVariable("productId") Integer productId, @RequestBody ProductDto productDto) throws Exception {
        Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());
        if (!optionalCategory.isPresent()) {
            return new ResponseEntity<APIResponse>(new APIResponse(false, "category does not exists"), HttpStatus.BAD_REQUEST);
        }
        productService.updateProduct(productDto, productId);
        return new ResponseEntity<APIResponse>(new APIResponse(true, "product has been updated"), HttpStatus.OK);
    }

    @DeleteMapping("/sellar/{productId}")
    public  void deleteProduct(@PathVariable("productId") Integer productId){
        productService.deleteProduct(productId);
    }

}
