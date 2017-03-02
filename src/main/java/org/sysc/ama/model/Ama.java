package org.sysc.ama.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import java.util.Date;

@Entity
public class Ama {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User subject;

    private boolean isPublic;
    private String title;
    private Date created;
    private Date updated;

    public Ama () {
        this("", null, false);
    }

    public Ama(String title, User subject, boolean isPublic) {
        this.title = title;
        this.subject = subject;
        this.isPublic = isPublic;
        this.created = new Date();
        this.updated = new Date();
    }

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public User getSubject() {
        return subject;
    }

    public void setSubject(User subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreated () {
        return this.created;
    }

    public void setUpdated (Date updated) {
        this.updated = updated;
    }

    public Date getUpdated () {
        return this.updated;
    }
}
