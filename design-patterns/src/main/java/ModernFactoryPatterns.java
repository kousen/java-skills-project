import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
  Modern Factory Pattern Demonstrations (2025)
  <p>
  This class demonstrates how the Factory pattern is actually used in modern Java:
  1. Static factory methods (like Optional.of, List.of, Files.newBufferedReader)
  2. Real-world factory classes for HTTP clients and configuration parsers
  3. Framework integration patterns
 */

// =============================================================================
// PART 1: Static Factory Methods (Most Common Modern Usage)
// =============================================================================

/**
 * Demonstrates static factory methods - the most common form of Factory pattern in modern Java
 */
class StaticFactoryMethods {
    private static final Logger logger = LoggerFactory.getLogger(StaticFactoryMethods.class);
    
    @SuppressWarnings("OptionalOfNullableMisuse")
    public static void demonstrateJdkFactories() {
        logger.info("=== JDK Static Factory Methods ===");
        
        // Logger factory - real Factory pattern in action!
        Logger specificLogger = LoggerFactory.getLogger("com.example.demo");
        logger.info("LoggerFactory.getLogger(): Created logger for {}", specificLogger.getName());
        
        // Optional factories
        var presentValue = Optional.of("Hello World");
        var emptyValue = Optional.ofNullable(null);
        logger.info("Optional.of(): {}", presentValue);
        logger.info("Optional.ofNullable(null): {}", emptyValue);
        
        // Collection factories
        var list = List.of("Java", "Python", "JavaScript");
        var set = Set.of("Spring", "React", "Docker");
        var map = Map.of("language", "Java", "version", "21");
        logger.info("List.of(): {}", list);
        logger.info("Set.of(): {}", set);
        logger.info("Map.of(): {}", map);
        
        // Time/Date factories
        var now = LocalDateTime.now();
        var specificDate = LocalDateTime.of(2025, 1, 1, 12, 0);
        logger.info("LocalDateTime.now(): {}", now);
        logger.info("LocalDateTime.of(): {}", specificDate);
        
        logger.info("Notice how LoggerFactory.getLogger() is itself a perfect example of the Factory pattern!");
    }
}

// =============================================================================
// PART 2: HTTP Client Factory (Real-world Factory Classes)
// =============================================================================

// Configuration for different HTTP client types
record HttpClientConfig(
    Duration timeout,
    Duration connectionTimeout,
    boolean followRedirects,
    String userAgent,
    Map<String, String> defaultHeaders,
    boolean enableRetry,
    boolean enableCircuitBreaker
) {
    // Convenience constructor for basic config
    public HttpClientConfig(Duration timeout, String userAgent) {
        this(timeout, Duration.ofSeconds(10), true, userAgent, 
             Map.of(), false, false);
    }
}

// Abstract HTTP client interface
interface HttpClient {
    String getName();
    HttpClientConfig getConfig();
    String get(String url);
    String post(String url, String body);
    void close();
    
    default String getClientInfo() {
        var config = getConfig();
        return """
            HTTP Client: %s
              Timeout: %s
              Connection Timeout: %s
              User Agent: %s
              Follow Redirects: %s
              Retry Enabled: %s
              Circuit Breaker: %s
            """.formatted(getName(), config.timeout(), config.connectionTimeout(),
                config.userAgent(), config.followRedirects(), 
                config.enableRetry(), config.enableCircuitBreaker());
    }
}

// Concrete HTTP client implementations
record BasicHttpClient(HttpClientConfig config) implements HttpClient {
    @Override
    public String getName() { return "Basic HTTP Client"; }
    
    @Override
    public HttpClientConfig getConfig() { return config; }
    
    @Override
    public String get(String url) {
        return "GET " + url + " (timeout: " + config.timeout() + ")";
    }
    
    @Override
    public String post(String url, String body) {
        return "POST " + url + " with body length: " + body.length();
    }
    
    @Override
    public void close() {
        System.out.println("Closing basic HTTP client");
    }
}

class ResilientHttpClient implements HttpClient {
    private final HttpClientConfig config;
    private int retryCount = 0;
    
    public ResilientHttpClient(HttpClientConfig config) {
        this.config = config;
    }
    
    @Override
    public String getName() { return "Resilient HTTP Client"; }
    
    @Override
    public HttpClientConfig getConfig() { return config; }
    
    @Override
    public String get(String url) {
        retryCount++;
        return "GET " + url + " (with retry #" + retryCount + " and circuit breaker)";
    }
    
    @Override
    public String post(String url, String body) {
        return "POST " + url + " (resilient with retries and timeouts)";
    }
    
    @Override
    public void close() {
        System.out.println("Closing resilient HTTP client (retry count: " + retryCount + ")");
    }
}

record AuthenticatedHttpClient(HttpClientConfig config, String authToken) implements HttpClient {
    @Override
    public String getName() { return "Authenticated HTTP Client"; }
    
    @Override
    public HttpClientConfig getConfig() { return config; }
    
    @Override
    public String get(String url) {
        return "GET " + url + " (with auth token: " + authToken.substring(0, 8) + "...)";
    }
    
    @Override
    public String post(String url, String body) {
        return "POST " + url + " (authenticated with bearer token)";
    }
    
    @Override
    public void close() {
        System.out.println("Closing authenticated HTTP client");
    }
}


class HttpClientFactory {
    
    // Static factory method - most common pattern
    public static HttpClient createBasicClient(Duration timeout, String userAgent) {
        var config = new HttpClientConfig(timeout, userAgent);
        return new BasicHttpClient(config);
    }
    
    // Static factory method with full configuration
    public static HttpClient createResilientClient(Duration timeout, String userAgent) {
        var config = new HttpClientConfig(
            timeout,
            Duration.ofSeconds(5),
            true,
            userAgent,
            Map.of("Accept", "application/json", "Content-Type", "application/json"),
            true,  // enable retry
            true   // enable circuit breaker
        );
        return new ResilientHttpClient(config);
    }
    
    // Static factory method for authenticated clients
    public static HttpClient createAuthenticatedClient(Duration timeout, String userAgent, String authToken) {
        var config = new HttpClientConfig(
            timeout,
            Duration.ofSeconds(10),
            true,
            userAgent,
            Map.of("Authorization", "Bearer " + authToken),
            false,
            false
        );
        return new AuthenticatedHttpClient(config, authToken);
    }
    
}

// =============================================================================
// PART 3: Configuration Parser Factory (Another Real-world Example)
// =============================================================================

// Configuration data structure
record AppConfig(
    String appName,
    String version,
    Map<String, String> database,
    Map<String, Object> features
) {}

// Parser interface
interface ConfigParser {
    String getFormat();
    AppConfig parse(String configData);
    
    default String getParserInfo() {
        return "Configuration Parser for " + getFormat() + " format";
    }
}

// Concrete parsers
class JsonConfigParser implements ConfigParser {
    @Override
    public String getFormat() { return "JSON"; }
    
    @Override
    public AppConfig parse(String configData) {
        // Simplified JSON parsing simulation
        return new AppConfig(
            "MyApp",
            "1.0.0",
            Map.of("url", "jdbc:postgresql://localhost:5432/myapp", "driver", "postgresql"),
            Map.of("enableLogging", true, "maxConnections", 100)
        );
    }
}

class YamlConfigParser implements ConfigParser {
    @Override
    public String getFormat() { return "YAML"; }
    
    @Override
    public AppConfig parse(String configData) {
        // Simplified YAML parsing simulation
        return new AppConfig(
            "MyApp-YAML",
            "2.0.0",
            Map.of("url", "jdbc:mysql://localhost:3306/myapp", "driver", "mysql"),
            Map.of("enableMetrics", true, "timeout", 30)
        );
    }
}

class PropertiesConfigParser implements ConfigParser {
    @Override
    public String getFormat() { return "Properties"; }
    
    @Override
    public AppConfig parse(String configData) {
        // Simplified Properties parsing simulation
        return new AppConfig(
            "MyApp-Props",
            "1.5.0",
            Map.of("url", "jdbc:h2:mem:testdb", "driver", "h2"),
            Map.of("devMode", true, "debugLevel", 2)
        );
    }
}

// Configuration parser factory
class ConfigParserFactory {
    
    // Static factory methods based on file extension
    public static ConfigParser forFile(String filename) {
        if (filename.endsWith(".json")) {
            return new JsonConfigParser();
        } else if (filename.endsWith(".yml") || filename.endsWith(".yaml")) {
            return new YamlConfigParser();
        } else if (filename.endsWith(".properties")) {
            return new PropertiesConfigParser();
        } else {
            throw new IllegalArgumentException("Unsupported config file format: " + filename);
        }
    }
    
    // Static factory method based on content type
    public static ConfigParser forContentType(String contentType) {
        return switch (contentType.toLowerCase()) {
            case "application/json" -> new JsonConfigParser();
            case "application/x-yaml", "text/yaml" -> new YamlConfigParser();
            case "text/plain" -> new PropertiesConfigParser();
            default -> throw new IllegalArgumentException("Unsupported content type: " + contentType);
        };
    }
    
}

// =============================================================================
// MAIN DEMO CLASS
// =============================================================================

public class ModernFactoryPatterns {
    private static final Logger logger = LoggerFactory.getLogger(ModernFactoryPatterns.class);
    
    public static void main(String[] args) {
        demonstrateStaticFactoryMethods();
        logger.info("\n{}\n", "=".repeat(80));
        
        demonstrateHttpClientFactory();
        logger.info("\n{}\n", "=".repeat(80));
        
        demonstrateConfigParserFactory();
        logger.info("\n{}\n", "=".repeat(80));
        
        demonstrateFactoryBenefits();
    }
    
    private static void demonstrateStaticFactoryMethods() {
        logger.info("MODERN FACTORY PATTERNS IN JAVA 21 (2025)");
        logger.info("=".repeat(50));
        
        StaticFactoryMethods.demonstrateJdkFactories();
    }
    
    private static void demonstrateHttpClientFactory() {
        System.out.println("=== HTTP Client Factory Demo ===");
        
        // Create different types of HTTP clients using static factory methods
        var basicClient = HttpClientFactory.createBasicClient(
            Duration.ofSeconds(30), "MyApp/1.0"
        );
        
        var resilientClient = HttpClientFactory.createResilientClient(
            Duration.ofSeconds(60), "MyApp/1.0 (Resilient)"
        );
        
        var authClient = HttpClientFactory.createAuthenticatedClient(
            Duration.ofSeconds(45), "MyApp/1.0 (Auth)", "abc123token456def"
        );
        
        // Display client information
        List<HttpClient> clients = List.of(basicClient, resilientClient, authClient);
        
        for (HttpClient client : clients) {
            System.out.println(client.getClientInfo());
            
            // Test some operations
            System.out.println("  " + client.get("https://api.example.com/users"));
            System.out.println("  " + client.post("https://api.example.com/users", "{\"name\":\"John\"}"));
            
            client.close();
            System.out.println();
        }
    }
    
    private static void demonstrateConfigParserFactory() {
        System.out.println("=== Configuration Parser Factory Demo ===");
        
        // Different ways to create parsers using static factory methods
        var jsonParser = ConfigParserFactory.forFile("application.json");
        var yamlParser = ConfigParserFactory.forFile("config.yml");
        var propsParser = ConfigParserFactory.forContentType("text/plain");
        
        System.out.println();
        
        // Test parsing with different parsers
        List<ConfigParser> parsers = List.of(jsonParser, yamlParser, propsParser);
        
        for (ConfigParser parser : parsers) {
            System.out.println(parser.getParserInfo());
            AppConfig config = parser.parse("dummy config data");
            System.out.println("  Parsed: " + config.appName() + " v" + config.version());
            System.out.println("  Database: " + config.database());
            System.out.println("  Features: " + config.features());
            System.out.println();
        }
    }
    
    private static void demonstrateFactoryBenefits() {
        System.out.println("""
            === Modern Factory Pattern Benefits ===
            
            ✓ STATIC FACTORY METHODS (Most Common):
              • Optional.of(), List.of(), Files.newBufferedReader()
              • Clear naming: 'of', 'from', 'valueOf', 'getInstance'
              • Can return cached instances or subclasses
              • More readable than constructors
            
            ✓ FACTORY CLASSES (Framework Integration):
              • Spring BeanFactory for dependency injection
              • SLF4J LoggerFactory for pluggable logging
              • Database DataSource factories for connection management
            
            ✓ REGISTRY-BASED FACTORIES:
              • Plugin architectures
              • Content-type based processors
              • Runtime strategy selection
            
            ✓ FRAMEWORK INTEGRATION:
              • Jackson ObjectMapper creation
              • Spring configuration factories
              • Database DataSource factories
            
            🔧 Modern Usage vs Traditional:
              • Prefer static factory methods for simple creation
              • Use builders for complex object creation (HttpClient, Gson)
              • Factories mainly for framework integration and plugin architectures
            """);
    }
}