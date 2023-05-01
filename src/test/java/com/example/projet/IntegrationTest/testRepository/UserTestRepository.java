package com.example.projet.IntegrationTest.testRepository;

import com.example.projet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTestRepository extends JpaRepository<User,Integer> {
}
