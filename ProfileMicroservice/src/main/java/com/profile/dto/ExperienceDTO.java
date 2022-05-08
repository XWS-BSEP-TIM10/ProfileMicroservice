package com.profile.dto;

import com.profile.model.Experience;

public class ExperienceDTO {

    private Long id;
    
    private String institution;

    private String position;

    private String fromDate;

    private String toDate;

    private String description;

    private String type;

    public ExperienceDTO() {
    }

    public ExperienceDTO(Experience experience) {
        this.id = experience.getId();
        this.position = experience.getPosition();
        this.fromDate = experience.getFromDate().toString();
        this.toDate = experience.getToDate().toString();
        this.description = experience.getDescription();
        this.type = experience.getType().toString();
        this.institution = experience.getInstitution();
    }



	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



	public String getInstitution() {
		return institution;
	}



	public void setInstitution(String institution) {
		this.institution = institution;
	}
    
    
}
