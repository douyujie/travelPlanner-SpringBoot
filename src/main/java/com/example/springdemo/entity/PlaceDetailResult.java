package com.example.springdemo.entity;


import java.io.Serializable;

@lombok.Data
public class PlaceDetailResult {
    private String status;
    private Data Result;

    @lombok.Data
    public static class Data implements Serializable {
        private String name;
        private Geometry geometry;
        private String business_status;

        private String formatted_address;
        private String formatted_phone_number;
        private String international_phone_number;
        private Photo[] photos;
        private OpeningHours opening_hours;

        private String icon;
        private String[] types;

        private int price_level;
        private Double rating;
        private int user_ratings_total;
        private Review[] reviews;

        private String url;
        private String website;
    }

    @lombok.Data
    public static class OpeningHours implements Serializable {
        private boolean opening;
        private String[] weekday_text;
    }

    @lombok.Data
    public static class Photo implements Serializable {
        private double height;
        private double width;
        private String photo_reference;
    }

    @lombok.Data
    public static class Review implements Serializable {
        private String author_name;
        private String language;
        private double rating;
        private String relative_time_description;
        String text;
        long time;
    }
}
