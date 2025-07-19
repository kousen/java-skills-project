package com.oreilly.webservices;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Reactive Programming with Project Reactor Demonstration
 * Shows reactive patterns using Mono and Flux with practical examples
 * for the Employee Management System.
 */
public class ReactiveDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Reactive Programming with Project Reactor ===");
        
        ReactiveDemo demo = new ReactiveDemo();
        
        // Basic Mono and Flux operations
        demo.demonstrateMonoBasics();
        demo.demonstrateFluxBasics();
        
        // Transformations and combinations
        demo.demonstrateTransformations();
        demo.demonstrateCombiningStreams();
        
        // Error handling
        demo.demonstrateErrorHandling();
        
        // Backpressure and flow control
        demo.demonstrateBackpressure();
        
        // Testing reactive streams
        demo.demonstrateTesting();
        
        // WebFlux examples
        demo.demonstrateWebFlux();
        
        System.out.println("\n=== Reactive programming demonstration complete ===");
    }
    
    /**
     * Basic Mono operations - single values
     */
    public void demonstrateMonoBasics() {
        System.out.println("\n--- Mono Basics (Single Values) ---");
        
        // Creating Monos
        Mono<String> helloMono = Mono.just("Hello Reactive World!");
        Mono<ReactiveEmployee> employeeMono = Mono.fromSupplier(() -> 
            new ReactiveEmployee(1L, "Alice Johnson", "Engineering"));
        Mono<String> emptyMono = Mono.empty();
        Mono<String> errorMono = Mono.error(new RuntimeException("Something went wrong"));
        
        // Subscribe and consume
        helloMono.subscribe(message -> System.out.println("✓ " + message));
        
        employeeMono
            .map(emp -> emp.getName().toUpperCase())
            .subscribe(name -> System.out.println("✓ Employee: " + name));
        
        // Demonstrate lazy evaluation
        System.out.println("✓ Monos created (but not executed yet)");
        
        // Chain operations
        Mono<String> processedEmployee = employeeMono
            .map(ReactiveEmployee::getName)
            .map(String::toUpperCase)
            .map(name -> "Processed: " + name);
        
        processedEmployee.subscribe(result -> System.out.println("✓ " + result));
    }
    
    /**
     * Basic Flux operations - streams of values
     */
    public void demonstrateFluxBasics() {
        System.out.println("\n--- Flux Basics (Multiple Values) ---");
        
        // Creating Fluxes
        Flux<String> departmentFlux = Flux.just("Engineering", "Marketing", "Sales", "HR");
        Flux<Integer> numbersFlux = Flux.range(1, 5);
        Flux<ReactiveEmployee> employeeFlux = Flux.fromIterable(createSampleEmployees());
        
        // Subscribe and consume all values
        departmentFlux.subscribe(dept -> System.out.println("✓ Department: " + dept));
        
        // Transform each element
        employeeFlux
            .map(emp -> emp.getName() + " (" + emp.getDepartment() + ")")
            .subscribe(info -> System.out.println("✓ " + info));
        
        // Filter elements
        employeeFlux
            .filter(emp -> "Engineering".equals(emp.getDepartment()))
            .map(ReactiveEmployee::getName)
            .subscribe(name -> System.out.println("✓ Engineer: " + name));
        
        // Count elements
        employeeFlux
            .count()
            .subscribe(count -> System.out.println("✓ Total employees: " + count));
    }
    
    /**
     * Transformations and operations
     */
    public void demonstrateTransformations() {
        System.out.println("\n--- Transformations and Operations ---");
        
        Flux<ReactiveEmployee> employees = Flux.fromIterable(createSampleEmployees());
        
        // Map - transform each element
        employees
            .map(emp -> new EmployeeDto(emp.getId(), emp.getName(), emp.getDepartment(), emp.getSalary() * 1.1))
            .subscribe(dto -> System.out.println("✓ Salary increased: " + dto.getName() + " -> $" + dto.getSalary()));
        
        // FlatMap - one-to-many transformation
        employees
            .flatMap(emp -> Flux.just(emp.getName().split(" ")))
            .subscribe(namePart -> System.out.println("✓ Name part: " + namePart));
        
        // GroupBy - group elements
        employees
            .groupBy(ReactiveEmployee::getDepartment)
            .flatMap(group -> 
                group.collectList()
                    .map(empList -> group.key() + ": " + empList.size() + " employees")
            )
            .subscribe(summary -> System.out.println("✓ " + summary));
        
        // Collect to different structures
        employees
            .collect(HashMap::new, (map, emp) -> map.put(emp.getId(), emp))
            .subscribe(empMap -> System.out.println("✓ Collected " + empMap.size() + " employees to map"));
    }
    
    /**
     * Combining multiple reactive streams
     */
    public void demonstrateCombiningStreams() {
        System.out.println("\n--- Combining Reactive Streams ---");
        
        Mono<ReactiveEmployee> employeeMono = Mono.just(new ReactiveEmployee(1L, "Alice Johnson", "Engineering"));
        Mono<ReactiveDepartment> departmentMono = Mono.just(new ReactiveDepartment(1L, "Engineering", "Software Development"));
        Mono<ReactiveSalary> salaryMono = Mono.just(new ReactiveSalary(1L, 85000.0, "USD"));
        
        // Zip - combine multiple Monos
        Mono<EmployeeProfile> profileMono = Mono.zip(employeeMono, departmentMono, salaryMono)
            .map(tuple -> new EmployeeProfile(tuple.getT1(), tuple.getT2(), tuple.getT3()));
        
        profileMono.subscribe(profile -> {
            System.out.println("✓ Complete profile created:");
            System.out.println("  Employee: " + profile.getEmployee().getName());
            System.out.println("  Department: " + profile.getDepartment().getName());
            System.out.println("  Salary: $" + profile.getSalary().getAmount());
        });
        
        // Merge - combine multiple Fluxes
        Flux<String> engineersFlux = Flux.just("Alice", "Bob", "Charlie");
        Flux<String> managersFlux = Flux.just("David", "Eve");
        
        Flux.merge(engineersFlux, managersFlux)
            .subscribe(name -> System.out.println("✓ Team member: " + name));
        
        // Concat - ordered combination
        Flux.concat(managersFlux, engineersFlux)
            .collectList()
            .subscribe(allMembers -> System.out.println("✓ Ordered team: " + allMembers));
    }
    
    /**
     * Error handling in reactive streams
     */
    public void demonstrateErrorHandling() {
        System.out.println("\n--- Error Handling ---");
        
        // Error recovery with onErrorReturn
        Flux<ReactiveEmployee> employeesWithError = Flux.fromIterable(createSampleEmployees())
            .map(emp -> {
                if ("Bob Smith".equals(emp.getName())) {
                    throw new RuntimeException("Database error for employee: " + emp.getName());
                }
                return emp;
            })
            .onErrorReturn(new ReactiveEmployee(-1L, "Unknown Employee", "Unknown"));
        
        employeesWithError.subscribe(
            emp -> System.out.println("✓ Processed: " + emp.getName()),
            error -> System.out.println("✗ Error: " + error.getMessage()),
            () -> System.out.println("✓ Processing complete")
        );
        
        // Error recovery with onErrorResume
        Mono<ReactiveEmployee> employeeWithFallback = getEmployeeFromDatabase(999L)
            .onErrorResume(error -> {
                System.out.println("⚠️ Database error, using cache: " + error.getMessage());
                return getEmployeeFromCache(999L);
            });
        
        employeeWithFallback.subscribe(emp -> System.out.println("✓ Got employee: " + emp.getName()));
        
        // Retry on error
        Mono<String> unreliableService = Mono.fromSupplier(() -> {
            if (Math.random() > 0.7) {
                return "Success!";
            }
            throw new RuntimeException("Service temporarily unavailable");
        });
        
        unreliableService
            .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
            .subscribe(
                result -> System.out.println("✓ Service call: " + result),
                error -> System.out.println("✗ Failed after retries: " + error.getMessage())
            );
    }
    
    /**
     * Backpressure and flow control
     */
    public void demonstrateBackpressure() {
        System.out.println("\n--- Backpressure and Flow Control ---");
        
        // Fast publisher, slow subscriber
        Flux<Integer> fastPublisher = Flux.range(1, 1000)
            .delayElements(Duration.ofMillis(1)); // Fast emission
        
        fastPublisher
            .buffer(10) // Buffer elements in groups of 10
            .delayElements(Duration.ofMillis(50)) // Slow processing
            .take(3) // Take only first 3 buffers for demo
            .subscribe(batch -> System.out.println("✓ Processed batch: " + batch));
        
        // Sample - take periodic samples
        Flux.interval(Duration.ofMillis(10))
            .sample(Duration.ofMillis(100))
            .take(3)
            .subscribe(sample -> System.out.println("✓ Sample: " + sample));
        
        // Rate limiting
        Flux.fromIterable(createSampleEmployees())
            .limitRate(2) // Limit to 2 elements at a time
            .subscribe(emp -> System.out.println("✓ Rate limited: " + emp.getName()));
    }
    
    /**
     * Testing reactive streams
     */
    public void demonstrateTesting() {
        System.out.println("\n--- Testing Reactive Streams ---");
        
        // Test simple Mono
        Mono<String> helloMono = Mono.just("Hello");
        
        StepVerifier.create(helloMono)
            .expectNext("Hello")
            .verifyComplete();
        
        System.out.println("✓ Mono test passed");
        
        // Test Flux with multiple values
        Flux<String> departmentFlux = Flux.just("Engineering", "Marketing", "Sales");
        
        StepVerifier.create(departmentFlux)
            .expectNext("Engineering")
            .expectNext("Marketing")
            .expectNext("Sales")
            .verifyComplete();
        
        System.out.println("✓ Flux test passed");
        
        // Test error scenarios
        Mono<String> errorMono = Mono.error(new RuntimeException("Test error"));
        
        StepVerifier.create(errorMono)
            .expectError(RuntimeException.class)
            .verify();
        
        System.out.println("✓ Error test passed");
        
        // Test with virtual time
        Flux<Long> timedFlux = Flux.interval(Duration.ofSeconds(1)).take(3);
        
        StepVerifier.withVirtualTime(() -> timedFlux)
            .expectSubscription()
            .expectNoEvent(Duration.ofSeconds(1))
            .expectNext(0L)
            .thenAwait(Duration.ofSeconds(1))
            .expectNext(1L)
            .thenAwait(Duration.ofSeconds(1))
            .expectNext(2L)
            .verifyComplete();
        
        System.out.println("✓ Virtual time test passed");
    }
    
    /**
     * WebFlux controller examples
     */
    public void demonstrateWebFlux() {
        System.out.println("\n--- WebFlux Examples ---");
        
        ReactiveEmployeeController controller = new ReactiveEmployeeController();
        
        // Simulate WebFlux operations
        System.out.println("WebFlux Controller Methods:");
        System.out.println("✓ GET /api/employees/{id} -> Mono<ReactiveEmployee>");
        System.out.println("✓ GET /api/employees -> Flux<ReactiveEmployee>");
        System.out.println("✓ POST /api/employees -> Mono<ReactiveEmployee>");
        System.out.println("✓ GET /api/employees/stream -> Server-Sent Events");
        
        // Demonstrate reactive operations
        controller.simulateOperations();
    }
    
    // Helper methods for demonstration
    private List<ReactiveEmployee> createSampleEmployees() {
        return Arrays.asList(
            new ReactiveEmployee(1L, "Alice Johnson", "Engineering", 85000.0),
            new ReactiveEmployee(2L, "Bob Smith", "Marketing", 70000.0),
            new ReactiveEmployee(3L, "Carol Davis", "Engineering", 90000.0),
            new ReactiveEmployee(4L, "David Wilson", "Sales", 65000.0),
            new ReactiveEmployee(5L, "Eve Brown", "HR", 75000.0)
        );
    }
    
    private Mono<ReactiveEmployee> getEmployeeFromDatabase(Long id) {
        return Mono.error(new RuntimeException("Database connection failed"));
    }
    
    private Mono<ReactiveEmployee> getEmployeeFromCache(Long id) {
        return Mono.just(new ReactiveEmployee(id, "Cached Employee", "Unknown", 0.0));
    }
}

/**
 * Reactive WebFlux controller
 */
@RestController
@RequestMapping("/api/employees")
class ReactiveEmployeeController {
    
    @Autowired
    private ReactiveEmployeeService employeeService;
    
    /**
     * Get single employee - returns Mono
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ReactiveEmployee>> getEmployee(@PathVariable Long id) {
        return employeeService.findById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    /**
     * Get all employees - returns Flux
     */
    @GetMapping
    public Flux<ReactiveEmployee> getAllEmployees() {
        return employeeService.findAll();
    }
    
    /**
     * Create employee - returns Mono
     */
    @PostMapping
    public Mono<ReactiveEmployee> createEmployee(@RequestBody ReactiveEmployee employee) {
        return employeeService.save(employee);
    }
    
    /**
     * Stream employees - Server-Sent Events
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ReactiveEmployee> streamEmployees() {
        return employeeService.findAll()
            .delayElements(Duration.ofSeconds(1));
    }
    
    /**
     * Search employees with reactive filtering
     */
    @GetMapping("/search")
    public Flux<ReactiveEmployee> searchEmployees(@RequestParam String department) {
        return employeeService.findAll()
            .filter(emp -> emp.getDepartment().equals(department));
    }
    
    public void simulateOperations() {
        System.out.println("\n--- Simulating WebFlux Operations ---");
        
        ReactiveEmployeeService service = new ReactiveEmployeeService();
        
        // Simulate getting an employee
        service.findById(1L)
            .subscribe(emp -> System.out.println("✓ Found: " + emp.getName()));
        
        // Simulate getting all employees
        service.findAll()
            .count()
            .subscribe(count -> System.out.println("✓ Total employees: " + count));
        
        // Simulate creating an employee
        ReactiveEmployee newEmployee = new ReactiveEmployee(null, "John Doe", "IT", 80000.0);
        service.save(newEmployee)
            .subscribe(saved -> System.out.println("✓ Saved: " + saved.getName()));
    }
}

/**
 * Reactive service layer
 */
class ReactiveEmployeeService {
    
    private final Map<Long, ReactiveEmployee> employees = new HashMap<>();
    
    public ReactiveEmployeeService() {
        // Initialize with sample data
        employees.put(1L, new ReactiveEmployee(1L, "Alice Johnson", "Engineering", 85000.0));
        employees.put(2L, new ReactiveEmployee(2L, "Bob Smith", "Marketing", 70000.0));
        employees.put(3L, new ReactiveEmployee(3L, "Carol Davis", "Engineering", 90000.0));
    }
    
    public Mono<ReactiveEmployee> findById(Long id) {
        return Mono.fromSupplier(() -> employees.get(id))
            .switchIfEmpty(Mono.empty());
    }
    
    public Flux<ReactiveEmployee> findAll() {
        return Flux.fromIterable(employees.values());
    }
    
    public Mono<ReactiveEmployee> save(ReactiveEmployee employee) {
        return Mono.fromSupplier(() -> {
            if (employee.getId() == null) {
                employee.setId((long) (employees.size() + 1));
            }
            employees.put(employee.getId(), employee);
            return employee;
        });
    }
    
    public Flux<ReactiveEmployee> findByDepartment(String department) {
        return findAll()
            .filter(emp -> emp.getDepartment().equals(department));
    }
    
    /**
     * Simulate async database operations
     */
    public Mono<ReactiveEmployee> findByIdAsync(Long id) {
        return Mono.fromSupplier(() -> employees.get(id))
            .delayElement(Duration.ofMillis(100)) // Simulate network delay
            .switchIfEmpty(Mono.error(new RuntimeException("Employee not found")));
    }
    
    /**
     * Combine multiple async operations
     */
    public Mono<EmployeeProfile> getCompleteProfile(Long employeeId) {
        Mono<ReactiveEmployee> employeeMono = findById(employeeId);
        Mono<ReactiveDepartment> departmentMono = getDepartmentInfo(employeeId);
        Mono<ReactiveSalary> salaryMono = getSalaryInfo(employeeId);
        
        return Mono.zip(employeeMono, departmentMono, salaryMono)
            .map(tuple -> new EmployeeProfile(tuple.getT1(), tuple.getT2(), tuple.getT3()));
    }
    
    private Mono<ReactiveDepartment> getDepartmentInfo(Long employeeId) {
        return Mono.just(new ReactiveDepartment(1L, "Engineering", "Software Development"))
            .delayElement(Duration.ofMillis(50));
    }
    
    private Mono<ReactiveSalary> getSalaryInfo(Long employeeId) {
        return Mono.just(new ReactiveSalary(employeeId, 85000.0, "USD"))
            .delayElement(Duration.ofMillis(75));
    }
}

// Data classes
class ReactiveEmployee {
    private Long id;
    private String name;
    private String department;
    private Double salary;
    
    public ReactiveEmployee() {}
    
    public ReactiveEmployee(Long id, String name, String department) {
        this(id, name, department, 0.0);
    }
    
    public ReactiveEmployee(Long id, String name, String department, Double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }
}

class EmployeeDto {
    private Long id;
    private String name;
    private String department;
    private Double salary;
    
    public EmployeeDto(Long id, String name, String department, Double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }
    
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public Double getSalary() { return salary; }
}

class ReactiveDepartment {
    private Long id;
    private String name;
    private String description;
    
    public ReactiveDepartment(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}

class ReactiveSalary {
    private Long employeeId;
    private Double amount;
    private String currency;
    
    public ReactiveSalary(Long employeeId, Double amount, String currency) {
        this.employeeId = employeeId;
        this.amount = amount;
        this.currency = currency;
    }
    
    public Long getEmployeeId() { return employeeId; }
    public Double getAmount() { return amount; }
    public String getCurrency() { return currency; }
}

class EmployeeProfile {
    private ReactiveEmployee employee;
    private ReactiveDepartment department;
    private ReactiveSalary salary;
    
    public EmployeeProfile(ReactiveEmployee employee, ReactiveDepartment department, ReactiveSalary salary) {
        this.employee = employee;
        this.department = department;
        this.salary = salary;
    }
    
    public ReactiveEmployee getEmployee() { return employee; }
    public ReactiveDepartment getDepartment() { return department; }
    public ReactiveSalary getSalary() { return salary; }
}