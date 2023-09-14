package com.nttdata.bootcamp.s01accountservice.config.advice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.nttdata.bootcamp.s01accountservice.common.AccountCreationException;
import com.nttdata.bootcamp.s01accountservice.common.AccountNotFoundException;
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Método para guardar una transacción.
     *
     * @param ex parametro de AccountCreationException.
     * @return Retorna HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(AccountCreationException.class)
    public ResponseEntity<ErrorResponse> handleAccountCreationException(final AccountCreationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Método para guardar una transacción.
     *
     * @param ex parametro de AccountNotFoundException.
     * @return Retorna HttpStatus.NOT_FOUND.
     */
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFoundException(final AccountNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    private static class ErrorResponse {
        /**
         * status de error.
         */
        private int status;

        /**
         * message mensaje.
         */
        private String message;

        ErrorResponse(final int status, final String message) {
            this.status = status;
            this.message = message;
        }
        public int getStatus() {
            return status;
        }
        public String getMessage() {
            return message;
        }
    }
}
