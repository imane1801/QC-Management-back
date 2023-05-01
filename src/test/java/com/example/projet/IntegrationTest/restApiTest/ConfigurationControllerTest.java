package com.example.projet.IntegrationTest.restApiTest;

import com.example.projet.IntegrationTest.testRepository.ConfigurationTestRepository;
import com.example.projet.entity.Configuration;
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
public class ConfigurationControllerTest {
    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private ConfigurationTestRepository configurationTestRepository;

    @BeforeAll
    public static void  init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public  void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/api");

    }

    @Test
    @Sql(statements = "INSERT INTO configuration (conf_id, quantity, unite) VALUES (1, 20, 'Go')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "INSERT INTO configuration (conf_id, quantity, unite) VALUES (2, 75, 'pièce')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM configuration", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAllConfigurations() {
        //get all the configurations
        ResponseEntity<List<Configuration>> response = restTemplate.exchange(
                baseUrl.concat("/configurations/all"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Configuration>>(){});

        List<Configuration> configurations = response.getBody();

        // Assert that the response status code is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert that the returned list is not null
        assertNotNull(configurations);

        // Assert that the returned list is not empty
        assertFalse(configurations.isEmpty());
    }

    @Test
    @Sql(statements = "INSERT INTO configuration (conf_id, quantity, unite) VALUES (1, 20, 'Go')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM configuration", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public  void testUpdateConfiguration() {
        //update the configuration (change quantity)
        Configuration configuration = new Configuration(1,150,"Go",null,null);

        //send a PUT request to update the configuration
        restTemplate.put(baseUrl.concat("/configurations/update"), configuration);

        //assert that the configuration was updated in the DB
        List<Configuration> allConfigurations = configurationTestRepository.findAll();
        assertEquals(1, allConfigurations.size());
        assertEquals(configuration.getConfId(), allConfigurations.get(0).getConfId());

    }

    @Test
    @Sql(statements = "DELETE FROM configuration", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testCreateConfiguration() {
        Configuration configuration = new Configuration(3,80,"Go",null,null);

        //send a Post request to create the configuration
        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl.concat("/configurations/add"), configuration, Void.class);

        //assert that the response status code is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //assert that the configuration was created in the database
        List<Configuration> allConfigurations = configurationTestRepository.findAll();
        assertEquals(1, allConfigurations.size());
        assertEquals(configuration.getQuantity(), allConfigurations.get(0).getQuantity());
    }

    @Test
    @Sql(statements = "INSERT INTO configuration (conf_id, quantity, unite) VALUES (1, 80, 'pièce')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteConfiguration() {
        //assert that there is only one configuration in the DB
        int countConfigurations = configurationTestRepository.findAll().size();
        assertEquals(1, countConfigurations);

        //send a DELETE request to delete the configuration
        restTemplate.delete(baseUrl.concat("/configurations/delete/" + 1));

        //assert that the configuration was deleted from DB
        countConfigurations = configurationTestRepository.findAll().size();
        assertEquals(0, countConfigurations);
    }
}
