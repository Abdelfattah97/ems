package com.ems.core.service.pagination;

import java.util.Arrays;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

/**
 * A factory class for creating {@link Pageable} instances with sensible default
 * values.
 * 
 * <p>
 * This utility class helps in building a {@link PageRequest} object with the
 * specified parameters while providing fallback defaults for null or invalid
 * inputs. It ensures the following:
 * <ul>
 * <li>The page size has a minimum value of 10 when it is null or less than
 * 2.</li>
 * <li>The page number defaults to 0 if it is null or negative.</li>
 * <li>The sorting direction defaults to {@link Direction#ASC} when null.</li>
 * <li>If sorting properties are provided, they are filtered to exclude null or
 * empty values.</li>
 * <li>If no valid sorting properties are specified, the {@link PageRequest} is
 * created without sorting.</li>
 * </ul>
 * </p>
 */
public class PageRequestFactory {

	/**
	 * Creates a {@link Pageable} instance based on the provided parameters.
	 * 
	 * <p>
	 * This method applies the following defaults:
	 * <ul>
	 * <li><strong>Page Size:</strong> Defaults to 10 if the provided value is null
	 * or less than 2.</li>
	 * <li><strong>Page Number:</strong> Defaults to 0 if the provided value is null
	 * or negative.</li>
	 * <li><strong>(Sorting) Direction:</strong> Defaults to {@link Direction#ASC} if
	 * the provided value is null.</li>
	 * <li><strong>(Sorting) Properties:</strong> If null or all properties are empty,
	 * sorting is not applied.</li>
	 * </ul>
	 * </p>
	 * 
	 * @param pageSize   The number of elements per page.
	 * @param pageNum    The page number to be retrieved. 
	 * @param dir        The direction of sorting (ASC or DESC) applies for all properties.
	 * @param properties The properties to sort by, if empty or null no sorting is applied.
	 * @return A {@link Pageable} instance with the specified configurations or
	 *         defaults.
	 */
	public static Pageable createPageRequest(Integer pageSize, Integer pageNum, Direction dir, String... properties) {
		pageSize = pageSize == null || pageSize < 2 ? 10 : pageSize;
		pageNum = pageNum == null || pageNum < 0 ? 0 : pageNum;

		properties = properties != null && properties.length > 0 ? Arrays.stream(properties)
				// only include non (null||empty) properties
				.filter(s -> s != null && s.trim().length() > 0).toArray(String[]::new) : null;

		if (properties == null || properties.length < 1) {
			return PageRequest.of(pageNum, pageSize);
		}

		dir = dir == null ? Direction.ASC : dir;

		return PageRequest.of(pageNum, pageSize, dir, properties);
	}

}
