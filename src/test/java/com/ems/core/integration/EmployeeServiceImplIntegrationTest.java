package com.ems.core.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import com.ems.core.dto.PageResponse;
import com.ems.core.exceptions.EntityDependencyMissingException;
import com.ems.core.exceptions.NullIdUpdateException;
import com.ems.core.exceptions.ResourceNotFoundException;
import com.ems.core.model.Department;
import com.ems.core.model.Employee;
import com.ems.core.repository.DepartmentRepository;
import com.ems.core.repository.EmployeeRepository;
import com.ems.core.service.EmployeeService;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;

@SpringBootTest(properties = "spring.profiles.active=test")
@Transactional
public class EmployeeServiceImplIntegrationTest {

	private EmployeeService empService;
	private EmployeeRepository empRepo;
	private DepartmentRepository deptRepo;


	@Autowired
	public EmployeeServiceImplIntegrationTest(EmployeeRepository empRepo, EmployeeService empService,
			DepartmentRepository deptRepo) {
		this.deptRepo = deptRepo;
		this.empService = empService;
		this.empRepo = empRepo;
	}

	private Department createDepartment() {
		Department testDept = new Department();
		testDept.setDeptName("testDept");
		return deptRepo.save(testDept);
	}

	public Employee createValidEmp() {
		return createValidEmp(createDepartment());
	}
	
	public Employee createValidEmp(Department dept) {
		
		Employee emp = new Employee();
		emp.setDepartment(dept);
		
		emp.setFirstName("john");
		emp.setLastName("doe");
		emp.setEmail("abc@gmail.com");
		emp.setSalary(1000.0);
		emp.setPosition("test entity");
		return emp;
	}

	@Nested
	class ConstraintViolationExceptionThrown {

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
			Employee insertedEmp = empService.insert(emp);
			assertNotNull(insertedEmp);
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
			assertThrows(NullPointerException.class, () -> empService.insert(employee));
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
			assertThrows(ResourceNotFoundException.class, () -> empService.update(emp));
		}

		public void EMPLOYEE_IS_UPDATED() {

			String newEmail = "updatedEmail@gmail.com";
			String newFirstName = "updatedFirstName";
			String newLastName = "updatedLastName";
			String newPosition = "updated Position";
			double newSalary = 2000.0;

			Employee emp = createValidEmp();
			Employee insertedEmp = empService.insert(emp);

			insertedEmp.setEmail(newEmail);
			insertedEmp.setFirstName(newFirstName);
			insertedEmp.setLastName(newLastName);
			insertedEmp.setPosition(newPosition);
			insertedEmp.setSalary(newSalary);

			Employee updatedEmp = empService.update(insertedEmp);

			assertEquals(updatedEmp.getFirstName(), newFirstName);
			assertEquals(updatedEmp.getLastName(), newLastName);
			assertEquals(updatedEmp.getEmail(), newEmail);
			assertEquals(updatedEmp.getPosition(), newPosition);
			assertEquals(updatedEmp.getSalary(), newSalary);

		}

	}

	@Test
	public void EMPLOYEE_IS_DELETED() {

		Employee emp = createValidEmp();
		Employee insertedEmp = empService.insert(emp);
		Long empId = insertedEmp.getEmpId();

		assertNotNull(empRepo.findById(empId).orElse(null));
		empService.deleteById(insertedEmp.getEmpId());
		assertNull(empRepo.findById(empId).orElse(null));

	}

	@Nested
	class FindByIdTests {
		@Test
		public void WHEN_FINDBYID_NOTFOUND_RESOURCENOTFOUND_IS_THROWN() {
			assertThrows(ResourceNotFoundException.class, () -> empService.findById(0L));
		}

		@Test
		public void EMPLOYEE_IS_FOUND_BY_ID() {
			Employee emp = createValidEmp();
			Long empId = empService.insert(emp).getEmpId();
			assertEquals(emp, empService.findById(empId));
		}
		@Test
		public void WHEN_ID_IS_NULL_NULLPOINTER_IS_THROWN() {
			assertThrows(NullPointerException.class, () -> empService.findById(null));
		}
	}

	@Nested
	class PaginationTests{
		
		@Test
		public void WHEN_PAGINATION_IS_REQUESTED_PAGINATED_EMPLOYEES_ARE_RETURNED() {
		
			Employee emp1 = createValidEmp();
			emp1.setEmail("emp1@gmail.com");
			Employee emp2 = createValidEmp(emp1.getDepartment());
			emp2.setEmail("emp2@gmail.com");
			Employee emp3 = createValidEmp(emp1.getDepartment());
			emp3.setEmail("emp3@gmail.com");
			Employee emp4 = createValidEmp(emp1.getDepartment());
			emp4.setEmail("emp4@gmail.com");
			Employee emp5 = createValidEmp(emp1.getDepartment());
			emp5.setEmail("emp5@gmail.com");
			Employee emp6 = createValidEmp(emp1.getDepartment());
			emp6.setEmail("emp6@gmail.com");
			Employee emp7 = createValidEmp(emp1.getDepartment());
			emp7.setEmail("emp7@gmail.com");
			Employee emp8 = createValidEmp(emp1.getDepartment());
			emp8.setEmail("emp8@gmail.com");
			
			Employee[] emps = {emp1,emp2,emp3,emp4,emp5,emp6,emp7,emp8};
			
			empRepo.saveAll(List.of(emps));
			
			int page = 0;
            int size = 10;
            
            PageResponse<Employee> pageResponse = empService.findAll(PageRequest.of(page, size));
            List<Employee> empList = pageResponse.getContent();
            
            assertNotNull(empList);
            assertThat(empList).hasSizeLessThanOrEqualTo(size);
           	assertEquals(size,pageResponse.getPageSize() );
            assertEquals(page,pageResponse.getPageNumber());
            assertTrue(pageResponse.getTotalElements()>=emps.length);
            assertNotNull(pageResponse.getTotalPages());
            
		}
		
	}

}
