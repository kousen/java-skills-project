import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

/**
 * Reactive Programming Concepts Demonstration
 * Shows reactive patterns and principles without Project Reactor dependencies.
 * Demonstrates the concepts that libraries like Project Reactor implement.
 */
public class ReactiveConcepts {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Reactive Programming Concepts ===");
        
        ReactiveConcepts demo = new ReactiveConcepts();
        
        // Basic reactive concepts
        demo.demonstrateObserverPattern();
        demo.demonstrateAsyncProcessing();
        demo.demonstrateDataStreams();
        demo.demonstrateBackpressure();
        demo.demonstrateErrorHandling();
        demo.demonstrateComposition();
        
        System.out.println("\n=== Reactive concepts demonstration complete ===");
    }
    
    /**
     * Observer Pattern - Foundation of reactive programming
     */
    public void demonstrateObserverPattern() {
        System.out.println("\n--- Observer Pattern (Reactive Foundation) ---");
        
        EmployeeEventPublisher publisher = new EmployeeEventPublisher();
        
        // Subscribe different observers
        publisher.subscribe(event -> 
            System.out.println("✓ PayrollService: Processing salary for " + event.employeeName()));
        
        publisher.subscribe(event -> 
            System.out.println("✓ NotificationService: Sending welcome email to " + event.employeeName()));
        
        publisher.subscribe(event -> 
            System.out.println("✓ AuditService: Logging employee creation: " + event.employeeName()));
        
        // Publish events
        publisher.publishEmployeeCreated(new EmployeeEvent("Alice Johnson", "Engineering"));
        publisher.publishEmployeeCreated(new EmployeeEvent("Bob Smith", "Marketing"));
        
        System.out.println("\nObserver Pattern Benefits:");
        System.out.println("- Loose coupling between publisher and subscribers");
        System.out.println("- Dynamic subscription/unsubscription");
        System.out.println("- Multiple observers can react to same event");
    }
    
    /**
     * Asynchronous Processing - Non-blocking operations
     */
    public void demonstrateAsyncProcessing() throws InterruptedException {
        System.out.println("\n--- Asynchronous Processing ---");
        
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        // Simulate async service calls
        CompletableFuture<EmployeeInfo> employeeFuture = CompletableFuture.supplyAsync(() -> {
            simulateDelay(100, "Fetching employee data");
            return new EmployeeInfo(1L, "Alice Johnson", "Engineering");
        }, executor);
        
        CompletableFuture<DepartmentInfo> departmentFuture = CompletableFuture.supplyAsync(() -> {
            simulateDelay(150, "Fetching department data");
            return new DepartmentInfo(1L, "Engineering", "Software Development");
        }, executor);
        
        CompletableFuture<SalaryInfo> salaryFuture = CompletableFuture.supplyAsync(() -> {
            simulateDelay(80, "Calculating salary");
            return new SalaryInfo(1L, 85000.0, "USD");
        }, executor);
        
        // Combine all async results
        CompletableFuture<CompleteProfile> combinedFuture = employeeFuture
            .thenCombine(departmentFuture, Pair::new)
            .thenCombine(salaryFuture, (empDept, salary) -> 
                new CompleteProfile(empDept.first(), empDept.second(), salary));
        
        // Non-blocking - continue with other work
        System.out.println("✓ Started async operations, continuing with other work...");
        
        // Get result when ready
        combinedFuture.thenAccept(profile -> {
            System.out.println("✓ Combined result ready:");
            System.out.println("  " + profile.employee().name() + " in " + profile.department().name() +
                               " earns $" + profile.salary().amount());
        });
        
        // Wait for completion (only for demo)
        Thread.sleep(200);
        executor.shutdown();
        
        System.out.println("\nAsync Benefits:");
        System.out.println("- Non-blocking operations");
        System.out.println("- Better resource utilization");
        System.out.println("- Improved scalability");
    }
    
    /**
     * Data Streams - Processing sequences of data
     */
    public void demonstrateDataStreams() {
        System.out.println("\n--- Data Streams ---");
        
        ReactiveStream<EmployeeInfo> employeeStream = new ReactiveStream<>();
        
        // Subscribe to filtered stream
        employeeStream
            .filter(emp -> "Engineering".equals(emp.department()))
            .forEach(emp -> System.out.println("✓ Engineer: " + emp.name().toUpperCase()));
        
        // Emit data to stream
        employeeStream.emit(new EmployeeInfo(1L, "Alice Johnson", "Engineering"));
        employeeStream.emit(new EmployeeInfo(2L, "Bob Smith", "Marketing"));
        employeeStream.emit(new EmployeeInfo(3L, "Carol Davis", "Engineering"));
        employeeStream.emit(new EmployeeInfo(4L, "David Wilson", "Sales"));
        employeeStream.complete();
        
        // Group by department
        ReactiveStream<EmployeeInfo> groupingStream = new ReactiveStream<>();
        Map<String, List<EmployeeInfo>> groups = new HashMap<>();
        
        groupingStream.forEach(emp ->
                groups.computeIfAbsent(emp.department(),
                        k -> new ArrayList<>()).add(emp));
        
        groupingStream.onComplete(() -> {
            System.out.println("✓ Employees by department:");
            groups.forEach((dept, employees) -> 
                System.out.println("  " + dept + ": " + employees.size() + " employees"));
        });
        
        // Emit data
        Arrays.asList(
            new EmployeeInfo(1L, "Alice", "Engineering"),
            new EmployeeInfo(2L, "Bob", "Marketing"),
            new EmployeeInfo(3L, "Carol", "Engineering"),
            new EmployeeInfo(4L, "David", "Marketing")
        ).forEach(groupingStream::emit);
        groupingStream.complete();
    }
    
    /**
     * Backpressure - Handling fast producers and slow consumers
     */
    public void demonstrateBackpressure() throws InterruptedException {
        System.out.println("\n--- Backpressure Handling ---");
        
        BackpressureStream<Integer> stream = new BackpressureStream<>(3); // Buffer size 3
        
        // Slow consumer
        stream.subscribe(value -> {
            simulateDelay(50, "Processing " + value);
            System.out.println("✓ Processed: " + value);
        });
        
        // Fast producer
        try (ExecutorService producer = Executors.newSingleThreadExecutor()) {
            producer.submit(() -> {
                for (int i = 1; i <= 10; i++) {
                    try {
                        stream.emit(i);
                        System.out.println("📤 Produced: " + i);
                        Thread.sleep(10); // Fast production
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                stream.complete();
            });

            Thread.sleep(1000); // Wait for processing
            producer.shutdown();
        }

        System.out.println("\nBackpressure Strategies:");
        System.out.println("- Buffer: Store excess items temporarily");
        System.out.println("- Drop: Discard excess items");
        System.out.println("- Block: Slow down producer");
        System.out.println("- Sample: Take periodic samples");
    }
    
    /**
     * Error Handling - Resilient stream processing
     */
    public void demonstrateErrorHandling() {
        System.out.println("\n--- Error Handling in Streams ---");
        
        ReactiveStream<String> stream = new ReactiveStream<>();
        
        // Error recovery
        stream
            .map(data -> {
                if ("ERROR".equals(data)) {
                    throw new RuntimeException("Processing error for: " + data);
                }
                return data.toUpperCase();
            })
            .onError(error -> {
                System.out.println("⚠️ Error handled: " + error.getMessage());
                return "DEFAULT_VALUE"; // Fallback
            })
            .forEach(result -> System.out.println("✓ Result: " + result));
        
        // Emit data including error case
        stream.emit("hello");
        stream.emit("world");
        stream.emit("ERROR");
        stream.emit("recovery");
        stream.complete();
        
        // Retry mechanism
        RetryableOperation operation = new RetryableOperation();
        String result = operation.executeWithRetry(() -> {
            if (Math.random() > 0.6) {
                return "Success!";
            }
            throw new RuntimeException("Operation failed");
        }, 3);
        
        System.out.println("✓ Retry result: " + result);
        
        System.out.println("\nError Handling Patterns:");
        System.out.println("- onError: Handle errors gracefully");
        System.out.println("- retry: Retry failed operations");
        System.out.println("- fallback: Provide default values");
        System.out.println("- circuit breaker: Prevent cascading failures");
    }
    
    /**
     * Composition - Combining reactive streams
     */
    public void demonstrateComposition() {
        System.out.println("\n--- Stream Composition ---");
        
        ReactiveStream<EmployeeInfo> employeeStream = new ReactiveStream<>();
        ReactiveStream<SalaryInfo> salaryStream = new ReactiveStream<>();
        
        // Combine streams
        StreamCombiner<EmployeeInfo, SalaryInfo, String> combiner = 
            new StreamCombiner<>(employeeStream, salaryStream);
        
        combiner.combine((emp, salary) ->
                emp.name() + " earns $" + salary.amount()
        ).forEach(result -> System.out.println("✓ Combined: " + result));
        
        // Emit data to both streams
        employeeStream.emit(new EmployeeInfo(1L, "Alice Johnson", "Engineering"));
        salaryStream.emit(new SalaryInfo(1L, 85000.0, "USD"));
        
        employeeStream.emit(new EmployeeInfo(2L, "Bob Smith", "Marketing"));
        salaryStream.emit(new SalaryInfo(2L, 70000.0, "USD"));
        
        employeeStream.complete();
        salaryStream.complete();
        
        // Merge multiple streams
        ReactiveStream<String> stream1 = new ReactiveStream<>();
        ReactiveStream<String> stream2 = new ReactiveStream<>();
        
        StreamMerger<String> merger = new StreamMerger<>(Arrays.asList(stream1, stream2));
        merger.forEach(value -> System.out.println("✓ Merged: " + value));
        
        stream1.emit("A1");
        stream2.emit("B1");
        stream1.emit("A2");
        stream2.emit("B2");
        
        stream1.complete();
        stream2.complete();
        
        System.out.println("\nComposition Benefits:");
        System.out.println("- Combine multiple data sources");
        System.out.println("- Transform and filter composed data");
        System.out.println("- Coordinate async operations");
    }
    
    // Helper method for simulating delays
    private void simulateDelay(int milliseconds, String operation) {
        try {
            Thread.sleep(milliseconds);
            System.out.println("⏱️ " + operation + " completed in " + milliseconds + "ms");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Basic reactive stream implementation
class ReactiveStream<T> {
    private final List<Consumer<T>> subscribers = new ArrayList<>();
    private final List<Function<T, T>> transformations = new ArrayList<>();
    private final List<Predicate<T>> filters = new ArrayList<>();
    private final List<Runnable> completionHandlers = new ArrayList<>();
    private Function<Throwable, T> errorHandler;
    
    public ReactiveStream<T> subscribe(Consumer<T> subscriber) {
        subscribers.add(subscriber);
        return this;
    }
    
    public ReactiveStream<T> map(Function<T, T> mapper) {
        transformations.add(mapper);
        return this;
    }
    
    public ReactiveStream<T> filter(Predicate<T> predicate) {
        filters.add(predicate);
        return this;
    }
    
    public ReactiveStream<T> forEach(Consumer<T> action) {
        return subscribe(action);
    }
    
    public ReactiveStream<T> onError(Function<Throwable, T> handler) {
        this.errorHandler = handler;
        return this;
    }
    
    public ReactiveStream<T> onComplete(Runnable handler) {
        completionHandlers.add(handler);
        return this;
    }
    
    public void emit(T value) {
        try {
            T processedValue = value;
            
            // Apply filters
            for (Predicate<T> filter : filters) {
                if (!filter.test(processedValue)) {
                    return; // Skip this value
                }
            }
            
            // Apply transformations
            for (Function<T, T> transformation : transformations) {
                processedValue = transformation.apply(processedValue);
            }
            
            // Notify subscribers
            for (Consumer<T> subscriber : subscribers) {
                subscriber.accept(processedValue);
            }
            
        } catch (Exception e) {
            if (errorHandler != null) {
                T fallbackValue = errorHandler.apply(e);
                for (Consumer<T> subscriber : subscribers) {
                    subscriber.accept(fallbackValue);
                }
            } else {
                throw new RuntimeException(e);
            }
        }
    }
    
    public void complete() {
        for (Runnable handler : completionHandlers) {
            handler.run();
        }
    }
}

// Backpressure handling stream
class BackpressureStream<T> {
    private final BlockingQueue<T> buffer;
    private final List<Consumer<T>> subscribers = new ArrayList<>();
    private volatile boolean completed = false;
    
    public BackpressureStream(int bufferSize) {
        this.buffer = new ArrayBlockingQueue<>(bufferSize);
        startConsumerThread();
    }
    
    public void subscribe(Consumer<T> subscriber) {
        subscribers.add(subscriber);
    }
    
    public void emit(T value) throws InterruptedException {
        if (!buffer.offer(value, 100, TimeUnit.MILLISECONDS)) {
            System.out.println("⚠️ Buffer full, applying backpressure for: " + value);
            // In real implementation, could drop, block, or apply other strategies
        }
    }
    
    public void complete() {
        completed = true;
    }
    
    private void startConsumerThread() {
        new Thread(() -> {
            try {
                while (!completed || !buffer.isEmpty()) {
                    T value = buffer.poll(100, TimeUnit.MILLISECONDS);
                    if (value != null) {
                        for (Consumer<T> subscriber : subscribers) {
                            subscriber.accept(value);
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}

// Stream combination utilities
class StreamCombiner<T, U, R> {
    private final ReactiveStream<T> stream1;
    private final ReactiveStream<U> stream2;
    private final List<T> buffer1 = new ArrayList<>();
    private final List<U> buffer2 = new ArrayList<>();
    
    public StreamCombiner(ReactiveStream<T> stream1, ReactiveStream<U> stream2) {
        this.stream1 = stream1;
        this.stream2 = stream2;
    }
    
    public ReactiveStream<R> combine(BiFunction<T, U, R> combiner) {
        ReactiveStream<R> resultStream = new ReactiveStream<>();
        
        stream1.subscribe(value1 -> {
            buffer1.add(value1);
            tryEmitCombined(resultStream, combiner);
        });
        
        stream2.subscribe(value2 -> {
            buffer2.add(value2);
            tryEmitCombined(resultStream, combiner);
        });
        
        return resultStream;
    }
    
    private void tryEmitCombined(ReactiveStream<R> resultStream, BiFunction<T, U, R> combiner) {
        if (!buffer1.isEmpty() && !buffer2.isEmpty()) {
            T value1 = buffer1.removeFirst();
            U value2 = buffer2.removeFirst();
            R result = combiner.apply(value1, value2);
            resultStream.emit(result);
        }
    }
}

class StreamMerger<T> {
    private final ReactiveStream<T> mergedStream = new ReactiveStream<>();
    
    public StreamMerger(List<ReactiveStream<T>> streams) {

        for (ReactiveStream<T> stream : streams) {
            stream.subscribe(mergedStream::emit);
        }
    }
    
    public ReactiveStream<T> forEach(Consumer<T> action) {
        return mergedStream.forEach(action);
    }
}

// Retry mechanism
class RetryableOperation {
    public <T> T executeWithRetry(Supplier<T> operation, int maxRetries) {
        int attempts = 0;
        Exception lastException = null;
        
        while (attempts < maxRetries) {
            try {
                return operation.get();
            } catch (Exception e) {
                lastException = e;
                attempts++;
                System.out.println("⚠️ Attempt " + attempts + " failed: " + e.getMessage());
                
                if (attempts < maxRetries) {
                    try {
                        Thread.sleep(100L * attempts); // Exponential backoff
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
        
        throw new RuntimeException("Operation failed after " + maxRetries + " attempts", lastException);
    }
}

// Event publisher with observer pattern
class EmployeeEventPublisher {
    private final List<Consumer<EmployeeEvent>> observers = new ArrayList<>();
    
    public void subscribe(Consumer<EmployeeEvent> observer) {
        observers.add(observer);
    }
    
    public void unsubscribe(Consumer<EmployeeEvent> observer) {
        observers.remove(observer);
    }
    
    public void publishEmployeeCreated(EmployeeEvent event) {
        System.out.println("📤 Publishing EmployeeCreated event: " + event.employeeName());
        
        for (Consumer<EmployeeEvent> observer : observers) {
            try {
                observer.accept(event);
            } catch (Exception e) {
                System.out.println("⚠️ Error notifying observer: " + e.getMessage());
            }
        }
    }
}

// Data classes
record EmployeeEvent(String employeeName, String department) { }

record EmployeeInfo(Long id, String name, String department) {
}

record DepartmentInfo(Long id, String name, String description) {
}

record SalaryInfo(Long employeeId, Double amount, String currency) {
}

record CompleteProfile(EmployeeInfo employee, DepartmentInfo department, SalaryInfo salary) {
}

record Pair<T, U>(T first, U second) {
}