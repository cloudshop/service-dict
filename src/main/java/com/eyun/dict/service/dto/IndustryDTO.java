package com.eyun.dict.service.dto;


import java.time.Instant;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Industry entity.
 */
public class IndustryDTO implements Serializable {

    private Long id;

    private String name;

    private String code;

    private Instant createdtime;

    private Instant modifiedtime;

    private Long modifiedby;

    private Boolean deleted;

    private Integer status;

    private Long childId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Instant createdtime) {
        this.createdtime = createdtime;
    }

    public Instant getModifiedtime() {
        return modifiedtime;
    }

    public void setModifiedtime(Instant modifiedtime) {
        this.modifiedtime = modifiedtime;
    }

    public Long getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(Long modifiedby) {
        this.modifiedby = modifiedby;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long industryId) {
        this.childId = industryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IndustryDTO industryDTO = (IndustryDTO) o;
        if(industryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), industryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IndustryDTO{" +
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
