package com.projects.coin_monitor_hub.service;

import com.projects.coin_monitor_hub.dto.TokenPriceRequestDto;
import com.projects.coin_monitor_hub.dto.TokenPriceResponseDto;

import java.math.BigDecimal;

public interface CoinService {

     TokenPriceResponseDto getTokenPrice(TokenPriceRequestDto tokenPriceRequestDto);
}
