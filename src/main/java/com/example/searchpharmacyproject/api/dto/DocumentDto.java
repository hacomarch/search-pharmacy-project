package com.example.searchpharmacyproject.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
* 카카오 주소 api에서 받은 response에서
* document 부분에서 주소와 위도, 경도를 받아와 dto로 저장
*/
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {

    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("y")
    private double latitude; //위도

    @JsonProperty("x")
    private double longitude; //경도
}
