package com.VMcom.VMcom.repository;

import com.VMcom.VMcom.model.Address;
import com.VMcom.VMcom.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllByAppUser(AppUser appUser);
}
