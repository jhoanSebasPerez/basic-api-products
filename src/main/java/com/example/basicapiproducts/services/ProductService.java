package com.example.basicapiproducts.services;

import com.example.basicapiproducts.entities.Product;
import com.example.basicapiproducts.entities.ProductImg;
import com.example.basicapiproducts.repositories.ProductRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ProductService {

    private ProductRepository productRepository;

    private AzureStorageService azureStorageService;

    public ProductService(ProductRepository productRepository,
                          AzureStorageService azureStorageService) {
        this.azureStorageService = azureStorageService;
        this.productRepository = productRepository;
    }

    public Product save(Product product){
        return productRepository.save(product);
    }

    public List<Product> findAll(){
        return productRepository.findAll();
    }

    public Product findByName(String name){
        Optional<Product> productOpt = productRepository.findByNameContains(name);
        if (!productOpt.isPresent())
            throw new NoSuchElementException("Not found product with that name!");
        return productOpt.get();
    }


    public Product update(Long id,Product product){
        Optional<Product> productOpt = productRepository.findById(id);
        if (!productOpt.isPresent())
            throw new NoSuchElementException("Not found product with that ID!");
        Product productDB = productOpt.get();
        productDB.fromOther(product);
        return productRepository.save(productDB);
    }

    public void delete(Long id){
        if(!productRepository.existsById(id))
            throw new NoSuchElementException("Not found product with that ID!");
        productRepository.deleteById(id);
    }

    public boolean uploadImage(@NonNull MultipartFile file, @NonNull String name){
        String url = azureStorageService.upload(file);
        if(url.isEmpty()) {
            return false;
        }
        ProductImg img = new ProductImg(file.getOriginalFilename(), url);
        Product product = findByName(name);

        product.getImages().add(img);
        productRepository.save(product);

        return true;
    }
}
