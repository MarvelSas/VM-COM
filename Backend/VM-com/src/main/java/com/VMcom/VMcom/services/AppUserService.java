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


    public AppUserDetails updateAppUserDetails(AppUserDetails appUserDetails) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
            throw new IllegalStateException("No authentication object found in security context");

        AppUser appUser = appUserRepository.findByUsername(authentication.getName()).orElseThrow(()-> new UsernameNotFoundException("User with username "+authentication.getName()+" not found"));

        if (appUserDetails != null)
            appUser.setFirstName(appUserDetails.getFirstName());
        if (appUserDetails != null)
            appUser.setLastName(appUserDetails.getLastName());

        appUserRepository.save(appUser);

        return appUserDetails;
    }
}
