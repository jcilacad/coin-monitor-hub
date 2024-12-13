package com.projects.coin_monitor_hub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a coin in the system.
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coins")
public class Coin {

    /**
     * The unique identifier for the coin.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coins_generator")
    @SequenceGenerator(name = "coins_generator", sequenceName = "coins_sequence", allocationSize = 1)
    private Long id;

    /**
     * The platform ID of the asset. Cannot be null.
     */
    @Column(name = "asset_platform_id", nullable = false)
    private String assetPlatformId;

    /**
     * The contract address of the token. Must be unique and cannot be null.
     */
    @Column(name = "token_contract_address", nullable = false, unique = true)
    private String tokenContractAddress;

    /**
     * The target currency. Cannot be null.
     */
    @Column(name = "target_currency", nullable = false)
    private String targetCurrency;

    /**
     * The name of the token.
     */
    @Column(name = "token_name")
    private String tokenName;

    /**
     * The low price threshold for 25%. Cannot be null.
     */
    @Column(name = "low_twenty_five", nullable = false)
    private BigDecimal lowTwentyFive;

    /**
     * The high price threshold for 25%. Cannot be null.
     */
    @Column(name = "high_twenty_five", nullable = false)
    private BigDecimal highTwentyFive;

    /**
     * The low price threshold for 50%. Cannot be null.
     */
    @Column(name = "low_fifty", nullable = false)
    private BigDecimal lowFifty;

    /**
     * The high price threshold for 50%. Cannot be null.
     */
    @Column(name = "high_fifty", nullable = false)
    private BigDecimal highFifty;

    /**
     * The low price threshold for 75%. Cannot be null.
     */
    @Column(name = "low_seventy_five", nullable = false)
    private BigDecimal lowSeventyFive;

    /**
     * The high price threshold for 75%. Cannot be null.
     */
    @Column(name = "high_seventy_five", nullable = false)
    private BigDecimal highSeventyFive;

    /**
     * The low price threshold for 100%. Cannot be null.
     */
    @Column(name = "low_one_hundred", nullable = false)
    private BigDecimal lowOneHundred;

    /**
     * The high price threshold for 100%. Cannot be null.
     */
    @Column(name = "high_one_hundred", nullable = false)
    private BigDecimal highOneHundred;

    /**
     * The timestamp when the coin was created. Automatically generated.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * The timestamp when the coin was last updated. Automatically generated.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

