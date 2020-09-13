package com.example.springdemo.controller;

import com.example.springdemo.entity.NearbySearchResult;
import com.example.springdemo.entity.PlaceDetailResult;
import com.example.springdemo.rpc.GoogleMapClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class PlaceController {
    @GetMapping ("/attractions")
    @Cacheable(value="city", keyGenerator = "customKeyGenerator")
    public NearbySearchResult.Data[] getNearbyPlaces(@RequestParam("city") String city) {
        GoogleMapClient client = new GoogleMapClient();
        return client.getNearbyResult(city);
    }

    @GetMapping("/detail")
    @Cacheable(value="place", keyGenerator = "customKeyGenerator")
    public PlaceDetailResult.Data getPlaceDetail(@RequestParam("id") String id) {
        GoogleMapClient client = new GoogleMapClient();
        return client.getDetailResult(id);
    }
}
