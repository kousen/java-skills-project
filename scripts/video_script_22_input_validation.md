# Video Script: Input Validation for Security

## Introduction (0:00-0:15)

Welcome to one of the most important videos in this series. Today we're talking about input validation - your first and best defense against security attacks. If you've ever wondered how websites get hacked or databases get compromised, stick around. More importantly, you'll learn how to prevent it.

## The Threat Is Real (0:15-0:45)

Let me tell you a scary statistic - SQL injection has been the number one web vulnerability for over a decade. It's how major data breaches happen. And cross-site scripting? That's how attackers steal user sessions and credentials.

The good news? Both are completely preventable with proper input validation. Let's see how.

## SQL Injection Explained (0:45-1:30)

SQL injection happens when user input is concatenated directly into SQL queries.

[Show dangerous example]

See this innocent-looking code? If I enter "Robert'; DROP TABLE employees; --" as the name, I've just deleted your entire employee table. The semicolon ends the first query, DROP TABLE runs next, and the double dash comments out the rest.

Never, ever build SQL queries with string concatenation. 

[Show safe example]

Instead, use prepared statements. The question mark is a placeholder that gets safely filled by setString. The database knows this is data, not SQL commands, so injection is impossible.

## Modern ORM Protection (1:30-2:00)

If you're using Spring Data JPA or Hibernate, you're already protected in most cases.

[Show JPA examples]

These frameworks use parameterized queries under the hood. Named parameters with colons or positional parameters with question marks - both are safe. The key is letting the framework handle the SQL generation.

## Cross-Site Scripting (XSS) (2:00-2:45)

XSS is sneakier. It happens when user input gets displayed on a webpage without encoding.

[Show XSS example]

If someone enters JavaScript in a comment field and you display it directly, that script runs in every user's browser. Attackers can steal cookies, redirect users, or deface your site.

The fix? Always encode output.

[Show encoding example]

Spring's HtmlUtils.htmlEscape converts dangerous characters to safe HTML entities. That script tag becomes harmless text that displays but doesn't execute.

## Bean Validation Framework (2:45-3:30)

Java gives us a powerful validation framework. Let's use it!

[Show Employee class with annotations]

Look at these annotations. @NotNull ensures the field exists. @Size limits length. @Pattern enforces a regex - here we're only allowing letters and spaces in names. @Email validates email format.

Add @Valid to your controller method, and Spring automatically validates incoming data. Invalid requests get rejected with a 400 status before they reach your business logic.

## Custom Validators (3:30-4:00)

Sometimes you need custom validation logic.

[Show custom validator]

This validator checks for SQL injection patterns - semicolons, SQL comments, script tags. Create an annotation, implement the validator, and you've got reusable security validation.

The regex might look scary, but it's checking for common attack patterns. In production, you might use a more sophisticated approach, but this shows the concept.

## File Upload Security (4:00-4:30)

File uploads are particularly dangerous. You need multiple checks.

[Show file upload validation]

First, validate the content type - only accept expected file types. Then check file size to prevent denial of service. Finally, sanitize the filename to prevent path traversal attacks.

Never trust the original filename! Always generate your own or carefully validate it.

## Defense in Depth (4:30-4:50)

Security isn't one thing - it's layers of protection. Validate input at the edge. Use parameterized queries. Encode output. Set security headers. Implement rate limiting.

Think of it like a medieval castle - moat, walls, gates, guards. Each layer makes attack harder.

## Wrapping Up (4:50-5:05)

Input validation isn't optional - it's essential. Use prepared statements for SQL. Encode output for XSS prevention. Leverage Bean Validation. Create custom validators for your business rules.

Remember: never trust user input. Ever. Validate everything.

Next time, we'll explore Java's cryptographic APIs for encrypting sensitive data. Until then, validate those inputs and keep your applications secure!

## Code Examples Referenced:

1. SQL injection vulnerability and fix
2. Prepared statement usage
3. Spring Data JPA safety
4. XSS vulnerability and HTML encoding
5. Bean Validation annotations
6. Custom SQL injection validator
7. File upload validation
8. Security headers configuration