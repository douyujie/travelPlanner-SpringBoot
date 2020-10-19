package com.example.springdemo.models;

import java.io.Serializable;


/**
 * This class includes three coordinates that
 * describe a place: center, northeast and southwest
 */
@lombok.Data
public class Geometry implements Serializable {
    private Location location;
    private Viewport viewport;

    @lombok.Data
    public static class Location implements Serializable {
        private double lat;
        private double lng;
    }

    @lombok.Data
    public static class Viewport implements Serializable {
        private Location northeast;
        private Location southwest;
    }
}
