package org.sysc.ama.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Cameron on 27/02/2017.
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

}
