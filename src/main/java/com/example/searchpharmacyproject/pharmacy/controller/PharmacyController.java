package com.example.searchpharmacyproject.pharmacy.controller;

import com.example.searchpharmacyproject.pharmacy.cache.PharmacyRedisTemplateService;
import com.example.searchpharmacyproject.pharmacy.dto.PharmacyDto;
import com.example.searchpharmacyproject.pharmacy.service.PharmacyRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyRepositoryService pharmacyRepositoryService;
    private final PharmacyRedisTemplateService pharmacyRedisTemplateService;

    //데이터 초기 세팅을 위한 임시 메서드
    @GetMapping("/redis/save")
    public String save() {
        List<PharmacyDto> pharmacyDtoList = pharmacyRepositoryService.findAll()
                .stream().map(pharmacy -> PharmacyDto.builder()
                        .id(pharmacy.getId())
                        .pharmacyName(pharmacy.getPharmacyName())
                        .pharmacyAddress(pharmacy.getPharmacyAddress())
                        .latitude(pharmacy.getLatitude())
                        .longitude(pharmacy.getLongitude())
                        .build())
                .collect(Collectors.toList());

        //모든 약국 데이터를 redis 에 저장
        pharmacyDtoList.forEach(pharmacyRedisTemplateService::save);

        return "success";
    }
}
