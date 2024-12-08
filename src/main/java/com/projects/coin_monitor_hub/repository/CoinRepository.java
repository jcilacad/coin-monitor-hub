package com.projects.coin_monitor_hub.repository;

import com.projects.coin_monitor_hub.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, Long> {

    boolean existsByTokenContractAddress(String tokenContractAddress);
}
