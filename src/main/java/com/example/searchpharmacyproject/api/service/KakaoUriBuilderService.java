package com.example.searchpharmacyproject.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/*
* 기본적으로 정해져 있는 url에
* address를 받아서 query 값으로 지정해
* 새로운 url을 만들어 반환
*/
@Slf4j
@Service
public class KakaoUriBuilderService {

    private static final String KAKAO_LOCAL_SEARCH_ADDRESS_URL = "https://dapi.kakao.com/v2/local/search/address.json";

    public URI buildUriByAddressSearch(String address) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_SEARCH_ADDRESS_URL);
        uriBuilder.queryParam("query", address);

        URI uri = uriBuilder.build().encode().toUri(); //https://dapi.kakao.com/v2/local/search/address.json?query=${address} 가 저장된다.
        log.info("[KakaoUriBuilderService.buildUriByAddressSearch] address: {}, uri: {}", address, uri);

        return uri;
    }
}
