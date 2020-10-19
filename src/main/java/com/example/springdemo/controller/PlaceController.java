package com.example.springdemo.controller;

import com.example.springdemo.models.AttractionsResult;
import com.example.springdemo.models.AttractionDetail;
import com.example.springdemo.models.response.BaseResponse;
import com.example.springdemo.rpc.GoogleMapClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * the controller is defined to get the information of places
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class PlaceController {
    /**
     * Return tourist attractions of the input city
     */
    @GetMapping ("/attractions")
    @Cacheable(value="SecondsCache", keyGenerator = "customKeyGenerator")
    public BaseResponse<AttractionsResult> getNearbyPlaces(@RequestParam("city") String city) {
        GoogleMapClient client = new GoogleMapClient();

        Set<String> allTypes = new HashSet<>();
        try {
            AttractionsResult res = client.getAttractionsResult(city);
            for (int i = 0; i < res.getResults().length; i++) {
                AttractionsResult.Attraction cur = res.getResults()[i];

                cur.setKey(i);

                List<String> originTypes = res.getResults()[i].getTypes();
                List<String> modifiedTypes = new ArrayList<>(originTypes);
                HashSet<String> set = new HashSet<>(
                        Arrays.asList("tourist_attraction","point_of_interest",
                                "establishment","general_contractor", "premise"));

                modifiedTypes.removeIf(set::contains);

                for (int j = 0; j < modifiedTypes.size(); j++) {
                    String modifiedType = modifiedTypes.get(j).replace("_", " ");
                    modifiedTypes.set(j, modifiedType);
                    allTypes.add(modifiedType);
                }

                if (modifiedTypes.size() == 0) modifiedTypes.add("attraction");
                cur.setTypes(modifiedTypes);
            }
            res.setAllTypes(new ArrayList<>(allTypes));
            return new BaseResponse<>("200", res, "");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Return the detail of a place
     * @param id: returned by the google Map API in getNearbyPlaces
     */
    @GetMapping("/detail")
    @Cacheable(value="place", keyGenerator = "customKeyGenerator")
    public BaseResponse<AttractionDetail> getPlaceDetail(@RequestParam("id") String id) {
        GoogleMapClient client = new GoogleMapClient();
        try {
            AttractionDetail res = client.getPlaceDetailResult(id);
            return new BaseResponse<>("200", res, "");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
