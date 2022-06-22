package fr.cnam.stefangeorgesco.dmp.exception.domain;

@SuppressWarnings("serial")
public class ObjectNotFoundException extends FinderException {

	public ObjectNotFoundException(String message) {
		super(message);
	}

}
