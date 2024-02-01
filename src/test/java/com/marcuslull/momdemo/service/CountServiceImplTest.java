package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.model.Count;
import com.marcuslull.momdemo.view.ViewModel;
import com.rabbitmq.client.Channel;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CountServiceImplTest {
    private RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
    private ExecutorTrackingService executorTrackingService = mock(ExecutorTrackingService.class);
    private ViewModel viewModel = mock(ViewModel.class);
    private CountService countService = new CountServiceImpl(rabbitTemplate, executorTrackingService, viewModel);
    private Count count = new Count("1", "1", "1", "1", "1", "1", "1");

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCount() throws IOException {
        Channel channel = mock(Channel.class);

        when(rabbitTemplate.execute(any(ChannelCallback.class))).thenReturn(1L); // this is half of the lambda
        when(channel.messageCount(anyString())).thenReturn(1L); // the other side of the lambda

        assertEquals(count.getWater(), countService.getCount().getWater());
        assertEquals(count.getEducation(), countService.getCount().getEducation());
        assertEquals(count.getEnergy(), countService.getCount().getEnergy());
    }

    @Test
    void monitorCount() {
        countService.monitorCount();
        // need to wait for the scheduled task to run
        Awaitility.await().atMost(1500, TimeUnit.MILLISECONDS).untilAsserted(() ->
                verify(viewModel, atLeastOnce()).setCounts(any(Count.class)));
        verify(executorTrackingService, atLeastOnce()).register(any());
    }
}