package com.example.EcommerceBackend.Service;

import com.example.EcommerceBackend.Common.APIResponse;
import com.example.EcommerceBackend.DTO.ProductDto;
import com.example.EcommerceBackend.Entity.Category;
import com.example.EcommerceBackend.Entity.Product;
import com.example.EcommerceBackend.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public void createProduct(ProductDto productdto, Category category){
        Product product = new Product();
        product.setDescription(productdto.getDescription());

        product.setName(productdto.getName());
        product.setCategory(category);
        product.setPrice(productdto.getPrice());
        productRepository.save(product);
    }

    public List<ProductDto> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();

        List<ProductDto> productDtos = new ArrayList<>();
        for(Product product: allProducts) {
            productDtos.add(getProductDto(product));
        }
        return productDtos;
    }

    public  ProductDto getProductByid(int id){
        Product product=productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Product not found"));

        return getProductDto(product);

    }

    public void deleteProduct(int id){
        Product product=productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Product not found"));

        productRepository.delete(product);
    }

    public ProductDto getProductDto(Product product){
        ProductDto productDto=new ProductDto();
        productDto.setDescription(product.getDescription());
        productDto.setName(product.getName());
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setPrice(product.getPrice());
        productDto.setId(product.getId());
        return productDto;
    }

    public void updateProduct(ProductDto productDto, Integer productId) throws Exception {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        // throw an exception if product does not exists
        if (!optionalProduct.isPresent()) {
            throw new Exception("product not present");
        }
        Product product = optionalProduct.get();
        product.setDescription(productDto.getDescription());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        productRepository.save(product);
    }
}
