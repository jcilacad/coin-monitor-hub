package com.projects.coin_monitor_hub.mapper;

import com.projects.coin_monitor_hub.dto.request.TokenPriceRequestDto;
import com.projects.coin_monitor_hub.dto.response.TokenPriceResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting between TokenPriceRequestDto and TokenPriceResponseDto.
 */
@Mapper
public interface TokenPriceMapper {

    /**
     * An instance of the TokenPriceMapper.
     */
    TokenPriceMapper INSTANCE = Mappers.getMapper(TokenPriceMapper.class);

    /**
     * Converts a TokenPriceRequestDto to a TokenPriceResponseDto.
     *
     * @param tokenPriceRequestDto the TokenPriceRequestDto to convert
     * @return the converted TokenPriceResponseDto
     */
    TokenPriceResponseDto tokenPriceRequestToResponse(TokenPriceRequestDto tokenPriceRequestDto);
}

