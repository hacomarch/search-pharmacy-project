package com.example.searchpharmacyproject.pharmacy.service;

import com.example.searchpharmacyproject.api.dto.DocumentDto;
import com.example.searchpharmacyproject.api.dto.KakaoApiResponseDto;
import com.example.searchpharmacyproject.api.service.KakaoAddressSearchService;
import com.example.searchpharmacyproject.direction.entity.Direction;
import com.example.searchpharmacyproject.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyRecommendationService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;

    public void recommendPharmacyList(String address) {
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        //validation check
        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
            log.error("[PharmacyRecommendationService.recommendPharmacyList fail] Input address: {}", address);
            return;
        }

        //사용자가 입력한 주소를 dto로 변환해서 가져옴
        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);
        //사용자 위치에서 가까운 약국을 리스트로 가져와서 DB에 저장
        //List<Direction> directionList = directionService.buildDirectionList(documentDto);
        List<Direction> directionList = directionService.buildDirectionListByCategoryApi(documentDto);
        directionService.saveAll(directionList);

    }

}
