package com.projects.coin_monitor_hub.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.coin_monitor_hub.dto.TokenPriceRequestDto;
import com.projects.coin_monitor_hub.dto.TokenPriceResponseDto;
import com.projects.coin_monitor_hub.service.CoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoinServiceImpl implements CoinService {

    @Value("${coin.gecko.api.key}")
    private String coinGeckoApiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public CoinServiceImpl(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("https://api.coingecko.com").build();
        this.objectMapper = objectMapper;
    }

    @Override
    public TokenPriceResponseDto getTokenPrice(TokenPriceRequestDto tokenPriceRequestDto) {
        String assetPlatformId = tokenPriceRequestDto.getAssetPlatformId();
        String tokenContractAddress = tokenPriceRequestDto.getTokenContractAddress().toLowerCase();
        String targetCurrency = tokenPriceRequestDto.getTargetCurrency();
        String tokenName = tokenPriceRequestDto.getTokenName();
        String rawResponse = webClient.get()
                .uri("/api/v3/simple/token_price/{assetPlatformId}" +
                                "?contract_addresses={tokenContractAddress}&vs_currencies={targetCurrency}",
                        assetPlatformId, tokenContractAddress, targetCurrency)
                .header("accept", "application/json")
                .header("x-cg-demo-api-key", coinGeckoApiKey)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Map<String, Map<String, BigDecimal>> response;
        try {
            response = objectMapper.readValue(rawResponse, new TypeReference<>() {});
        } catch (Exception e) {
            // TODO: Create a custom exception
            throw new RuntimeException("Failed to parse API response", e);
        }

        Map<String, BigDecimal> tokenData = response.get(tokenContractAddress);
        BigDecimal extractedPrice = tokenData != null ? tokenData.get(targetCurrency) : null;
        return TokenPriceResponseDto.builder()
                .assetPlatformId(assetPlatformId)
                .tokenContractAddress(tokenContractAddress)
                .targetCurrency(targetCurrency)
                .tokenName(tokenName)
                .price(extractedPrice)
                .build();
    }
}
