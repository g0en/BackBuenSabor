package com.entidades.buenSabor.presentation.rest;

import com.entidades.buenSabor.business.service.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/imagenes")
@CrossOrigin("*")
public class ImagenController {
    @Autowired
    private ImagenService imagenService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImages(
            @RequestParam(value = "uploads", required = true) MultipartFile[] files) {
        try {
            return this.imagenService.uploadImages(files);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/deleteImg")
    public ResponseEntity<String> deleteById(@RequestParam("publicId") String publicId, @RequestParam("id") Long id) {
        try {
            //UUID uuid = UUID.fromString(uuidString);
            return this.imagenService.deleteImage(publicId, id);
        } catch (IllegalArgumentException e) {
            // UUID.fromString lanzará una IllegalArgumentException si la cadena no es un UUID válido
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid UUID format");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred");
        }
    }

    //TRAE TODAS LAS IMAGENES EN LA TABAL IMAGENES
    @GetMapping("/getImages")
    public ResponseEntity<List<Map<String, Object>>> getAll() {
        try {
            return this.imagenService.getAllImages();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    };
}