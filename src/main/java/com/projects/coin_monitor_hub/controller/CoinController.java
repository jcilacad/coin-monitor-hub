package com.projects.coin_monitor_hub.controller;

import com.projects.coin_monitor_hub.dto.request.CoinRequestDto;
import com.projects.coin_monitor_hub.dto.response.CoinResponseDto;
import com.projects.coin_monitor_hub.service.CoinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coins")
public class CoinController {

    private final CoinService coinService;

    @PostMapping
    public ResponseEntity<CoinResponseDto> createCoin(@Valid @RequestBody CoinRequestDto coinRequestDto) {
        return new ResponseEntity<>(coinService.createCoin(coinRequestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CoinResponseDto>> getAllCoins() {
        return ResponseEntity.ok(coinService.getAllCoins());
    }

    @GetMapping("/{coinId}")
    public ResponseEntity<CoinResponseDto> getCoinById(@PathVariable Long coinId) {
        return ResponseEntity.ok(coinService.getCoinById(coinId));
    }

    @PutMapping("/{coinId}")
    public ResponseEntity<CoinResponseDto> updateCoinById(@Valid @PathVariable Long coinId,
                                                          @RequestBody CoinRequestDto updatedCoinRequestDto) {
        return ResponseEntity.ok(coinService.updateCoin(coinId, updatedCoinRequestDto));
    }

    @DeleteMapping("/{coinId}")
    public ResponseEntity<Void> deleteCoinById(@PathVariable Long coinId) {
        coinService.deleteCoin(coinId);
        return ResponseEntity.noContent().build();
    }
}
