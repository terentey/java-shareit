package ru.practicum.shareit.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class IncorrectIdException extends RuntimeException {
}
