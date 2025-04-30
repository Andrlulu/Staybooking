package com.laioffer.staybooking.storage;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ImageStorageService {
// interact with GCP

    public String upload(MultipartFile file) {
        // TODO: upload image to GCS
        return "https://storage.googleapis.com/staybooking/placeholder.jpeg";
    }
}
