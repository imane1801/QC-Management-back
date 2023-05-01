package com.example.projet.IntegrationTest.testRepository;

import com.example.projet.entity.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeTestRepository extends JpaRepository<Commande, Integer> {
}
