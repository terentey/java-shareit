package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
class ItemRequestServiceTest {
    @Autowired
    private ItemRequestService service;

    @Test
    @Sql("classpath:reboot.sql")
    @Sql("classpath:test.sql")
    void findAll() {
        List<ItemRequestDtoResponse> result = service.findAll(0, 1, 1);

        assertEquals(1, result.get(0).getId());
    }
}