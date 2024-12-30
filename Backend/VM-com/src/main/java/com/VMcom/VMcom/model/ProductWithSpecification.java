package com.VMcom.VMcom.model;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record ProductWithSpecification(
        Long id,
        String name,
        String description,
        Double price,
        List<String> photos,
        int mainPhotoId,
        Long amount,
        ProductCategory productCategory,
        Map<String,String> productSpecificationLines,
        String additionalInformation
) {
}
