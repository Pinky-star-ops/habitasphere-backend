package com.habitasphere.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse>
    handleResourceNotFound(ResourceNotFoundException ex) {

        ApiResponse response =
                new ApiResponse(
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage()
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse>
    handleBadRequest(BadRequestException ex) {

        ApiResponse response =
                new ApiResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage()
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse>
    handleAccessDenied(AccessDeniedException ex) {

        ApiResponse response =
                new ApiResponse(
                        HttpStatus.FORBIDDEN.value(),
                        "Access denied. You do not have permission to access this API."
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse>
    handleGlobalException(Exception ex) {

        ApiResponse response =
                new ApiResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        ex.getMessage()
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
