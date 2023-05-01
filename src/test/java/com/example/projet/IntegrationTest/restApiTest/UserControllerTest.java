package com.example.projet.IntegrationTest.restApiTest;

import com.example.projet.IntegrationTest.testRepository.UserTestRepository;
import com.example.projet.entity.Login;
import com.example.projet.entity.Product;
import com.example.projet.entity.User;
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
public class UserControllerTest {
    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private UserTestRepository userTestRepository;

    @BeforeAll
    public static void  init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public  void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/api/user");
    }
    @Test
    @Sql(statements ="INSERT INTO user(user_id,first_name,last_name,email,password,full_number,adress,city,state,country,zip_code, is_admin) VALUES (9, 'imane', 'talbi', 'ima201@gmail.com', 'imane', '066666666', 'Berkane , Maroc', 'Berkane', 'Oujda', 'Maroc', 63300, 1)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM user ", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testlogin(){
        Login loginRequest = new Login("ima201@gmail.com", "imane");
        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl.concat("/login"), loginRequest, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<User> user = userTestRepository.findAll();
        assertEquals(1, user.size());
        assertEquals(loginRequest.login(), user.get(0).getEmail());
        assertEquals(loginRequest.password(), user.get(0).getPassword());
    }
    @Test
    @Sql(statements ="INSERT INTO user(user_id,first_name,last_name,email,password,full_number,adress,city,state,country,zip_code, is_admin) VALUES (9, 'imane', 'talbi', 'imane2001@gmail.com', 'imane', '066666666', 'Berkane , Maroc', 'Berkane', 'Oujda', 'Maroc', 63300, 1)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM user", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testgetUserByName() {
        ResponseEntity<List<User>> response = restTemplate.exchange(
                baseUrl.concat("/imane"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>(){});
        List<User> user = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(user);
        assertFalse(user.isEmpty());
        assertEquals(1, user.size());
        assertEquals(1, userTestRepository.findAll().size());
        assertEquals("imane",user.get(0).getFirstName());

    }
    @Test
    @Sql(statements ="INSERT INTO user(user_id,first_name,last_name,email,password,full_number,adress,city,state,country,zip_code, is_admin) VALUES (9, 'imane', 'talbi', 'imane2001@gmail.com', 'imane', '066666666', 'Berkane , Maroc', 'Berkane', 'Oujda', 'Maroc', 63300, 1)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM user", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAllUsers() {
        ResponseEntity<List<User>> response = restTemplate.exchange(
                baseUrl.concat("/allUser"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>(){});
        List<User> users = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals(1, userTestRepository.findAll().size());
    }
    @Test
    public void testAddUser() {
        User user = new User(16, "emane", "talbi", "ima2001@gmail.com", "Emane", "066666666", "Berkane , Maroc", "Berkane", "Oujda", "Maroc", 63300,true,null);
          ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl.concat("/addUser"), user, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<User> allUsers= userTestRepository.findAll();
        assertEquals(1, allUsers.size());
        assertEquals(user.getEmail(), allUsers.get(0).getEmail());
    }

    @Test
    @Sql(statements ="INSERT INTO user(user_id,first_name,last_name,email,password,full_number,adress,city,state,country,zip_code, is_admin) VALUES (9, 'imane', 'talbi', 'imane2001@gmail.com', 'imane', '066666666', 'Berkane , Maroc', 'Berkane', 'Oujda', 'Maroc', 63300, 1)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM user", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public  void testUpdateUser() {

        User user = new User(9, "emane", "talbi", "ima2001@gmail.com", "Emane", "066666666", "Berkane , Maroc", "Berkane", "Oujda", "Maroc", 63300,true,null);
        restTemplate.put(baseUrl.concat("/modifier"), user);
        List<User> allUsers = userTestRepository.findAll();
        assertEquals(1, allUsers.size());
        assertEquals(user.getEmail(), allUsers.get(0).getEmail());
        assertEquals(user.getFirstName(), allUsers.get(0).getFirstName());
    }
    @Test
    @Sql(statements ="INSERT INTO user(user_id,first_name,last_name,email,password,full_number,adress,city,state,country,zip_code, is_admin) VALUES (9, 'imane', 'talbi', 'imane2001@gmail.com', 'imane', '066666666', 'Berkane , Maroc', 'Berkane', 'Oujda', 'Maroc', 63300, 1)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteUser() {
        int countusers= userTestRepository.findAll().size();
        assertEquals(1, countusers);
        restTemplate.delete(baseUrl.concat("/deleteUser/" + 9));
        countusers = userTestRepository.findAll().size();
        assertEquals(0, countusers);

    }
}