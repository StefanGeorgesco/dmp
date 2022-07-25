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
import fr.cnam.stefangeorgesco.dmp.exception.domain.DeleteException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.UpdateException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;

@ControllerAdvice
@RestController
public class ExceptionController {
	
	private static Map<Class<? extends Throwable>, HttpStatus> map = new HashMap<>();
	
	static {
		map.put(DuplicateKeyException.class, HttpStatus.CONFLICT);
		map.put(FinderException.class, HttpStatus.NOT_FOUND);
		map.put(UpdateException.class, HttpStatus.CONFLICT);
		map.put(DeleteException.class, HttpStatus.CONFLICT);
	}

	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField()
					.replaceAll("(DTO|\\[|\\])", "").replaceAll("[.]", "_");
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<RestResponse> handleApplicationException(ApplicationException ex) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		if (map.get(ex.getClass()) != null) {
			status = map.get(ex.getClass());
		}
		
		RestResponse response = new RestResponse();
		response.setStatus(status.value());
		response.setMessage(ex.getMessage());

		return ResponseEntity.status(status).body(response);
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler({ BadCredentialsException.class, UsernameNotFoundException.class })
	public RestResponse handleBadCredentialsException(BadCredentialsException ex) {
		RestResponse response = new RestResponse();
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
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
