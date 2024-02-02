package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.model.Resource;
import com.marcuslull.momdemo.model.enums.Difficulty;
import com.marcuslull.momdemo.model.enums.Production;
import com.marcuslull.momdemo.model.enums.Rarity;
import com.marcuslull.momdemo.model.enums.TechLevel;
import com.marcuslull.momdemo.model.records.ResourceRecord;
import com.marcuslull.momdemo.view.ViewModel;
import com.rabbitmq.client.Channel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class SimulationServiceImplTest {
    private RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
    private ViewModel viewModel = mock(ViewModel.class);
    private RecordService recordService = mock(RecordService.class);
    private AssemblerService assemblerService = mock(AssemblerService.class);
    private CountService countService = mock(CountService.class);
    private ProducerService producerService = mock(ProducerService.class);
    private SimulationService simulationService = new SimulationServiceImpl(rabbitTemplate, viewModel, recordService, assemblerService, countService, producerService);

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void init() {
        ResourceRecord resourceRecord = new ResourceRecord("Name", null, TechLevel.TECH_LEVEL_1,
                Rarity.COMMON, Production.FAST, Difficulty.EASY, null);
        when(recordService.getRecord(anyString())).thenReturn(resourceRecord);
        simulationService.init();
        verify(viewModel, times(7)).setResources(any(Resource.class));
    }

    @Test
    void start() throws ExecutionException, InterruptedException {
        simulationService.start();
        verify(countService, atLeastOnce()).monitorCount();
        verify(assemblerService, atLeastOnce()).updateViewModel();
        verify(producerService, atLeastOnce()).autoProduce(null);
    }

    @Test
    void reset() throws IOException {
        Channel channel = mock(Channel.class);
        // ****************************************************************
        // testing of lambdas is hard - this portion is credited to copilot
        when(channel.queuePurge(anyString())).thenReturn(null); // inside the lambda
        when(rabbitTemplate.execute(any(ChannelCallback.class))).thenAnswer(invocation -> {
            ChannelCallback callback = invocation.getArgument(0);
            return callback.doInRabbit(channel);
        }); // outside part of the lambda
        // ****************************************************************
        simulationService.reset();
        verify(channel, times(7)).queuePurge(anyString());
    }

    @Test
    void setCurrentTechLevel() {
        simulationService.setCurrentTechLevel(TechLevel.TECH_LEVEL_2);
        verify(viewModel, atLeastOnce()).setTechLabel("2");
    }

    @Test
    void advanceTechLevel() throws NoSuchFieldException, IllegalAccessException {
        Field field = SimulationServiceImpl.class.getDeclaredField("currentTechLevel");
        field.setAccessible(true);
        field.set(simulationService, TechLevel.PLACEHOLDER);
        TechLevel techLevel = (TechLevel) field.get(simulationService);
        boolean isTechLevelZero = techLevel.equals(TechLevel.PLACEHOLDER);
        simulationService.advanceTechLevel();
        simulationService.advanceTechLevel();
        techLevel = (TechLevel) field.get(simulationService);
        boolean isTechLevelTwo = techLevel.equals(TechLevel.TECH_LEVEL_2);
        assertTrue(isTechLevelZero && isTechLevelTwo);
    }
}