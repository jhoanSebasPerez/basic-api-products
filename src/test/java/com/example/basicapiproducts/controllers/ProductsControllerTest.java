package com.example.basicapiproducts.controllers;

import com.example.basicapiproducts.dtos.AuthResponse;
import com.example.basicapiproducts.dtos.ResponseDto;
import com.example.basicapiproducts.entities.Product;
import com.example.basicapiproducts.repositories.ProductRepository;
import org.apache.coyote.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductsControllerTest {

    private TestRestTemplate testRestTemplate;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @LocalServerPort
    private int port;

    @Autowired
    private ApplicationContext applicationContext;

    private HttpHeaders authorizationHeader;

    @BeforeAll
    public void setUp(){
       restTemplateBuilder =  restTemplateBuilder.rootUri("http://localhost:" + port);
       testRestTemplate = new TestRestTemplate(restTemplateBuilder);

       ProductRepository productRepository = applicationContext.getBean(ProductRepository.class);
       Product p1 = new Product("smartv 4k", "samsung", 1240.99f);
       Product p2 = new Product("soundbar ultra", "sony", 325.77f);

       productRepository.saveAll(Arrays.asList(p1, p2));
       authorizationHeader = loginUser();
    }

    private HttpHeaders loginUser(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        String json = """
                {
                    "username": "admin",
                    "password": "admin1234"
                }
                """;

        HttpEntity request = new HttpEntity(json, headers);

        ResponseEntity response =
                testRestTemplate.exchange("/auth/login", HttpMethod.POST, request, AuthResponse.class);

        AuthResponse authResponse = (AuthResponse) response.getBody();

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", String.format("Bearer %s", authResponse.getToken()));

        return header;
    }


    @Test
    @Order(1)
    void save() {
        authorizationHeader.setContentType(MediaType.APPLICATION_JSON);
        authorizationHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));


        String json = """
                {
                    "name": "macbook air 13",
                    "brand": "apple",
                    "price": 1950.89
                }
                """;

        HttpEntity request = new HttpEntity(json, authorizationHeader);

        ResponseEntity response =
                testRestTemplate.exchange("/api/products", HttpMethod.POST, request, ResponseDto.class);

        ResponseDto data = (ResponseDto) response.getBody();
        System.out.println(response.getStatusCodeValue());
        Product product = (Product)fromHashMapToProduct((LinkedHashMap) data.getData().get("product"));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(product.getId() != null);
    }

    @Test
    @Order(2)
    void findAll() {
        HttpEntity request = new HttpEntity(authorizationHeader);

        ResponseEntity response =
                testRestTemplate.exchange("/api/products", HttpMethod.GET, request, ResponseDto.class);
        ResponseDto data = (ResponseDto) response.getBody();
       List<Product> products = (ArrayList<Product>) data.getData().get("products");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(products.size() >= 2);

    }

    @Test
    @Order(3)
    void findByName() {
        HttpEntity request = new HttpEntity(authorizationHeader);
        ResponseEntity response =
                testRestTemplate.exchange("/api/products/smartv", HttpMethod.GET, request, ResponseDto.class);
        ResponseDto data = (ResponseDto) response.getBody();
        Product product = (Product)fromHashMapToProduct((LinkedHashMap) data.getData().get("product"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("smartv 4k", product.getName());
        assertEquals("samsung", product.getBrand());
        assertEquals(1240.99f, product.getPrice());
    }

    @Test
    @Order(4)
    void update() {
        authorizationHeader.setContentType(MediaType.APPLICATION_JSON);
        authorizationHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        String json = """
                {
                    "price": 1950.89
                }
                """;

        HttpEntity request = new HttpEntity(json, authorizationHeader);

        ResponseEntity response =
                testRestTemplate.exchange("/api/products/1", HttpMethod.PUT, request, ResponseDto.class);

        ResponseDto data = (ResponseDto) response.getBody();
        Product product = (Product)fromHashMapToProduct((LinkedHashMap) data.getData().get("product"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, product.getId());
        assertEquals(1950.89f, product.getPrice());
    }


    @Test
    @Order(5)
    void delete() {
        HttpEntity request = new HttpEntity(authorizationHeader);
        ResponseEntity response =
                testRestTemplate.exchange("/api/products/1",HttpMethod.DELETE, request, ResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private Product fromHashMapToProduct(LinkedHashMap linkedHashMap){
        Long id = ((Integer)linkedHashMap.get("id")).longValue();
        String name = (String) linkedHashMap.get("name");
        String brand = (String) linkedHashMap.get("brand");
        Float price = ((Double) linkedHashMap.get("price")).floatValue();
        Product product =  new Product(name, brand, price);
        product.setId(id);
        return product;
    }
}