package com.projects.coin_monitor_hub.dto.response;

import lombok.*;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Data Transfer Object for coin-related responses.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoinResponseDto {

    /**
     * The ID of the coin.
     */
    private Long id;

    /**
     * The platform ID of the asset.
     */
    private String assetPlatformId;

    /**
     * The contract address of the token.
     */
    private String tokenContractAddress;

    /**
     * The target currency.
     */
    private String targetCurrency;

    /**
     * The name of the token.
     */
    private String tokenName;

    /**
     * The low price threshold for 25%.
     */
    private BigDecimal lowTwentyFive;

    /**
     * The high price threshold for 25%.
     */
    private BigDecimal highTwentyFive;

    /**
     * The low price threshold for 50%.
     */
    private BigDecimal lowFifty;

    /**
     * The high price threshold for 50%.
     */
    private BigDecimal highFifty;

    /**
     * The low price threshold for 75%.
     */
    private BigDecimal lowSeventyFive;

    /**
     * The high price threshold for 75%.
     */
    private BigDecimal highSeventyFive;

    /**
     * The low price threshold for 100%.
     */
    private BigDecimal lowOneHundred;

    /**
     * The high price threshold for 100%.
     */
    private BigDecimal highOneHundred;
}

