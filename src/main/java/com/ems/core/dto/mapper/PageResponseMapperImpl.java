package com.ems.core.dto.mapper;

import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.ems.core.dto.PageResponse;

@Component
public class PageResponseMapperImpl<entity, dto> implements PageResponseDtoMapper<entity, dto> {

	@Override
	public PageResponse<dto> toDto(PageResponse<entity> pageResponse, Function<entity, dto> elementMapper) {

		List<dto> dtos = pageResponse.getContent().stream().map(e -> elementMapper.apply(e)).toList();

		PageResponse<dto> dtoPage = PageResponse.<dto>builder().content(dtos).pageNumber(pageResponse.getPageNumber())
				.pageSize(pageResponse.getPageSize()).totalPages(pageResponse.getTotalPages())
				.totalElements(pageResponse.getTotalElements()).build();

		return dtoPage;

	}

}
