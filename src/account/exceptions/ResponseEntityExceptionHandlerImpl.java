package account.exceptions;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Date;

@ControllerAdvice
public class ResponseEntityExceptionHandlerImpl extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String defaultMessage = exception
                .getBindingResult()
                .getAllErrors()
                .stream().findFirst()
                .map(ObjectError::getDefaultMessage)
                .orElse("No message");

        ErrorResponse responseEntity = ErrorResponse
                .builder()
                .timestamp(new Date())
                .error(status.getReasonPhrase())
                .status(status.value())
                .message(defaultMessage)
                .path(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().getPath())
                .build();

        return new ResponseEntity<>(responseEntity, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse responseEntity = ErrorResponse
                .builder()
                .timestamp(new Date())
                .error(status.getReasonPhrase())
                .status(status.value())
                .message(exception.getLocalizedMessage())
                .path(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().getPath())
                .build();

        return new ResponseEntity<>(responseEntity, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse responseEntity = ErrorResponse
                .builder()
                .timestamp(new Date())
                .error(status.getReasonPhrase())
                .status(status.value())
                .message(exception.getLocalizedMessage())
                .path(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().getPath())
                .build();

        return new ResponseEntity<>(responseEntity, headers, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
