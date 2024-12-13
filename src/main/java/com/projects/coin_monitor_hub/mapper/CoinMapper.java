package com.projects.coin_monitor_hub.mapper;

import com.projects.coin_monitor_hub.dto.request.CoinRequestDto;
import com.projects.coin_monitor_hub.dto.response.CoinResponseDto;
import com.projects.coin_monitor_hub.entity.Coin;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting between Coin and its DTOs.
 */
@Mapper
public interface CoinMapper {

    /**
     * An instance of the CoinMapper.
     */
    CoinMapper INSTANCE = Mappers.getMapper(CoinMapper.class);

    /**
     * Converts a CoinRequestDto to a Coin entity.
     *
     * @param coinRequestDto the CoinRequestDto to convert
     * @return the converted Coin entity
     */
    Coin coinRequestToCoin(CoinRequestDto coinRequestDto);

    /**
     * Converts a Coin entity to a CoinResponseDto.
     *
     * @param coin the Coin entity to convert
     * @return the converted CoinResponseDto
     */
    CoinResponseDto coinToCoinResponse(Coin coin);
}
