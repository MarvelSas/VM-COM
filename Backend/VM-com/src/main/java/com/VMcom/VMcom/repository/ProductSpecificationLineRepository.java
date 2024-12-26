package com.VMcom.VMcom.repository;

import com.VMcom.VMcom.model.ProductSpecificationLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSpecificationLineRepository extends JpaRepository<ProductSpecificationLine,Long> {
}
