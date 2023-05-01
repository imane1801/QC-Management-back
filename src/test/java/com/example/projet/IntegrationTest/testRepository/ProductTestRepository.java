package com.example.projet.IntegrationTest.testRepository;

import com.example.projet.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTestRepository extends JpaRepository<Product,Integer> {
}
