package org.sysc.ama.controller.exception;

/**
 * Created by Cameron on 29/03/2017.
 */
public class BadRequestError extends RuntimeException{
    public BadRequestError(String message) {
        super(message);
    }
}
