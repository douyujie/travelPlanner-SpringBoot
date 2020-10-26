package com.example.springdemo.controller;

import com.example.springdemo.models.Geometry;
import com.example.springdemo.models.UserPlanData;
import com.example.springdemo.models.entity.*;
import com.example.springdemo.models.response.BaseResponse;
import com.example.springdemo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class PlanController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    PlanRouteRepository planRouteRepository;

    @Autowired
    AttractionRepository attractionRepository;

    @Autowired
    RouteAttractionRepository routeAttractionRepository;

    @Autowired
    UserPlanRepository userPlanRepository;

    @PostMapping(value = "/addplan")
    public BaseResponse<String> addPlan(@RequestBody UserPlanData userPlanData) {
        try {
            String username = userPlanData.getUsername();
            UserPlanData.PlanData planData = userPlanData.getPlanDataList().get(0);
            String planName = planData.getPlanName();
            String cityName = planData.getCity();
            List<UserPlanData.RouteData> routeDataList = planData.getRouteDataList();

            City city = cityRepository.findByName(cityName);
            if (city == null) {
                city = new City();
                city.setName(cityName);
                cityRepository.save(city);
            }

            String planHashCode = String.valueOf(planData.hashCode());
            Plan plan = planRepository.findByHashcode(planHashCode);
            boolean planExists = true;
            if (plan == null) {
                planExists = false;
                plan = new Plan();
                plan.setCity(city);
                plan.setName(planName);
                plan.setHashcode(planHashCode);
                planRepository.save(plan);
            }

            for (UserPlanData.RouteData routeData : routeDataList) {
                String routeHashCode = String.valueOf(routeData.hashCode());
                Route route = routeRepository.findByHashcode(routeHashCode);
                boolean routeExists = true;
                if (route == null) {
                    routeExists = false;
                    route = new Route();
                    route.setHashcode(routeHashCode);
                    route.setDay(routeData.getDay());

                    routeRepository.save(route);
                }

                if (!planExists || !routeExists) {
                    PlanRoute planRoute = new PlanRoute();
                    planRoute.setPlan(plan);
                    planRoute.setRoute(route);
                    planRouteRepository.save(planRoute);
                }

                List<UserPlanData.AttractionData> attractionDataList = routeData.getAttractionDataList();
                for (int i = 0; i < attractionDataList.size(); i++) {
                    UserPlanData.AttractionData attractionData = attractionDataList.get(i);

                    int hashCode = attractionData.hashCode();
                    Attraction attraction = attractionRepository.findByHashcode(String.valueOf(hashCode));
                    if (attraction == null) {
                        attraction = new Attraction();
                        attraction.setName(attractionData.getAttractionName());
                        attraction.setLatitude(attractionData.getGeometry().getLocation().getLat());
                        attraction.setLongitude(attractionData.getGeometry().getLocation().getLng());
                        attraction.setType(attractionData.getType());
                        attraction.setRating(attractionData.getRating());
                        attraction.setHashcode(String.valueOf(hashCode));
                        attractionRepository.save(attraction);
                    }

                    RouteAttraction routeAttraction = routeAttractionRepository
                            .findByRouteIdAndAttractionId(route.getId(), attraction.getId());
                    if (routeAttraction == null || routeAttraction.getOrder() != i)  {
                        routeAttraction = new RouteAttraction();
                        routeAttraction.setRoute(route);
                        routeAttraction.setAttraction(attraction);
                        routeAttraction.setOrder(i);
                        routeAttractionRepository.save(routeAttraction);
                    }
                }

                User user = userRepository.findByUsername(username);
                UserPlan userPlan = userPlanRepository
                        .findByUserIdAndPlanId(user.getId(), plan.getId());
                if (userPlan == null) {
                    userPlan = new UserPlan();
                    userPlan.setUser(user);
                    userPlan.setPlan(plan);
                    userPlanRepository.save(userPlan);
                }
            }
            return new BaseResponse<>("200", null, "Add plan succeeded");
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResponse<>("500", null, e.getMessage());
        }
    }

    @GetMapping(value = "/allplans")
    public BaseResponse<UserPlanData> getAllPlansByUserName(@RequestParam(value="username") String username) {
        try {
            // Initialize UserPlanData
            UserPlanData userPlanData = new UserPlanData();
            userPlanData.setPlanDataList(new ArrayList<>());
            // Get PlanList and values of attributes from PlanList
            User user = userRepository.findByUsername(username);
            List<UserPlan> userPlanList = userPlanRepository.findAllByUserId(user.getId());
            if (userPlanList == null || userPlanList.size() == 0) {
                return new BaseResponse<>("200", null, "No saved plan is available");
            }
            userPlanData.setUsername(username);
            for (UserPlan userPlan : userPlanList) {
                Plan plan = userPlan.getPlan();
                String planName = plan.getName();
                long planId = plan.getId();
                String city = plan.getCity().getName();
                long cityId = plan.getCity().getId();
                // Set values of attributes in PlanData
                UserPlanData.PlanData planData = new UserPlanData.PlanData();
                planData.setPlanId(planId);
                planData.setPlanName(planName);
                planData.setCity(city);
                planData.setCityId(cityId);
                List<PlanRoute> planRouteList = planRouteRepository.findAllByPlanId(planId);
                if (planRouteList == null || planRouteList.size() == 0) {
                    continue;
                }
                List<UserPlanData.RouteData> routeDataList = new ArrayList<>();
                for (PlanRoute planRoute : planRouteList) {
                    Route route = planRoute.getRoute();
                    UserPlanData.RouteData routeData = new UserPlanData.RouteData();
                    routeData.setRouteId(route.getId());
                    routeData.setDay(route.getDay());
                    List<RouteAttraction> attractionList = routeAttractionRepository.findAllByRouteId(route.getId());
                    if (attractionList == null || attractionList.size() == 0) {
                        continue;
                    }
                    List<UserPlanData.AttractionData> attractionDataList = new ArrayList<>();
                    for (RouteAttraction routeAttraction : attractionList) {
                        Attraction attraction = routeAttraction.getAttraction();
                        UserPlanData.AttractionData attractionData = new UserPlanData.AttractionData();
                        attractionData.setAttractionName(attraction.getName());
                        attractionData.setAttactionId(attraction.getId());
                        attractionData.setGeometry(new Geometry());
                        attractionData.getGeometry().setLocation(new Geometry.Location());
                        attractionData.getGeometry().getLocation().setLat(attraction.getLatitude());
                        attractionData.getGeometry().getLocation().setLng(attraction.getLongitude());
                        attractionData.setType(attraction.getType());
                        attractionData.setRating(attraction.getRating());
                        attractionDataList.add(attractionData);
                    }
                    routeData.setAttractionDataList(attractionDataList);
                    routeDataList.add(routeData);
                }
                planData.setRouteDataList(routeDataList);
                userPlanData.getPlanDataList().add(planData);
            }
            return new BaseResponse<>("200", userPlanData, "Get all plans succeeded");
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResponse<>("500", null, e.getMessage());
        }
    }

    @PostMapping(value = "/saverecommendedplan")
    public BaseResponse<String> saveRecommendedPlanByIds(@RequestParam("username") String username,
                                                         @RequestParam("planid") long planId) {
        try {
            User user = userRepository.findByUsername(username);
            Plan plan = planRepository.findById(planId).orElseThrow(
                    () -> new RuntimeException("Not Found")
            );
            UserPlan userPlan = new UserPlan();
            userPlan.setPlan(plan);
            userPlan.setUser(user);
            userPlanRepository.save(userPlan);
            return new BaseResponse<>("200", null, "Save recommended plan by ids succeeded");
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResponse<>("500", null, e.getMessage());
        }
    }

    private String getSHA512(String input){
        String output = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(input.getBytes(StandardCharsets.UTF_8));
            output = String.format("%0128x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    @GetMapping(value = "/getrecommendationplans")
    @ResponseBody
    public BaseResponse<UserPlanData> getRecommendationPlansByUserId(@RequestParam(value = "username") String username,
                                                                     @RequestParam(value = "cityname") String cityName) {
        Set<Plan> plans = new HashSet<>();
        try {
            long userId = userRepository.findByUsername(username).getId();
            UserPlanData userPlanData = new UserPlanData();
            userPlanData.setPlanDataList(new ArrayList<>());

            long cityId = cityRepository.findByName(cityName).getId();
            List<UserPlan> allUserPlanList = userPlanRepository.findAll();

            List<UserPlan> singleUserPlanList = userPlanRepository.findAllByUserId(userId);
            Set<Plan> singlePlanSet = new HashSet<>();
            for (UserPlan uP: singleUserPlanList){
                singlePlanSet.add(uP.getPlan());
            }
            // no need to see repetitive result, besides, transfer UserPlan to Plan as we need not show userId back to User
            for (UserPlan uP: allUserPlanList) {
                Plan tempPlan = uP.getPlan();
                if (tempPlan.getCity().getId() == cityId && !plans.contains(tempPlan) && !singlePlanSet.contains(tempPlan)) {
                    plans.add(tempPlan);
                }
            }

            if (plans.size() == 0) {
                return new BaseResponse<>("200", null, "No saved plan is available");
            }

            userPlanData.setUsername(null);
            for (Plan plan: plans){
                String planName = plan.getName();
                long planId = plan.getId();
                String city = plan.getCity().getName();
                // Set values of attributes in PlanData
                UserPlanData.PlanData planData = new UserPlanData.PlanData();
                planData.setPlanName(planName);
                planData.setPlanId(planId);
                planData.setCity(city);
                planData.setCityId(cityId);
                List<PlanRoute> planRouteList = planRouteRepository.findAllByPlanId(planId);
                if (planRouteList == null || planRouteList.size() == 0) {
                    continue;
                }
                List<UserPlanData.RouteData> routeDataList = new ArrayList<>();
                for (PlanRoute planRoute : planRouteList) {
                    Route route = planRoute.getRoute();
                    UserPlanData.RouteData routeData = new UserPlanData.RouteData();
                    routeData.setRouteId(route.getId());
                    routeData.setDay(route.getDay());
                    List<RouteAttraction> routeAttractionList = routeAttractionRepository.findAllByRouteId(route.getId());
                    if (routeAttractionList == null || routeAttractionList.size() == 0) {
                        continue;
                    }
                    List<UserPlanData.AttractionData> attractionDataList = new ArrayList<>();
                    for (RouteAttraction routeAttraction : routeAttractionList) {
                        Attraction attraction = routeAttraction.getAttraction();
                        UserPlanData.AttractionData attractionData = new UserPlanData.AttractionData();
                        attractionData.setAttractionName(attraction.getName());
                        attractionData.setAttactionId(attraction.getId());
                        attractionData.setGeometry(new Geometry());
                        attractionData.getGeometry().setLocation(new Geometry.Location());
                        attractionData.getGeometry().getLocation().setLat(attraction.getLatitude());
                        attractionData.getGeometry().getLocation().setLng(attraction.getLongitude());
                        attractionData.setType(attraction.getType());
                        attractionData.setRating(attraction.getRating());
                        attractionDataList.add(attractionData);
                    }
                    routeData.setAttractionDataList(attractionDataList);
                    routeDataList.add(routeData);
                }
                planData.setRouteDataList(routeDataList);
                userPlanData.getPlanDataList().add(planData);
            }
            return new BaseResponse<UserPlanData>("200", userPlanData, "All recommendation result");
        } catch (Exception e){
            return new BaseResponse<>("500",null, e.getMessage());
        }
    }

    @DeleteMapping(value = "/deleteplan")
    @ResponseBody
    public BaseResponse<String> deletePlanByIds(@RequestParam(value = "username") String username,
                                                @RequestParam(value = "planid") int planId){
        try {
            long userId = userRepository.findByUsername(username).getId();
            UserPlan userPlan = userPlanRepository.findByUserIdAndPlanId(userId, planId);
            userPlanRepository.delete(userPlan);
            return new BaseResponse<>("200", null, "delete successfully");
        }catch (Exception e){
            return new BaseResponse<>("500",null,"server failed to connect");
        }
    }
}
