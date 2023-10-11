package com.example.searchpharmacyproject.direction.controller

import com.example.searchpharmacyproject.direction.service.DirectionService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class DirectionControllerTest extends Specification {

    private MockMvc mockMvc;
    private DirectionService directionService = Mock();

    def setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new DirectionController(directionService)).build()
    }

    def "GET /dir/{encodedId}"() {
        given:
        String encodedId = "r"

        String redirectUrl = "https://map.kakao.com/link/map/pharmacy,38.11,128.11"

        when:
        directionService.findDirectionUrlById(encodedId) >> redirectUrl
        def result = mockMvc.perform(get("/dir/{encodedId}", encodedId))

        then:
        result
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectUrl))
                .andDo(print())
    }

    def "GET /roadView/{encodedId}"() {
        given:
        String encodedId = "r"

        String redirectUrl = "https://map.kakao.com/link/roadview/38.11,128.11"

        when:
        directionService.findRoadViewUrlById(encodedId) >> redirectUrl
        def result = mockMvc.perform(get("/roadView/{encodedId}", encodedId))

        then:
        result
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectUrl))
                .andDo(print())
    }
}
