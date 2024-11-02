package com.VMcom.VMcom.services;

import java.lang.foreign.Linker.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public ShopCartLineDAO addShopCartLine(ShopCartLineDAO shopCartLineDAO) {

        var shopCartLine = findShopCartLineForUserAndProduct(shopCartLineDAO.getProduct().getId()).orElse(null);

        if(shopCartLine == null){
            ShopCartLine shopCartLineToAdd = new ShopCartLine();
            shopCartLineToAdd.setProduct(shopCartLineDAO.getProduct());
            shopCartLineToAdd.setShopCard(getShopCart());
            shopCartLineToAdd.setQuantity(shopCartLineDAO.getQuantity());
            shopCartLineToAdd = shopCartLineRepository.save(shopCartLineToAdd);

            updateShopCartTotalPrice(getShopCart().getTotalPrice() + shopCartLineDAO.getProduct().getPrice() * shopCartLineDAO.getQuantity());

            return ShopCartLineDAO.builder()
            .product(shopCartLineToAdd.getProduct())
            .quantity(shopCartLineToAdd.getQuantity())
            .build();

        }else{

            shopCartLine.setQuantity(shopCartLine.getQuantity() + shopCartLineDAO.getQuantity());
            shopCartLine = shopCartLineRepository.save(shopCartLine);
            
            updateShopCartTotalPrice(getShopCart().getTotalPrice() + shopCartLineDAO.getProduct().getPrice() * shopCartLineDAO.getQuantity());

            return ShopCartLineDAO.builder()
            .product(shopCartLine.getProduct())
            .quantity(shopCartLine.getQuantity())
            .build();
        }
        
        
    }

    private Optional<ShopCartLine> findShopCartLineForUserAndProduct(Long productId){
        return shopCartLineRepository.findByAppUserAndProduct(getAppUserFromContextHolder().getShopCart().getId(), productId);
    }

    private void updateShopCartTotalPrice(Double totalPrice){
        getShopCart().setTotalPrice(totalPrice);
        shopCartRepository.save(getShopCart());
    }

}
