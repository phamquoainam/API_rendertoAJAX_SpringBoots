package com.hoainam.service;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadService {

    public String handleSaveUploadFile(MultipartFile file, String targetFolder) {
        if (file.isEmpty()) {
            return null;
        }

        String finalName = "";
        try {
            String projectDir = System.getProperty("user.dir");

            String uploadPath = projectDir + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "uploads" + File.separator + targetFolder;

            File dir = new File(uploadPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();

            File serverFile = new File(dir.getAbsolutePath() + File.separator + finalName);
            file.transferTo(serverFile);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return targetFolder + "/" + finalName;
    }
}