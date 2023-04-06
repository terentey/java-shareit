package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Collections;
import java.util.Map;

import static java.lang.String.format;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> save(ItemDto itemDto, long userId) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> saveComment(CommentDto commentDto, long itemId, long userId) {
        return post(format("/%d/comment", itemId), userId, commentDto);
    }

    public ResponseEntity<Object> update(ItemDto itemDto, long id, long userId) {
        return patch(format("/%d", id), userId, itemDto);
    }

    public ResponseEntity<Object> findById(long id, long userId) {
        return get(format("/%d", id), userId);
    }

    public ResponseEntity<Object> findAll(long userId, int from, int size) {
        return get("?from={from}&size={size}", userId, Map.of("from", from, "size", size));
    }

    public ResponseEntity<Object> search(long userId, String text, int from, int size) {
        if (text.isBlank()) {
            return ResponseEntity.status(HttpStatus.OK).body(Collections.emptyList());
        }
        return get("/search?text={text}&from={from}&size={size}", userId, Map.of("text", text, "from", from, "size", size));
    }
}
