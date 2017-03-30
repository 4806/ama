package org.sysc.ama.controller.exception;

public class BadRequestError extends Exception{
    public BadRequestError(String message) {
        super(message);
    }
}
