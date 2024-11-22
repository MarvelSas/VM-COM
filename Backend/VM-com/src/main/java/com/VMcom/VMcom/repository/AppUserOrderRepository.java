package com.VMcom.VMcom.repository;

import com.VMcom.VMcom.model.AppUser;
import com.VMcom.VMcom.model.AppUserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserOrderRepository extends JpaRepository<AppUserOrder,Long> {

    List<AppUserOrder> findByAppUser(AppUser appUser);
}
