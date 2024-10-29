package com.VMcom.VMcom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.VMcom.VMcom.model.ShopCartLine;

@Repository
public interface ShopCartLineRepository extends JpaRepository<ShopCartLine, Long> {
    
}
