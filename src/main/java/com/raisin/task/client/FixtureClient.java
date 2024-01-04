package com.raisin.task.client;

import com.raisin.task.config.FixtureFeignConfig;
import com.raisin.task.model.dto.FixtureSourceAResponseDTO;
import com.raisin.task.model.dto.FixtureSourceBResponseDTO;
import com.raisin.task.model.dto.SinkDataRequestDTO;
import com.raisin.task.model.dto.SinkDataResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "fixture", url = "${clients.fixture.base-url}", configuration = FixtureFeignConfig.class)
public interface FixtureClient {

    @GetMapping(value = "/source/a", produces = MediaType.APPLICATION_JSON_VALUE)
    FixtureSourceAResponseDTO findFromSourceA();

    @GetMapping(value = "/source/b", produces = MediaType.APPLICATION_XML_VALUE)
    FixtureSourceBResponseDTO findFromSourceB();

    @PostMapping(value = "/sink/a", produces = MediaType.APPLICATION_JSON_VALUE)
    SinkDataResponseDTO sinkData(@RequestBody SinkDataRequestDTO request);
}
