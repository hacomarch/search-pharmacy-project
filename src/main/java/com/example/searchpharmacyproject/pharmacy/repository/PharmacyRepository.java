package com.example.searchpharmacyproject.pharmacy.repository;

import com.example.searchpharmacyproject.pharmacy.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
}
