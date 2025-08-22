# Video Script: Input Validation for Security

**Goal:** 22. Implement secure input validation to prevent SQL injection and XSS attacks.
**Target Duration:** 4-5 minutes

---

### SCENE 1: Introduction (0:00 - 0:30)

**(Show Slide 1: Title Slide - "Input Validation for Security")**

**Host:**
"Welcome to one of the most important videos in this series. Today we're talking about input validation - your first and best defense against security attacks. If you've ever wondered how websites get hacked or databases get compromised, stick around. More importantly, you'll learn how to prevent it."

## The Threat Is Real (0:15-0:45)

Let me tell you a scary statistic - SQL injection has been the number one web vulnerability for over a decade. It's how major data breaches happen. And cross-site scripting? That's how attackers steal user sessions and credentials.

The good news? Both are completely preventable with proper input validation. Let's see how.

## SQL Injection Explained (0:45-1:30)

SQL injection happens when user input is concatenated directly into SQL queries.

**(Show Slide 3: SQL Injection Example)**

See this innocent-looking code? If I enter "Robert'; DROP TABLE employees; --" as the name, I've just deleted your entire employee table. The semicolon ends the first query, DROP TABLE runs next, and the double dash comments out the rest.

Never, ever build SQL queries with string concatenation. 

**(Show Slide 4: Prepared Statements)**

Instead, use prepared statements. The question mark is a placeholder that gets safely filled by setString. The database knows this is data, not SQL commands, so injection is impossible.

## Modern ORM Protection (1:30-2:00)

If you're using Spring Data JPA or Hibernate, you're already protected in most cases.

**(Show Slide 5: JPA Protection)**

These frameworks use parameterized queries under the hood. Named parameters with colons or positional parameters with question marks - both are safe. The key is letting the framework handle the SQL generation.

## Cross-Site Scripting (XSS) (2:00-2:45)

XSS is sneakier. It happens when user input gets displayed on a webpage without encoding.

**(Show Slide 6: XSS Attack Example)**

If someone enters JavaScript in a comment field and you display it directly, that script runs in every user's browser. Attackers can steal cookies, redirect users, or deface your site.

The fix? Always encode output.

**(Show Slide 7: HTML Encoding)**

Spring's HtmlUtils.htmlEscape converts dangerous characters to safe HTML entities. That script tag becomes harmless text that displays but doesn't execute.

## Modern Validation Service (2:45-3:30)

Let's look at a production-ready validation service that's properly tested.

**(Show Slide 8: Modern Java Validation Service)**

Our InputValidator class uses modern Java patterns. Business constants are clearly defined. Validation patterns are compiled once for performance. Package-private methods enable comprehensive testing while keeping the API clean.

The beauty of this approach? Every validation method is thoroughly tested.

**(Show Slide 9: Comprehensive Test Coverage)**

We have 61 tests covering every security scenario. Field validation tests check patterns and business rules. Security tests verify SQL injection and XSS detection. Business rule tests ensure domain policies are enforced.

This isn't just demo code - this is production-ready validation with test coverage that proves it works.

## Layered Security Validation (3:30-4:00)

Here's how we prevent XSS attacks at the field level.

**(Show Slide 10: XSS Prevention in Action)**

Our validateEmployeeName method combines basic pattern validation with XSS detection. First, we check if the name matches our allowed pattern. Then we scan for dangerous XSS patterns like script tags and JavaScript injection.

This layered approach means an attacker has to bypass multiple validation checks to succeed.

## REST API Integration (4:00-4:30)

Now let's see how this validation integrates with Spring Boot REST APIs.

**(Show Slide 11: REST API Integration)**

Our SecurityController uses the InputValidator service to provide real-time validation. Send an employee object to the /validate endpoint and get back detailed validation results - both field errors and business rule violations.

This isn't theoretical - you can test this right now with curl or Postman. The API returns structured JSON showing exactly what's valid and what isn't.

## Test-Driven Security Benefits (4:30-4:50)

This approach gives us confidence that our security actually works.

**(Show Slide 12: Security Best Practices)**

Test-driven security means every validation rule is verified by tests. Parameterized tests systematically cover edge cases. No security validation goes untested.

The separation of concerns means we can test validation logic independently of HTTP concerns. And the production-ready design means this code can go straight into your applications.

## Wrapping Up (4:50-5:05)

Input validation isn't optional - it's essential. We've seen how to build a production-ready validation service with comprehensive test coverage. Use prepared statements for SQL. Implement layered XSS detection. Test every security validation rule.

Remember: never trust user input. Ever. But now you have the tools to validate it properly.

Next time, we'll explore Java's cryptographic APIs for encrypting sensitive data. Until then, validate those inputs and keep your applications secure!

## Code Examples Referenced:

1. InputValidator service class with modern Java patterns
2. Comprehensive test suite with 61 security validation tests  
3. SQL injection detection with pattern matching
4. XSS prevention with layered validation
5. REST API integration showing real validation in action
6. Business rule validation with domain policies
7. Package-private testing patterns for security code
8. Production-ready error handling and reporting