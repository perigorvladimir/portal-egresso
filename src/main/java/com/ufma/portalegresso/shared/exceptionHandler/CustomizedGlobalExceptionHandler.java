package com.ufma.portalegresso.shared.exceptionHandler;

import com.ufma.portalegresso.shared.ResponseApi;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;


@ControllerAdvice
public class CustomizedGlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomizedGlobalExceptionHandler.class);


    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ResponseApi<Object>> handleAllException(Exception ex, WebRequest request){
        logger.error("Erro inesperado: {}", ex.getMessage(), ex);
        ResponseApi<Object> exceptionResponse = ResponseApi.builder()
                .mensagem("Ocorreu um erro interno inesperado. Por favor, tente novamente mais tarde. Se o problema persistir, entre em contato.")
                .detalhes(request.getDescription(false).concat(": ").concat(ex.getMessage()))
                .status(500)
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<ResponseApi<Object>> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request){
        ResponseApi<Object> exceptionResponse = ResponseApi.builder()
                .mensagem(ex.getMessage())
                .detalhes(request.getDescription(false))
                .status(HttpStatus.NOT_FOUND.value())
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ResponseApi<Object>> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request){
        ResponseApi<Object> exceptionResponse = ResponseApi.builder()
                .mensagem(ex.getMessage())
                .detalhes(request.getDescription(false))
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<ResponseApi<Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        String mensagemErro;
        if (ex.getCause() instanceof com.fasterxml.jackson.databind.exc.MismatchedInputException) {
            mensagemErro = "Algum tipo de dado enviado na requisição não é compatível com o esperado.";
        } else if (ex.getCause() instanceof com.fasterxml.jackson.core.JsonParseException) {
            mensagemErro = "O corpo da requisição está malformado ou contém erros de sintaxe.";
        } else {
            mensagemErro = "O corpo da requisição está ausente ou não pode ser lido.";
        }
        ResponseApi<Object> exceptionResponse = ResponseApi.builder()
                .mensagem(mensagemErro)
                .detalhes(request.getDescription(false))
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        // Capturar erros de validação
        Object errorMessages = getErrors(ex);

        ResponseApi<Object> exceptionResponse = ResponseApi.builder()
                .mensagem("Erro de validação de request. Quantidade de erros: " + ex.getBindingResult().getFieldErrors().size())
                .detalhes(errorMessages)
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.badRequest().body(exceptionResponse);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public final ResponseEntity<ResponseApi<Object>> handleBadCredentialsException(BadCredentialsException ex, WebRequest request){
        ResponseApi<Object> exceptionResponse = ResponseApi.builder()
                .mensagem(ex.getMessage())
                .detalhes(request.getDescription(false))
                .status(HttpStatus.UNAUTHORIZED.value())
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    private Object getErrors(MethodArgumentNotValidException ex) {
//        if(ex.getBindingResult().getFieldErrors().size() == 1) { // se tiver apenas 1 erro
//            // retorna apenas String
//            return String.format("'%s': '%s'", ex.getBindingResult().getFieldErrors().getFirst().getField(), ex.getBindingResult().getFieldErrors().getFirst().getDefaultMessage());
//        }
        ArrayList<String> arrayErrorMessages = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            arrayErrorMessages.add(String.format("'%s': '%s'", error.getField(), error.getDefaultMessage()));
        }
        return arrayErrorMessages;
    }
}
