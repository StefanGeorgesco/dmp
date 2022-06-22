package fr.cnam.stefangeorgesco.dmp.exception.domain;

@SuppressWarnings("serial")
public abstract class ApplicationException extends Exception {
	
    protected ApplicationException(final String message) {
        super(message);
    }
	
}
