package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Duration;

/**
 * A Activity.
 */
@Entity
@Table(name = "activity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "cost")
    private String cost;

    @Column(name = "min_participants")
    private Integer minParticipants;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "time")
    private Duration time;

    @Column(name = "location")
    private String location;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Activity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Activity description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCost() {
        return this.cost;
    }

    public Activity cost(String cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public Integer getMinParticipants() {
        return this.minParticipants;
    }

    public Activity minParticipants(Integer minParticipants) {
        this.setMinParticipants(minParticipants);
        return this;
    }

    public void setMinParticipants(Integer minParticipants) {
        this.minParticipants = minParticipants;
    }

    public Integer getMaxParticipants() {
        return this.maxParticipants;
    }

    public Activity maxParticipants(Integer maxParticipants) {
        this.setMaxParticipants(maxParticipants);
        return this;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Duration getTime() {
        return this.time;
    }

    public Activity time(Duration time) {
        this.setTime(time);
        return this;
    }

    public void setTime(Duration time) {
        this.time = time;
    }

    public String getLocation() {
        return this.location;
    }

    public Activity location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Activity)) {
            return false;
        }
        return getId() != null && getId().equals(((Activity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Activity{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", cost='" + getCost() + "'" +
            ", minParticipants=" + getMinParticipants() +
            ", maxParticipants=" + getMaxParticipants() +
            ", time='" + getTime() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
