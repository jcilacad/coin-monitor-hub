package com.projects.coin_monitor_hub.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.coin_monitor_hub.dto.ExpectedPriceRequestDto;
import com.projects.coin_monitor_hub.dto.TokenPriceRequestDto;
import com.projects.coin_monitor_hub.dto.TokenPriceResponseDto;
import com.projects.coin_monitor_hub.mapper.TokenPriceMapper;
import com.projects.coin_monitor_hub.service.CoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
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

    @Async
    @Override
//    @Scheduled(cron = "0 */13 * * * *")
    @Scheduled(cron = "*/1 * * * * *") // Runs every second
    public void getTokenPrice() {

        log.info("RUNNING EVERY 1 SECOND");
        getDatasets().stream()
                .forEach(tokenPriceResponseDto -> {
                    String assetPlatformId = tokenPriceResponseDto.getAssetPlatformId();
                    String tokenContractAddress = tokenPriceResponseDto.getTokenContractAddress().toLowerCase();
                    String targetCurrency = tokenPriceResponseDto.getTargetCurrency();
                    String tokenName = tokenPriceResponseDto.getTokenName();
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
                        response = objectMapper.readValue(rawResponse, new TypeReference<>() {
                        });
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse API response", e);
                    }

                    Map<String, BigDecimal> tokenData = response.get(tokenContractAddress);
                    BigDecimal extractedPrice = tokenData != null ? tokenData.get(targetCurrency) : null;

                    List<ExpectedPriceRequestDto> priceRequestDtoList = tokenPriceResponseDto.getExpectedPriceRequestDto();

                    priceRequestDtoList.stream()
                            .forEach(expectedPriceRequestDto -> {
                                BigDecimal lowLimitExpectedPrice = expectedPriceRequestDto.getLowLimitExpectedPrice();
                                BigDecimal highLimitExpectedPrice = expectedPriceRequestDto.getHighLimitExpectedPrice();

                                boolean isInRange = extractedPrice.compareTo(lowLimitExpectedPrice) >= 0 &&
                                        extractedPrice.compareTo(highLimitExpectedPrice) <= 0;

                                if (isInRange) {
                                    // TODO: Send an email and SMS
                                    log.info("IS BETWEEN THE RANGE OF: {} - {} WITH A PERCENT OF {}", lowLimitExpectedPrice.toString(),
                                            highLimitExpectedPrice.toString(), expectedPriceRequestDto.getPercent());
                                }
                            });
                });
    }

    private List<TokenPriceResponseDto> getDatasets() {

        List<ExpectedPriceRequestDto> metaStrikeExpectedPriceRequestDto = getExpectedPriceRequestDto(
                BigDecimal.valueOf(0.23), BigDecimal.valueOf(0.23),
                BigDecimal.valueOf(0.23), BigDecimal.valueOf(0.23),
                BigDecimal.valueOf(0.23), BigDecimal.valueOf(0.23),
                BigDecimal.valueOf(0.23), BigDecimal.valueOf(0.23));

        TokenPriceRequestDto metaStrikeTokenPriceRequestDto = TokenPriceRequestDto.builder()
                .assetPlatformId("binance-smart-chain")
                .tokenContractAddress("0x496cC0b4ee12Aa2AC4c42E93067484e7Ff50294b")
                .targetCurrency("usd")
                .tokenName("Metastrike")
                .expectedPriceRequestDto(metaStrikeExpectedPriceRequestDto)
                .build();

        TokenPriceResponseDto metaStrikeTokenPriceResponseDto = TokenPriceMapper.INSTANCE.tokenPriceRequestToResponse(metaStrikeTokenPriceRequestDto);

        return List.of(metaStrikeTokenPriceResponseDto);
    }

    private List<ExpectedPriceRequestDto> getExpectedPriceRequestDto(BigDecimal lowTwentyFive, BigDecimal highTwentyFive,
                                                                     BigDecimal lowFifty, BigDecimal highFifty,
                                                                     BigDecimal lowSeventyFive, BigDecimal highSeventyFive,
                                                                     BigDecimal lowOneHundred, BigDecimal highOneHundred) {

        List<ExpectedPriceRequestDto> expectedPriceRequestDtoList = Arrays.asList(
                ExpectedPriceRequestDto.builder().lowLimitExpectedPrice(lowTwentyFive)
                        .highLimitExpectedPrice(highTwentyFive).percent(25).build(),

                ExpectedPriceRequestDto.builder().lowLimitExpectedPrice(lowFifty)
                        .highLimitExpectedPrice(highFifty).percent(50).build(),

                ExpectedPriceRequestDto.builder().lowLimitExpectedPrice(lowSeventyFive)
                        .highLimitExpectedPrice(highSeventyFive).percent(75).build(),

                ExpectedPriceRequestDto.builder().lowLimitExpectedPrice(lowOneHundred)
                        .highLimitExpectedPrice(highOneHundred).percent(100).build()
        );

        return expectedPriceRequestDtoList;
    }
}
