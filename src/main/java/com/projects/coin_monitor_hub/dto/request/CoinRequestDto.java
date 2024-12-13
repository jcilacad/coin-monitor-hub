package com.projects.coin_monitor_hub.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

/**
 * Data Transfer Object for coin-related requests.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoinRequestDto {

    /**
     * The platform ID of the asset. This field is mandatory.
     */
    @NotBlank(message = "Asset Platform Id is Mandatory")
    private String assetPlatformId;

    /**
     * The contract address of the token. This field is mandatory.
     */
    @NotBlank(message = "Token Contract Address is Mandatory")
    private String tokenContractAddress;

    /**
     * The target currency. This field is mandatory and must not exceed 3 characters.
     */
    @NotBlank(message = "Target Currency is Mandatory")
    @Size(max = 3)
    private String targetCurrency;

    /**
     * The name of the token. This field is mandatory.
     */
    @NotBlank(message = "Token Name is Mandatory")
    private String tokenName;

    /**
     * The low price threshold for 25%. Must be a non-negative value.
     */
    @DecimalMin(value = "0.0")
    private BigDecimal lowTwentyFive;

    /**
     * The high price threshold for 25%. Must be a non-negative value.
     */
    @DecimalMin(value = "0.0")
    private BigDecimal highTwentyFive;

    /**
     * The low price threshold for 50%. Must be a non-negative value.
     */
    @DecimalMin(value = "0.0")
    private BigDecimal lowFifty;

    /**
     * The high price threshold for 50%. Must be a non-negative value.
     */
    @DecimalMin(value = "0.0")
    private BigDecimal highFifty;

    /**
     * The low price threshold for 75%. Must be a non-negative value.
     */
    @DecimalMin(value = "0.0")
    private BigDecimal lowSeventyFive;

    /**
     * The high price threshold for 75%. Must be a non-negative value.
     */
    @DecimalMin(value = "0.0")
    private BigDecimal highSeventyFive;

    /**
     * The low price threshold for 100%. Must be a non-negative value.
     */
    @DecimalMin(value = "0.0")
    private BigDecimal lowOneHundred;

    /**
     * The high price threshold for 100%. Must be a non-negative value.
     */
    @DecimalMin(value = "0.0")
    private BigDecimal highOneHundred;
}

