package com.projects.coin_monitor_hub.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CoinAlreadyExistsException extends RuntimeException {

    public CoinAlreadyExistsException(String tokenContractAddress) {
        super(String.format("Coin already exists with contract address : %s", tokenContractAddress));
        log.error("Coin already exists with contract address : {}", tokenContractAddress);
    }
}
