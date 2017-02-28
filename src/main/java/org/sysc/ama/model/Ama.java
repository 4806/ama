package org.sysc.ama.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Ama {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = { CascadeType.ALL })
    private User subject;

    private boolean isPublic;
    private String title;

    public Ama(String title, User subject, boolean isPublic) {
        this.title = title;
        this.subject = subject;
        this.isPublic = isPublic;
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
}
