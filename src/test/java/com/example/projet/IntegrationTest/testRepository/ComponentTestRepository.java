package com.example.projet.IntegrationTest.testRepository;

import com.example.projet.entity.Component;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponentTestRepository extends JpaRepository<Component, Integer> {
}
