package com.example.projet.IntegrationTest.restApiTest;

import com.example.projet.IntegrationTest.testRepository.BascketTestRepository;
import com.example.projet.entity.Bascket;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BasketControllerTest {
    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private BascketTestRepository bascketTestRepository;

    @BeforeAll
    public static void  init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public  void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/api");
    }




    @Test
    public void testCreateBasket() {
        Bascket basket = new Bascket(1, 599.99, null);

        //send a Post request to create the basket
        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl.concat("/bascket"), basket, Void.class);

        //assert that the response status code is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //assert that the basket was created in the database
        List<Bascket> allBaskets = bascketTestRepository.findAll();
        assertEquals(1, allBaskets.size());
        assertEquals(basket.getTotaleAmount(), allBaskets.get(0).getTotaleAmount());

    }

    @Test
    @Sql(statements = "INSERT INTO bascket (bascket_id, totale_amount) VALUES (1, 219.99)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "INSERT INTO bascket (bascket_id, totale_amount) VALUES (2, 519.99)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM bascket", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAllBaskets() {
        //get all the baskets
        ResponseEntity<List<Bascket>> response = restTemplate.exchange(
                baseUrl.concat("/basckets/allBaskets"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Bascket>>(){});

        List<Bascket> baskets = response.getBody();

        // Assert that the response status code is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert that the returned list is not null
        assertNotNull(baskets);

        // Assert that the returned list is not empty
        assertFalse(baskets.isEmpty());

    }

    @Test
    @Sql(statements = "INSERT INTO bascket (bascket_id, totale_amount) VALUES (1, 819.99)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM bascket", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public  void testUpdateBasket() {
        //update the basket (change the total amount)
        Bascket basket = new Bascket(1, 356.99, null);

        //send a PUT request to update the basket
        restTemplate.put(baseUrl.concat("/basckets/updateBasckets"), basket);

        //assert that the basket was updated in the DB
        List<Bascket> allBaskets = bascketTestRepository.findAll();
        assertEquals(1, allBaskets.size());
        assertEquals(basket.getTotaleAmount(), allBaskets.get(0).getTotaleAmount());

    }

    @Test
    @Sql(statements = "INSERT INTO bascket (bascket_id, totale_amount) VALUES (4, 620.99)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteBasket() {
        //assert that there is only one basket in the DB
        int countCommands = bascketTestRepository.findAll().size();
        assertEquals(1, countCommands);
        //send a DELETE request to delete the basket
        restTemplate.delete(baseUrl.concat("/basckets/delete/" + 4));

        //assert that the basket was deleted from DB
        countCommands = bascketTestRepository.findAll().size();
        assertEquals(0, countCommands);

    }
}
