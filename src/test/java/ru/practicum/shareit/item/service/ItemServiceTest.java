package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDtoResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Transactional
@SpringBootTest
class ItemServiceTest {
    @Autowired
    private ItemService service;

    @Test
    @Sql("classpath:reboot.sql")
    @Sql("classpath:test.sql")
    void search() {
        List<ItemDtoResponse> result = service.search(2, "it", 0, null);

        assertEquals(1, result.get(0).getId());
    }
}