package com.example.projet.IntegrationTest.restApiTest;

import com.example.projet.IntegrationTest.testRepository.ProductTestRepository;
import com.example.projet.entity.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {
    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private ProductTestRepository productTestRepository;

    @BeforeAll
    public static void  init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public  void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/api");
    }


@Test
@Sql(statements = "INSERT INTO product (product_id,product_name,product_type,brand,model,description,image,product_price,garrnty_perriode,weight)VALUES(6, 'Laptop Dell XPS 13', 'Laptop', 'DELL', 'XPS 13', 'Un ordinateur portable ultrafin et puissant pour les professionnels', 'image1.jpg', 1499.99, 'mois', 2.7)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(statements = "DELETE FROM product", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAllProducts() {
        //get all the products
        ResponseEntity<List<Product>> response = restTemplate.exchange(
                baseUrl.concat("/product/allProduct"), HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Product>>(){});
        List<Product> products = response.getBody();
        // Assert that the response status code is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Assert that the returned list is not null
        assertNotNull(products);
        // Assert that the returned list is not empty
        assertFalse(products.isEmpty());
    assertEquals(1,products.size());
    assertEquals(1,productTestRepository.findAll().size());

    }
    @Test
    @Sql(statements = "INSERT INTO component (comp_id, comp_name, comp_type, comp_price) VALUES (1, '16Go', 'RAM', 149.99)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM component", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testAddProduct() {
        List<Configuration> configurations = new ArrayList<>();
        Component component = new Component(1, "16Go", "RAM", 149.99, null);
        Configuration configuration = new Configuration(3, 80, "Go", null, component);
        configurations.add(configuration);
        Product product = new Product(22, "Laptop Dell XPS 13", "Laptop", "DELL", "XPS 13", "Un ordinateur portable ultrafin et puissant pour les professionnels", "image1.jpg", 1499.99,"mois", 2.7, configurations, null);
        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl.concat("/product/addProduct"), product, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Product> allProducts= productTestRepository.findAll();
        assertEquals(1, allProducts.size());
        assertEquals(product.getProductPrice(), allProducts.get(0).getProductPrice());

    }
    @Test
    @Sql(statements = "INSERT INTO product (product_id,product_name,product_type,brand,model,description,image,product_price,garrnty_perriode,weight)VALUES(6, 'Laptop Dell XPS 13', 'Laptop', 'DELL', 'XPS 13', 'Un ordinateur portable ultrafin et puissant pour les professionnels', 'image1.jpg', 1499.99, 'mois', 2.7)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM product", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testgetProductById() {
        ResponseEntity <Product> response = restTemplate.exchange(
                baseUrl.concat("/product/"+6),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Product>(){});
        Product product = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(product);
        assertEquals(1, productTestRepository.findAll().size());
        assertEquals(6, product.getProductId());
        assertEquals("Laptop Dell XPS 13", product.getProductName());

    }
    @Test
    @Sql(statements = "INSERT INTO component (comp_id, comp_name, comp_type, comp_price) VALUES (1, '16Go', 'RAM', 149.99)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM component", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testAddProducts() {
        ArrayList<Product> products = new ArrayList<Product>();
        List<Configuration> configurations = new ArrayList<Configuration>();
        Component component = new Component(1, "16Go", "RAM", 149.99, null);
        Configuration configuration = new Configuration(3, 80, "Go", null, component);
        configurations.add(configuration);
        Product product1 = new Product(22, "Laptop Dell XPS 13", "Laptop", "DELL", "XPS 13", "Un ordinateur portable ultrafin et puissant pour les professionnels", "image1.jpg", 1499.99, "mois", 2.7, configurations, null);
        Product product2 = new Product(23, "Imprimante HP LaserJet Pro MFP M281fdw", "Imprimante", "HP", "LaserJet Pro MFP M281fdw", "Une imprimante multifonction rapide et efficace pour les bureaux", "image2.jpg", 349.99, "mois", 182.7, configurations, null);
        products.add(product1);
        products.add(product2);
        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl.concat("/product/addProducts"), products, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Product> allProducts= productTestRepository.findAll();
        assertEquals(2, allProducts.size());
        assertEquals(2, products.size());
        assertEquals(product1.getProductName(), allProducts.get(0).getProductName());
        assertEquals(product2.getProductName(), allProducts.get(1).getProductName());
    }
    @Test
    @Sql(statements = "INSERT INTO product(product_id,product_name,product_type,brand,model,description,image,product_price,garrnty_perriode,weight)VALUES(6, 'Laptop Dell XPS 13', 'Laptop', 'DELL', 'XPS 13', 'Un ordinateur portable ultrafin et puissant pour les professionnels', 'image1.jpg', 1499.99, 'mois', 2.7)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM product", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public  void testUpdateProduct() {
        Product product = new Product(6, "Laptop Dell XPS 13", "Laptop", "DELL", "XPS 13", "Un ordinateur portable ultrafin et puissant pour les professionnels", "image1.jpg", 1399.99,"mois", 2.7, null, null);
        restTemplate.put(baseUrl.concat("/product/updateProduct"), product);
        List<Product> allProducts = productTestRepository.findAll();
        assertEquals(1, allProducts.size());
        assertEquals(product.getProductPrice(), allProducts.get(0).getProductPrice());
    }
    @Test
    @Sql(statements = "INSERT INTO product(product_id,product_name,product_type,brand,model,description,image,product_price,garrnty_perriode,weight)VALUES(6, 'Laptop Dell XPS 13', 'Laptop', 'DELL', 'XPS 13', 'Un ordinateur portable ultrafin et puissant pour les professionnels', 'image1.jpg', 1499.99, 'mois', 2.7)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteProduct() {
        int countProducts = productTestRepository.findAll().size();
        assertEquals(1, countProducts);
        restTemplate.delete(baseUrl.concat("/product/deleteProduct/" + 6));
        countProducts = productTestRepository.findAll().size();
        assertEquals(0, countProducts);

    }
}