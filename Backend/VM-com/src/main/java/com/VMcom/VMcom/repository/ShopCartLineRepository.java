package com.VMcom.VMcom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.VMcom.VMcom.model.ShopCartLine;

@Repository
public interface ShopCartLineRepository extends JpaRepository<ShopCartLine, Long> {

    @Query(value = "select u from ShopCartLine u where u.shopCard.id=?1 and u.product.id=?2")
    Optional<ShopCartLine> findByAppUserAndProduct(Long id, Long productId);
    
}
