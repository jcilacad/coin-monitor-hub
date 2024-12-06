package com.projects.coin_monitor_hub.mapper;

import com.projects.coin_monitor_hub.dto.request.TokenPriceRequestDto;
import com.projects.coin_monitor_hub.dto.response.TokenPriceResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TokenPriceMapper {

    TokenPriceMapper INSTANCE = Mappers.getMapper(TokenPriceMapper.class);

    TokenPriceResponseDto tokenPriceRequestToResponse(TokenPriceRequestDto tokenPriceRequestDto);
}
