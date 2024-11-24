package com.ems.core.unit_test.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import com.ems.core.dto.PageResponse;
import com.ems.core.exceptions.EntityDependencyMissingException;
import com.ems.core.exceptions.NullIdUpdateException;
import com.ems.core.exceptions.ResourceNotFoundException;
import com.ems.core.model.Department;
import com.ems.core.model.Employee;
import com.ems.core.repository.DepartmentRepository;
import com.ems.core.repository.EmployeeRepository;
import com.ems.core.service.EmployeeService;

import com.ems.core.service.EmployeeServiceImpl;


import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;


//@SpringBootTest(properties = "spring.profiles.active=test")
@ExtendWith(MockitoExtension.class)
@Transactional
public class EmployeeServiceImplTest {

//	@Mock
	@InjectMocks
	private EmployeeService empService = new EmployeeServiceImpl();
	@Mock
	private EmployeeRepository empRepo;
	@Mock
	private DepartmentRepository deptRepo;

	
	public Employee createValidEmp() {
		Department dept  = new Department();
		dept.setDeptId(1L);
		dept.setDeptName("testDept");
		Employee emp = new Employee();
		emp.setDepartment(dept);
		emp.setEmpId(1L);
		emp.setFirstName("john");
		emp.setLastName("doe");
		emp.setEmail("abc@gmail.com");
		emp.setSalary(1000.0);
		emp.setPosition("test entity");
		return emp;
	}


//	@Nested
	class ConstraintViolationExceptionThrown {

		@BeforeEach
		public void deptMock() {
			Mockito.when(deptRepo.existsById(anyLong())).thenReturn(true);
		}
		
		@Test
		public void invalidFirstName() {
			Employee emp = createValidEmp();
			// Firstname
			emp.setFirstName("john2");
			assertThrows(ConstraintViolationException.class, () -> empService.insert(emp));
			emp.setFirstName("john");
		}

		@Test
		public void invalidLastName() {
			Employee emp = createValidEmp();
			// Lastname
			emp.setLastName("doe2");
			assertThrows(ConstraintViolationException.class, () -> empService.insert(emp));
			emp.setLastName("doe");
		}

		@Test
		public void invalidEmail() {
			Employee emp = createValidEmp();
			// Email
			emp.setEmail("invalidemail-.com");
			assertThrows(ConstraintViolationException.class, () -> empService.insert(emp));
			emp.setEmail("validEmail@gmail.com");
		}

		@Test
		public void invalidPosition() {
			Employee emp = createValidEmp();
			// position
			emp.setPosition("invalid position2");
			assertThrows(ConstraintViolationException.class, () -> empService.insert(emp));
			emp.setPosition("test position");
		}

		@Test
		public void invalidSalary() {
			Employee emp = createValidEmp();
			// salary
			emp.setSalary(0.0);
			assertThrows(ConstraintViolationException.class, () -> empService.insert(emp));
			emp.setSalary(1000.0);
		}

	}

	@Nested
	class InsertionTests {

		@Test
		public void WHEN_NULL_EMPLOYEE_INSERTED_NULLPOINTER_IS_THROWN() {
			Employee employee = null;
			assertThrows(NullPointerException.class, () -> empService.insert(employee));
		}

		@Test
		public void Employee_is_inserted() {
			Employee emp = createValidEmp();
			Long deptId = emp.getDepartment().getDeptId();
			Mockito.when(deptRepo.existsById(deptId)).thenReturn(true);
			Mockito.when(empRepo.save(emp)).thenReturn(emp);
			Employee insertedEmp = empService.insert(emp);
			assertNotNull(insertedEmp);
			assertEquals(emp, insertedEmp);
		}

		@Nested
		class RelationshipParent {

			@Test
			public void MissingDependencyThrown_When_Department_NotFound() {
				Employee emp = createValidEmp();
				Department dept = new Department();
				dept.setDeptId(0L);
				emp.setDepartment(dept);

				assertThrows(EntityDependencyMissingException.class, () -> empService.insert(emp));
			}

			@Test
			public void IllegalArgumentThrown_When_Department_Or_DeptId_Is_Null() {
				Employee emp = createValidEmp();
				Department dept = new Department();
				dept.setDeptId(null);
				emp.setDepartment(dept);
				assertThrows(IllegalArgumentException.class, () -> empService.insert(emp));
				emp.setDepartment(null);
				assertThrows(IllegalArgumentException.class, () -> empService.insert(emp));
			}

		}

	}

	@Nested
	class UpdateTests {

		@Test
		public void WHEN_NULL_EMPLOYEE_UPDATED_NULLPOINTER_IS_THROWN() {
			Employee employee = null;
			assertThrows(NullPointerException.class, () -> empService.update(employee));
		}

		@Test
		public void WHEN_NULL_empId_NuLLIDUpdate_Is_Thrown() {
			Employee emp = createValidEmp();
			emp.setEmpId(null);
			assertThrows(NullIdUpdateException.class, () -> empService.update(emp));
		}

		@Test
		public void WHEN_EMPLOYEE_NOTFOUND_RESOURCENOTFOUND_IS_THROWN() {
			Employee emp = createValidEmp();
			emp.setEmpId(0L);

			Mockito.when(empRepo.existsById(emp.getEmpId())).thenReturn(false);
			assertThrows(ResourceNotFoundException.class, () -> empService.update(emp));
		}

		@Test
		public void EMPLOYEE_IS_UPDATED() {

			String newEmail = "updatedEmail@gmail.com";
			String newFirstName = "updatedFirstName";
			String newLastName = "updatedLastName";
			String newPosition = "updated Position";
			double newSalary = 2000.0;

			Employee emp = createValidEmp();
			Employee insertedEmp=emp;
			insertedEmp.setEmail(newEmail);
			insertedEmp.setFirstName(newFirstName);
			insertedEmp.setLastName(newLastName);
			insertedEmp.setPosition(newPosition);
			insertedEmp.setSalary(newSalary);

			Mockito.when(deptRepo.existsById(emp.getDepartment().getDeptId())).thenReturn(true);
			Mockito.when(empRepo.existsById(insertedEmp.getEmpId())).thenReturn(true);
			Mockito.when(empRepo.save(insertedEmp)).thenReturn(insertedEmp);
			

			Employee updatedEmp = empService.update(insertedEmp);

			assertEquals(updatedEmp.getFirstName(), newFirstName);
			assertEquals(updatedEmp.getLastName(), newLastName);
			assertEquals(updatedEmp.getEmail(), newEmail);
			assertEquals(updatedEmp.getPosition(), newPosition);
			assertEquals(updatedEmp.getSalary(), newSalary);

		}

	}


	@Nested
	class FindByIdTests {
		@Test
		public void WHEN_FINDBYID_NOTFOUND_RESOURCENOTFOUND_IS_THROWN() {

			Mockito.when(empRepo.findById(0L)).thenReturn(Optional.empty());

			assertThrows(ResourceNotFoundException.class, () -> empService.findById(0L));
		}

		@Test
		public void EMPLOYEE_IS_FOUND_BY_ID() {
			Employee emp = createValidEmp();
			Mockito.when(empRepo.findById(emp.getEmpId())).thenReturn(Optional.of(emp));
			assertEquals(emp, empService.findById(emp.getEmpId()));
		}
		@Test
		public void WHEN_ID_IS_NULL_NULLPOINTER_IS_THROWN() {
//			Mockito.when(empService.findById(nullable(Long.class))).thenThrow(NullPointerException.class);

			assertThrows(NullPointerException.class, () -> empService.findById(null));
		}
	}

	@Nested
	class PaginationTests{
		
		@Test
		public void WHEN_PAGINATION_IS_REQUESTED_PAGINATED_EMPLOYEES_ARE_RETURNED() {
		
			Employee emp1 = createValidEmp();
			emp1.setEmail("emp1@gmail.com");

			Employee emp2 = createValidEmp();
			emp2.setEmail("emp2@gmail.com");
			Employee emp3 = createValidEmp();
			emp3.setEmail("emp3@gmail.com");
			Employee emp4 = createValidEmp();
			emp4.setEmail("emp4@gmail.com");
			Employee emp5 = createValidEmp();
			emp5.setEmail("emp5@gmail.com");
			Employee emp6 = createValidEmp();
			emp6.setEmail("emp6@gmail.com");
			Employee emp7 = createValidEmp();
			emp7.setEmail("emp7@gmail.com");
			Employee emp8 = createValidEmp();
			emp8.setEmail("emp8@gmail.com");
			
			Employee[] emps = {emp1,emp2,emp3,emp4,emp5,emp6,emp7,emp8};
			List<Employee> empList = List.of(emps);

			int pageNum = 0;
            int size = 10;
            
            Page<Employee> page = new PageImpl<Employee>(empList, PageRequest.of(pageNum, size), size);
//         
            Mockito.when(empRepo.findAll(any(Pageable.class))).thenReturn(page);
            PageResponse<Employee> pageResponse = empService.findAll(PageRequest.of(pageNum, size));
            List<Employee> retEmpList = pageResponse.getContent();
            
            assertNotNull(retEmpList);
            assertThat(retEmpList).hasSizeLessThanOrEqualTo(size);
           	assertEquals(size,pageResponse.getPageSize() );
            assertEquals(pageNum,pageResponse.getPageNumber());
            assertTrue(pageResponse.getTotalElements()>=emps.length);
            assertNotNull(pageResponse.getTotalPages());
            
		}
		
	}

}
