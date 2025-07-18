package com.oreilly.employee.service;

import com.oreilly.employee.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Reactive service layer using Project Reactor.
 * Demonstrates Mono and Flux operations with non-blocking I/O.
 */
@Service
public class ReactiveEmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(ReactiveEmployeeService.class);
    
    private final Map<Long, Employee> employees = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    public ReactiveEmployeeService() {
        initializeSampleData();
    }
    
    /**
     * Find employee by ID reactively.
     */
    public Mono<Employee> findById(Long id) {
        logger.debug("Reactive findById: {}", id);
        
        return Mono.fromCallable(() -> employees.get(id))
            .subscribeOn(Schedulers.boundedElastic()) // Non-blocking I/O
            .filter(emp -> emp != null)
            .doOnNext(emp -> logger.debug("Found employee: {}", emp.getName()))
            .doOnCancel(() -> logger.debug("FindById cancelled for ID: {}", id));
    }
    
    /**
     * Get all employees as a reactive stream.
     */
    public Flux<Employee> findAll() {
        logger.debug("Reactive findAll");
        
        return Flux.fromIterable(employees.values())
            .subscribeOn(Schedulers.boundedElastic())
            .doOnNext(emp -> logger.debug("Emitting employee: {}", emp.getName()))
            .doOnComplete(() -> logger.debug("Completed emitting all employees"));
    }
    
    /**
     * Find employees by department reactively.
     */
    public Flux<Employee> findByDepartment(String department) {
        logger.debug("Reactive findByDepartment: {}", department);
        
        return findAll()
            .filter(emp -> emp.getDepartment().equalsIgnoreCase(department))
            .doOnNext(emp -> logger.debug("Filtered employee: {}", emp.getName()));
    }
    
    /**
     * Save employee reactively.
     */
    public Mono<Employee> save(Employee employee) {
        logger.debug("Reactive save: {}", employee.getName());
        
        return Mono.fromCallable(() -> {
            if (employee.getId() == null) {
                employee.setId(idGenerator.getAndIncrement());
                logger.info("Creating new employee: {} with ID: {}", employee.getName(), employee.getId());
            } else {
                logger.info("Updating employee: {} with ID: {}", employee.getName(), employee.getId());
            }
            
            employees.put(employee.getId(), employee);
            return employee;
        })
        .subscribeOn(Schedulers.boundedElastic())
        .doOnSuccess(emp -> logger.debug("Saved employee: {}", emp.getName()));
    }
    
    /**
     * Delete employee by ID reactively.
     */
    public Mono<Void> deleteById(Long id) {
        logger.debug("Reactive deleteById: {}", id);
        
        return Mono.fromRunnable(() -> {
            Employee removed = employees.remove(id);
            if (removed != null) {
                logger.info("Deleted employee: {}", removed.getName());
            } else {
                logger.warn("Employee not found for deletion: {}", id);
            }
        })
        .subscribeOn(Schedulers.boundedElastic())
        .then();
    }
    
    /**
     * Count employees reactively.
     */
    public Mono<Long> count() {
        logger.debug("Reactive count");
        
        return Mono.fromCallable(() -> (long) employees.size())
            .subscribeOn(Schedulers.boundedElastic())
            .doOnNext(count -> logger.debug("Employee count: {}", count));
    }
    
    /**
     * Check if employee exists reactively.
     */
    public Mono<Boolean> existsById(Long id) {
        logger.debug("Reactive existsById: {}", id);
        
        return Mono.fromCallable(() -> employees.containsKey(id))
            .subscribeOn(Schedulers.boundedElastic());
    }
    
    /**
     * Simulate async processing with delays.
     */
    public Mono<Employee> findByIdWithDelay(Long id, Duration delay) {
        logger.debug("Reactive findByIdWithDelay: {} (delay: {})", id, delay);
        
        return findById(id)
            .delayElement(delay)
            .doOnNext(emp -> logger.debug("Delayed result for employee: {}", emp.getName()));
    }
    
    /**
     * Process employees with backpressure handling.
     */
    public Flux<Employee> processEmployeesWithBackpressure() {
        logger.debug("Processing employees with backpressure");
        
        return findAll()
            .onBackpressureBuffer(10) // Buffer up to 10 items
            .map(this::processEmployee)
            .subscribeOn(Schedulers.parallel())
            .doOnNext(emp -> logger.debug("Processed employee: {}", emp.getName()));
    }
    
    /**
     * Combine multiple reactive streams.
     */
    public Mono<EmployeeSummary> getEmployeeSummary() {
        logger.debug("Getting employee summary");
        
        Mono<Long> totalCount = count();
        Mono<Double> avgSalary = findAll()
            .map(Employee::getSalary)
            .reduce(Double::sum)
            .zipWith(totalCount)
            .map(tuple -> tuple.getT1() / tuple.getT2())
            .defaultIfEmpty(0.0);
        
        return Mono.zip(totalCount, avgSalary)
            .map(tuple -> new EmployeeSummary(tuple.getT1(), tuple.getT2()))
            .doOnSuccess(summary -> logger.info("Employee summary: {}", summary));
    }
    
    /**
     * Error handling with retry and fallback.
     */
    public Mono<Employee> findByIdWithRetry(Long id) {
        logger.debug("Reactive findByIdWithRetry: {}", id);
        
        return findById(id)
            .timeout(Duration.ofSeconds(5))
            .retry(3)
            .onErrorResume(error -> {
                logger.error("Failed to find employee after retries: {}", error.getMessage());
                return Mono.just(createFallbackEmployee(id));
            });
    }
    
    /**
     * Parallel processing of employees.
     */
    public Flux<Employee> processEmployeesParallel() {
        logger.debug("Processing employees in parallel");
        
        return findAll()
            .parallel(4) // Use 4 parallel threads
            .runOn(Schedulers.parallel())
            .map(this::processEmployeeIntensive)
            .sequential()
            .doOnNext(emp -> logger.debug("Parallel processed: {}", emp.getName()));
    }
    
    /**
     * Simulate intensive processing.
     */
    private Employee processEmployee(Employee employee) {
        // Simulate some processing
        try {
            Thread.sleep(100); // Simulate work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return employee;
    }
    
    /**
     * Simulate more intensive processing.
     */
    private Employee processEmployeeIntensive(Employee employee) {
        // Simulate intensive work
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return employee;
    }
    
    /**
     * Create fallback employee for error scenarios.
     */
    private Employee createFallbackEmployee(Long id) {
        Employee fallback = new Employee();
        fallback.setId(id);
        fallback.setName("Unknown Employee");
        fallback.setEmail("unknown@company.com");
        fallback.setDepartment("Unknown");
        fallback.setSalary(0.0);
        fallback.setActive(false);
        return fallback;
    }
    
    /**
     * Initialize sample data.
     */
    private void initializeSampleData() {
        logger.info("Initializing reactive sample data");
        
        // Create initial employees reactively
        Flux.just(
            new Employee(null, "Alice Johnson", "alice@reactive.com", "Engineering", 85000.0),
            new Employee(null, "Bob Smith", "bob@reactive.com", "Marketing", 65000.0),
            new Employee(null, "Carol Williams", "carol@reactive.com", "Engineering", 90000.0),
            new Employee(null, "David Brown", "david@reactive.com", "Sales", 70000.0)
        )
        .flatMap(this::save)
        .subscribe(
            emp -> logger.debug("Initialized employee: {}", emp.getName()),
            error -> logger.error("Error initializing data", error),
            () -> logger.info("Reactive sample data initialization complete")
        );
    }
    
    /**
     * Employee summary data class.
     */
    public static class EmployeeSummary {
        private final long totalEmployees;
        private final double averageSalary;
        
        public EmployeeSummary(long totalEmployees, double averageSalary) {
            this.totalEmployees = totalEmployees;
            this.averageSalary = averageSalary;
        }
        
        public long getTotalEmployees() { return totalEmployees; }
        public double getAverageSalary() { return averageSalary; }
        
        @Override
        public String toString() {
            return String.format("EmployeeSummary{total=%d, avgSalary=%.2f}", 
                totalEmployees, averageSalary);
        }
    }
}