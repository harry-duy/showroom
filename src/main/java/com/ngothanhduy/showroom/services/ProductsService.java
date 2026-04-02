package com.ngothanhduy.showroom.services;

import com.ngothanhduy.showroom.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductsService {
    private final Repo repo;
    private final Storage storage;

    public ProductsService(Repo repo, Storage storage) {
        this.repo = repo;
        this.storage = storage;
    }

    public List<Product> getProducts() {
        return repo.loadProducts();
    }

    public void addProduct(String name, MultipartFile image) throws Exception {
        // 1. Kiểm tra tên
        if (StringUtils.isNullOrBlank(name)) {
            throw new Exception("Product name cannot be null or blank");
        }

        // 2. Kiểm tra trùng tên (Do It Yourself #3)
        var exists = repo.loadProducts().stream()
                .anyMatch(p -> p.name().equalsIgnoreCase(name.trim()));
        if (exists) {
            throw new Exception("Product name already exists");
        }

        // 3. Kiểm tra image
        if (image == null || image.isEmpty()) {
            throw new Exception("Product image cannot be null or empty");
        }
        var contentType = image.getContentType();
        if (!"image/jpeg".equals(contentType)) {
            throw new Exception("Product image must be in JPG format");
        }
        if (image.getSize() > 1024 * 1024) {
            throw new Exception("Product image cannot be bigger than 1MB");
        }

        // 4. Lưu vào DB và storage (name.trim() = Do It Yourself #1)
        var id = repo.insertProduct(name.trim());
        storage.saveFile(image, "products/" + id + ".jpg");
    }
}