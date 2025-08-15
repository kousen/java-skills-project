
# Video Script: The Factory Design Pattern in Modern Java

## 1. Introduction

**Host:** "Welcome back to our series on Java design patterns. Today, we're looking at the Factory pattern, but I want to show you how it's actually used in modern Java 2025 - not the artificial examples you might have seen before."

**Host:** "The truth is, you use factory patterns every day in Java without even realizing it. Every time you call `Optional.of()`, `List.of()`, or `Files.newBufferedReader()`, you're using static factory methods. And when you're setting up HTTP clients or configuration parsers in real applications, you're often using factory classes."

**Host:** "Let's explore both the theory and the practical, real-world applications you'll encounter in professional Java development."

---

## 2. Factory Patterns in Modern Java

**Host:** "In modern Java, we primarily see two types of factory patterns. First, and most common, are **static factory methods**. These are simple static methods that return instances - like `Optional.of()` or `List.of()`. They're everywhere in the JDK."

**Host:** "Second, we have **factory classes** for more complex scenarios. These are typically used when you need to configure objects with multiple dependencies, like HTTP clients, database connections, or configuration parsers."

**Host:** "The traditional Gang of Four factory method pattern with abstract creators and concrete creators? That's largely been replaced by builders for complex object creation. But the core idea - hiding object creation complexity - is still everywhere."

---

## 3. Code Demo: Static Factory Methods

**Host:** "Let's start with static factory methods, since these are what you'll use most often. Here are some you already know from the JDK."

```java
// JDK Static Factory Methods - you use these every day!
var presentValue = Optional.of("Hello World");
var emptyValue = Optional.ofNullable(null);

var list = List.of("Java", "Python", "JavaScript");
var set = Set.of("Spring", "React", "Docker");
var map = Map.of("language", "Java", "version", "21");

var now = LocalDateTime.now();
var specificDate = LocalDateTime.of(2025, 1, 1, 12, 0);
```

**Host:** "Notice the naming conventions: `of`, `ofNullable`, `now`. These are much clearer than constructor names, and they can return cached instances or even different subclasses."

**Host:** "And here's one you probably use every day without thinking about it - SLF4J's LoggerFactory:"

```java
Logger logger = LoggerFactory.getLogger(MyClass.class);
Logger namedLogger = LoggerFactory.getLogger("com.example.service");
```

**Host:** "This is a perfect factory pattern - it hides which logging implementation you're using and returns the appropriate logger. We'll dive deep into this in Section 19 on logging."

## 4. Code Demo: HTTP Client Factory

**(Show `design-patterns/src/main/java/ModernFactoryPatterns.java` on screen)**

**Host:** "Now let's look at a real-world factory class. In modern applications, you often need different HTTP client configurations - basic clients, resilient clients with retries, authenticated clients. Here's how you'd use a factory for that."

```java
// Static factory methods for different client types - notice the SLF4J LoggerFactory calls!
public static HttpClient createBasicClient(Duration timeout, String userAgent) {
    var config = new HttpClientConfig(timeout, userAgent);
    return new BasicHttpClient(config);  // Record with config
}

public static HttpClient createResilientClient(Duration timeout, String userAgent) {
    var config = new HttpClientConfig(timeout, connectionTimeout, true, userAgent, 
                    headers, true, true);  // retry + circuit breaker enabled
    return new ResilientHttpClient(config);
}
```

---

## 5. Using the Factories

**Host:** "Here's how you'd use these factories in real code. The client code is clean and expressive."

```java
// Create different HTTP clients based on needs
var basicClient = HttpClientFactory.createBasicClient(
    Duration.ofSeconds(30), "MyApp/1.0"
);

var resilientClient = HttpClientFactory.createResilientClient(
    Duration.ofSeconds(60), "MyApp/1.0 (Resilient)"
);

var authClient = HttpClientFactory.createAuthenticatedClient(
    Duration.ofSeconds(45), "MyApp/1.0", "bearer-token-here"
);
```

**Host:** "The beautiful part is that all these return the same `HttpClient` interface, so your code can work with any of them polymorphically. You can switch from a basic client to a resilient client just by changing one line."

## 6. Configuration Parser Factory

**Host:** "Here's another real-world example - configuration parsers. You often need to parse JSON, YAML, or Properties files. Instead of hardcoding the parser type, you use a factory based on the file extension or content type."

```java
// Factory methods based on filename
var jsonParser = ConfigParserFactory.forFile("application.json");
var yamlParser = ConfigParserFactory.forFile("config.yml");

// Or based on content type
var parser = ConfigParserFactory.forContentType("application/json");
```

---

## 7. Summary

**Host:** "Let's wrap up with the key takeaways for factory patterns in modern Java 2025."

**Host:** "First, **static factory methods** are everywhere and should be your go-to approach. They're clearer than constructors, can return cached instances, and follow established naming conventions like `of`, `from`, `valueOf`, and `getInstance`."

**Host:** "Second, **factory classes** are still valuable for some scenarios, though builders have largely taken over complex object creation. You'll see factories mainly in framework integration - like Spring's BeanFactory or SLF4J's LoggerFactory, which we'll cover in our logging section."

**Host:** "Third, the traditional Gang of Four factory method pattern with abstract creators? That's mostly been replaced by builders. But the core principle - hiding creation complexity from client code - is more relevant than ever."

**Host:** "You're already using factory patterns every day in Java. Now you know the theory behind them and can apply the pattern intentionally in your own code."

**Host:** "That's the modern Factory pattern in Java 2025. Thanks for watching!"
