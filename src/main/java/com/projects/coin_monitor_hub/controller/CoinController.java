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

/**
 * REST controller for managing coin-related operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coins")
public class CoinController {

    private final CoinService coinService;

    /**
     * Creates a new coin.
     *
     * @param coinRequestDto the DTO containing the details of the coin to be created
     * @return a ResponseEntity containing the created CoinResponseDto and HTTP status CREATED
     */
    @PostMapping
    public ResponseEntity<CoinResponseDto> createCoin(@Valid @RequestBody CoinRequestDto coinRequestDto) {
        return new ResponseEntity<>(coinService.createCoin(coinRequestDto), HttpStatus.CREATED);
    }

    /**
     * Retrieves all coins.
     *
     * @return a ResponseEntity containing a list of all CoinResponseDto and HTTP status OK
     */
    @GetMapping
    public ResponseEntity<List<CoinResponseDto>> getAllCoins() {
        return ResponseEntity.ok(coinService.getAllCoins());
    }

    /**
     * Retrieves a coin by its ID.
     *
     * @param coinId the ID of the coin to be retrieved
     * @return a ResponseEntity containing the CoinResponseDto of the specified coin and HTTP status OK
     */
    @GetMapping("/{coinId}")
    public ResponseEntity<CoinResponseDto> getCoinById(@PathVariable Long coinId) {
        return ResponseEntity.ok(coinService.getCoinById(coinId));
    }

    /**
     * Updates a coin by its ID.
     *
     * @param coinId the ID of the coin to be updated
     * @param updatedCoinRequestDto the DTO containing the updated details of the coin
     * @return a ResponseEntity containing the updated CoinResponseDto and HTTP status OK
     */
    @PutMapping("/{coinId}")
    public ResponseEntity<CoinResponseDto> updateCoinById(@Valid @PathVariable Long coinId,
                                                          @RequestBody CoinRequestDto updatedCoinRequestDto) {
        return ResponseEntity.ok(coinService.updateCoin(coinId, updatedCoinRequestDto));
    }

    /**
     * Deletes a coin by its ID.
     *
     * @param coinId the ID of the coin to be deleted
     * @return a ResponseEntity with HTTP status NO_CONTENT
     */
    @DeleteMapping("/{coinId}")
    public ResponseEntity<Void> deleteCoinById(@PathVariable Long coinId) {
        coinService.deleteCoin(coinId);
        return ResponseEntity.noContent().build();
    }
}
