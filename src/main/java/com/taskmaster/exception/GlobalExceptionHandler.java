package com.taskmaster.exception;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.stream.Collectors;
@RestControllerAdvice
public class GlobalExceptionHandler {
    record ErrorResponse(Instant timestamp, int status, String error, String message, String path) {}
    @ExceptionHandler(ApiException.class) ResponseEntity<ErrorResponse> api(ApiException e, HttpServletRequest r) { return response(e.getStatus(),e.getMessage(),r); }
    @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException e,HttpServletRequest r) {
        String m=e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).collect(Collectors.joining(", "));
        return response(HttpStatus.BAD_REQUEST,m,r);
    }
    @ExceptionHandler(ConstraintViolationException.class) ResponseEntity<ErrorResponse> violation(ConstraintViolationException e,HttpServletRequest r){return response(HttpStatus.BAD_REQUEST,e.getMessage(),r);}
    @ExceptionHandler(Exception.class) ResponseEntity<ErrorResponse> unexpected(Exception e,HttpServletRequest r){return response(HttpStatus.INTERNAL_SERVER_ERROR,"An unexpected error occurred",r);}
    private ResponseEntity<ErrorResponse> response(HttpStatus s,String m,HttpServletRequest r){return ResponseEntity.status(s).body(new ErrorResponse(Instant.now(),s.value(),s.getReasonPhrase(),m,r.getRequestURI()));}
}
