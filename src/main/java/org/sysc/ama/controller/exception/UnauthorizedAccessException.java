package org.sysc.ama.controller.exception;

public class UnauthorizedAccessException extends RuntimeException {

    private String message;

    public UnauthorizedAccessException(String message){
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
