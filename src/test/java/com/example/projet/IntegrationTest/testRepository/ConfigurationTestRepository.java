package com.example.projet.IntegrationTest.testRepository;

import com.example.projet.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationTestRepository extends JpaRepository<Configuration, Integer> {
}
