package com.example.searchpharmacyproject.api.service;

import com.example.searchpharmacyproject.api.dto.KakaoApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

//주소를 입력 받아 카카오 주소 api를 호출하는 서비스

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAddressSearchService {

    private final RestTemplate restTemplate;
    private final KakaoUriBuilderService kakaoUriBuilderService;

    @Value("${kakao.rest.api.key}") //application.yml에서 정의한 키 값을 가져온다.
    private String kakaoRestApiKey;

    public KakaoApiResponseDto requestAddressSearch(String address) {

        //입력한 주소가 널인지 체크
        if(ObjectUtils.isEmpty(address)) return null;

        //주소를 받아서 형식에 맞는 요청 주소로 변환(query값 붙여서)
        URI uri = kakaoUriBuilderService.buildUriByAddressSearch(address);

        //헤더에 rest api key 값 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);
        //HttpEntitiy 값에 헤더를 넣어 저장
        HttpEntity httpEntity = new HttpEntity<>(headers);

        //kakao api 호출
        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoApiResponseDto.class).getBody();

    }
}
