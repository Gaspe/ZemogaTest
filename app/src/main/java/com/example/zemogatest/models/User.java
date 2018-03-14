package com.example.zemogatest.models;

public class User {
    private int id;
    private Address address;
    private Company company;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;

    public int getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public Company getCompany() {
        return company;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }
}
