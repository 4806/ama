package org.sysc.ama.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class EntityNotFoundException extends RuntimeException {

    private String entityName;

    public EntityNotFoundException(String entityName){
        super();
        this.entityName = entityName;
    }

    public String getEntityName() {
        return entityName;
    }
}
