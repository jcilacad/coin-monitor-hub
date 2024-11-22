package com.projects.coin_monitor_hub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coins")
public class CoinController {

    @GetMapping
    public String helloWorld() {
        return "Hello World";
    }

}
