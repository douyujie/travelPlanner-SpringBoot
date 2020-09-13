package com.example.springdemo.rpc;

import com.example.springdemo.entity.*;
import com.example.springdemo.entity.PlaceDetailResult;
import org.springframework.web.client.RestTemplate;


public class GoogleMapClient {
    private static final String KEY = "AIzaSyCix0Yrl0MXuF5y7LBrYFO2ev6x09VDjfg";
    private static final String TYPE = "tourist_attraction";
    private static final int LIMIT = 60;

    private static final String FIND_PLACE_URL =
            "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?" +
                    "input=%s&" +
                    "inputtype=textquery&" +
                    "fields=geometry&" +
                    "key=%s";

    private static final String NEARBY_SEARCH_URL =
            "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                    "location=%s,%s&" +
                    "radius=%s&" +
                    "type=%s&" +
                    "key=%s";

    private static final String NEARBY_SEARCH_NEXT_PAGE_URL =
            "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                    "pagetoken=%s&" +
                    "key=%s";

    private static final String PLACE_DETAIL_SEARCH_URL =
            "https://maps.googleapis.com/maps/api/place/details/json?" +
                    "place_id=%s&" +
                    "key=%s";

    private RestTemplate restTemplate;

    public GoogleMapClient() {
        restTemplate = new RestTemplate();
    }


    public static void main(String[] args) {
        GoogleMapClient c = new GoogleMapClient();
    }


    public NearbySearchResult.Data[] getNearbyResult(String city) {
        Geometry geometry = getCityInfo(city);

        double centerLat = geometry.getLocation().getLat();
        double centerLng = geometry.getLocation().getLng();

        double northEastLat = geometry.getViewport().getNortheast().getLat();
        double northEastLng = geometry.getViewport().getNortheast().getLng();

        double southWestLat = geometry.getViewport().getSouthwest().getLat();
        double southWestLng = geometry.getViewport().getSouthwest().getLng();

        int dist1 = calcDist(centerLat, centerLng, northEastLat, northEastLng);
        int dist2 = calcDist(centerLat, centerLng, southWestLat, southWestLng);

        int radius = (dist1 + dist2) / 2;

        return getNearbyResult(centerLat, centerLng, radius);
    }

    private Geometry getCityInfo(String city) {
        String url = String.format(FIND_PLACE_URL, city, KEY);
        System.out.println(url);
        FindPlaceResult res = restTemplate.getForObject(url, FindPlaceResult.class);
        FindPlaceResult.Candidate[] c = res.getCandidates();
        if (c.length == 0) {
            return null;
        }
        return c[0].getGeometry();
    }

    private int calcDist(double lat1, double lng1, double lat2, double lng2) {
        return 20000;
    }

    private NearbySearchResult.Data[] getNearbyResult(double lat, double lng, int radius) {
        NearbySearchResult.Data[] data = new NearbySearchResult.Data[LIMIT];
        String url = String.format(NEARBY_SEARCH_URL, lat, lng, radius, TYPE, KEY);
        System.out.println("calling: " + url);
        NearbySearchResult res = restTemplate.getForObject(url, NearbySearchResult.class);

        int k = 0;
        for (NearbySearchResult.Data d : res.getResults()) {
            data[k++] = d;
        }

        for (int i = 0; i < 2; i++) {
            String token = res.getNext_page_token();
            url = String.format(NEARBY_SEARCH_NEXT_PAGE_URL, token, KEY);
            System.out.println("calling: " + url);
            res = restTemplate.getForObject(url, NearbySearchResult.class);
            System.out.println(res.getStatus());
            for (NearbySearchResult.Data d : res.getResults()) {
                data[k++] = d;
            }
        }
        return data;
    }

    public PlaceDetailResult.Data getDetailResult(String place_id) {
        String url = String.format(PLACE_DETAIL_SEARCH_URL, place_id, KEY);
        System.out.println("calling: " + url);
        PlaceDetailResult res = restTemplate.getForObject(url, PlaceDetailResult.class);
        return res.getResult();
    }
}


