package com.projects.coin_monitor_hub.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenPriceRequestDto {
    private String assetPlatformId;
    private String tokenContractAddress;
    private String targetCurrency;
    private String tokenName;
    private List<ExpectedPriceRequestDto> expectedPriceRequestDto;
}
