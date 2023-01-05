package ru.yandex.ewmmain.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.ewmmain.exception.dto.ExceptionDto;
import ru.yandex.ewmmain.exception.model.AlreadyExists;
import ru.yandex.ewmmain.exception.model.ForbiddenException;
import ru.yandex.ewmmain.exception.model.NotFoundException;
import ru.yandex.ewmmain.exception.model.ValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class EwmMainExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDto> notFound(NotFoundException e) {
        return new ResponseEntity<>(new ExceptionDto(
                e.getMessage(),
                "Object is not found",
                HttpStatus.NOT_FOUND,
                List.of(),
                LocalDateTime.now()
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExists.class)
    public ResponseEntity<ExceptionDto> alreadyExists(AlreadyExists e) {
        return new ResponseEntity<>(new ExceptionDto(
                e.getMessage(),
                "Object with this param is already exists",
                HttpStatus.CONFLICT,
                List.of(),
                LocalDateTime.now()
        ), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionDto> forbidden(ForbiddenException e) {
        return new ResponseEntity<>(new ExceptionDto(
                e.getMessage(),
                "No access rights",
                HttpStatus.FORBIDDEN,
                List.of(),
                LocalDateTime.now()
        ), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExceptionDto> validation(ValidationException e) {
        return new ResponseEntity<>(new ExceptionDto(
                e.getMessage(),
                "Validation Error",
                HttpStatus.BAD_REQUEST,
                List.of(),
                LocalDateTime.now()
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> validationArg(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new ExceptionDto(
                Objects.requireNonNull(e.getFieldError()).getDefaultMessage(),
                "Validation Error",
                HttpStatus.BAD_REQUEST,
                List.of(),
                LocalDateTime.now()
        ), HttpStatus.BAD_REQUEST);
    }
}
