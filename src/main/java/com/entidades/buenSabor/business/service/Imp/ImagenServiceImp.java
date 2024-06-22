package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.CloudinaryService;
import com.entidades.buenSabor.business.service.ImagenService;
import com.entidades.buenSabor.domain.entities.ImagenArticulo;
import com.entidades.buenSabor.repositories.ImagenArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ImagenServiceImp implements ImagenService {
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ImagenArticuloRepository imageRepository;


    @Override
    public ResponseEntity<List<Map<String, Object>>> getAllImages() {
        try {
            List<ImagenArticulo> images = imageRepository.findAll();
            List<Map<String, Object>> imageList = new ArrayList<>();


            for (ImagenArticulo image : images) {
                Map<String, Object> imageMap = new HashMap<>();
                imageMap.put("id", image.getId());
                imageMap.put("url", image.getUrl());
                imageList.add(imageMap);
            }


            return ResponseEntity.ok(imageList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @Override
    public ResponseEntity<String> uploadImages(MultipartFile[] files) {
        List<String> urls = new ArrayList<>();
        try {
            for (MultipartFile file : files) {


                if (file.getName().isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }


                ImagenArticulo image = new ImagenArticulo();
                image.setUrl(cloudinaryService.uploadFile(file));
                if (image.getUrl() == null) {
                    return ResponseEntity.badRequest().build();
                }


                // Agregar la URL a la lista de URLs
                urls.add(image.getUrl());
                imageRepository.save(image);
            };


            // Convertir la lista de URLs a un array de strings y devolver como JSON
            return new ResponseEntity<>("{\"status\":\"OK\", \"urls\":" + urls + "}", HttpStatus.OK);


        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("{\"status\":\"ERROR\", \"message\":\"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
        }


    }
    @Override
    public ResponseEntity<String> deleteImage(String publicId, UUID idBd) {
        Long id = idBd.getMostSignificantBits();
        try {
            imageRepository.deleteById(id);
            return cloudinaryService.deleteImage(publicId, idBd);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("{\"status\":\"ERROR\", \"message\":\"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
        }
    }
}