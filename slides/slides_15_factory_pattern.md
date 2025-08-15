---
layout: cover
--- 

# The Factory Pattern in Modern Java

<div class="pt-12">
  <span class="px-2 py-1 rounded">
    Goal 15: Master factory patterns as they're actually used in Java 2025 - from Optional.of() to HTTP client factories.
  </span>
</div>

---

# Contact Info

Ken Kousen<br>
Kousen IT, Inc.

- ken.kousen@kousenit.com
- http://www.kousenit.com
- http://kousenit.org (blog)
- Social Media:
  - [@kenkousen](https://twitter.com/kenkousen) (Twitter)
  - [@kousenit.com](https://bsky.app/profile/kousenit.com) (Bluesky)
  - [https://www.linkedin.com/in/kenkousen/](https://www.linkedin.com/in/kenkousen/) (LinkedIn)
- *Tales from the jar side* (free newsletter)
  - https://kenkousen.substack.com
  - https://youtube.com/@talesfromthejarside

---
layout: section
---

# Factory Patterns in Modern Java 2025

<v-clicks>

- **Static Factory Methods**: The most common approach (Optional.of(), List.of(), Files.newBufferedReader())
- **Factory Classes**: For complex scenarios (HTTP clients, database connections, configuration parsers)
- **Registry-based Factories**: For plugin architectures and runtime strategy selection

</v-clicks>

<div class="mt-8">
<v-click>

**Key Insight:** You already use factory patterns every day in Java!

</v-click>
</div>

---

# Static Factory Methods (Most Common)

Static methods that return instances of a class.

<v-clicks>

1.  **JDK Examples:** `Optional.of()`, `List.of()`, `LocalDateTime.now()`

2.  **Clear Naming:** Better than constructors - `valueOf()`, `of()`, `from()`, `getInstance()`

3.  **Flexibility:** Can return cached instances, subclasses, or null

4.  **Modern Preference:** Preferred over traditional factory method pattern

</v-clicks>

---

# JDK Static Factory Methods

Examples you use every day:

```java
// Optional factories
var present = Optional.of("Hello");
var empty = Optional.ofNullable(null);

// Collection factories  
var list = List.of("Java", "Python", "JavaScript");
var set = Set.of("Spring", "React", "Docker");
var map = Map.of("language", "Java", "version", "21");

// Logger factory - you use this every day!
Logger logger = LoggerFactory.getLogger(MyClass.class);
Logger namedLogger = LoggerFactory.getLogger("com.example.service");

// Date/Time factories
var now = LocalDateTime.now();
var date = LocalDateTime.of(2025, 1, 1, 12, 0);
```

<v-click>

**LoggerFactory.getLogger()** is a perfect Factory pattern - previews Section 19!

</v-click>

---

# Real-World Example: HTTP Client Factory

Actual factory pattern used in modern applications.

**(Show `design-patterns/src/main/java/ModernFactoryPatterns.java`)**

**Scenario:** Different HTTP client configurations for different needs.

```java
class HttpClientFactory {
    
    // Static factory methods for different client types
    public static HttpClient createBasicClient(Duration timeout, String userAgent) {
        var config = new HttpClientConfig(timeout, userAgent);
        return new BasicHttpClient(config);  // Record implementation
    }
    
    public static HttpClient createResilientClient(Duration timeout, String userAgent) {
        var config = new HttpClientConfig(timeout, connectionTimeout, true, 
                       userAgent, headers, true, true);  // retry + circuit breaker
        return new ResilientHttpClient(config);
    }
    
    // Notice: Uses records for the HTTP client implementations!
}
```

---

# Configuration Parser Factory

Another real-world example: parsing different config formats.

```java
public class ConfigParserFactory {
    
    // Static factory based on file extension
    public static ConfigParser forFile(String filename) {
        if (filename.endsWith(".json")) {
            return new JsonConfigParser();
        } else if (filename.endsWith(".yml")) {
            return new YamlConfigParser();
        } else if (filename.endsWith(".properties")) {
            return new PropertiesConfigParser();
        }
        throw new IllegalArgumentException("Unsupported format: " + filename);
    }
    
    // Static factory based on content type
    public static ConfigParser forContentType(String contentType) {
        return switch (contentType.toLowerCase()) {
            case "application/json" -> new JsonConfigParser();
            case "text/yaml" -> new YamlConfigParser();
            case "text/plain" -> new PropertiesConfigParser();
            default -> throw new IllegalArgumentException("Unsupported: " + contentType);
        };
    }
}
```

---

# Using Modern Factories

Clean, expressive client code:

```java
// HTTP Client factories in action
var basicClient = HttpClientFactory.createBasicClient(
    Duration.ofSeconds(30), "MyApp/1.0"
);

var resilientClient = HttpClientFactory.createResilientClient(
    Duration.ofSeconds(60), "MyApp/1.0 (Resilient)"
);

var authClient = HttpClientFactory.createAuthenticatedClient(
    Duration.ofSeconds(45), "MyApp/1.0", "bearer-token"
);

// All return HttpClient interface - polymorphic usage
HttpClient client = getClientForEnvironment(); // Could be any type
client.get("https://api.example.com/users");
```

<v-click>

```java
// Config parser factories
var jsonParser = ConfigParserFactory.forFile("application.json");
var yamlParser = ConfigParserFactory.forFile("config.yml");
var propsParser = ConfigParserFactory.forContentType("text/plain");
```

</v-click>

---
layout: section
---

# Modern Factory Pattern Guidelines

<v-clicks>

- **Prefer static factory methods** over traditional factory classes for most scenarios
- **Use factory classes** mainly for framework integration (builders handle complex creation)
- **Traditional factory method pattern** has largely been replaced by builders
- **Registry-based factories** work well for plugin architectures

</v-clicks>

---

# Factory Pattern Benefits (2025)

<v-clicks>

- **Static Factory Methods**: Clear naming, caching, return subclasses
- **Factory Classes**: Framework integration (Spring BeanFactory, SLF4J LoggerFactory)
- **Framework Integration**: Spring BeanFactory, Jackson ObjectMapper creation
- **Runtime Selection**: Content-type based parsers, environment-specific clients

</v-clicks>

<div class="mt-8">
<v-click>

**Bottom Line:** Factory patterns are everywhere in modern Java - now you can use them intentionally!

</v-click>
</div>
