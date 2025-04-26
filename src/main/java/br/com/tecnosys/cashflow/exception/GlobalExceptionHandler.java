package br.com.tecnosys.cashflow.exception;

import br.com.tecnosys.cashflow.dto.ApiResponse;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public final ApiResponse<Void> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        log.warn("Erro de validação: {}", errorMessage);
        return new ApiResponse<>(errorMessage);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> businessException(BusinessException ex) {
        log.warn("Exceção de negócio: {}", ex.getMessage());
        return new ApiResponse<>(ex.getMessage());
    }

    @ExceptionHandler(FeignException.ServiceUnavailable.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ApiResponse<Void> handleServiceUnavailable(FeignException.ServiceUnavailable ex) {
        log.error("Serviço externo indisponível: {}", ex.getMessage());
        return new ApiResponse<>("Serviço indisponível");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleNoResourceFoundException(NoResourceFoundException ex) {
        log.warn("Recurso não encontrado: {}", ex.getMessage());
        return new ApiResponse<>("Recurso não encontrado");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ApiResponse<Void> handleAllExceptions(Exception ex) {
        log.error("Erro não tratado: ", ex);
        return new ApiResponse<>("Erro interno do servidor: " + ex.getLocalizedMessage());
    }
}
