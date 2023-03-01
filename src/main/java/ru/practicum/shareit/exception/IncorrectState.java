package ru.practicum.shareit.exception;

public class IncorrectState extends RuntimeException {
    private final String message;

    public IncorrectState() {
        message = "Unknown state: UNSUPPORTED_STATUS";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
