package com.projects.coin_monitor_hub.dto.response;

import com.projects.coin_monitor_hub.dto.request.ExpectedPriceRequestDto;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object for token price response.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenPriceResponseDto {

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
     * The price of the token.
     */
    private BigDecimal price;

    /**
     * A list of expected price request details.
     */
    private List<ExpectedPriceRequestDto> expectedPriceRequestDto;
}

