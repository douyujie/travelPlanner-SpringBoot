package com.example.springdemo.rpc;

import com.example.springdemo.models.AttractionDetail;
import com.example.springdemo.models.AttractionsResult;
import com.example.springdemo.models.Geometry;
import com.example.springdemo.models.PlaceCoordinate;
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

    private final RestTemplate restTemplate;

    public GoogleMapClient() {
        restTemplate = new RestTemplate();
    }


    public static void main(String[] args) {
        GoogleMapClient c = new GoogleMapClient();
    }


    /**
     * @return the tourist attractions which are stored in an AttractionsResult object.
     * The main logic is in the overloaded function below. In this method, we get the
     * coordinates of center and calculate for radius which are required by the overloaded
     * method.
     */
    public AttractionsResult getAttractionsResult(String city) {
        Geometry geometry = getPlaceCoordinate(city);

        double centerLat = geometry.getLocation().getLat();
        double centerLng = geometry.getLocation().getLng();

        double northEastLat = geometry.getViewport().getNortheast().getLat();
        double northEastLng = geometry.getViewport().getNortheast().getLng();

        double southWestLat = geometry.getViewport().getSouthwest().getLat();
        double southWestLng = geometry.getViewport().getSouthwest().getLng();

        int dist1 = calcDist(centerLat, centerLng, northEastLat, northEastLng);
        int dist2 = calcDist(centerLat, centerLng, southWestLat, southWestLng);

        int radius = (dist1 + dist2) / 2;

        return getAttractionsResult(centerLat, centerLng, radius);
    }

    /**
     * @return the geometry of the city, including center/northeast/southwest coordinates.
     * Attention, there may be several candidates, we only return the first one, if any
     * else return null.
     */
    private Geometry getPlaceCoordinate(String city) {
        String url = String.format(FIND_PLACE_URL, city, KEY);
        System.out.println(url);
        PlaceCoordinate res = restTemplate.getForObject(url, PlaceCoordinate.class);
        PlaceCoordinate.Candidate[] candidates = res.getCandidates();
        if (candidates.length == 0) return null;
        return candidates[0].getGeometry();
    }

    // TODO this function should be re-implemented
    /**
     * @return the spherical distance between two coordinates
     */
    private int calcDist(double lat1, double lng1, double lat2, double lng2) {
        return 20000;
    }


    /**
     * @param lat: latitude of the center of the city
     * @param lng: longitude of the center of the city
     * @param radius: radius of the city
     * @return the tourist attractions in the city which are stored in the AttractionResult
     * object
     */
    private AttractionsResult getAttractionsResult(double lat, double lng, int radius) {
        String url = String.format(NEARBY_SEARCH_URL, lat, lng, radius, TYPE, KEY);
        System.out.println("calling: " + url);
        AttractionsResult res = restTemplate.getForObject(url, AttractionsResult.class);
        return res;
    }

    /**
     * @param place_id: this info is returned by Google Map API for each
     * tourist attraction stored in AttractionsResult
     * @return the detail information of a tourist attractions, which is
     * stored in an AttractionDetail object
     */
    public AttractionDetail getPlaceDetailResult(String place_id) {
        String url = String.format(PLACE_DETAIL_SEARCH_URL, place_id, KEY);
        System.out.println("calling: " + url);
        return restTemplate.getForObject(url, AttractionDetail.class);
    }

}



