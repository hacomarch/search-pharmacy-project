package com.example.searchpharmacyproject.api.service

import com.example.searchpharmacyproject.AbstractIntegrationContainerBaseTest
import org.springframework.beans.factory.annotation.Autowired

class KakaoCategorySearchServiceTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private KakaoCategorySearchService kakaoCategorySearchService;

    def "위도, 경도, 거리가 주어졌을 때, requestPharmacyCategorySearch 메소드는 정상적으로 document를 반환"() {
        given:
        double inputLatitude = 37.85348
        double inputLongitude = 127.7367
        double radius = 10.0

        when:
        def result = kakaoCategorySearchService.requestPharmacyCategorySearch(inputLatitude, inputLongitude, radius)

        then:
        result.documentList.size() > 0
        result.metaDto.totalCount > 0
        println(result.documentList.get(0).placeName + ", 주소 :  " + result.documentList.get(0).addressName);
        println(result.documentList.get(1).placeName + ", 주소 :  " + result.documentList.get(1).addressName);
    }
}
