package com.oreilly.webservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test for the Employee Search Controller Exercise
 * <p>
 * This test verifies that the solution endpoints work correctly.
 * Students can use this to understand the expected behavior
 * and test their own implementation.
 */
@WebMvcTest(controllers = EmployeeSearchControllerSolution.class)
class EmployeeSearchControllerExerciseTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private EmployeeService employeeService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testSearchByDepartment() throws Exception {
        // Given
        List<Employee> engineeringEmployees = List.of(
            new Employee(1L, "Alice", "Engineering", 75000.0),
            new Employee(2L, "Bob", "Engineering", 80000.0)
        );
        when(employeeService.findByDepartment("Engineering")).thenReturn(engineeringEmployees);
        
        // When & Then
        mockMvc.perform(get("/api/search/solution/department/Engineering"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").value("Alice"))
            .andExpect(jsonPath("$[0].department").value("Engineering"))
            .andExpect(jsonPath("$[1].name").value("Bob"));
    }
    
    @Test
    void testAdvancedSearch() throws Exception {
        // Given
        List<Employee> allEmployees = List.of(
            new Employee(1L, "Alice Johnson", "Engineering", 75000.0),
            new Employee(2L, "Bob Smith", "Engineering", 85000.0),
            new Employee(3L, "Carol Davis", "Marketing", 65000.0),
            new Employee(4L, "Alice Brown", "Marketing", 70000.0)
        );
        when(employeeService.findAll()).thenReturn(allEmployees);
        
        SearchCriteria criteria = new SearchCriteria("Engineering",
                70000.0, 80000.0, "alice");
        
        // When & Then
        mockMvc.perform(post("/api/search/solution/advanced")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criteria)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].name").value("Alice Johnson"))
            .andExpect(jsonPath("$[0].department").value("Engineering"));
    }
    
    @Test
    void testGetUniqueDepartments() throws Exception {
        // Given
        List<Employee> employees = List.of(
            new Employee(1L, "Alice", "Engineering", 75000.0),
            new Employee(2L, "Bob", "Engineering", 80000.0),
            new Employee(3L, "Carol", "Marketing", 65000.0),
            new Employee(4L, "Dave", "Sales", 60000.0)
        );
        when(employeeService.findAll()).thenReturn(employees);
        
        // When & Then
        mockMvc.perform(get("/api/search/solution/departments"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(3))
            // Note: Set order is not guaranteed, so we check contains instead of exact positions
            .andExpect(jsonPath("$[*]")
                    .value(org.hamcrest.Matchers.hasItems("Engineering", "Marketing", "Sales")));
    }
    
    @Test 
    void testAdvancedSearchWithNullCriteria() throws Exception {
        // Given
        List<Employee> employees = List.of(
            new Employee(1L, "Alice", "Engineering", 75000.0)
        );
        when(employeeService.findAll()).thenReturn(employees);
        
        // Search with all null criteria should return all employees
        SearchCriteria criteria = new SearchCriteria(null, null, null, null);
        
        // When & Then
        mockMvc.perform(post("/api/search/solution/advanced")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criteria)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].name").value("Alice"));
    }
}