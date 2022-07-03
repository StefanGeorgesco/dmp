package fr.cnam.stefangeorgesco.dmp.exception.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.cnam.stefangeorgesco.dmp.api.RestResponse;
import fr.cnam.stefangeorgesco.dmp.exception.domain.ApplicationException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

@ControllerAdvice
@RestController
public class ExceptionController {
	
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(
	  MethodArgumentNotValidException ex) {
	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    return errors;
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ApplicationException.class)
	public RestResponse handleApplicationException(ApplicationException ex) {
		RestResponse response = new RestResponse();
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setMessage(ex.getMessage());
		
		return response;
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public RestResponse handleUnknownException(Exception ex) {
		RestResponse response = new RestResponse();
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.setMessage(ex.getMessage());
		
		return response;
	}
	
}
