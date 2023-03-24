package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

import static java.lang.String.format;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> save(BookingDto bookingDto, long userId) {
        return post("", userId, bookingDto);
    }

    public ResponseEntity<Object> update(long id, long userId, boolean approved) {
        return patch(format("/%d?approved={approved}", id), userId, Map.of("approved", approved), null);
    }

    public ResponseEntity<Object> findById(long id, long ownerId) {
        return get(format("/%d", id), ownerId);
    }

    public ResponseEntity<Object> findAllByUserId(long userId, String state, int from, int size) {
        return get("?state={state}&from={from}&size={size}", userId, Map.of("state", state, "from", from, "size", size));
    }

    public ResponseEntity<Object> findAllByOwnerId(long ownerId, String state, int from, int size) {
        return get("/owner?state={state}&from={from}&size={size}", ownerId, Map.of("state", state, "from", from, "size", size));
    }
}
