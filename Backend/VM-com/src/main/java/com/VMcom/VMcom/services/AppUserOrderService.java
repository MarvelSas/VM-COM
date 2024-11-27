package com.VMcom.VMcom.services;

import com.VMcom.VMcom.enums.OrderStatus;
import com.VMcom.VMcom.model.*;
import com.VMcom.VMcom.repository.AddressRepository;
import com.VMcom.VMcom.repository.AppUserOrderLineRepository;
import com.VMcom.VMcom.repository.AppUserOrderRepository;
import com.VMcom.VMcom.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AppUserOrderService {

    private final AppUserOrderRepository appUserOrderRepository;
    private final AppUserOrderLineRepository appUserOrderLineRepository;
    private final AppUserRepository appUserRepository;
    private final ShopCartService shopCartService;
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


    public AppUserOrder addAppUserOrder(AppUserOrderDetails appUserOrderDetails) {

        verifyIfShopCartContainsAnyLine();
        AppUserOrder appUserOrder = buildAppUserOrderWithoutLines(appUserOrderDetails);
        addAppUserOrderLineToAppUserOrder(appUserOrder);
        shopCartService.clearShopCart();
        return appUserOrder;

    }

    private void verifyIfShopCartContainsAnyLine(){
        if(getAppUserFromContextHolder().getShopCart().getShopCardLines().size()<=0){
            throw new RuntimeException("Your shop cart is empty");
        }

    }

    private AppUserOrder buildAppUserOrderWithoutLines(AppUserOrderDetails appUserOrderDetails){

        AppUser appUser = getAppUserFromContextHolder();
        Address address = addressRepository.findById(appUserOrderDetails.getAddress().getId()).orElseThrow(() -> new RuntimeException("Address with id :" +appUserOrderDetails.getAddress().getId() + " doesn't exist in database" ));

        return appUserOrderRepository.save(AppUserOrder.builder()
                .paymentMethod(appUserOrderDetails.getPaymentMethod())
                .address(address)
                .appUser(appUser)
                .totalPrice(appUser.getShopCart().getTotalPrice())
                .orderStatus(OrderStatus.NEW)
                .createDate(LocalDate.now())
                .build());
    }

    private void addAppUserOrderLineToAppUserOrder(AppUserOrder appUserOrder){

        Stream<AppUserOrderLine> appUserOrderLines;
        List<ShopCartLine> shopCartLines = getAppUserFromContextHolder().getShopCart().getShopCardLines();

        appUserOrderLines = shopCartLines.stream().map(shopCartLine -> {

            System.out.println(shopCartLine.getId());
           return appUserOrderLineRepository.save(AppUserOrderLine.builder()
                    .appUserOrder(appUserOrder)
                    .price(shopCartLine.getProduct().getPrice())
                    .product(shopCartLine.getProduct())
                    .quantity(shopCartLine.getQuantity())
                    .build());
        });

        appUserOrder.setAppUserOrderLines(appUserOrderLines.toList());



    }

    public List<AppUserOrder> getAllUserOrders() {
        return appUserOrderRepository.findByAppUser(getAppUserFromContextHolder());
    }


    public AppUserOrder getAppUserOrder(Long appUserOrderId) {

        AppUserOrder appUserOrder = appUserOrderRepository.findById(appUserOrderId).orElseThrow(
                () -> new RuntimeException("Order with id: "+ appUserOrderId + " doesn't exist"));
        verifyAppUserOrderOwnership(appUserOrder);
        return appUserOrder;
    }



    private void verifyAppUserOrderOwnership(AppUserOrder appUserOrder) {
        if (!appUserOrder.getAppUser().equals(getAppUserFromContextHolder())) {
            throw new RuntimeException("This Order doesn't belong to the logged-on user");
        }
    }

    public AppUserOrder updateAppUserOrderStatus(Long appUserOrderId, OrderStatus orderStatus) {

        AppUserOrder appUserOrder = appUserOrderRepository.findById(appUserOrderId).orElseThrow(
                () -> new RuntimeException("Order with id: "+ appUserOrderId + " doesn't exist"));
        appUserOrder.setOrderStatus(orderStatus);
        appUserOrderRepository.save(appUserOrder);
        return appUserOrder;
    }


}
