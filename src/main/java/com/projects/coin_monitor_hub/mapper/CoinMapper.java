package com.projects.coin_monitor_hub.mapper;

import com.projects.coin_monitor_hub.dto.request.CoinRequestDto;
import com.projects.coin_monitor_hub.dto.response.CoinResponseDto;
import com.projects.coin_monitor_hub.entity.Coin;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CoinMapper {

    CoinMapper INSTANCE = Mappers.getMapper(CoinMapper.class);

    Coin coinRequestToCoin (CoinRequestDto coinRequestDto);

    CoinResponseDto coinToCoinResponse(Coin coin);
}
