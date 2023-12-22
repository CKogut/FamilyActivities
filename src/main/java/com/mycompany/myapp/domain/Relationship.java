package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Relationship.
 */
@Entity
@Table(name = "relationship")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Relationship implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "liked")
    private Boolean liked;

    @Column(name = "shared")
    private Boolean shared;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Activity description;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Relationship id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getLiked() {
        return this.liked;
    }

    public Relationship liked(Boolean liked) {
        this.setLiked(liked);
        return this;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Boolean getShared() {
        return this.shared;
    }

    public Relationship shared(Boolean shared) {
        this.setShared(shared);
        return this;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Relationship user(User user) {
        this.setUser(user);
        return this;
    }

    public Activity getDescription() {
        return this.description;
    }

    public void setDescription(Activity activity) {
        this.description = activity;
    }

    public Relationship description(Activity activity) {
        this.setDescription(activity);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Relationship)) {
            return false;
        }
        return getId() != null && getId().equals(((Relationship) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Relationship{" +
            "id=" + getId() +
            ", liked='" + getLiked() + "'" +
            ", shared='" + getShared() + "'" +
            "}";
    }
}
