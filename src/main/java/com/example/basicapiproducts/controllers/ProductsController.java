package com.example.basicapiproducts.controllers;

import com.example.basicapiproducts.dtos.ResponseDto;
import com.example.basicapiproducts.entities.Product;
import com.example.basicapiproducts.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    private ProductService productService;

    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> save(@RequestBody Product product){
        Product newProduct = productService.save(product);

        Map data = new HashMap<String, Object>();
        data.put("product", newProduct);

        ResponseDto response =
                new ResponseDto(HttpStatus.CREATED.value(), data, LocalDate.now());

        return ResponseEntity.created(URI.create("/api/products/" + product.getId())).body(response);
    }

    @GetMapping("")
    public ResponseEntity<ResponseDto> findAll(){
        List<Product> products = productService.findAll();

        Map data = new HashMap<String, Object>();
        data.put("products", products);
        data.put("length", products.size());

        ResponseDto response =
                new ResponseDto(HttpStatus.CREATED.value(), data, LocalDate.now());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{name}")
    public ResponseEntity<ResponseDto> findByName(@PathVariable String name){
        Product product = productService.findByName(name);

        Map data = new HashMap<String, Object>();
        data.put("product", product);

        ResponseDto response =
                new ResponseDto(HttpStatus.CREATED.value(), data, LocalDate.now());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> update(@PathVariable Long id, @RequestBody Product product){
        Product productUpd = productService.update(id, product);

        Map data = new HashMap<String, Object>();
        data.put("product", productUpd);

        ResponseDto response =
                new ResponseDto(HttpStatus.CREATED.value(), data, LocalDate.now());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> delete(@PathVariable Long id){
        productService.delete(id);

        Map data = new HashMap<String, Object>();
        data.put("message", "Succesful delete");

        ResponseDto response =
                new ResponseDto(HttpStatus.OK.value(), data, LocalDate.now());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/upload-image/{name}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> uploadImg(@RequestParam("file") MultipartFile file,
                                                 @PathVariable String name){
        boolean isUploaded = productService.uploadImage(file, name);
        Map data = new HashMap<String, Object>();
        int status = HttpStatus.OK.value();
        if(isUploaded){
            data.put("message", "Succesful upload image!");
        }else{
            data.put("error", "image could not be uploaded");
            status = HttpStatus.BAD_REQUEST.value();
        }
        ResponseDto response =
                new ResponseDto(status, data, LocalDate.now());
        return ResponseEntity.status(status).body(response);
    }
}
