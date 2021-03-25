package com.waveaccess.conference.exception;

import com.waveaccess.conference.dto.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class SimpleExceptionHandler {

    @ExceptionHandler(value = {TimePresentationsException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationExceptionError(TimePresentationsException ex) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ExceptionDto exceptionDto = new ExceptionDto(ex.getMessage(), ex.getPresentations(),
                badRequest);
        return new ResponseEntity<>(exceptionDto, badRequest);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentExceptionError(IllegalArgumentException ex) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ExceptionDto exceptionDto = new ExceptionDto(ex.getMessage(), null,
                badRequest);
        return new ResponseEntity<>(exceptionDto, badRequest);
    }

}
