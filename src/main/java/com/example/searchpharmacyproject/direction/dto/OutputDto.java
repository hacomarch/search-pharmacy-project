package com.example.searchpharmacyproject.direction.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OutputDto {

    private String pharmacyName;    //약국 이름
    private String pharmacyAddress; //약국 주소
    private String directionUrl;    //길안내 url
    private String roadViewUrl;     //로드뷰 url
    private String distance;        //고객이 입력한 주소와 약국 주소의 거리
}
