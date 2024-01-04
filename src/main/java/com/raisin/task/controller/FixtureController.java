package com.raisin.task.controller;

import com.raisin.task.service.FixtureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FixtureController {

    private final FixtureService fixtureService;

    @GetMapping("/sync")
    public void syncData() throws InterruptedException {
        fixtureService.init();
    }
}
