package com.VMcom.VMcom.repository;

import com.VMcom.VMcom.model.AppUserOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserOrderLineRepository extends JpaRepository<AppUserOrderLine,Long> {
}
