/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nuvek.web.timeline.api.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nuvek.web.timeline.api.util.DateSerializer;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="timelineItem")
@EntityListeners(AuditingEntityListener.class)
public class TimelineItem implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    @NotBlank
    private String title;
    
    @NotBlank
    private String eventDescription;
    
    @Column(nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDate;
    
    private String imagePath;
    
    @Column(name="`references`")
    private String references;
    
    public TimelineItem() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    @JsonSerialize(using=DateSerializer.class)
    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }
    
    
}
