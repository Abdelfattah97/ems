package com.ems.core.dto;

import java.util.Collection;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * This class wraps the content of a page that is fetched from a data source
 * containing a {@link Collection} of <T> elements and page meta-data. <br>
 * meta-data: pageNumber, pageSize, totalElements, totalPages
 * 
 * @param <T> The wrapped elements type , specifies the the type of the
 *            collection holding the actual data
 */
@Builder
@Getter
@Setter
public class PageResponse<T> {

	private List<T> content;
	private Integer pageNumber;
	private Integer pageSize;
	private int totalElements;
	private int totalPages;

}
