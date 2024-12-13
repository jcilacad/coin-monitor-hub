package com.projects.coin_monitor_hub.repository;

import com.projects.coin_monitor_hub.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Coin entities.
 */
public interface CoinRepository extends JpaRepository<Coin, Long> {

    /**
     * Checks if a coin with the specified token contract address exists.
     *
     * @param tokenContractAddress the token contract address to check
     * @return true if a coin with the specified token contract address exists, false otherwise
     */
    boolean existsByTokenContractAddress(String tokenContractAddress);
}

