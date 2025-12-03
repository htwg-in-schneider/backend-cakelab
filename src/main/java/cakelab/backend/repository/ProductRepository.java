package cakelab.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cakelab.backend.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}