package com.example.searchpharmacyproject.direction.controller

import com.example.searchpharmacyproject.direction.dto.OutputDto
import com.example.searchpharmacyproject.pharmacy.service.PharmacyRecommendationService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

class FormControllerTest extends Specification {

    private MockMvc mockMvc
    private PharmacyRecommendationService pharmacyRecommendationService = Mock()
    private List<OutputDto> outputDtoList

    def setup() {
        //FormController를 MockMvc에 등록
        //주로 단위 테스트나 통합 테스트 시 사용된다.
        mockMvc = MockMvcBuilders.standaloneSetup(new FormController(pharmacyRecommendationService)).build()

        outputDtoList = new ArrayList<>()
        outputDtoList.addAll(
                OutputDto.builder()
                .pharmacyName("바다약국")
                .build(),
                OutputDto.builder()
                .pharmacyName("속초약국")
                .build()
        )
    }

    def "GET /"() {
        expect:
        //FormController의 "/" URI를 get 방식으로 호출
        mockMvc.perform (get("/"))
        .andExpect {handler().handlerType(FormController.class)}
        .andExpect {handler().methodName("main")}
        .andExpect {status().isOk()}
        .andExpect {view().name("main")}
        .andDo(log())
    }

    def "POST /search"() {
        given:
        String inputAddress = "서울 성북구 종암동"

        when:
        def resultActions = mockMvc.perform(post("/search")
                                                            .param("address", inputAddress))

        then:
        //1 * : recommendPharmacyList가 1번만 호출되는지 확인
        1 * pharmacyRecommendationService.recommendPharmacyList(argument -> {
            assert argument == inputAddress //mock 객체의 argument 검증
        }) >> outputDtoList //outputDtoList 형식으로 리턴되는지

        resultActions
                .andExpect {status().isOk()}
                .andExpect {view().name("output")}
                .andExpect {model().attributeExists("outputFormList")} //model에 outputFormList가 있는지
                .andExpect {model().attribute("outputFormList", outputDtoList)} //outputFormList가 outputDtoList 객체인지
                .andDo (print())
    }
}
