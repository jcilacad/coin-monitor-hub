package com.projects.coin_monitor_hub.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpectedPriceRequestDto {
    private BigDecimal lowLimitExpectedPrice;
    private BigDecimal highLimitExpectedPrice;
    private int percent;
}
