package org.sysc.ama.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.sysc.ama.controller.EntityNotFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlingAdvice {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<FieldError> processValidationError(ConstraintViolationException ex) {
        List<FieldError> errors = new ArrayList<FieldError>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations())
        {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.add(new FieldError("name",propertyPath,

                    "Invalid "+ propertyPath + "(" + message + ")"));
        }
        return errors;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> processValidationError(EntityNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND ).body("Bad Request: No " + ex.getEntityName() +
                " exists with that id");
    }
}
