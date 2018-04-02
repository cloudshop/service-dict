package com.eyun.dict.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Industry.
 */
@Entity
@Table(name = "industry")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Industry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "createdtime")
    private Instant createdtime;

    @Column(name = "modifiedtime")
    private Instant modifiedtime;

    @Column(name = "modifiedby")
    private Long modifiedby;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "status")
    private Integer status;

    @OneToMany(mappedBy = "child")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Industry> parents = new HashSet<>();

    @ManyToOne
    private Industry child;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Industry name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public Industry code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getCreatedtime() {
        return createdtime;
    }

    public Industry createdtime(Instant createdtime) {
        this.createdtime = createdtime;
        return this;
    }

    public void setCreatedtime(Instant createdtime) {
        this.createdtime = createdtime;
    }

    public Instant getModifiedtime() {
        return modifiedtime;
    }

    public Industry modifiedtime(Instant modifiedtime) {
        this.modifiedtime = modifiedtime;
        return this;
    }

    public void setModifiedtime(Instant modifiedtime) {
        this.modifiedtime = modifiedtime;
    }

    public Long getModifiedby() {
        return modifiedby;
    }

    public Industry modifiedby(Long modifiedby) {
        this.modifiedby = modifiedby;
        return this;
    }

    public void setModifiedby(Long modifiedby) {
        this.modifiedby = modifiedby;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Industry deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getStatus() {
        return status;
    }

    public Industry status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Set<Industry> getParents() {
        return parents;
    }

    public Industry parents(Set<Industry> industries) {
        this.parents = industries;
        return this;
    }

    public Industry addParent(Industry industry) {
        this.parents.add(industry);
        industry.setChild(this);
        return this;
    }

    public Industry removeParent(Industry industry) {
        this.parents.remove(industry);
        industry.setChild(null);
        return this;
    }

    public void setParents(Set<Industry> industries) {
        this.parents = industries;
    }

    public Industry getChild() {
        return child;
    }

    public Industry child(Industry industry) {
        this.child = industry;
        return this;
    }

    public void setChild(Industry industry) {
        this.child = industry;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Industry industry = (Industry) o;
        if (industry.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), industry.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Industry{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", createdtime='" + getCreatedtime() + "'" +
            ", modifiedtime='" + getModifiedtime() + "'" +
            ", modifiedby=" + getModifiedby() +
            ", deleted='" + isDeleted() + "'" +
            ", status=" + getStatus() +
            "}";
    }
}
