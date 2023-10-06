package com.example.searchpharmacyproject.pharmacy.service;

import com.example.searchpharmacyproject.pharmacy.entity.Pharmacy;
import com.example.searchpharmacyproject.pharmacy.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyService {

    private final PharmacyRepository pharmacyRepository;

    @Transactional
    public void updateAddress(Long id, String address) {
        Pharmacy entity = pharmacyRepository.findById(id).orElse(null);

        if(Objects.isNull(entity)) {
            log.error("[PharmacyService.updateAddress] not found id: {}" , id);
            return;
        }

        entity.changePharmacyAddress(address);

    }
}