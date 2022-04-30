package com.profile.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "institution")
    private String institution;

    @Column(name = "position")
    private String position;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "from_date")
    private Date fromDate;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "to_date")
    private Date toDate;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ExperienceType type;

    public Experience() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getInstitution() {
        return institution;
    }

    public String getPosition() {
        return position;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public String getDescription() {
        return description;
    }

    public ExperienceType getType() {
        return type;
    }
}
