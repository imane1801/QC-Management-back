package com.example.projet.IntegrationTest.testRepository;

import com.example.projet.entity.Bascket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BascketTestRepository extends JpaRepository<Bascket, Integer> {
}
