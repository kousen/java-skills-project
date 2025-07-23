---
theme: seriph
background: https://source.unsplash.com/collection/94734566/1920x1080
class: text-center
highlighter: shiki
lineNumbers: false
info: |
  ## Java Logging Frameworks
  Learn to configure and use logging frameworks for application diagnostics
drawings:
  persist: false
defaults:
  foo: true
transition: slide-left
title: Java Logging Frameworks
---

# Java Logging Frameworks

Configure and Use Logging for Application Diagnostics

<div class="pt-12">
  <span @click="$slidev.nav.next" class="px-2 py-1 rounded cursor-pointer" hover="bg-white bg-opacity-10">
    Press Space for next page <carbon:arrow-right class="inline"/>
  </span>
</div>

---
transition: slide-left
---

# Why Logging Matters

## Application Diagnostics

<v-clicks>

- Debug issues in production
- Track application behavior
- Monitor performance

</v-clicks>

## Better Than System.out.println()

<v-clicks>

- Control output levels
- Write to multiple destinations
- Format messages consistently

</v-clicks>

---
transition: slide-left
---

# Java Logging Options

## Built-in Options

<v-clicks>

- **java.util.logging** - JDK built-in
- Simple but limited features

</v-clicks>

## Popular Frameworks

<v-clicks>

- **SLF4J** - Simple Logging Facade
- **Logback** - Modern implementation
- **Log4j2** - Feature-rich option

</v-clicks>

---
transition: slide-left
---

# SLF4J + Logback Setup

## Maven/Gradle Dependencies

```xml
<!-- Already in our project! -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.17</version>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.5.18</version>
</dependency>
```

---
transition: slide-left
---

# Creating a Logger

## Basic Logger Setup

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmployeeService {
    private static final Logger logger = 
        LoggerFactory.getLogger(EmployeeService.class);
    
    public void processEmployee(Employee emp) {
        logger.info("Processing employee: {}", emp.getName());
    }
}
```

---
transition: slide-left
---

# Logging Levels

## From Least to Most Severe

<v-clicks>

- **TRACE** - Detailed diagnostic info
- **DEBUG** - Debugging information
- **INFO** - General information

</v-clicks>

---
transition: slide-left
---

# Logging Levels (continued)

## Error Levels

<v-clicks>

- **WARN** - Warning messages
- **ERROR** - Error conditions
- **OFF** - Turn off logging

</v-clicks>

---
transition: slide-left
---

# Using Different Log Levels

## Example Implementation

```java
public class EmployeeValidator {
    private static final Logger logger = 
        LoggerFactory.getLogger(EmployeeValidator.class);
    
    public boolean validate(Employee emp) {
        logger.trace("Validating employee: {}", emp);
        
        if (emp.getName() == null) {
            logger.error("Employee name cannot be null");
            return false;
        }
        
        if (emp.getSalary() < 0) {
            logger.warn("Negative salary for: {}", emp.getName());
        }
        
        logger.debug("Validation complete for: {}", emp.getName());
        return true;
    }
}
```

---
transition: slide-left
---

# Parameterized Messages

## Efficient String Formatting

```java
// Bad - string concatenation always happens
logger.info("Processing " + count + " employees");

// Good - only concatenates if INFO is enabled
logger.info("Processing {} employees", count);

// Multiple parameters
logger.info("Employee {} has salary ${}", 
    emp.getName(), emp.getSalary());
```

---
transition: slide-left
---

# Exception Logging

## Logging Stack Traces

```java
public void saveEmployee(Employee emp) {
    try {
        database.save(emp);
        logger.info("Saved employee: {}", emp.getName());
    } catch (DatabaseException e) {
        // Log with stack trace
        logger.error("Failed to save employee: {}", 
            emp.getName(), e);
    }
}
```

---
transition: slide-left
---

# Logback Configuration

## Basic logback.xml

```xml
<configuration>
    <appender name="STDOUT" 
              class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <root level="info">
        <appender-ref ref="STDOUT" />
    </root>
</configuration>
```

---
transition: slide-left
---

# Log Pattern Elements

## Common Pattern Components

<v-clicks>

- **%d{pattern}** - Date/time
- **%thread** - Thread name
- **%-5level** - Log level (padded)

</v-clicks>

---
transition: slide-left
---

# Log Pattern Elements (continued)

## Additional Components

<v-clicks>

- **%logger{36}** - Logger name (truncated)
- **%msg** - Log message
- **%n** - Line separator

</v-clicks>

---
transition: slide-left
---

# File Appender

## Writing Logs to Files

```xml
<appender name="FILE" 
          class="ch.qos.logback.core.FileAppender">
    <file>logs/employee-system.log</file>
    <encoder>
        <pattern>%d %level %logger - %msg%n</pattern>
    </encoder>
</appender>

<root level="info">
    <appender-ref ref="STDOUT" />
    <appender-ref ref="FILE" />
</root>
```

---
transition: slide-left
---

# Rolling File Appender

## Automatic Log Rotation

```xml
<appender name="ROLLING" 
    class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>logs/app.log</file>
    
    <rollingPolicy 
        class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
        <fileNamePattern>logs/app.%d{yyyy-MM-dd}.log</fileNamePattern>
        <maxHistory>30</maxHistory>
    </rollingPolicy>
    
    <encoder>
        <pattern>%d %-5level %logger{35} - %msg%n</pattern>
    </encoder>
</appender>
```

---
transition: slide-left
---

# Logger Hierarchy

## Package-based Configuration

```xml
<configuration>
    <!-- Root logger at INFO -->
    <root level="info">
        <appender-ref ref="STDOUT" />
    </root>
    
    <!-- Specific package at DEBUG -->
    <logger name="com.oreilly.employee.service" level="debug"/>
    
    <!-- Third-party library at WARN -->
    <logger name="org.springframework" level="warn"/>
</configuration>
```

---
transition: slide-left
---

# Conditional Logging

## Performance Optimization

```java
public void processLargeDataset(List<Employee> employees) {
    // Check if debug is enabled before expensive operation
    if (logger.isDebugEnabled()) {
        logger.debug("Processing {} employees: {}", 
            employees.size(), 
            employees.stream()
                .map(Employee::getName)
                .collect(Collectors.joining(", ")));
    }
    
    // Process employees...
}
```

---
transition: slide-left
---

# MDC (Mapped Diagnostic Context)

## Adding Context to Logs

```java
import org.slf4j.MDC;

public void processRequest(String userId, String requestId) {
    try {
        MDC.put("userId", userId);
        MDC.put("requestId", requestId);
        
        logger.info("Processing request");
        // All logs will include userId and requestId
        
    } finally {
        MDC.clear();
    }
}
```

---
transition: slide-left
---

# MDC in Log Pattern

## Including Context in Output

```xml
<encoder>
    <pattern>%d [%X{userId}] [%X{requestId}] %-5level %logger - %msg%n</pattern>
</encoder>
```

Output:
```
2025-01-15 [john123] [req-456] INFO EmployeeService - Processing request
```

---
transition: slide-left
---

# Async Logging

## Improve Performance

```xml
<appender name="ASYNC" 
          class="ch.qos.logback.classic.AsyncAppender">
    <queueSize>500</queueSize>
    <discardingThreshold>0</discardingThreshold>
    <appender-ref ref="FILE" />
</appender>

<root level="info">
    <appender-ref ref="ASYNC" />
</root>
```

---
transition: slide-left
---

# Best Practices

## Logging Guidelines

<v-clicks>

- Use appropriate log levels
- Include context in messages
- Avoid logging sensitive data

</v-clicks>

---
transition: slide-left
---

# Best Practices (continued)

## Performance Tips

<v-clicks>

- Use parameterized messages
- Guard expensive operations
- Configure async appenders for high volume

</v-clicks>

---
transition: slide-left
---

# Common Mistakes

## What to Avoid

<v-clicks>

- Logging at wrong level
- Including passwords or PII
- Excessive DEBUG logging in production

</v-clicks>

---
transition: slide-left
---

# Migration from println

## Before and After

```java
// Before
System.out.println("Processing employee: " + emp.getName());
System.err.println("Error: " + e.getMessage());

// After
logger.info("Processing employee: {}", emp.getName());
logger.error("Failed to process employee", e);
```

---
transition: slide-left
layout: center
---

# Summary

<v-clicks>

- Logging provides better diagnostics than println
- SLF4J + Logback is a popular choice
- Configure levels and appenders appropriately
- Use MDC for request context
- Follow best practices for performance

</v-clicks>

---
transition: slide-left
layout: center
---

# Next: Consuming REST APIs

Learn to use Java's built-in HTTP client