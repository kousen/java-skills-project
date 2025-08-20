package com.oreilly.webservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for EmployeeController using the layered architecture.
 * 
 * This demonstrates:
 * - @WebMvcTest for testing only the web layer
 * - MockMvc for simulating HTTP requests
 * - @MockBean for mocking service dependencies
 * - JSON path testing for response validation
 * - Testing all HTTP methods (GET, POST, PUT, DELETE)
 * - Business logic integration testing
 */
@WebMvcTest(controllers = EmployeeController.class)
@ContextConfiguration(classes = {EmployeeController.class, GlobalExceptionHandler.class})
class EmployeeControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private EmployeeService employeeService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private Employee testEmployee;
    private List<Employee> testEmployees;
    
    @BeforeEach
    void setUp() {
        testEmployee = new Employee(1L, "John Doe", "Engineering", 75000.0);
        testEmployees = List.of(
            testEmployee,
            new Employee(2L, "Jane Smith", "Marketing", 65000.0),
            new Employee(3L, "Bob Johnson", "Engineering", 80000.0)
        );
    }
    
    // ========== BASIC ENDPOINT TESTS ==========
    
    @Test
    void testHelloEndpoint() throws Exception {
        mockMvc.perform(get("/api/employees/hello"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("Welcome to Employee Service!"));
    }
    
    @Test
    void testHealthEndpoint() throws Exception {
        when(employeeService.count()).thenReturn(3L);
        
        mockMvc.perform(get("/api/employees/health"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is("UP")))
            .andExpect(jsonPath("$.service", is("EmployeeService")))
            .andExpect(jsonPath("$.employeeCount", is("3")));
        
        verify(employeeService).count();
    }
    
    // ========== CRUD OPERATION TESTS ==========
    
    @Test
    void testGetAllEmployees() throws Exception {
        when(employeeService.findAll()).thenReturn(testEmployees);
        
        mockMvc.perform(get("/api/employees"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string("X-Total-Count", "3"))
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("John Doe")))
            .andExpect(jsonPath("$[0].department", is("Engineering")))
            .andExpect(jsonPath("$[0].salary", is(75000.0)))
            .andExpect(jsonPath("$[1].name", is("Jane Smith")))
            .andExpect(jsonPath("$[2].name", is("Bob Johnson")));
        
        verify(employeeService).findAll();
    }
    
    @Test
    void testGetEmployeeById() throws Exception {
        when(employeeService.findById(1L)).thenReturn(Optional.of(testEmployee));
        
        mockMvc.perform(get("/api/employees/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string("X-Employee-Version", "1.0"))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("John Doe")))
            .andExpect(jsonPath("$.department", is("Engineering")))
            .andExpect(jsonPath("$.salary", is(75000.0)));
        
        verify(employeeService).findById(1L);
    }
    
    @Test
    void testGetEmployeeByIdNotFound() throws Exception {
        when(employeeService.findById(999L)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/api/employees/999"))
            .andDo(print())
            .andExpect(status().isNotFound());
        
        verify(employeeService).findById(999L);
    }
    
    @Test
    void testCreateEmployee() throws Exception {
        Employee newEmployee = new Employee("Alice Johnson", "Sales", 70000.0);
        Employee savedEmployee = new Employee(4L, "Alice Johnson", "Sales", 70000.0);
        
        when(employeeService.save(any(Employee.class))).thenReturn(savedEmployee);
        
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEmployee)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "http://localhost/api/employees/4"))
            .andExpect(jsonPath("$.id", is(4)))
            .andExpect(jsonPath("$.name", is("Alice Johnson")))
            .andExpect(jsonPath("$.department", is("Sales")))
            .andExpect(jsonPath("$.salary", is(70000.0)));
        
        verify(employeeService).save(any(Employee.class));
    }
    
    @Test
    void testCreateEmployeeWithValidationErrors() throws Exception {
        Employee invalidEmployee = new Employee("", "", -1000.0);
        
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmployee)))
            .andDo(print())
            .andExpect(status().isBadRequest());
        
        verify(employeeService, never()).save(any(Employee.class));
    }
    
    @Test
    void testUpdateEmployee() throws Exception {
        Employee updatedEmployee = new Employee(1L, "John Doe Updated", "Engineering", 85000.0);
        
        when(employeeService.updateEmployee(eq(1L), any(Employee.class))).thenReturn(updatedEmployee);
        
        mockMvc.perform(put("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("John Doe Updated")))
            .andExpect(jsonPath("$.salary", is(85000.0)));
        
        verify(employeeService).updateEmployee(eq(1L), any(Employee.class));
    }
    
    @Test
    void testUpdateEmployeeNotFound() throws Exception {
        Employee employee = new Employee(999L, "Non Existent", "IT", 60000.0);
        
        when(employeeService.updateEmployee(eq(999L), any(Employee.class)))
            .thenThrow(new EmployeeNotFoundException(999L));
        
        mockMvc.perform(put("/api/employees/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
            .andDo(print())
            .andExpect(status().isNotFound());
        
        verify(employeeService).updateEmployee(eq(999L), any(Employee.class));
    }
    
    @Test
    void testDeleteEmployee() throws Exception {
        // terminateEmployee handles business logic including existence check
        
        mockMvc.perform(delete("/api/employees/1"))
            .andDo(print())
            .andExpect(status().isNoContent());
        
        verify(employeeService).terminateEmployee(1L);
    }
    
    @Test
    void testDeleteEmployeeNotFound() throws Exception {
        doThrow(new EmployeeNotFoundException(999L)).when(employeeService).terminateEmployee(999L);
        
        mockMvc.perform(delete("/api/employees/999"))
            .andDo(print())
            .andExpect(status().isNotFound());
        
        verify(employeeService).terminateEmployee(999L);
    }
    
    // ========== SEARCH AND FILTER TESTS ==========
    
    @Test
    void testSearchEmployeesByDepartment() throws Exception {
        List<Employee> engineeringEmployees = List.of(testEmployee, 
            new Employee(3L, "Bob Johnson", "Engineering", 80000.0));
        
        when(employeeService.searchEmployees("Engineering", 0.0)).thenReturn(engineeringEmployees);
        
        mockMvc.perform(get("/api/employees?department=Engineering"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string("X-Total-Count", "2"))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].department", is("Engineering")))
            .andExpect(jsonPath("$[1].department", is("Engineering")));
        
        verify(employeeService).searchEmployees("Engineering", 0.0);
    }
    
    @Test
    void testSearchEmployeesByMinSalary() throws Exception {
        List<Employee> highSalaryEmployees = List.of(
            new Employee(3L, "Bob Johnson", "Engineering", 80000.0));
        
        when(employeeService.searchEmployees(null, 80000.0)).thenReturn(highSalaryEmployees);
        
        mockMvc.perform(get("/api/employees?minSalary=80000"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].salary", is(80000.0)));
        
        verify(employeeService).searchEmployees(null, 80000.0);
    }
    
    @Test
    void testGetEmployeesByDepartmentEndpoint() throws Exception {
        List<Employee> engineeringEmployees = List.of(testEmployee);
        
        when(employeeService.findByDepartment("Engineering")).thenReturn(engineeringEmployees);
        
        mockMvc.perform(get("/api/employees/department/Engineering"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string("X-Department", "Engineering"))
            .andExpect(header().string("X-Count", "1"))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].department", is("Engineering")));
        
        verify(employeeService).findByDepartment("Engineering");
    }
    
    @Test
    void testGetEmployeeCount() throws Exception {
        when(employeeService.count()).thenReturn(5L);
        
        mockMvc.perform(get("/api/employees/count"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count", is(5)));
        
        verify(employeeService).count();
    }
    
    // ========== BUSINESS LOGIC ENDPOINT TESTS ==========
    
    @Test
    void testGiveEmployeeRaise() throws Exception {
        Employee employeeWithRaise = new Employee(1L, "John Doe", "Engineering", 80000.0);
        
        when(employeeService.giveRaise(1L, 5000.0)).thenReturn(employeeWithRaise);
        
        mockMvc.perform(put("/api/employees/1/raise?amount=5000"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.salary", is(80000.0)));
        
        verify(employeeService).giveRaise(1L, 5000.0);
    }
    
    @Test
    void testGiveStandardRaise() throws Exception {
        Employee employeeWithRaise = new Employee(1L, "John Doe", "Engineering", 78750.0); // 5% raise
        
        when(employeeService.giveStandardRaise(1L)).thenReturn(employeeWithRaise);
        
        mockMvc.perform(put("/api/employees/1/standard-raise"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.salary", is(78750.0)));
        
        verify(employeeService).giveStandardRaise(1L);
    }
    
    @Test
    void testTransferEmployee() throws Exception {
        Employee transferredEmployee = new Employee(1L, "John Doe", "Marketing", 75000.0);
        
        when(employeeService.transferEmployee(1L, "Marketing")).thenReturn(transferredEmployee);
        
        mockMvc.perform(put("/api/employees/1/transfer?department=Marketing"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.department", is("Marketing")));
        
        verify(employeeService).transferEmployee(1L, "Marketing");
    }
    
    @Test
    void testGetHighPerformers() throws Exception {
        List<Employee> highPerformers = List.of(
            new Employee(3L, "Bob Johnson", "Engineering", 80000.0)
        );
        
        when(employeeService.findHighPerformers()).thenReturn(highPerformers);
        
        mockMvc.perform(get("/api/employees/high-performers"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string("X-High-Performers-Count", "1"))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].salary", greaterThanOrEqualTo(80000.0)));
        
        verify(employeeService).findHighPerformers();
    }
    
    @Test
    void testGetDepartmentExpense() throws Exception {
        when(employeeService.calculateDepartmentSalaryExpense("Engineering")).thenReturn(155000.0);
        
        mockMvc.perform(get("/api/employees/department/Engineering/expense"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.department", is("Engineering")))
            .andExpect(jsonPath("$.totalSalaryExpense", is(155000.0)));
        
        verify(employeeService).calculateDepartmentSalaryExpense("Engineering");
    }
    
    // ========== ERROR HANDLING TESTS ==========
    
    @Test
    void testBusinessLogicValidationError() throws Exception {
        when(employeeService.giveRaise(1L, -5000.0))
            .thenThrow(new IllegalArgumentException("Raise amount must be positive"));
        
        mockMvc.perform(put("/api/employees/1/raise?amount=-5000"))
            .andDo(print())
            .andExpect(status().isBadRequest());
        
        verify(employeeService).giveRaise(1L, -5000.0);
    }
    
    @Test
    void testTransferToSameDepartmentError() throws Exception {
        when(employeeService.transferEmployee(1L, "Engineering"))
            .thenThrow(new IllegalArgumentException("Employee is already in Engineering department"));
        
        mockMvc.perform(put("/api/employees/1/transfer?department=Engineering"))
            .andDo(print())
            .andExpect(status().isBadRequest());
        
        verify(employeeService).transferEmployee(1L, "Engineering");
    }
}