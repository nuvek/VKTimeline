/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nuvek.web.timeline.api.repository;

import com.nuvek.web.timeline.api.model.TimelineItem;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimelineItemRepository extends JpaRepository<TimelineItem, Long> {
    
}
