package com.ngothanhduy.showroom.services;

import java.util.List;

public interface Repo {
    boolean checkLogin(String username, String password);
    CompanyName loadCompanyName();
    void saveCompanyName(CompanyName companyName);
    String loadAboutContent();
    void saveAboutContent(String aboutContent);

    // P5 mới thêm:
    List<Product> loadProducts();
    int insertProduct(String name);
}