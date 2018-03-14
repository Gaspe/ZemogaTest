package com.example.zemogatest.models;

public class Address {

    private String street;
    private String suite;
    private String city;
    private String zipcode;
    private Geo geo;

    public String getStreet() {
        return street;
    }

    public String getSuite() {
        return suite;
    }

    public String getCity() {
        return city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public Geo getGeo() {
        return geo;
    }

    public class Geo {
        String lat;
        String lng;

        public String getLat() {
            return lat;
        }

        public String getLng() {
            return lng;
        }
    }

}
