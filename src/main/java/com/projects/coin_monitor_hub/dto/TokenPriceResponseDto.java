package com.projects.coin_monitor_hub.dto;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenPriceResponseDto {

    private String assetPlatformId;
    private String tokenContractAddress;
    private String targetCurrency;
    private String tokenName;
    private BigDecimal price;
}
