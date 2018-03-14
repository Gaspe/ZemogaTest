package com.example.zemogatest.requestImpl;


class Config {
    static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    final static ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
}
