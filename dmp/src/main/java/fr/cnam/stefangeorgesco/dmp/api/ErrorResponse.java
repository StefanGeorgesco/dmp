package fr.cnam.stefangeorgesco.dmp.api;

import lombok.Data;

@Data
public class ErrorResponse {
	
    private int status;
    private String message;

}
