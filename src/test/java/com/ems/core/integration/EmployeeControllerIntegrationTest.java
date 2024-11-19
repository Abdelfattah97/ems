package com.ems.core.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.ems.core.dto.EmployeeDto;
import com.ems.core.dto.mapper.EmployeeDtoMapper;
import com.ems.core.model.AppRole;
import com.ems.core.model.AppUser;
import com.ems.core.model.Department;
import com.ems.core.model.Employee;
import com.ems.core.repository.AppRoleRepository;
import com.ems.core.repository.AppUserRepository;
import com.ems.core.repository.DepartmentRepository;
import com.ems.core.repository.EmployeeRepository;
import com.ems.core.security.JwtService;
import com.ems.core.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@Transactional
public class EmployeeControllerIntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	DepartmentRepository deptRepo;

	@Autowired
	EmployeeService empService;

	@Autowired
	EmployeeDtoMapper empMapper;

	@Autowired
	JwtService jwtService;

	@Autowired
	AppUserRepository userRepo;

	@Autowired
	AppRoleRepository roleRepo;

	@Autowired
	EmployeeRepository empRepo;

	private String generateToken(String username, String role) {

		AppRole appRole = roleRepo.findByRoleName(role).orElseGet(() -> {
			AppRole r = new AppRole();
			r.setRoleName(role);
			return roleRepo.save(r);
		});
		AppUser user = userRepo.findByUsername(username).orElseGet(() -> {
			AppUser u = new AppUser();
			u.setUsername(username);
			u.setPassword("password");
			return u;
		});
		Set<AppRole> set = new HashSet<>();
		set.add(appRole);
		user.setRoles(set);
		user = userRepo.save(user);
		return jwtService.generateToken(username);
	}

	private Employee insertEmployee() {
		Department dept = deptRepo.findByDeptName("testDept").orElseGet(() -> {
			Department d = new Department();
			d.setDeptName("testDept");
			return deptRepo.save(d);
		});
		Employee emp1 = new Employee();
		emp1.setEmpId(1L);
		emp1.setFirstName("empFirst");
		emp1.setLastName("empLast");
		emp1.setEmail("emp1@gmail.com");
		emp1.setPosition("position");
		emp1.setSalary(1000.0);
		emp1.setDepartment(dept);
		emp1 = empService.insert(emp1);
		return emp1;
	}

	private List<Employee> insertEmployeeList() {

		Department dummyDept = new Department();
		dummyDept.setDeptName("dummyDept");
		dummyDept = deptRepo.save(dummyDept);

		List<Employee> employeeList = new ArrayList<Employee>();

		String firstName = "First";
		String lastName = "Last";
		String position = " position";
		String email = "@gmail.com";
		double salary = 1000.0;

		for (int i = 0; i < 10; i++) {

			String prefix = "emp" + String.valueOf(i + 1);

			Employee emp = new Employee();
			emp.setFirstName("emp" + firstName);
			emp.setLastName("emp" + lastName);
			emp.setPosition("emp" + position);
			emp.setEmail(prefix + email);
			emp.setSalary(++salary);
			emp.setDepartment(dummyDept);

			employeeList.add(emp);
		}

		return empRepo.saveAll(employeeList);
	}

	private Employee createEmployee() {
		Department dept = deptRepo.findByDeptName("testDept").orElseGet(() -> {
			Department d = new Department();
			d.setDeptName("testDept");
			return deptRepo.save(d);
		});
		Employee emp1 = new Employee();
		emp1.setEmpId(1L);
		emp1.setFirstName("empFirst");
		emp1.setLastName("empLast");
		emp1.setEmail("emp1@gmail.com");
		emp1.setPosition("position");
		emp1.setSalary(1000.0);
		emp1.setDepartment(dept);
		return emp1;
	}

	private String bearer(String token) {
		return String.format("Bearer %s", token);
	}

	@Nested
	class Authorization {

		@Nested
		class findById {

			@Test
//			@WithMockUser(username = "user", roles = "user")
			public void findById_User_Return_OK() throws Exception {
				Employee emp = insertEmployee();
				mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/" + emp.getEmpId()).header("Authorization",
						bearer(generateToken("user", "user")))).andExpect(status().isOk());
			}

			@Test
//			@WithMockUser(username = "admin", roles = "admin")
			public void findById_admin_Return_OK() throws Exception {
				Employee emp = insertEmployee();
				mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/" + emp.getEmpId()).header("Authorization",
						bearer(generateToken("admin", "admin")))).andExpect(status().isOk());
			}

			@Test
//			@WithMockUser(username = "user", roles = "none")
			public void findById_NoRole_Return_forbidden() throws Exception {
				Employee emp = insertEmployee();
				mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/" + emp.getEmpId()).header("Authorization",
						bearer(generateToken("user", "none")))).andExpect(status().isForbidden());
			}

		}

		@Nested
		class findAllPaged {

			@Test
//			@WithMockUser(username = "user", roles = "user")
			public void findAllPaged_user_return_ok() throws Exception {
				insertEmployee();

				mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/paged").queryParam("pageNum", "0")
						.queryParam("pageSize", "10").header("Authorization", bearer(generateToken("user", "user"))))
						.andExpect(status().isOk());
			}

			@Test
//			@WithMockUser(username = "admin", roles = "admin")
			public void findAllPaged_admin_return_ok() throws Exception {
				insertEmployee();
				mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/paged").queryParam("pageNum", "0")
						.queryParam("pageSize", "10").header("Authorization", bearer(generateToken("admin", "admin"))))
						.andExpect(status().isOk());
			}

			@Test
//			@WithMockUser(username = "user", roles = "none")
			public void findAllPaged_noRole_return_forbidden() throws Exception {
				insertEmployee();
				mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/paged").queryParam("pageNum", "0")
						.queryParam("pageSize", "10").header("Authorization", bearer(generateToken("user", "none"))))
						.andExpect(status().isForbidden());
			}

		}

		@Nested
		class insert {

			@Test
			public void insert_admin_return_ok() throws Exception {
				Employee emp = createEmployee();

				String json = new ObjectMapper().writeValueAsString(empMapper.toDto(emp));

				mockMvc.perform(MockMvcRequestBuilders.post("/api/employees").contentType(MediaType.APPLICATION_JSON)
						.content(json).header("Authorization", bearer(generateToken("admin", "admin"))))
						.andExpect(status().isOk());
			}

			@Test
			public void insert_user_return_forbidden() throws Exception {
				Employee emp = createEmployee();

				String json = new ObjectMapper().writeValueAsString(emp);

				mockMvc.perform(MockMvcRequestBuilders.post("/api/employees").contentType(MediaType.APPLICATION_JSON)
						.content(json).header("Authorization", bearer(generateToken("user", "user"))))
						.andExpect(status().isForbidden());
			}
		}

		@Nested
		class delete_by_id {

			@Test
			public void delete_admin_return_ok() throws Exception {
				Employee emp = insertEmployee();

				String json = new ObjectMapper().writeValueAsString(empMapper.toDto(emp));
				//employee exists before request
				assertTrue(empRepo.existsById(emp.getEmpId()));
				mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/" + emp.getEmpId())
						.contentType(MediaType.APPLICATION_JSON).content(json)
						.header("Authorization", bearer(generateToken("admin", "admin")))).andExpect(status().isOk());

				//employee doesn't exist after request
				assertFalse(empRepo.existsById(emp.getEmpId()));
			}

			@Test
			public void delete_user_return_forbidden() throws Exception {
				Employee emp = insertEmployee();

				String json = new ObjectMapper().writeValueAsString(empMapper.toDto(emp));
				
				//employee exists before request
				assertTrue(empRepo.existsById(emp.getEmpId()));
				mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/" + emp.getEmpId())
						.contentType(MediaType.APPLICATION_JSON).content(json)
						.header("Authorization", bearer(generateToken("user", "user")))).andExpect(status().isForbidden());

				//employee exists after request
				assertTrue(empRepo.existsById(emp.getEmpId()));
			}
		}
		
		@Nested
		class update {
			@Test
			public void updating_by_userRole_return_forbidden() throws Exception {

				Employee emp = insertEmployee();

				Employee updatedEmp = emp;
				updatedEmp.setFirstName("updated");
				updatedEmp.setLastName("updated");
				updatedEmp.setEmail("updated_email@gmail.com");

				String json = new ObjectMapper().writeValueAsString(updatedEmp);

				mockMvc.perform(MockMvcRequestBuilders.put("/api/employees").contentType(MediaType.APPLICATION_JSON)
						.content(json).header("Authorization", bearer(generateToken("user", "user"))))
						.andExpect(status().isForbidden());
				
			}
		}
	}

	@Test
	public void fetching_nonExisting_return_404_NotFound() throws Exception {
		Long id = 0L;
		mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/" + id).header("Authorization",
				bearer(generateToken("admin", "admin")))).andExpect(status().isNotFound())
				.andExpect(content().string(String.format("No Employee found with id:%s", id)));

	}

	@Test
	public void fetches_employee_with_id_and_return_200_ok() throws Exception {
		Employee emp = insertEmployee();
		Long id = emp.getEmpId();
		mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/" + id).header("Authorization",
				bearer(generateToken("admin", "admin")))).andExpect(status().isOk())
				.andExpect(jsonPath("firstName", is(emp.getFirstName())))
				.andExpect(jsonPath("lastName", is(emp.getLastName()))).andExpect(jsonPath("email", is(emp.getEmail())))
				.andExpect(jsonPath("position", is(emp.getPosition())))
				.andExpect(jsonPath("salary", is(emp.getSalary())))
				.andExpect(jsonPath("deptId").value(emp.getDepartment().getDeptId()))
				.andExpect(jsonPath("deptName", is(emp.getDepartment().getDeptName())));
	}

	@Test
	public void should_fetch_page_ordered() throws Exception {

		List<Employee> empList = insertEmployeeList().stream()
				.sorted(Comparator.comparing(Employee::getEmpId).reversed()).toList();
		int pageNum = 0, pageSize = 10;
		ResultActions resp = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/paged")
				.queryParam("pageNum", String.valueOf(pageNum)).queryParam("pageSize", String.valueOf(pageSize))
				.queryParam("dir", "DESC").queryParam("sortBy", "empId")
				.header("Authorization", bearer(generateToken("admin", "admin"))));

		resp.andExpect(status().isOk()).andExpect(jsonPath("content").isArray())
				.andExpect(jsonPath("content.length()").value(empList.size()))
				.andExpect(jsonPath("pageNumber").value(pageNum)).andExpect(jsonPath("pageSize").value(pageSize));

		for (int i = 0; i < empList.size(); i++) {
			String target = String.format("content[%s].", String.valueOf(i));
			Employee emp = empList.get(i);
			resp.andExpect(jsonPath(target + "empId").value((emp.getEmpId())));
			resp.andExpect(jsonPath(target + "email", is(emp.getEmail())));
		}

	}

	@Test
	public void should_update_employees() throws Exception {

		Employee emp = insertEmployee();

		EmployeeDto updatedEmp = empMapper.toDto(emp);
		updatedEmp.setFirstName("updated");
		updatedEmp.setLastName("updated");
		updatedEmp.setEmail("updated_email@gmail.com");
		
		String json = new ObjectMapper().writeValueAsString(updatedEmp);

		mockMvc.perform(MockMvcRequestBuilders.put("/api/employees").contentType(MediaType.APPLICATION_JSON)
				.content(json).header("Authorization", bearer(generateToken("admin", "admin"))))
				.andExpect(status().isOk()).andExpect(jsonPath("firstName", is(updatedEmp.getFirstName())))
				.andExpect(jsonPath("lastName", is(updatedEmp.getLastName())))
				.andExpect(jsonPath("email", is(updatedEmp.getEmail())))
				.andExpect(jsonPath("position", is(emp.getPosition())));
	}

	@Test
	public void should_delete_employee_by_id() throws Exception {
		Employee emp = insertEmployee();
		
		//employee exists before request
		assertTrue(empRepo.existsById(emp.getEmpId()));
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/" + emp.getEmpId())
				.header("Authorization", bearer(generateToken("admin", "admin")))).andExpect(status().isOk());
		//employee exists after request
		assertFalse(empRepo.existsById(emp.getEmpId()));
	}
	
	@Test
	public void should_insert_employee() throws Exception {
		Employee emp = createEmployee();
		emp.setFirstName("inserted");;
		emp.setLastName("inserted");
		emp.setEmail("inserted@mail.com");
		
		String json = new ObjectMapper().writeValueAsString(empMapper.toDto(emp));
	
		mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.header("Authorization", bearer(generateToken("admin", "admin"))))
		.andExpect(status().isOk())
		.andExpect(jsonPath("empId").isNotEmpty())
		.andExpect(jsonPath("empId").isNumber())
		.andExpect(jsonPath("firstName", is("inserted")))
		.andExpect(jsonPath("lastName", is("inserted")))
		.andExpect(jsonPath("email", is("inserted@mail.com")));
		
	}
	
}
