/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nuvek.web.timeline.api.controller;

import com.nuvek.web.timeline.api.model.TimelineItem;
import com.nuvek.web.timeline.api.repository.TimelineItemRepository;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/timeline")
public class TimelineController {
    @Autowired
    TimelineItemRepository timelineItemRepository;
    
    // Get All Timeline Items
    @GetMapping("/items")
    public List<TimelineItem> getAllItems() {
        return timelineItemRepository.findAll();
    }
    
    // Create a new Timeline Item
    @PostMapping("/items")
    public TimelineItem createItem(@Valid @RequestBody TimelineItem item) {
        return timelineItemRepository.save(item);
    }
    
    // Get a Single Timeline Item by Id
    @GetMapping("/items/{id}")
    public ResponseEntity<TimelineItem> getTimelineItemById(@PathVariable(value = "id") Long itemId){
        TimelineItem item = timelineItemRepository.findOne(itemId);
        if(item == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(item);
    }
    
    // Update a Timeline Item
    @PutMapping("/items")
    public ResponseEntity<TimelineItem> updateTimelineItem(@Valid @RequestBody TimelineItem item){
        if(timelineItemRepository.findOne(item.getId()) == null)
            return ResponseEntity.notFound().build();
        item = timelineItemRepository.save(item);
        return ResponseEntity.ok(item);
    }
    
    @DeleteMapping("/items/{id}")
    public ResponseEntity<TimelineItem> deleteTimelineItem(@PathVariable(value = "id") Long itemId){
        TimelineItem item = timelineItemRepository.findOne(itemId);
        if(item == null)
            return ResponseEntity.notFound().build();
        timelineItemRepository.delete(item);
        return ResponseEntity.ok().build();
    }
}
