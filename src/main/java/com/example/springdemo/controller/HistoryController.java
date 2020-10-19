package com.example.springdemo.controller;

import com.example.springdemo.models.entity.History;
import com.example.springdemo.models.entity.User;
import com.example.springdemo.repository.HistoryRepository;
import com.example.springdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public List<String> getHistoryById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        User user = userRepository.findByUsername(name);
        if (user == null) throw new UsernameNotFoundException("User Not Found with username: " + name);

        List<History> list = historyRepository.findAllByUser(user);
        List<String> res = new ArrayList<>();
        for (History h : list) res.add(h.getRoute());
        return res;
    }

    @PostMapping
    public String saveHistory(@RequestBody String route) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("User Not Found with username: " + username);
        History history = new History(user, route);
        historyRepository.save(history);
        return route;
    }
}
