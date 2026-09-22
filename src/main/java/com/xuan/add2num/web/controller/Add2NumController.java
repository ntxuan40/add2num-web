package com.xuan.add2num.web.controller;

import com.xuan.add2num.web.service.Add2NumService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Add2NumController {

    private final Add2NumService service;

    public Add2NumController(Add2NumService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/add")
    public String add(
            @RequestParam String stn1,
            @RequestParam String stn2,
            Model model) {

        String result = service.add(stn1, stn2);

        model.addAttribute("stn1", stn1);
        model.addAttribute("stn2", stn2);
        model.addAttribute("result", result);

        return "index";
    }
}