package br.com.tecnosys.cashflow.exception;

import br.com.tecnosys.cashflow.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex,
            WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        ApiResponse<Void> errorResponse = new ApiResponse<>(errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiResponse<Void>> handleAllExceptions(Exception ex) {
        ApiResponse<Void> errorResponse = new ApiResponse<>(
                "Erro interno do servidor: " + ex.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
