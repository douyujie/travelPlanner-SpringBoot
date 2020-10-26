package com.example.springdemo.models;

import java.util.List;

@lombok.Data
public class UserPlanData {

    private String username;
    private static final int mod = (int) (Math.pow(2, 31) - 1);
    List<PlanData> planDataList;

    @lombok.Data
    public static class PlanData {
        private long planId;
        private String planName;
        private String city;
        private long cityId;

        private List<RouteData> routeDataList;

        @Override
        public int hashCode() {
            long res = 0;
            for (RouteData route : routeDataList) {
                res = (res + route.hashCode() + mod) % mod;
            }
            res = (res + city.hashCode() + mod) % mod;
            res = (res + planName.hashCode() + mod) % mod;
            return (int) res;
        }
    }

    @lombok.Data
    public static class RouteData {
        long routeId;
        int day;
        private List<AttractionData> attractionDataList;

        @Override
        public int hashCode() {
            long res = day;
            for (int i = 0; i < attractionDataList.size(); i++) {
                AttractionData attraction = attractionDataList.get(i);
                res = (res + attraction.hashCode() * (i + 1) + mod) % mod;
            }
            return (int) res;
        }
    }

    @lombok.Data
    public static class AttractionData {
        private String attractionName;
        private long attactionId;
        private Geometry geometry;
        private String type;
        private float rating;

        @Override
        public int hashCode() {
            return  ((int)(geometry.getLocation().getLat() * 1000001L + geometry.getLocation().getLng()) + mod) % mod;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof AttractionData) {
                AttractionData attractionData = (AttractionData) o;
                return attractionData.getGeometry().getLocation().getLat() == this.getGeometry().getLocation().getLat()
                        &&
                        attractionData.getGeometry().getLocation().getLng() == this.getGeometry().getLocation().getLng();
            }
            return false;
        }
    }
}
