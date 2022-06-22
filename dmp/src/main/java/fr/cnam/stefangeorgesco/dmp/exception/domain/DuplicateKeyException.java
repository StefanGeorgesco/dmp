package fr.cnam.stefangeorgesco.dmp.exception.domain;

@SuppressWarnings("serial")
public class DuplicateKeyException extends CreateException {

	public DuplicateKeyException(String message) {
		super(message);
	}

}
