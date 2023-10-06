package com.example.searchpharmacyproject.direction.service;

import com.example.searchpharmacyproject.api.dto.DocumentDto;
import com.example.searchpharmacyproject.direction.entity.Direction;
import com.example.searchpharmacyproject.direction.repository.DirectionRepository;
import com.example.searchpharmacyproject.pharmacy.dto.PharmacyDto;
import com.example.searchpharmacyproject.pharmacy.service.PharmacySearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectionService {

    private static final int MAX_SEARCH_COUNT = 3; //약국 최대 검색 갯수
    private static final double RADIUS_KM = 10.0; // 반경 10 km

    private final PharmacySearchService pharmacySearchService;
    private final DirectionRepository directionRepository;

    @Transactional
    public List<Direction> saveAll(List<Direction> directionList) {
        if(CollectionUtils.isEmpty(directionList)) return Collections.emptyList();

        return directionRepository.saveAll(directionList);
    }

    //사용자가 입력한 주소를 기반으로 10km 이내의 약국 3개를 뽑아 리스트로 반환
    public List<Direction> buildDirectionList(DocumentDto documentDto) {

        if(Objects.isNull(documentDto)) return Collections.emptyList();

        return pharmacySearchService.searchPharmacyDtoList()
                .stream().map(pharmacyDto ->
                        Direction.builder()
                                .inputAddress(documentDto.getAddressName())
                                .inputLatitude(documentDto.getLatitude())
                                .inputLongitude(documentDto.getLongitude())
                                .targetPharmacyName(pharmacyDto.getPharmacyName())
                                .targetAddress(pharmacyDto.getPharmacyAddress())
                                .targetLatitude(pharmacyDto.getLatitude())
                                .targetLongitude(pharmacyDto.getLongitude())
                                .distance( //Haversine formula 알고리즘을 사용해 거리 계산
                                        calculateDistance(documentDto.getLatitude(), documentDto.getLongitude(),
                                                            pharmacyDto.getLatitude(), pharmacyDto.getLongitude())
                                )
                                .build()
                        )
                .filter(direction -> direction.getDistance() <= RADIUS_KM) //10km 반경 이내
                .sorted(Comparator.comparing(Direction::getDistance)) //가까운 거리순으로 정렬
                .limit(MAX_SEARCH_COUNT) //3개로 제한
                .collect(Collectors.toList());

    }

    // Haversine formula 알고리즘
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        //각도를 라디안으로 변환
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371; //Kilometers
        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
    }
}
