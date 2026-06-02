package com.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
