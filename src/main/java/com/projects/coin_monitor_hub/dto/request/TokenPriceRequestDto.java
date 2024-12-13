package com.projects.coin_monitor_hub.dto.request;

import lombok.*;

import java.util.List;


/**
 * Data Transfer Object for token price request.
 */
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenPriceRequestDto {

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
     * A list of expected price request details.
     */
    private List<ExpectedPriceRequestDto> expectedPriceRequestDto;
}

