package com.VMcom.VMcom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.VMcom.VMcom.model.ShopCardLine;

@Repository
public interface ShopCardLineRepository extends JpaRepository<ShopCardLine, Long> {
    
}
