package org.sysc.ama.model;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.jsoup.Jsoup;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;

import javax.validation.constraints.Past;

import java.time.Instant;
import java.util.Date;

@Entity
public class Post {

    @Id
    @GeneratedValue
    protected Long id;

    @ManyToOne
    private User author;

    @Past
    private Date created;

    private Date updated;

    @NotBlank
    private String body;

    private boolean edited;

    @ManyToOne
    private Ama ama;

    public Post () {}

    public Post (User author, Ama ama, String body) {
        this.author = author;
        this.ama    = ama;
        this.body  = Jsoup.clean(body, Whitelist.simpleText());

        // After running into many issues during testing, it appears that if we use the default Date() constructor,
        // the @Past constraint on created date is sometimes violated. The default constructor is deprecated

        this.created = Date.from(Instant.now().minusMillis(1));
        this.updated = new Date();
        this.edited = false;
    }

    public Long getId () {
        return this.id;
    }

    public boolean isEdited () {
        return this.edited;
    }

    public User getAuthor () {
        return this.author;
    }

    public Date getCreated () {
        return this.created;
    }

    public Ama getAma () {
        return this.ama;
    }

    public String getBody () {
        return this.body;
    }

    public void setBody (String body) {
        this.edited = true;
        this.body = Jsoup.clean(body, Whitelist.simpleText());
    }

    public void setUpdated (Date updated) {
        this.updated = updated;
    }

    public Date getUpdated () {
        return this.updated;
    }

}
