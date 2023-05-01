package com.example.projet.IntegrationTest.restApiTest;

import com.example.projet.IntegrationTest.testRepository.OrderTestRepository;
import com.example.projet.entity.Commande;
import com.example.projet.entity.Ordere;
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

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {
    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private OrderTestRepository orderTestRepository;

    @BeforeAll
    public static void  init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public  void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/api/order");
    }
    @Test

    @Sql(statements = "INSERT INTO ordere(order_id,order_quantity,order_price, discount,order_date)VALUES(33, 1, 4, 4, '2023-04-19')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM ordere", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAllOrders() {
        ResponseEntity<List<Ordere>> response = restTemplate.exchange(
                baseUrl.concat("/allOrder"), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Ordere>>(){});
        List<Ordere> orders = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertEquals(1,orders.size());
        assertEquals(1,orderTestRepository.findAll().size());
    }
    @Test
    public void testAddOrder() {
        String dateString = "2023-04-01";
        Date date = Date.valueOf(dateString);
        Ordere order = new Ordere(23, 3, 0, 0, date,null, null, null,null);
        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl.concat("/addOrdere"), order, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Ordere> allOrders = orderTestRepository.findAll();
        assertEquals(1, allOrders.size());
        assertEquals(order.getOrderPrice(), allOrders.get(0).getOrderPrice());
    }
    @Test
    @Sql(statements = "INSERT INTO ordere(order_id,order_quantity,order_price, discount,order_date)VALUES(33, 1, 4, 4, '2023-04-19')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM ordere", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public  void testUpdateOrder() {
        String dateString = "2023-04-01";
        Date date = Date.valueOf(dateString);
        Ordere order = new Ordere(33, 53, 0, 0, date,null, null, null,null);
        restTemplate.put(baseUrl.concat("/updateOrder"), order);
        List<Ordere> allOrders = orderTestRepository.findAll();
        assertEquals(1, allOrders.size());
        assertEquals(order.getOrderQuantity(), allOrders.get(0).getOrderQuantity());

    }
    @Test
    @Sql(statements = "INSERT INTO ordere(order_id,order_quantity,order_price, discount,order_date)VALUES(33, 1, 4, 4, '2023-04-19')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteOrder(){
        int countorders = orderTestRepository.findAll().size();
        assertEquals(1, countorders);
        restTemplate.delete(baseUrl.concat("/deleteOrder/" + 33));
        countorders = orderTestRepository.findAll().size();
        assertEquals(0, countorders);

    }
}
