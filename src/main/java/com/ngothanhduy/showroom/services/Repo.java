package com.ngothanhduy.showroom.services;

public interface Repo {
    boolean checkLogin(String username, String password);
    CompanyName loadCompanyName();
    void saveCompanyName(CompanyName companyName);
}