package com.example.springdemo.entity;


import java.io.Serializable;

@lombok.Data
public class FindPlaceResult implements Serializable {
    private Candidate[] candidates;

    @lombok.Data
    public static class Candidate {
        private Geometry geometry;
    }
}
