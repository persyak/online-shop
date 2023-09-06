package org.ogorodnik.shop.exception;

import org.apache.tomcat.websocket.AuthenticationException;
import org.ogorodnik.shop.exception.entity.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ItemNotFountException.class)
    public ResponseEntity<ErrorMessage> itemNotFoundException(ItemNotFountException exception, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorMessage);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorMessage> authenticationException(AuthenticationException exception, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.UNAUTHORIZED, exception.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(errorMessage);
    }

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<ErrorMessage> sessionNotFoundException(
            SessionNotFoundException exception, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorMessage);
    }
    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ErrorMessage> tokenNotFoundException(TokenNotFoundException exception){
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
}
