package com.VMcom.VMcom.services;

import com.VMcom.VMcom.model.*;
import com.VMcom.VMcom.repository.AppUserRepository;
import com.VMcom.VMcom.repository.ProductRepository;
import com.VMcom.VMcom.repository.ShopCartLineRepository;
import com.VMcom.VMcom.repository.ShopCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopCartService {

    private final ShopCartRepository shopCartRepository;
    private final ShopCartLineRepository shopCartLineRepository;
    private final AppUserRepository appUserRepository;
    private final ProductRepository productRepository;
    
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
            ShopCartLine shopCartLineToAdd = generateNewShopCartLine(shopCartLineDAO);
            calculateCurrentTotalShopCardPrice();
            return buildShopCareLineDAO(shopCartLineToAdd);
        }else{
            shopCartLine.setQuantity(shopCartLine.getQuantity() + shopCartLineDAO.getQuantity());
            shopCartLine = shopCartLineRepository.save(shopCartLine);
            calculateCurrentTotalShopCardPrice();
            return  buildShopCareLineDAO(shopCartLine);
        }
        
        
    }

    private Optional<ShopCartLine> findShopCartLineForUserAndProduct(Long productId){
        return shopCartLineRepository.findByAppUserAndProduct(getAppUserFromContextHolder().getShopCart().getId(), productId);
    }


    private ShopCartLineDAO buildShopCareLineDAO(ShopCartLine shopCartLine){
        return ShopCartLineDAO.builder()
                .product(shopCartLine.getProduct())
                .quantity(shopCartLine.getQuantity())
                .build();
    }

    private ShopCartLine generateNewShopCartLine(ShopCartLineDAO shopCartLineDAO){

        Product product = productRepository.findById(shopCartLineDAO.getProduct().getId()).orElseThrow(()-> new RuntimeException("Product with id :"+ shopCartLineDAO.getProduct().getId()+" doesn't exist in database"));

        ShopCartLine shopCartLine = new ShopCartLine();
        shopCartLine.setProduct(product);
        shopCartLine.setShopCard(getShopCart());
        shopCartLine.setQuantity(shopCartLineDAO.getQuantity());
        return shopCartLineRepository.save(shopCartLine);

    }

    public Boolean deleteShopCartLine(Long shopCartLineId) {
        ShopCartLine shopCartLine = findShopCartLineById(shopCartLineId);
        verifyIfShopCartLineBelongToLogOnUser(shopCartLine);
        shopCartLineRepository.deleteById(shopCartLineId);
        calculateCurrentTotalShopCardPrice();
        return true;

    }

    private ShopCartLine findShopCartLineById(Long shopCartLineId){
       return shopCartLineRepository.findById(shopCartLineId).orElseThrow(()->
                new RuntimeException("Shop cart line with id: "+shopCartLineId +" doesn't exist in database"));
    }

    private void verifyIfShopCartLineBelongToLogOnUser(ShopCartLine shopCartLine){
        if(!shopCartLine.getShopCard().getId().equals(getShopCart().getId())){
            throw new RuntimeException("This shop cart line doesn't belong to log on user");
        }

    }


    public ShopCartLine updateShopCartLineQuantity(Long shopCartLineId, int shopCartLineQuantity) {
        ShopCartLine shopCartLine = findShopCartLineById(shopCartLineId);
        if(shopCartLineQuantity<1)
            throw new InvalidParameterException("Shop card line quantity can't be lower than 1");
        shopCartLine.setQuantity(shopCartLineQuantity);
        shopCartLine = shopCartLineRepository.save(shopCartLine);
        calculateCurrentTotalShopCardPrice();
        return shopCartLine;

    }




    private void calculateCurrentTotalShopCardPrice(){
        double total = getShopCart().getShopCardLines()
                .stream()
                .mapToDouble(shopCartLine -> shopCartLine.getProduct().getPrice()*shopCartLine.getQuantity())
                .sum();
        ShopCart shopCart = getShopCart();
        shopCart.setTotalPrice(total);
        shopCartRepository.save(shopCart);

    }

    public boolean clearShopCart(){

        ShopCart shopCart = getAppUserFromContextHolder().getShopCart();
        shopCart.getShopCardLines().forEach(shopCartLineRepository::delete);
        shopCartRepository.delete(shopCart);
        createShopCart();
        return true;

    }

    public void createShopCart(){
        ShopCart shopCart = new ShopCart();
        List<ShopCartLine> shopCartLines = new ArrayList<>();
        shopCart.setShopCardLines(shopCartLines);
        shopCart.setAppUser(getAppUserFromContextHolder());
        shopCartRepository.save(shopCart);
    }

}
