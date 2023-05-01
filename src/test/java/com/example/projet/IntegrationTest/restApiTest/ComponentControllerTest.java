package com.example.projet.IntegrationTest.restApiTest;

import com.example.projet.IntegrationTest.testRepository.ComponentTestRepository;
import com.example.projet.entity.Component;
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
public class ComponentControllerTest {
    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private ComponentTestRepository componentTestRepository;

    @BeforeAll
    public static void  init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public  void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/api");

    }

    @Test
    @Sql(statements = "INSERT INTO component (comp_id, comp_name, comp_type, comp_price) VALUES (1, 'Mémoire RAM DDR4 Corsair Vengeance LPX', 'RAM', 149.99)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "INSERT INTO component (comp_id, comp_name, comp_type, comp_price) VALUES (2, 'Processeur Intel Core i7-1165G7', 'CPU', 399.99)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM component", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAllComponents() {
        //get all the component
        ResponseEntity<List<Component>> response = restTemplate.exchange(
                baseUrl.concat("/component/all"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Component>>(){});

        List<Component> components = response.getBody();

        // Assert that the response status code is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert that the returned list is not null
        assertNotNull(components);

        // Assert that the returned list is not empty
        assertFalse(components.isEmpty());
    }

    @Test
    @Sql(statements = "INSERT INTO component (comp_id, comp_name, comp_type, comp_price) VALUES (1, 'Mémoire RAM DDR4 Corsair Vengeance LPX', 'RAM', 149.99)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM component", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public  void testUpdateComponent() {
        //update the component (change compName)
        Component component = new Component(1, "16Go", "RAM", 149.99, null);

        //send a PUT request to update the component
        restTemplate.put(baseUrl.concat("/component/update"), component);

        //assert that the component was updated in the DB
        List<Component> allComponents = componentTestRepository.findAll();
        assertEquals(1, allComponents.size());
        assertEquals(component.getCompName(), allComponents.get(0).getCompName());

    }

    @Test
    @Sql(statements = "DELETE FROM component", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testAddComponent() {
        Component component = new Component(1,"Disque SSD NVMe Samsung 970 Evo Plus","Stockage",139.99,null);

        //send a Post request to create the component
        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl.concat("/component/add"), component, Void.class);

        //assert that the response status code is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //assert that the component was created in the database
        List<Component> allComponents = componentTestRepository.findAll();
        assertEquals(1, allComponents.size());
        assertEquals(component.getCompName(), allComponents.get(0).getCompName());
    }

    @Test
    @Sql(statements = "INSERT INTO component (comp_id, comp_name, comp_type, comp_price) VALUES (1, 'Mémoire RAM DDR4 Corsair Vengeance LPX', 'RAM', 149.99)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteComponent() {
        //assert that there is only one component in the DB
        int countComponents = componentTestRepository.findAll().size();
        assertEquals(1, countComponents);

        //send a DELETE request to delete the component
        restTemplate.delete(baseUrl.concat("/component/delete/" + 1));

        //assert that the component was deleted from DB
        countComponents = componentTestRepository.findAll().size();
        assertEquals(0, countComponents);
    }
}
