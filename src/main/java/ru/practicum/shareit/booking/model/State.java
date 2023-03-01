package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exception.IncorrectState;

public enum State {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static State fromString(String state) {
        switch (state) {
            case "ALL":
                return ALL;
            case "CURRENT":
                return CURRENT;
            case "PAST":
                return PAST;
            case "FUTURE":
                return FUTURE;
            case "WAITING":
                return WAITING;
            case "REJECTED":
                return REJECTED;
            default:
                throw new IncorrectState();
        }
    }
}
