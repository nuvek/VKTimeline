/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nuvek.web.timeline.api.service;

import com.nuvek.web.timeline.api.repository.FileRepository;
import com.nuvek.web.timeline.api.repository.TimelineItemRepository;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TimelineService {
    private static final String PATH_DEFAULT = "/api/file/";
    private String _defaultPath;
    
    @Autowired
    FileRepository fileRepository;
    
    public TimelineService() {
        _defaultPath = String.format("%2$s%1$sTimeline%1$sAttachments%1$s%3$s%1$s%3$s.jpg", 
                        File.separator, System.getProperty("user.home"), 0);
    }
    
    public String saveUploadedFile(MultipartFile file) {
        if(!file.isEmpty()){
            try {
                byte[] bytes = file.getBytes();
                String rootPath = System.getProperty("user.home");
                Date currentDate = new Date();
                String dateDirectoryFormat = String.format("yyyy%1$sMM%1$sdd%1$s", 
                        File.separator);
                SimpleDateFormat sdf = new SimpleDateFormat(dateDirectoryFormat);
                String dateValue = sdf.format(currentDate) + currentDate.getTime();
                String dirPath = String.format("%2$s%1$sTimeline%1$sAttachments%1$s%3$s", 
                        File.separator, rootPath, dateValue);
                File dir = new File(dirPath);
                if (!dir.exists())
                    dir.mkdirs();
                File serverFile = new File(dirPath + File.separator + file.getOriginalFilename());
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
                Long fileId = saveFileOnDb(file, serverFile.getAbsolutePath());
                return PATH_DEFAULT + fileId;
            }catch(Exception e) {
                return "ERROR|" + e.getMessage();
            }
        }
        return null;
    }
    
    public Long saveFileOnDb(MultipartFile file, String filePath) {
        com.nuvek.web.timeline.api.model.File timelineFile = new com.nuvek.web.timeline.api.model.File();
        timelineFile.setName(file.getOriginalFilename());
        timelineFile.setPath(filePath);
        timelineFile.setSize(file.getSize());
        timelineFile.setType(file.getContentType());
        fileRepository.save(timelineFile);
        return timelineFile.getId();
    }
    
    private File getFileById(Long fileId) {
        if(fileId < 0)
            return new File(_defaultPath);
        com.nuvek.web.timeline.api.model.File timelineFile = fileRepository.findOne(fileId);
        if(timelineFile == null)
            return new File(_defaultPath);
        return new File(timelineFile.getPath());
    }
    
    public Map<String, Object> getFile(Long fileId) {
        return getFileData(getFileById(fileId));
    }
    
    public Map<String, Object> getThumbnail(Long fileId) {
        File file = getFileById(fileId);
        if(file == null)
            return null;
        String filePath = file.getAbsolutePath();
        int lastPeriodPosition = filePath.lastIndexOf('.');
        String thumbnailPath = filePath.substring(0, lastPeriodPosition + 1)
                + "100x100.jpg";
        File thumbnail = new File(thumbnailPath);
        if(!thumbnail.exists()) {
            try {
                thumbnail = createThumbnail(filePath, thumbnailPath);
            }catch(Exception ex) {
                return null;
            }
        }
        
        return getFileData(thumbnail);
    }
    
    private Map<String, Object> getFileData(File file) {
        byte[] imageBytes = null;
        try {
            InputStream inputStream = new FileInputStream(file);
            imageBytes = new byte[(int)file.length()];
            inputStream.read(imageBytes);
        }catch(Exception ex) {
            return null;
        }
        Map<String, Object> thumbnailData = new HashMap<String, Object>();
        thumbnailData.put("filename", file.getName());
        try {
            thumbnailData.put("contentType", Files.probeContentType(file.toPath()));
        }catch(Exception ex) {
            thumbnailData.put("contentType", "image/jpeg");
        }
        thumbnailData.put("content", imageBytes);
        return thumbnailData;
    }
    
    private File createThumbnail(String filePath, String thumbnailPath) throws IOException {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        img.createGraphics().drawImage(ImageIO.read(new File(filePath)).getScaledInstance(100, 100, Image.SCALE_SMOOTH),0,0,null);
        File thumbnailFile = new File(thumbnailPath);
        ImageIO.write(img, "jpg", thumbnailFile);
        return thumbnailFile;
    }
}
