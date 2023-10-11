package com.example.searchpharmacyproject.direction.controller;

import com.example.searchpharmacyproject.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
@RequiredArgsConstructor
public class DirectionController {

    private final DirectionService directionService;

    @GetMapping("/dir/{encodedId}")
    public String searchDirectionWithBaseUrl(@PathVariable("encodedId") String encodedId) {
        String result = directionService.findDirectionUrlById(encodedId);

        log.info("[DirectionController.searchDirectionWithBaseUrl] direction url: {}", result);

        return "redirect:" + result;
    }

    @GetMapping("/roadView/{encodedId}")
    public String searchDirectionWithRoadViewUrl(@PathVariable("encodedId") String encodedId) {
        String result = directionService.findRoadViewUrlById(encodedId);

        log.info("[DirectionController.searchDirectionWithRoadViewUrl] direction url: {}", result);

        return "redirect:" + result;
    }
}
