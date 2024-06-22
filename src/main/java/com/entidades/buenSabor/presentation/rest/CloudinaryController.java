package com.entidades.buenSabor.presentation.rest;

import com.entidades.buenSabor.business.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/cloudinary")
public class CloudinaryController {
    @Autowired
    private CloudinaryService cloudinaryService;

    /*@PostMapping("/upload")
    public Map upload(@RequestParam("file") MultipartFile file) throws IOException {
        return cloudinaryService.uploadFile(file);
    }

    @DeleteMapping("/delete")
    public Map delete(@RequestParam("public_id") String publicId) {
        return cloudinaryService.deleteImage(publicId);
    }*/
}
