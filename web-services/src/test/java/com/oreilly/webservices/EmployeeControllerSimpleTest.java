package com.oreilly.webservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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
 * Simplified tests for EmployeeController focusing on core functionality.
 */
@WebMvcTest(controllers = EmployeeController.class)
@Import({GlobalExceptionHandler.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class EmployeeControllerSimpleTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private EmployeeService employeeService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private Employee testEmployee;
    
    @BeforeEach
    void setUp() {
        testEmployee = new Employee(1L, "John Doe", "Engineering", 75000.0);
    }
    
    @Test
    void testHelloEndpoint() throws Exception {
        mockMvc.perform(get("/api/employees/hello"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("Welcome to Employee Service!"));
    }
    
    @Test
    void testGetAllEmployees() throws Exception {
        List<Employee> employees = List.of(testEmployee);
        when(employeeService.findAll()).thenReturn(employees);
        
        mockMvc.perform(get("/api/employees"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("John Doe")));
        
        verify(employeeService).findAll();
    }
    
    @Test
    void testGetEmployeeById() throws Exception {
        when(employeeService.findById(1L)).thenReturn(Optional.of(testEmployee));
        
        mockMvc.perform(get("/api/employees/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("John Doe")))
            .andExpect(jsonPath("$.department", is("Engineering")));
        
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
            .andExpect(jsonPath("$.name", is("Alice Johnson")));
        
        verify(employeeService).save(any(Employee.class));
    }
    
    @Test
    void testUpdateEmployee() throws Exception {
        Employee updatedEmployee = new Employee(1L, "John Updated", "Engineering", 85000.0);
        
        when(employeeService.updateEmployee(eq(1L), any(Employee.class))).thenReturn(updatedEmployee);
        
        mockMvc.perform(put("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("John Updated")));
        
        verify(employeeService).updateEmployee(eq(1L), any(Employee.class));
    }
    
    @Test
    void testDeleteEmployee() throws Exception {
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
    
    @Test
    void testGetEmployeeCount() throws Exception {
        when(employeeService.count()).thenReturn(5L);
        
        mockMvc.perform(get("/api/employees/count"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count", is(5)));
        
        verify(employeeService).count();
    }
}