package com.projects.coin_monitor_hub.service;

import com.projects.coin_monitor_hub.dto.request.CoinRequestDto;
import com.projects.coin_monitor_hub.dto.response.CoinResponseDto;

import java.util.List;

public interface CoinService {

     void getTokenPrice();

     List<CoinResponseDto> getAllCoins();

     CoinResponseDto getCoinById(Long id);

     CoinResponseDto createCoin(CoinRequestDto coinRequestDto);

     CoinResponseDto updateCoin(Long coinId, CoinRequestDto updatedCoinRequestDto);

     void deleteCoin(Long coinId);
}
