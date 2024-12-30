package com.VMcom.VMcom.services;

import com.VMcom.VMcom.model.AppUser;
import com.VMcom.VMcom.model.AppUserDetails;
import com.VMcom.VMcom.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username).
                orElseThrow(()-> new UsernameNotFoundException("User with username "+username+" not found"));
    }

    private AppUser getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            throw new IllegalStateException("No authentication object found in security context");
        return appUserRepository.findByUsername(authentication.getName()).orElseThrow(()-> new UsernameNotFoundException("User with username "+authentication.getName()+" not found"));
    }


    public AppUserDetails updateAppUserDetails(AppUserDetails appUserDetails) {

        AppUser appUser = getLoggedInUser();

        if (appUserDetails != null){
            appUser.setFirstName(appUserDetails.getFirstName());
            appUser.setLastName(appUserDetails.getLastName());
            appUser.setPhoneNumber(appUserDetails.getPhoneNumber());
        }
        
        appUserRepository.save(appUser);
        return AppUserDetails.builder().lastName(
                appUser.getLastName()).
                firstName(appUser.getFirstName()).
                phoneNumber(appUser.getPhoneNumber())
                .build();
    }

    public AppUserDetails getAppUserDetails() {

        AppUser appUser = getLoggedInUser();
        return AppUserDetails.builder().lastName(
                appUser.getLastName()).
                firstName(appUser.getFirstName()).
                phoneNumber(appUser.getPhoneNumber())
                .build();

    }
}
