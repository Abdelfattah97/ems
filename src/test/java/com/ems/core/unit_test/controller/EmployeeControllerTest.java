package com.ems.core.unit_test.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.notNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.ems.core.controller.EmployeeController;
import com.ems.core.dto.EmployeeDto;
import com.ems.core.dto.PageResponse;
import com.ems.core.dto.mapper.EmployeeDtoMapper;
import com.ems.core.dto.mapper.PageResponseDtoMapper;
import com.ems.core.dto.mapper.PageResponseMapperImpl;
import com.ems.core.exceptions.ResourceNotFoundException;
import com.ems.core.model.Department;
import com.ems.core.model.Employee;
import com.ems.core.service.EmployeeService;
import com.ems.core.service.pagination.PageRequestFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = EmployeeController.class, excludeFilters= @Filter(pattern = "com\\.ems.core\\.security\\.filter\\.JwtAuthenticationFilter",type = FilterType.REGEX) )
//@AutoConfigureMockMvc(addFilters = false)
public class EmployeeControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	EmployeeService empService;

	@MockBean
	EmployeeDtoMapper empMapper;
	
	@MockBean(classes = PageResponseMapperImpl.class)
	PageResponseDtoMapper<Employee, EmployeeDto> pageMapper;
	
	@Test
	@WithMockUser
	public void findById() throws Exception {

		Department dept = new Department();
		dept.setDeptId(1L);
		dept.setDeptName("dept 1");
		Employee emp1 = new Employee();
		emp1.setEmpId(1L);
		emp1.setFirstName("firstName");
		emp1.setLastName("lastName");
		emp1.setEmail("email");
		emp1.setPosition("Position");
		emp1.setSalary(1000.0);
		emp1.setDepartment(dept);

		Mockito.when(empService.findById(1L)).thenReturn(emp1);

		EmployeeDto dto = new EmployeeDto();

		dto.setEmpId(emp1.getEmpId());
		dto.setFirstName(emp1.getFirstName());
		dto.setLastName(emp1.getLastName());
		dto.setEmail(emp1.getEmail());
		dto.setPosition(emp1.getPosition());
		dto.setSalary(emp1.getSalary());
		dto.setDeptId(emp1.getDepartment().getDeptId());
		dto.setDeptName(emp1.getDepartment().getDeptName());

		Mockito.when(empMapper.toDto(notNull())).thenReturn(dto);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/1")).andExpect(status().isOk())
				.andExpect(jsonPath("firstName", is("firstName"))).andExpect(jsonPath("lastName", is("lastName")))
				.andExpect(jsonPath("email", is("email"))).andExpect(jsonPath("salary", is(1000.0)))
				.andExpect(jsonPath("position", is("Position")))
				.andExpect(jsonPath("deptId", is(dept.getDeptId().intValue())))
				.andExpect(jsonPath("deptName", is(dept.getDeptName())));
	}

	@Test
	@WithMockUser(username = "admin", roles = "user")
	public void findById_notExists() throws Exception {

		Mockito.when(empService.findById(1L)).thenThrow(new ResourceNotFoundException("No Employee with id: 1 Found!"));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/1")).andExpect(status().isNotFound())
				.andExpect(content().string("No Employee with id: 1 Found!"));

	}

	@Test
	@WithMockUser
	public void findfindAllPageable() throws Exception {
		int pageNum = 0, pageSize = 5;
		Direction dir = Direction.ASC;
		String[] sortBy = { "empId" };

		Department dept = new Department();
		dept.setDeptId(1L);
		dept.setDeptName("dept 1");
		Employee emp1 = new Employee();
		emp1.setEmpId(1L);
		emp1.setFirstName("firstName");
		emp1.setLastName("lastName");
		emp1.setEmail("email");
		emp1.setPosition("Position");
		emp1.setSalary(1000.0);
		emp1.setDepartment(dept);

		EmployeeDto dto = new EmployeeDto();

		dto.setEmpId(emp1.getEmpId());
		dto.setFirstName(emp1.getFirstName());
		dto.setLastName(emp1.getLastName());
		dto.setEmail(emp1.getEmail());
		dto.setPosition(emp1.getPosition());
		dto.setSalary(emp1.getSalary());
		dto.setDeptId(emp1.getDepartment().getDeptId());
		dto.setDeptName(emp1.getDepartment().getDeptName());

		Mockito.when(empMapper.toDto(emp1)).thenReturn(dto);

		Pageable pageReq = PageRequestFactory.createPageRequest(pageSize, pageNum, dir, sortBy);
		List<Employee> emps = List.of(emp1);

		Mockito.when(empService.findAll(pageReq))
				.thenReturn(PageResponse.<Employee>builder().content(emps).pageNumber(pageNum).pageSize(pageSize)
						.totalElements(emps.size()).totalPages(emps.size() / pageSize).build());

		PageResponse<EmployeeDto> pageRespDto = PageResponse.<EmployeeDto>builder().content(List.of(dto))
				.pageNumber(pageNum).pageSize(pageSize).totalElements(emps.size())
				.totalPages((int) Math.ceil((double) emps.size() / pageSize)).build();

		Mockito.when(pageMapper.toDto(Mockito.any(), Mockito.any())).thenReturn(pageRespDto);
		assertThat(emps).isNotEmpty();

		mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/paged"))
				.andExpect(jsonPath("$.content[0].empId", is(dto.getEmpId().intValue())))
				.andExpect(jsonPath("$.content[0].firstName", is(dto.getFirstName())))
				.andExpect(jsonPath("$.content[0].lastName", is(dto.getLastName())))
				.andExpect(jsonPath("$.content[0].email", is(dto.getEmail())))
				.andExpect(jsonPath("$.content[0].position", is(dto.getPosition())))
				.andExpect(jsonPath("$.content[0].salary", is(dto.getSalary())))
				.andExpect(jsonPath("$.content[0].deptId", is(dto.getDeptId().intValue())))
				.andExpect(jsonPath("$.content[0].deptName", is(dto.getDeptName())))
				.andExpect(jsonPath("pageNumber", is(pageNum))).andExpect(jsonPath("pageSize", is(pageSize)))
				.andExpect(jsonPath("totalElements", is(1))).andExpect(jsonPath("totalPages", is(1)));
	}

	@Test
	@WithMockUser(username = "admin", roles = "admin")
	public void update() throws Exception {
		Department dept = new Department();
		dept.setDeptId(1L);
		dept.setDeptName("dept 1");
		Employee emp1 = new Employee();
		emp1.setEmpId(1L);
		emp1.setFirstName("firstName");
		emp1.setLastName("lastName");
		emp1.setEmail("email@gmail.com");
		emp1.setPosition("Position");
		emp1.setSalary(1000.0);
		emp1.setDepartment(dept);

		EmployeeDto dto = toDto(emp1);

		Mockito.when(empService.update(notNull(Employee.class))).thenReturn(emp1);
		Mockito.when(empMapper.toDto(notNull(Employee.class))).thenReturn(dto);
		Mockito.when(empMapper.toEntity(notNull(EmployeeDto.class))).thenReturn(emp1);

		String json = new ObjectMapper().writeValueAsString(dto);

		mockMvc.perform(MockMvcRequestBuilders.put("/api/employees").contentType(MediaType.APPLICATION_JSON)
				.content(json).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(status().isOk())
				.andExpect(jsonPath("firstName", is("firstName"))).andExpect(jsonPath("lastName", is("lastName")))
				.andExpect(jsonPath("email", is("email@gmail.com"))).andExpect(jsonPath("salary", is(1000.0)))
				.andExpect(jsonPath("position", is("Position")))
				.andExpect(jsonPath("deptId", is(dept.getDeptId().intValue())))
				.andExpect(jsonPath("deptName", is(dept.getDeptName())));
	}

	@Test
	@WithMockUser
	public void update_invalid() throws Exception {
		Department dept = createValidDept();
		Employee emp1 = createValidEmp(dept);
		EmployeeDto dto = toDto(emp1);
		dto.setFirstName("hello java");
		dto.setDeptId(null);
		Mockito.when(empService.update(notNull(Employee.class))).thenReturn(emp1);
		Mockito.when(empMapper.toDto(notNull(Employee.class))).thenReturn(dto);
		Mockito.when(empMapper.toEntity(notNull(EmployeeDto.class))).thenReturn(emp1);

		String json = new ObjectMapper().writeValueAsString(dto);

		mockMvc.perform(MockMvcRequestBuilders.put("/api/employees").contentType(MediaType.APPLICATION_JSON)
				.content(json).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(status().isBadRequest())
				.andExpect(jsonPath("message", is("Invalid Arguments!")))
				.andExpect(jsonPath("$.errors[?(@.field == 'firstName')].message")
						.value("This Field Should Contain One Alphabetic Word!"))
				.andExpect(jsonPath("$.errors[?(@.field == 'deptId')].message")
						.value("The Employee's Department Id is mandatory!"));
	}

	@Test
	@WithMockUser
	public void deleteById() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/api/employees/1").with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void insert() throws Exception {
		Department dept = createValidDept();
		Employee emp = createValidEmp(dept);
		EmployeeDto dto = toDto(emp);

		Mockito.when(empService.insert(notNull(Employee.class))).thenReturn(emp);
		Mockito.when(empMapper.toDto(notNull(Employee.class))).thenReturn(dto);
		Mockito.when(empMapper.toEntity(notNull(EmployeeDto.class))).thenReturn(emp);

		String json = new ObjectMapper().writeValueAsString(dto);

		mockMvc.perform(post("/api/employees").content(json).contentType(MediaType.APPLICATION_JSON)
				.with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(status().isOk())
				.andExpect(jsonPath("firstName", is(emp.getFirstName())))
				.andExpect(jsonPath("lastName", is(emp.getLastName()))).andExpect(jsonPath("email", is(emp.getEmail())))
				.andExpect(jsonPath("salary", is(emp.getSalary())))
				.andExpect(jsonPath("position", is(emp.getPosition())))
				.andExpect(jsonPath("deptId", is(dept.getDeptId().intValue())))
				.andExpect(jsonPath("deptName", is(dept.getDeptName())));
	}

	@Test
	@WithMockUser
	public void insert_invalid() throws Exception {
		Department dept = createValidDept();
		Employee emp = createValidEmp(dept);
		EmployeeDto dto = toDto(emp);
		dto.setEmail("email");
		Mockito.when(empService.insert(notNull(Employee.class))).thenReturn(emp);
		Mockito.when(empMapper.toDto(notNull(Employee.class))).thenReturn(dto);
		Mockito.when(empMapper.toEntity(notNull(EmployeeDto.class))).thenReturn(emp);

		String json = new ObjectMapper().writeValueAsString(dto);

		mockMvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(json)
				.with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(status().isBadRequest())
				.andExpect(jsonPath("message", is("Invalid Arguments!")))
				.andExpect(jsonPath("$.errors[?(@.field == 'email')].message").value("Provided Email is not valid!"));
	}

	private List<Employee> createEmployeeList() {

		Department dummyDept = new Department();
		dummyDept.setDeptId(0L);
		dummyDept.setDeptName("dummyDept");
		List<Employee> employeeList = new ArrayList<Employee>();

		Long empId = 1L;
		String firstName = "First";
		String lastName = "Last";
		String position = " position";
		String email = "@gmail.com";
		double salary = 1000.0;

		for (int i = 0; i < 10; i++) {

			String prefix = "emp" + String.valueOf(i + 1);

			Employee emp = new Employee();
			emp.setFirstName(prefix + firstName);
			emp.setLastName(prefix + lastName);
			emp.setPosition(prefix + position);
			emp.setEmail(prefix + email);
			emp.setEmpId(empId++);
			emp.setSalary(++salary);
			emp.setDepartment(dummyDept);

			employeeList.add(emp);
		}

		return employeeList;

	}

	private EmployeeDto toDto(Employee emp) {

		EmployeeDto dto = new EmployeeDto();
		dto.setEmpId(emp.getEmpId());
		dto.setFirstName(emp.getFirstName());
		dto.setLastName(emp.getLastName());
		dto.setEmail(emp.getEmail());
		dto.setSalary(emp.getSalary());
		dto.setPosition(emp.getPosition());
		dto.setDeptName(emp.getDepartment().getDeptName());
		dto.setDeptId(emp.getDepartment().getDeptId());
		return dto;

	}

	private Employee createValidEmp(Department dept) {

		Employee emp1 = new Employee();
		emp1.setEmpId(1L);
		emp1.setFirstName("firstName");
		emp1.setLastName("lastName");
		emp1.setEmail("email@gmail.com");
		emp1.setPosition("Position");
		emp1.setSalary(1000.0);
		emp1.setDepartment(dept);
		return emp1;
	}

	private Department createValidDept() {
		Department dept = new Department();
		dept.setDeptId(1L);
		dept.setDeptName("dept 1");
		return dept;
	}

}
