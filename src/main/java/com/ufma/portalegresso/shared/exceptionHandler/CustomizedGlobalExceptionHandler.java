package com.ufma.portalegresso.shared.exceptionHandler;

import com.ufma.portalegresso.shared.ResponseApi;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;



@ControllerAdvice
public class CustomizedGlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomizedGlobalExceptionHandler.class);

    public final ResponseEntity<ResponseApi<Object>> handleAllException(Exception ex, WebRequest request){
        logger.error("Erro inesperado: {}", ex.getMessage(), ex);
        ResponseApi<Object> exceptionResponse = ResponseApi.builder()
                .mensagem("Ocorreu um erro interno inesperado. Por favor, tente novamente mais tarde. Se o problema persistir, entre em contato.")
                .detalhes(request.getDescription(true)) // TODO: mudar para false
                .status(500)
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<ResponseApi<Object>> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request){
        logger.error("Not found: {}", ex.getMessage(), ex);
        ResponseApi<Object> exceptionResponse = ResponseApi.builder()
                .mensagem(ex.getMessage())
                .detalhes(request.getDescription(false))
                .status(HttpStatus.NOT_FOUND.value())
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ResponseApi<Object>> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request){
        logger.error("Bad request: {}", ex.getMessage(), ex);
        ResponseApi<Object> exceptionResponse = ResponseApi.builder()
                .mensagem(ex.getMessage())
                .detalhes(request.getDescription(true))
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
