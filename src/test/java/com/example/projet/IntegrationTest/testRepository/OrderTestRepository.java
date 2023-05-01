package com.example.projet.IntegrationTest.testRepository;

import com.example.projet.entity.Ordere;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTestRepository extends JpaRepository<Ordere,Integer> {
}
