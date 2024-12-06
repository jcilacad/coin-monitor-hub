package com.projects.coin_monitor_hub.dto.request;

import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoinRequestDto {

    private String assetPlatformId;
    private String tokenContractAddress;
    private String targetCurrency;
    private String tokenName;
    private BigDecimal lowTwentyFive;
    private BigDecimal highTwentyFive;
    private BigDecimal lowFifty;
    private BigDecimal highFifty;
    private BigDecimal lowSeventyFive;
    private BigDecimal highSeventyFive;
    private BigDecimal lowOneHundred;
    private BigDecimal highOneHundred;
}
