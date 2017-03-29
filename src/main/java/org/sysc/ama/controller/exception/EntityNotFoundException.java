package org.sysc.ama.controller.exception;

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
