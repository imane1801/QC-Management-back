package com.example.projet.IntegrationTest.restApiTest;

import com.example.projet.IntegrationTest.testRepository.BascketTestRepository;
import com.example.projet.IntegrationTest.testRepository.CommandeTestRepository;
import com.example.projet.entity.Bascket;
import com.example.projet.entity.Commande;
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

public class CommandeControllerTest {
    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private CommandeTestRepository commandeTestRepository;

    @BeforeAll
    public static void  init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public  void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/api");

    }

    @Test
    @Sql(statements = "INSERT INTO commande (comd_id, payement_method, totale_amount, delevrie_date, status) VALUES (1, 'credit card', 100.0, '2023-04-30', 'processing')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "INSERT INTO commande (comd_id, payement_method, totale_amount, delevrie_date, status) VALUES (2, 'credit card', 1600.0, '2023-04-30', 'processing')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM commande", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAllCommands() {
        //get all the commands
        ResponseEntity<List<Commande>> response = restTemplate.exchange(
                baseUrl.concat("/commandes/all"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Commande>>(){});
        List<Commande> commandes = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(commandes);
        assertFalse(commandes.isEmpty());
    }

    @Test
    @Sql(statements = "INSERT INTO commande (comd_id, payement_method, totale_amount, delevrie_date, status) VALUES (1, 'credit card', 100.0, '2023-04-30', 'processing')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM commande", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public  void testUpdateCommand() {
        //update the command inserted (change the deliveryDate)
        Date deliveryDate = Date.valueOf("2023-05-20");
        Commande commande = new Commande(1, "credit card", 100.0, deliveryDate, "processing", null);

        //send a PUT request to update the command
        restTemplate.put(baseUrl.concat("/commandes/update"), commande);

        //assert that the command was updated in the DB
        List<Commande> allCommands = commandeTestRepository.findAll();
        assertEquals(1, allCommands.size());
        assertEquals(commande.getTotalAmount(), allCommands.get(0).getTotalAmount());
    }

    @Test
    public void testAddCommand() {
        Commande commande = new Commande(1, "credit card", 100.0, new Date(System.currentTimeMillis()), "processing", null);

        //send a Post request to create the command
        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl.concat("/commandes/add"), commande, Void.class);

        //assert that the response status code is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //assert that the basket was created in the database
        List<Commande> allCommands = commandeTestRepository.findAll();
        assertEquals(1, allCommands.size());
        assertEquals(commande.getTotalAmount(), allCommands.get(0).getTotalAmount());
    }

    @Test
    @Sql(statements = "INSERT INTO commande (comd_id, payement_method, totale_amount, delevrie_date, status) VALUES (1, 'credit card', 100.0, '2023-04-30', 'processing')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteCommand() {
        //assert that there is only one command in the DB
        int countCommands = commandeTestRepository.findAll().size();
        assertEquals(1, countCommands);

        //send a DELETE request to delete the command
        restTemplate.delete(baseUrl.concat("/commandes/delete/" + 1));

        //assert that the basket was deleted from DB
        countCommands = commandeTestRepository.findAll().size();
        assertEquals(0, countCommands);

    }
}
