package com.VMcom.VMcom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.VMcom.VMcom.model.ShopCard;

@Repository
public interface ShopCardRepository extends JpaRepository<ShopCard, Long>{

    
} 