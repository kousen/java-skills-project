package com.oreilly.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Comprehensive tests for InputValidator security features
 * Demonstrates proper test-driven security validation
 */
@DisplayName("Input Validation Security Tests")
class InputValidatorTest {

    private final InputValidator validator = new InputValidator();

    @Nested
    @DisplayName("Employee Basic Validation")
    class EmployeeBasicValidationTest {

        @Test
        @DisplayName("Valid employee should pass all validation checks")
        void validEmployeeShouldPass() {
            // Given
            var validEmployee = new EmployeeDto(
                "Alice Johnson",
                "alice.johnson@company.com", 
                85000.0,
                "+1-555-123-4567",
                "123 Main St, Anytown, ST 12345"
            );

            // When
            var errors = validator.validateEmployee(validEmployee);

            // Then
            assertThat(errors).isEmpty();
        }

        @Test  
        @DisplayName("Invalid employee should fail with specific error messages")
        void invalidEmployeeShouldFailWithSpecificErrors() {
            // Given
            var invalidEmployee = new EmployeeDto(
                "",              // Empty name
                "not-an-email",  // Invalid email  
                -1000.0,         // Negative salary
                "123",           // Invalid phone
                ""               // Empty address
            );

            // When
            var errors = validator.validateEmployee(invalidEmployee);

            // Then
            assertThat(errors)
                .hasSize(5)
                .contains(
                    "Invalid name: must be 2-50 characters, letters only",
                    "Invalid email format", 
                    "Invalid salary: must be positive and under $1,000,000",
                    "Invalid phone format",
                    "Invalid address: cannot be empty and max 200 characters"
                );
        }
    }

    @Nested
    @DisplayName("Name Validation")
    class NameValidationTest {

        @ParameterizedTest
        @DisplayName("Valid names should be accepted")
        @ValueSource(strings = {
            "Alice Johnson",
            "Bob", 
            "Carol-Anne O'Brien",
            "Mary-Kate",
            "D'Angelo Smith"
        })
        void validNamesShouldBeAccepted(String name) {
            assertThat(validator.isValidName(name)).isTrue();
        }

        @ParameterizedTest
        @DisplayName("Invalid names should be rejected")
        @CsvSource({
            "'Dave123', 'Contains numbers'",
            "'', 'Empty string'", 
            "'A', 'Too short'",
            "'VeryLongNameThatExceedsTheMaximumLengthAllowedForEmployeeNames', 'Too long'",
            "'John <script>alert(\"xss\")</script> Doe', 'Contains XSS'"
        })
        void invalidNamesShouldBeRejected(String name, String reason) {
            assertThat(validator.isValidName(name))
                .as("Name '%s' should be invalid: %s", name, reason)
                .isFalse();
        }

        @Test
        @DisplayName("Null name should be rejected")
        void nullNameShouldBeRejected() {
            assertThat(validator.isValidName(null)).isFalse();
        }

        @Test
        @DisplayName("Whitespace-only name should be rejected")
        void whitespaceOnlyNameShouldBeRejected() {
            assertThat(validator.isValidName("   ")).isFalse();
        }
    }

    @Nested
    @DisplayName("Email Validation")
    class EmailValidationTest {

        @ParameterizedTest
        @DisplayName("Valid emails should be accepted")
        @ValueSource(strings = {
            "alice@company.com",
            "bob.smith@company.co.uk", 
            "carol+test@example.org",
            "dave_123@test-domain.com"
        })
        void validEmailsShouldBeAccepted(String email) {
            assertThat(validator.isValidEmail(email)).isTrue();
        }

        @ParameterizedTest
        @DisplayName("Invalid emails should be rejected")
        @ValueSource(strings = {
            "not-an-email",
            "missing@.com",
            "@company.com",
            "user@",
            "user space@company.com"
        })
        void invalidEmailsShouldBeRejected(String email) {
            assertThat(validator.isValidEmail(email)).isFalse();
        }
    }

    @Nested
    @DisplayName("SQL Injection Prevention")
    class SqlInjectionPreventionTest {

        @Test
        @DisplayName("Safe inputs should be accepted")
        void safeInputsShouldBeAccepted() {
            String[] safeInputs = {
                "Alice Johnson",
                "Bob Smith", 
                "Carol-Anne O'Brien",
                "Regular user input"
            };

            for (String input : safeInputs) {
                assertThat(validator.isSqlSafeInput(input))
                    .as("Input '%s' should be considered safe", input)
                    .isTrue();
            }
        }

        @ParameterizedTest
        @DisplayName("SQL injection attempts should be detected")
        @ValueSource(strings = {
            "Bob'; DROP TABLE employees; --",
            "Carol' OR '1'='1", 
            "Dave'; INSERT INTO users VALUES('hacker', 'password'); --",
            "Eve' UNION SELECT * FROM users --",
            "admin'--",
            "user'; EXEC xp_cmdshell('dir'); --"
        })
        void sqlInjectionAttemptsShouldBeDetected(String maliciousInput) {
            assertThat(validator.isSqlSafeInput(maliciousInput))
                .as("Input '%s' should be detected as potential SQL injection", maliciousInput)
                .isFalse();
        }

        @Test
        @DisplayName("Null input should be considered safe")
        void nullInputShouldBeConsideredSafe() {
            assertThat(validator.isSqlSafeInput(null)).isTrue();
        }
    }

    @Nested
    @DisplayName("XSS Prevention")
    class XssPreventionTest {

        @Test
        @DisplayName("Safe content should not be detected as XSS")
        void safeContentShouldNotBeDetectedAsXss() {
            String[] safeContent = {
                "Alice Johnson",
                "Regular text content",
                "Bob & Carol", 
                "Text with 'quotes'"
            };

            for (String content : safeContent) {
                assertThat(validator.containsPotentialXss(content))
                    .as("Content '%s' should not be detected as XSS", content)
                    .isFalse();
            }
        }

        @ParameterizedTest
        @DisplayName("XSS attempts should be detected")
        @ValueSource(strings = {
            "<script>alert('XSS')</script>",
            "<img src='x' onerror='alert(1)'>",
            "javascript:alert('XSS')",
            "<iframe src='evil.com'></iframe>",
            "onload=alert(1)"
        })
        void xssAttemptsShouldBeDetected(String xssContent) {
            assertThat(validator.containsPotentialXss(xssContent))
                .as("Content '%s' should be detected as potential XSS", xssContent)
                .isTrue();
        }

        @Test
        @DisplayName("HTML should be properly escaped")
        void htmlShouldBeProperlyEscaped() {
            // Given
            String htmlContent = "<script>alert('XSS')</script>";
            
            // When
            String escaped = validator.escapeHtml(htmlContent);
            
            // Then
            assertThat(escaped).isEqualTo("&lt;script&gt;alert(&#39;XSS&#39;)&lt;/script&gt;");
        }

        @Test
        @DisplayName("All dangerous HTML characters should be escaped")
        void allDangerousHtmlCharactersShouldBeEscaped() {
            // Given
            String dangerous = "< > & \" '";
            
            // When  
            String escaped = validator.escapeHtml(dangerous);
            
            // Then
            assertThat(escaped).isEqualTo("&lt; &gt; &amp; &quot; &#39;");
        }
    }

    @Nested
    @DisplayName("Business Rule Validation")
    class BusinessRuleValidationTest {

        @Test
        @DisplayName("Employee with company email should pass domain validation")
        void employeeWithCompanyEmailShouldPassDomainValidation() {
            // Given
            var employee = new EmployeeDto(
                "Alice Johnson", "alice@company.com", 75000.0, "555-123-4567", "123 Main St"
            );

            // When
            var errors = validator.validateEmployeeBusinessRules(employee);

            // Then
            assertThat(errors).isEmpty();
        }

        @Test
        @DisplayName("Employee with external email should fail domain validation")
        void employeeWithExternalEmailShouldFailDomainValidation() {
            // Given
            var employee = new EmployeeDto(
                "Bob Smith", "bob@external.com", 85000.0, "555-234-5678", "456 Oak Ave"
            );

            // When
            var errors = validator.validateEmployeeBusinessRules(employee);

            // Then
            assertThat(errors)
                .hasSize(1)
                .contains("Email domain not allowed for employees");
        }

        @Test
        @DisplayName("Executive with high salary should pass validation")
        void executiveWithHighSalaryShouldPassValidation() {
            // Given
            var executive = new EmployeeDto(
                "Carol CEO", "carol@company.com", 350000.0, "555-345-6789", "789 Pine Rd"
            );

            // When
            var errors = validator.validateEmployeeBusinessRules(executive);

            // Then
            assertThat(errors).isEmpty();
        }

        @Test
        @DisplayName("Non-executive with high salary should require approval")
        void nonExecutiveWithHighSalaryShouldRequireApproval() {
            // Given
            var employee = new EmployeeDto(
                "Dave Developer", "dave@company.com", 600000.0, "555-456-7890", "321 Elm St"
            );

            // When
            var errors = validator.validateEmployeeBusinessRules(employee);

            // Then
            assertThat(errors)
                .hasSize(1)
                .contains("High salary requires executive approval");
        }

        @Test
        @DisplayName("Employee with blacklisted name should be rejected")
        void employeeWithBlacklistedNameShouldBeRejected() {
            // Given
            var employee = new EmployeeDto(
                "admin", "admin@company.com", 50000.0, "555-456-7890", "321 Elm St"
            );

            // When
            var errors = validator.validateEmployeeBusinessRules(employee);

            // Then
            assertThat(errors)
                .hasSize(1)
                .contains("Name appears on restricted list");
        }

        @ParameterizedTest
        @DisplayName("Allowed email domains should be accepted")
        @ValueSource(strings = {
            "alice@company.com",
            "bob@contractor.company.com", 
            "carol@temp.company.com"
        })
        void allowedEmailDomainsShouldBeAccepted(String email) {
            // Given
            var employee = new EmployeeDto(
                "Test User", email, 75000.0, "555-123-4567", "123 Main St"
            );

            // When
            var errors = validator.validateEmployeeBusinessRules(employee);

            // Then
            assertThat(errors)
                .as("Email %s should be allowed", email)
                .doesNotContain("Email domain not allowed for employees");
        }
    }

    @Nested
    @DisplayName("Phone Validation")
    class PhoneValidationTest {

        @ParameterizedTest
        @DisplayName("Valid phone formats should be accepted")
        @ValueSource(strings = {
            "+1-555-123-4567",
            "15551234567",
            "+447700123456"
        })
        void validPhoneFormatsShouldBeAccepted(String phone) {
            assertThat(validator.isValidPhone(phone)).isTrue();
        }

        @ParameterizedTest
        @DisplayName("Invalid phone formats should be rejected")
        @ValueSource(strings = {
            "123",
            "abc-def-ghij",
            "+0-555-123-4567"  // Cannot start with 0
        })
        void invalidPhoneFormatsShouldBeRejected(String phone) {
            assertThat(validator.isValidPhone(phone)).isFalse();
        }
    }

    @Nested
    @DisplayName("Salary Validation")
    class SalaryValidationTest {

        @ParameterizedTest
        @DisplayName("Valid salaries should be accepted")
        @ValueSource(doubles = {30000.0, 75000.0, 100000.0, 999999.0})
        void validSalariesShouldBeAccepted(double salary) {
            assertThat(validator.isValidSalary(salary)).isTrue();
        }

        @ParameterizedTest
        @DisplayName("Invalid salaries should be rejected")
        @ValueSource(doubles = {-1000.0, 0.0, 1000001.0})
        void invalidSalariesShouldBeRejected(double salary) {
            assertThat(validator.isValidSalary(salary)).isFalse();
        }

        @Test
        @DisplayName("Null salary should be rejected")
        void nullSalaryShouldBeRejected() {
            assertThat(validator.isValidSalary(null)).isFalse();
        }
    }
}