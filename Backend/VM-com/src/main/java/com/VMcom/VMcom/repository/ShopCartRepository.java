package com.VMcom.VMcom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.VMcom.VMcom.model.ShopCart;

@Repository
public interface ShopCartRepository extends JpaRepository<ShopCart, Long>{

    
} 