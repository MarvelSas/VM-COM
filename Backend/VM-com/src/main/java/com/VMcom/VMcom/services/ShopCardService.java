package com.VMcom.VMcom.services;

import org.springframework.stereotype.Service;

import com.VMcom.VMcom.repository.ShopCardLineRepository;
import com.VMcom.VMcom.repository.ShopCardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShopCardService {

    private final ShopCardRepository shopCardRepository;
    private final ShopCardLineRepository shopCardLineRepository;

    

}
