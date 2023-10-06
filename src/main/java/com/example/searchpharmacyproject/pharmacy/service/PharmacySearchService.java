package com.example.searchpharmacyproject.pharmacy.service;

import com.example.searchpharmacyproject.pharmacy.dto.PharmacyDto;
import com.example.searchpharmacyproject.pharmacy.entity.Pharmacy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacySearchService {

    private final PharmacyRepositoryService pharmacyRepositoryService;

    //전체 약국에 대해 entity를 가져와 dto로 변환해서 dto list로 반환
    public List<PharmacyDto> searchPharmacyDtoList() {

        return pharmacyRepositoryService.findAll()
                .stream()
                .map(this::convertToPharmacyDto)
                .collect(Collectors.toList());
    }

    //entity -> dto
    private PharmacyDto convertToPharmacyDto(Pharmacy pharmacy) {
        return PharmacyDto.builder()
                .id(pharmacy.getId())
                .pharmacyAddress(pharmacy.getPharmacyAddress())
                .pharmacyName(pharmacy.getPharmacyName())
                .latitude(pharmacy.getLatitude())
                .longitude(pharmacy.getLongitude())
                .build();
    }

}