package com.Trackdev.control.Exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return construir(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(BusinessRuleException ex, HttpServletRequest request) {
        return construir(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidacion(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return construir(HttpStatus.BAD_REQUEST, mensaje, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleCredencialesInvalidas(BadCredentialsException ex, HttpServletRequest request) {
        return construir(HttpStatus.UNAUTHORIZED, "Email o contraseña incorrectos", request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthError(AuthenticationException ex, HttpServletRequest request) {
        return construir(HttpStatus.UNAUTHORIZED, "No autenticado", request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccesoDenegado(AccessDeniedException ex, HttpServletRequest request) {
        return construir(HttpStatus.FORBIDDEN, "No tenés permisos para esta accion", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, HttpServletRequest request) {
        return construir(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", request);
    }

    private ResponseEntity<ErrorResponse> construir(HttpStatus status, String mensaje, HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensaje,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(body);
    }
}