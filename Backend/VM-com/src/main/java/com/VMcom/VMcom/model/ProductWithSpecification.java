package com.VMcom.VMcom.model;

import java.util.List;
import java.util.Map;

public record ProductWithSpecification(
        String name,
        String description,
        Double price,
        List<String> photos,
        int mainPhotoId,
        Long amount,
        ProductCategory productCategory,
        Map<String,String> specificationLines,
        String additionalInformation
) {
}
