package com.example.springdemo.entity;


import java.io.Serializable;

@lombok.Data
public class NearbySearchResult {
    private String status;
    private String next_page_token;
    private Data[] results;

    @lombok.Data
    public static class Data implements Serializable {
        private String business_status;
        private Geometry geometry;
        private String icon;
        private String name;
        private String place_id;
        private String[] types;
    }
}
