package fr.cnam.stefangeorgesco.dmp.domain.model;

public class Hello {
	private String message;
	
	public Hello() {
		super();
	}

	public Hello(String string) {
		this.setMessage(string);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
