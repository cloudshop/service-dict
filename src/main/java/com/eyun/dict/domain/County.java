package com.eyun.dict.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A County.
 */
@Entity
@Table(name = "county")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class County implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "pinyin")
    private String pinyin;

    @ManyToOne
    private City city;

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

    public County name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public County pinyin(String pinyin) {
        this.pinyin = pinyin;
        return this;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public City getCity() {
        return city;
    }

    public County city(City city) {
        this.city = city;
        return this;
    }

    public void setCity(City city) {
        this.city = city;
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
        County county = (County) o;
        if (county.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), county.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "County{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", pinyin='" + getPinyin() + "'" +
            "}";
    }
}
