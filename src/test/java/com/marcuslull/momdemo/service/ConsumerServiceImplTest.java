package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.model.Resource;
import com.marcuslull.momdemo.model.enums.Difficulty;
import com.marcuslull.momdemo.model.enums.Production;
import com.marcuslull.momdemo.model.enums.Rarity;
import com.marcuslull.momdemo.model.enums.TechLevel;
import com.marcuslull.momdemo.model.records.ResourceRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.mockito.Mockito.*;

class ConsumerServiceImplTest {
    private RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
    private ConsumerService consumerService = new ConsumerServiceImpl(rabbitTemplate);

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void consume() throws ExecutionException, InterruptedException {
        ResourceRecord resourceRecord = new ResourceRecord("name", "description", TechLevel.TECH_LEVEL_1,
                Rarity.COMMON, Production.FAST, Difficulty.EASY, null);
        Resource resource = new Resource(resourceRecord);
        int amount = 1;
        Message message = mock(Message.class);
        MessageConverter messageConverter = mock(MessageConverter.class);

        when(rabbitTemplate.receive(resource.getName())).thenReturn(message);
        when(rabbitTemplate.getMessageConverter()).thenReturn(messageConverter);
        when(messageConverter.fromMessage(message)).thenReturn(resource);

        Future<List<Resource>> future = consumerService.consume(resource, amount);
        future.get(); // Wait for the CompletableFuture to complete
        verify(rabbitTemplate, atLeastOnce()).receive(resource.getName());
        verify(messageConverter, atLeastOnce()).fromMessage(message);
    }
}