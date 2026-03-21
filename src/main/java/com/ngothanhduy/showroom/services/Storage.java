package com.ngothanhduy.showroom.services;

import org.springframework.web.multipart.MultipartFile;

public interface Storage {
    void saveFile(MultipartFile file, String path);
}