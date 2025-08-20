# Try It Out: Employee Search Controller Exercise

## 🎯 Learning Objectives

Practice REST controller fundamentals by implementing search endpoints that reuse our existing service layer:

- **Constructor injection** with Spring
- **GET endpoints** with path variables  
- **POST endpoints** with request body
- **ResponseEntity** for proper HTTP responses
- **Stream processing** for filtering data

## 📁 Files to Complete

**Main Exercise**: `src/main/java/com/oreilly/webservices/EmployeeSearchController.java`

Complete the 4 TODOs to implement these endpoints:

### 1. Constructor Injection (TODO #1)
Replace the placeholder constructor with proper Spring dependency injection.

### 2. Search by Department (TODO #2) 
**GET** `/api/search/department/{department}`
- Uses `@PathVariable` to extract department name from URL
- Calls `employeeService.findByDepartment()`
- Returns list of employees in that department

### 3. Advanced Search (TODO #3)
**POST** `/api/search/advanced`
- Uses `@RequestBody` to accept `SearchCriteria` 
- Filters employees by multiple criteria using Java streams
- Handles null criteria gracefully

### 4. Unique Departments (TODO #4)
**GET** `/api/search/departments`
- Returns `Set<String>` of all unique department names
- Uses stream operations to extract departments from employee list

## 🧪 Testing Your Solution

Run the provided tests to verify your implementation:

```bash
gradle :web-services:test --tests "*EmployeeSearchControllerExerciseTest*"
```

## 📖 Reference Solution

Check `EmployeeSearchControllerSolution.java` if you get stuck - it shows the complete working implementation.

## 💡 Key Concepts Demonstrated

- **Reusing existing services** - No need to duplicate business logic
- **Different REST patterns** - Path variables vs request body vs query params
- **Stream processing** - Filtering collections with multiple criteria
- **Spring annotations** - `@RestController`, `@GetMapping`, `@PostMapping`, `@PathVariable`, `@RequestBody`
- **HTTP best practices** - Proper use of `ResponseEntity`

## 🚀 Example Usage

Once implemented, you can test your endpoints:

```bash
# Search by department
curl http://localhost:8080/api/search/department/Engineering

# Get unique departments  
curl http://localhost:8080/api/search/departments

# Advanced search with JSON body
curl -X POST http://localhost:8080/api/search/advanced \
  -H "Content-Type: application/json" \
  -d '{
    "department": "Engineering", 
    "minSalary": 70000,
    "maxSalary": 90000,
    "nameContains": "alice"
  }'
```

## 🎓 What You'll Learn

By completing this exercise, you'll understand:
- How Spring Boot controllers work with dependency injection
- Different ways to accept parameters in REST endpoints
- How to process request bodies with records
- Stream-based filtering for complex search criteria
- Professional REST API patterns used in real applications

Start with TODO #1 and work through each step. The tests will guide you toward the correct implementation!