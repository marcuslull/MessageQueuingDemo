package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.model.records.ResourceRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class RecordServiceImplTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getRecord() {
        RecordService recordService = new RecordServiceImpl();
        ResourceRecord resourceRecord1 = new ResourceRecord("Water", null, null, null,
                null, null, null);
        ResourceRecord resourceRecord2 = recordService.getRecord("water");
        assertEquals(resourceRecord2.name(), resourceRecord1.name());
    }
}