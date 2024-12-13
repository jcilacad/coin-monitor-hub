package com.projects.coin_monitor_hub.service;

import com.projects.coin_monitor_hub.dto.request.CoinRequestDto;
import com.projects.coin_monitor_hub.dto.response.CoinResponseDto;

import java.util.List;

/**
 * Service interface for coin-related operations.
 */
public interface CoinService {

     /**
      * Fetches the token price periodically.
      */
     void getTokenPrice();

     /**
      * Retrieves all coins.
      *
      * @return a list of CoinResponseDto representing all coins
      */
     List<CoinResponseDto> getAllCoins();

     /**
      * Retrieves a coin by its ID.
      *
      * @param id the ID of the coin to be retrieved
      * @return the CoinResponseDto representing the found coin
      */
     CoinResponseDto getCoinById(Long id);

     /**
      * Creates a new coin.
      *
      * @param coinRequestDto the CoinRequestDto containing the details of the coin to be created
      * @return the CoinResponseDto representing the created coin
      */
     CoinResponseDto createCoin(CoinRequestDto coinRequestDto);

     /**
      * Updates an existing coin by its ID.
      *
      * @param coinId the ID of the coin to be updated
      * @param updatedCoinRequestDto the CoinRequestDto containing the updated details
      * @return the CoinResponseDto representing the updated coin
      */
     CoinResponseDto updateCoin(Long coinId, CoinRequestDto updatedCoinRequestDto);

     /**
      * Deletes a coin by its ID.
      *
      * @param coinId the ID of the coin to be deleted
      */
     void deleteCoin(Long coinId);
}
