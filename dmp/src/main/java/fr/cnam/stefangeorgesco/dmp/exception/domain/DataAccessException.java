package fr.cnam.stefangeorgesco.dmp.exception.domain;

@SuppressWarnings("serial")
public class DataAccessException extends RuntimeException {
	
    public DataAccessException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
}
