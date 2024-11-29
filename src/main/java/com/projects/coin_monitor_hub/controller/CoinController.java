package com.projects.coin_monitor_hub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CoinController {

    @RequestMapping("/")
    public String home() {
        return "index.html";
    }
}
