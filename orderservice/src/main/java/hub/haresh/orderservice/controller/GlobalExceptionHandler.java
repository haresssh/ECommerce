package hub.haresh.orderservice.controller;

import hub.haresh.orderservice.dto.ErrorDto;
import hub.haresh.orderservice.exceptions.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorDto> handleOrderNotFoundException(OrderNotFoundException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(ex.getMessage());
        errorDto.setStatus("ORDER_NOT_FOUND");
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ErrorDto> handleRestClientException(RestClientException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage("External service call failed: " + ex.getMessage());
        errorDto.setStatus("EXTERNAL_SERVICE_ERROR");
        return new ResponseEntity<>(errorDto, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGenericException(Exception ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage("Something went wrong");
        errorDto.setStatus("INTERNAL_SERVER_ERROR");
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
