package com.VMcom.VMcom.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.VMcom.VMcom.model.AppUser;
import com.VMcom.VMcom.model.ShopCart;
import com.VMcom.VMcom.model.ShopCartLine;
import com.VMcom.VMcom.model.ShopCartLineDAO;
import com.VMcom.VMcom.repository.AppUserRepository;
import com.VMcom.VMcom.repository.ShopCartLineRepository;
import com.VMcom.VMcom.repository.ShopCartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShopCartService {

    private final ShopCartRepository shopCartRepository;
    private final ShopCartLineRepository shopCartLineRepository;
    private final AppUserRepository appUserRepository;
    
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
    
    
    public ShopCart getShopCart() {

        AppUser appUser = getAppUserFromContextHolder();
        return appUser.getShopCart();
    }

    public Object addShopCartLine(ShopCartLineDAO shopCartLineDAO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addShopCartLine'");
    }



}
