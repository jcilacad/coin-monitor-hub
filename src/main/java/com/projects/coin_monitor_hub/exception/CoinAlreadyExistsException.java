package com.projects.coin_monitor_hub.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom exception for handling cases where a coin already exists with the specified contract address.
 */
@Slf4j
public class CoinAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new CoinAlreadyExistsException with the specified token contract address.
     *
     * @param tokenContractAddress the contract address of the token that already exists
     */
    public CoinAlreadyExistsException(String tokenContractAddress) {
        super(String.format("Coin already exists with contract address : %s", tokenContractAddress));
        log.error("Coin already exists with contract address : {}", tokenContractAddress);
    }
}

