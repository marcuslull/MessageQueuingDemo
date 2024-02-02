package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.model.Resource;
import com.marcuslull.momdemo.model.enums.Difficulty;
import com.marcuslull.momdemo.model.enums.Production;
import com.marcuslull.momdemo.model.enums.Rarity;
import com.marcuslull.momdemo.model.enums.TechLevel;
import com.marcuslull.momdemo.model.records.ResourceRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

class ProducerServiceImplTest {
    private RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
    private ExecutorTrackingService executorTrackingService = mock(ExecutorTrackingService.class);
    private ProducerService producerService = new ProducerServiceImpl(rabbitTemplate, executorTrackingService);
    private ResourceRecord resourceRecord = new ResourceRecord("Water", "description", TechLevel.TECH_LEVEL_1,
            Rarity.COMMON, Production.FAST, Difficulty.EASY, null);
    private Resource resource = new Resource(resourceRecord);

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void autoProduce() {
        producerService.autoProduce(resource);
        Awaitility.await().atMost(1500, TimeUnit.MILLISECONDS).untilAsserted(() ->
                verify(rabbitTemplate, atLeastOnce()).convertAndSend("Water", resource));
        verify(executorTrackingService, atLeastOnce()).register(any(ScheduledExecutorService.class));
    }

    @Test
    void produce() throws InterruptedException {
        producerService.produce(resource, 3);
        verify(rabbitTemplate, times(3)).convertAndSend("Water", resource);
    }
}