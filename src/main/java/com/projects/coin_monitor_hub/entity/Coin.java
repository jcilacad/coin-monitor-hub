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

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coins")
public class Coin {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coins_generator")
    @SequenceGenerator(name = "coins_generator", sequenceName = "coins_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "asset_platform_id", nullable = false)
    private String assetPlatformId;

    @Column(name = "token_contract_address", nullable = false, unique = true)
    private String tokenContractAddress;

    @Column(name = "target_currency", nullable = false)
    private String targetCurrency;

    @Column(name = "token_name")
    private String tokenName;

    @Column(name = "low_twenty_five", nullable = false)
    private BigDecimal lowTwentyFive;

    @Column(name = "high_twenty_five", nullable = false)
    private BigDecimal highTwentyFive;

    @Column(name = "low_fifty", nullable = false)
    private BigDecimal lowFifty;

    @Column(name = "high_fifty", nullable = false)
    private BigDecimal highFifty;

    @Column(name = "low_seventy_five", nullable = false)
    private BigDecimal lowSeventyFive;

    @Column(name = "high_seventy_five", nullable = false)
    private BigDecimal highSeventyFive;

    @Column(name = "low_one_hundred", nullable = false)
    private BigDecimal lowOneHundred;

    @Column(name = "high_one_hundred", nullable = false)
    private BigDecimal highOneHundred;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
