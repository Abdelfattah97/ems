package com.ems.core.dto.mapper;

import java.util.function.Function;

import com.ems.core.dto.PageResponse;


public interface PageResponseDtoMapper<entity, dto> {

	 PageResponse<dto> toDto(PageResponse<entity> pageResponse, Function<entity, dto> elementMapper) ;


}
