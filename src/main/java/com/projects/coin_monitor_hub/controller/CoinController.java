package com.projects.coin_monitor_hub.controller;

import com.projects.coin_monitor_hub.dto.TokenPriceRequestDto;
import com.projects.coin_monitor_hub.dto.TokenPriceResponseDto;
import com.projects.coin_monitor_hub.service.CoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coins")
public class CoinController {

    private final CoinService coinService;

    @GetMapping
    public String helloWorld() {
        return "Hello World";
    }

    @PostMapping
    public ResponseEntity<TokenPriceResponseDto> getTokenPrice(@RequestBody TokenPriceRequestDto tokenPriceRequestDto) {
        return new ResponseEntity<>(coinService.getTokenPrice(tokenPriceRequestDto), HttpStatus.CREATED);
    }
}
