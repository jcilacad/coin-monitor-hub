package com.projects.coin_monitor_hub.dto.request;

import lombok.*;

import java.math.BigDecimal;

/**
 * Data Transfer Object for expected price request.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpectedPriceRequestDto {

    /**
     * The lower limit of the expected price range.
     */
    private BigDecimal lowLimitExpectedPrice;

    /**
     * The upper limit of the expected price range.
     */
    private BigDecimal highLimitExpectedPrice;

    /**
     * The percentage value.
     */
    private int percent;
}
