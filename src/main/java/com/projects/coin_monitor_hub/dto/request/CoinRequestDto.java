package com.projects.coin_monitor_hub.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoinRequestDto {

    @NotBlank(message = "Asset Platform Id is Mandatory")
    private String assetPlatformId;

    @NotBlank(message = "Token Contract Address is Mandatory")
    private String tokenContractAddress;

    @NotBlank(message = "Target Currency is Mandatory")
    @Size(max = 3)
    private String targetCurrency;
    
    @NotBlank(message = "Token Name is Mandatory")
    private String tokenName;
    
    @DecimalMin(value = "0.0")
    private BigDecimal lowTwentyFive;

    @DecimalMin(value = "0.0")
    private BigDecimal highTwentyFive;

    @DecimalMin(value = "0.0")
    private BigDecimal lowFifty;

    @DecimalMin(value = "0.0")
    private BigDecimal highFifty;

    @DecimalMin(value = "0.0")
    private BigDecimal lowSeventyFive;

    @DecimalMin(value = "0.0")
    private BigDecimal highSeventyFive;

    @DecimalMin(value = "0.0")
    private BigDecimal lowOneHundred;

    @DecimalMin(value = "0.0")
    private BigDecimal highOneHundred;
}
