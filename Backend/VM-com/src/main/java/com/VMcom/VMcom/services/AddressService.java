package com.VMcom.VMcom.services;

import com.VMcom.VMcom.model.Address;
import com.VMcom.VMcom.model.AppUser;
import com.VMcom.VMcom.repository.AddressRepository;
import com.VMcom.VMcom.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AppUserRepository appUserRepository;
    private final AddressRepository addressRepository;

    public AppUser getAppUserFromContextHolder(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            throw new IllegalStateException("No authentication object found in security context");
        return findUserByUsername(authentication.getName());
    }


    private AppUser findUserByUsername(String username) {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
    }

    public Address addAddress(Address address) {
        address.setAppUser(getAppUserFromContextHolder());
        return addressRepository.save(address);
    }

    public Address updateAddress(Address address, Long addressId) {
        Address addressFromRepository = findAddressById(addressId);
        verifyAddressOwnership(addressFromRepository);
        updateAddressDetails(addressFromRepository, address);
        return addressRepository.save(addressFromRepository);
    }

    private void verifyAddressOwnership(Address address) {
        if (!address.getAppUser().equals(getAppUserFromContextHolder())) {
            throw new RuntimeException("This address doesn't belong to the logged-on user");
        }
    }

    private void updateAddressDetails(Address target, Address source) {
        target.setStreet(source.getStreet());
        target.setZipCode(source.getZipCode());
        target.setCity(source.getCity());
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setPhoneNumber(source.getPhoneNumber());
    }

    private Address findAddressById(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new UsernameNotFoundException("Address with Id " + addressId + " doesn't exist in database"));
    }

    public boolean deleteAddress(Long addressId) {
        Address addressFromRepository = findAddressById(addressId);
        verifyAddressOwnership(addressFromRepository);
        addressRepository.deleteById(addressId);
        return true;
    }

    public Address getAddress(Long addressId) {
        Address addressFromRepository = findAddressById(addressId);
        verifyAddressOwnership(addressFromRepository);
        return addressFromRepository;
    }

    public List<Address> getAddresses() {
        return addressRepository.findAllByAppUser(getAppUserFromContextHolder());
    }
    
}
