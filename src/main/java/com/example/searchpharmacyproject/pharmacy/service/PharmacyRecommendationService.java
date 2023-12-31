package com.example.searchpharmacyproject.pharmacy.service;

import com.example.searchpharmacyproject.api.dto.DocumentDto;
import com.example.searchpharmacyproject.api.dto.KakaoApiResponseDto;
import com.example.searchpharmacyproject.api.service.KakaoAddressSearchService;
import com.example.searchpharmacyproject.direction.dto.OutputDto;
import com.example.searchpharmacyproject.direction.entity.Direction;
import com.example.searchpharmacyproject.direction.service.Base62Service;
import com.example.searchpharmacyproject.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyRecommendationService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;
    private final Base62Service base62Service;

    @Value("${pharmacy.recommendation.base.url}")
    private String baseUrl;
    @Value("${pharmacy.recommendation.roadView.url}")
    private String roadViewUrl;

    public List<OutputDto> recommendPharmacyList(String address) {
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        //validation check
        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
            log.error("[PharmacyRecommendationService.recommendPharmacyList fail] Input address: {}", address);
            return Collections.emptyList();
        }

        //사용자가 입력한 주소를 dto로 변환해서 가져옴
        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);
        //사용자 위치에서 가까운 약국을 리스트로 가져와서 DB에 저장
        //List<Direction> directionList = directionService.buildDirectionList(documentDto);
        List<Direction> directionList = directionService.buildDirectionListByCategoryApi(documentDto);
        return directionService.saveAll(directionList)
                .stream()
                .map(this::convertToOutputDto)
                .collect(Collectors.toList());

    }

    private OutputDto convertToOutputDto(Direction direction) {

        return OutputDto.builder()
                .pharmacyName(direction.getTargetPharmacyName())
                .pharmacyAddress(direction.getTargetAddress())
                .directionUrl(baseUrl + base62Service.encodeDirectionId(direction.getId())) // -> localhost:8080/dir/23Ferf로 저장
                .roadViewUrl(roadViewUrl + base62Service.encodeDirectionId(direction.getId()))
                .distance(String.format("%.2f km", direction.getDistance()))
                .build();
    }

}
