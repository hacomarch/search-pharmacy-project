package com.example.searchpharmacyproject.pharmacy.service;

import com.example.searchpharmacyproject.api.dto.DocumentDto;
import com.example.searchpharmacyproject.api.dto.KakaoApiResponseDto;
import com.example.searchpharmacyproject.api.service.KakaoAddressSearchService;
import com.example.searchpharmacyproject.direction.dto.OutputDto;
import com.example.searchpharmacyproject.direction.entity.Direction;
import com.example.searchpharmacyproject.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

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

    private static final String ROAD_VIEW_BASE_URL = "https://map.kakao.com/link/roadview/";
    private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";

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
        // "/link/map/이름,위도,경도" 이런 식으로 불러야 길찾기 할 수 있음.
        // , 를 기준으로 약국이름,위도,경도 이어 붙이기
        String params = String.join(",",
                direction.getTargetPharmacyName(),
                String.valueOf(direction.getTargetLatitude()),
                String.valueOf(direction.getTargetLongitude()));

        String directionUrl = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + params).toUriString();
        log.info("direction params : {}, url : {}", params, directionUrl);

        // "/link/roadview/위도,경도" 이런 식으로 불러야 로드뷰 볼 수 있음.
        String roadViewUrl = ROAD_VIEW_BASE_URL + direction.getTargetLatitude() + "," + direction.getTargetLongitude();

        return OutputDto.builder()
                .pharmacyName(direction.getTargetPharmacyName())
                .pharmacyAddress(direction.getTargetAddress())
                .directionUrl(directionUrl)
                .roadViewUrl(roadViewUrl)
                .distance(String.format("%.2f km", direction.getDistance()))
                .build();
    }

}
