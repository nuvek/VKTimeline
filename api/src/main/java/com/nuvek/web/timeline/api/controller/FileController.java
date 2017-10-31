/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nuvek.web.timeline.api.controller;

import com.nuvek.web.timeline.api.service.TimelineService;
import java.util.Collections;
import java.util.Map;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
public class FileController {
    @Autowired
    TimelineService timelineService;
    
    @GetMapping("/{id}/thumbnail")
    public @ResponseBody ResponseEntity getThumbnail(@PathVariable(value = "id") Long fileId) {
        Map<String, Object> thumbnailData = timelineService.getThumbnail(fileId);
        return getResponseFromFileData(thumbnailData);
    }
    
    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity getFile(@PathVariable(value = "id") Long fileId) {
        Map<String, Object> fileData = timelineService.getFile(fileId);
        return getResponseFromFileData(fileData);
    }
    
    private ResponseEntity getResponseFromFileData(Map<String, Object> fileData) {
        ResponseEntity respEntity = null;
        if(fileData != null) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("content-disposition", "inline; filename=" + 
                fileData.get("filename").toString());
            responseHeaders.add("Content-Type", fileData.get("contentType").toString());

            respEntity = new ResponseEntity((byte[])fileData.get("content"), responseHeaders,HttpStatus.OK);
        } else
            respEntity = new ResponseEntity ("File Not Found", HttpStatus.NOT_FOUND);
        return respEntity;
    }
    
    @PostMapping("/upload")
    public Map<String, String> uploadFile(@RequestParam("file") MultipartFile file) {
        String filePath = timelineService.saveUploadedFile(file);
        return Collections.singletonMap("path", filePath);
    }
}
