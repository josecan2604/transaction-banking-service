package org.banking.challenge.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.banking.challenge.dtos.wrapper.ApiResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerUtility {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponseWrapper<?>> handleAllExceptions(
      Exception ex, HttpServletRequest request) {
    ApiResponseWrapper<?> responseWrapper =
        new ApiResponseWrapper<>(null, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseWrapper);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ApiResponseWrapper<?>> handleMissingParams(
      MissingServletRequestParameterException ex) {
    String name = ex.getParameterName();
    return ResponseEntity.badRequest()
        .body(
            new ApiResponseWrapper<>(
                null, "Required request parameter  " + name + " is not present", 400));
  }
}
