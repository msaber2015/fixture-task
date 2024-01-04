package com.raisin.task.service;

import com.raisin.task.client.FixtureClient;
import com.raisin.task.exception.CustomRestException;
import com.raisin.task.model.dto.FixtureSourceAResponseDTO;
import com.raisin.task.model.dto.FixtureSourceBResponseDTO;
import com.raisin.task.model.dto.SinkDataRequestDTO;
import com.raisin.task.model.enums.ClientExceptionCode;
import com.raisin.task.model.enums.FixtureKind;
import feign.codec.DecodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FixtureService {

    private static boolean isSourceADone;
    private static boolean isSourceBDone;
    List<FixtureSourceAResponseDTO> dataFromSourceA = new ArrayList<>();
    List<FixtureSourceBResponseDTO> dataFromSourceB = new ArrayList<>();
    List<SinkDataRequestDTO> defectedData = new ArrayList<>();
    boolean isSourceABlocked = false;
    boolean isSourceBBlocked = false;

    private final FixtureClient fixtureClient;

    public void init() throws InterruptedException {
        readingDataFromSources();
    }

    public void readingDataFromSources() throws InterruptedException {
        log.info("#### All ### START READING DATA FROM All #### All ###");

        // clearing data
        dataFromSourceA = new ArrayList<>();
        // initialize threads
        Thread threadA = new Thread(() -> findAllFromSourceA());
        Thread threadB = new Thread(() -> findAllFromSourceB());
        threadA.start();
        threadB.start();
        // block until threads finish
        while (threadA.isAlive() || threadB.isAlive()) ;
        // categorize data and push it to sink api
        dataFromSourceA.stream().forEach(fixtureSourceADTO -> {
            SinkDataRequestDTO requestDTO;
            if (dataFromSourceB.stream().noneMatch(fixtureSourceBDTO ->
                    fixtureSourceADTO.getId().equals(fixtureSourceBDTO.getId()))) {
                requestDTO = SinkDataRequestDTO.builder()
                        .kind(FixtureKind.ORPHANED.name())
                        .id(fixtureSourceADTO.getId())
                        .build();
            } else {
                requestDTO = SinkDataRequestDTO.builder()
                        .kind(FixtureKind.JOINED.name())
                        .id(fixtureSourceADTO.getId())
                        .build();
            }
            fixtureClient.sinkData(requestDTO);
        });
        log.info("### {}", dataFromSourceA);
        log.info("### {}", dataFromSourceB);
        log.info("#### All ### END READING DATA FROM All #### All ###");
    }

    public void findAllFromSourceA() {
        log.info("#### AAAAAA ### START READING DATA FROM A #### AAAAAA ###");
        while (!isSourceADone && !isSourceABlocked) {
            try {
                var response = fixtureClient.findFromSourceA();
                if (response.getId() == null) {
                    isSourceADone = true;
                } else {
                    dataFromSourceA.add(FixtureSourceAResponseDTO.builder()
                            .id(response.getId())
                            .status(response.getStatus())
                            .build());
                }
                log.info("#### AAAAAA ### {} ###", response);
            } catch (CustomRestException exception) {
                log.error("#### AAAAAA ### {} ###", exception.getExceptionCode());
                if (exception.getExceptionCode() == ClientExceptionCode.BAD_PARSING) {
                    defectedData.add(SinkDataRequestDTO.builder()
                            .id(exception.getId())
                            .kind(FixtureKind.DEFECTIVE.name())
                            .build());
                } else if (exception.getExceptionCode() == ClientExceptionCode.BLOCKED) {
                    isSourceABlocked = true;
                } else {
                    throw exception;
                }
            } catch (DecodeException exception) {
                log.error("#### AAAAAA ### {} ###", ClientExceptionCode.BAD_PARSING);
                defectedData.add(SinkDataRequestDTO.builder()
                        .id(exception.getMessage())
                        .kind(FixtureKind.DEFECTIVE.name())
                        .build());
            }
        }
    }

    public void findAllFromSourceB() {
        log.info("#### BBBBBB ### START READING DATA FROM B #### BBBBBB ###");
        while (!isSourceBDone && !isSourceBBlocked) {
            try {
                var response = fixtureClient.findFromSourceB();
                if (response.getId() == null) {
                    isSourceBDone = true;
                } else {
                    dataFromSourceB.add(response);
                }
                log.info("#### BBBBBB ### {} ###", response);
            } catch (CustomRestException exception) {
                log.error("#### BBBBBB ### {} ###", exception.getExceptionCode());
                if (exception.getExceptionCode() == ClientExceptionCode.BAD_PARSING) {
                    defectedData.add(SinkDataRequestDTO.builder()
                            .id(exception.getId())
                            .kind(FixtureKind.DEFECTIVE.name())
                            .build());
                } else if (exception.getExceptionCode() == ClientExceptionCode.BLOCKED) {
                    isSourceBBlocked = true;
                } else {
                    throw exception;
                }
            } catch (DecodeException exception) {
                log.error("#### BBBBBB ### {} ###", ClientExceptionCode.BAD_PARSING);
                defectedData.add(SinkDataRequestDTO.builder()
                        .id(exception.getMessage())
                        .kind(FixtureKind.DEFECTIVE.name())
                        .build());
            }
        }
    }
}
