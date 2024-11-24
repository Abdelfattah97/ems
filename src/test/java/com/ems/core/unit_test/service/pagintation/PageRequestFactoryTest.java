package com.ems.core.unit_test.service.pagintation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import com.ems.core.service.pagination.PageRequestFactory;

public class PageRequestFactoryTest {

	@Test
	public void INSTANCE_OF_PAGEREQUEST() {
		Pageable req = PageRequestFactory.createPageRequest(10, 1, Direction.ASC, "first");
		assertInstanceOf(PageRequest.class, req);
	}

	@Test
	public void WHEN_SIZE_LESS_THAN_2_OR_NULL_DEFAULT_IS_TEN() {
		// Given: A PageRequestFactory with size set to null
		Pageable req = PageRequestFactory.createPageRequest(null, 1, Direction.ASC, "first");

		assertInstanceOf(PageRequest.class, req);
		assertEquals(10, req.getPageSize());

		// Given: A PageRequestFactory with size set to 1
		req = PageRequestFactory.createPageRequest(1, 1, Direction.ASC, "first");
		assertEquals(10, req.getPageSize());

	}
	

	@Test
	public void WHEN_PAGE_LESS_THAN_0_OR_NULL_DEFAULT_IS_TEN() {
		// Given: A PageRequestFactory with page set to null
		Pageable req = PageRequestFactory.createPageRequest(10, null, Direction.ASC, "first");

		assertEquals(0, req.getPageNumber());
		// Given: A PageRequestFactory with page set to -1
		 req = PageRequestFactory.createPageRequest(10, -1, Direction.ASC, "first");
		
		assertEquals(0, req.getPageNumber());

	}

	@Test
	public void WHEN_DIRECTION_NULL_DEFAULT_IS_ASC() {
		// Given: A PageRequestFactory with direction set to null
		Pageable req = PageRequestFactory.createPageRequest(10, 0, null, "first");

		assertNotNull(req.getSort());
		assertThat(req.getSort().get()).allMatch(o -> o.getDirection().equals(Direction.ASC));

	}

	@Test
	public void PROPERTIES_ASSIGNED_TO_REQUEST() {
		String[] properties = { "first", "second", "third" };
		// Given: A PageRequestFactory with properties
		Pageable req = PageRequestFactory.createPageRequest(10, 0, Direction.ASC, properties);

		assertNotNull(req.getSort());
		assertThat(req.getSort().get()).map(o -> o.getProperty()).containsExactlyInAnyOrder(properties);
	}

	@Test
	public void WHEN_PROPERTIES_IS_NULL_NO_ORDER_IS_SET() {
		String[] properties = null;
		// Given: A PageRequestFactory with null properties
		Pageable req = PageRequestFactory.createPageRequest(10, 0, Direction.ASC, properties);

		assertFalse(req.getSort().isSorted());

	}

	@Test
	public void WHEN_A_PROPERTY_IS_EMPTY_IT_IS_IGNORED() {
		String[] properties = { "", "one", "  " };
		// Given: A PageRequestFactory with empty property
		Pageable req = PageRequestFactory.createPageRequest(10, 0, Direction.ASC, properties);

		assertTrue(req.getSort().isSorted());
		assertThat(req.getSort().get()).map(o -> o.getProperty()).containsOnly("one");
	}

	@Test
	public void WHEN_A_PROPERTY_IS_NULL_IT_IS_IGNORED() {
		String[] properties = { null, "one", null };
		// Given: A PageRequestFactory with null property
		Pageable req = PageRequestFactory.createPageRequest(10, 0, Direction.ASC, properties);

		assertTrue(req.getSort().isSorted());
		assertThat(req.getSort().get()).map(o -> o.getProperty()).containsOnly("one");
	}

	@Test
	public void WHEN_ALL_ARRY_PROPERTIES_ARE_NULL_PAGE_IS_UNSORTED() {
		String[] properties = { null, null };
		// Given: A PageRequestFactory with all array properties are null
		Pageable req = PageRequestFactory.createPageRequest(10, 0, Direction.ASC, properties);

		assertFalse(req.getSort().isSorted());

	}

	@Test
	public void WHEN_ALL_ARRY_PROPERTIES_ARE_EMPTY_PAGE_IS_UNSORTED() {
		String[] properties = { "", "  " };
		// Given: A PageRequestFactory with all array properties are empty
		Pageable req = PageRequestFactory.createPageRequest(10, 0, Direction.ASC, properties);

		assertFalse(req.getSort().isSorted());

	}

}
