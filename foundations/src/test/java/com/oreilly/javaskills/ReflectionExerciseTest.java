package com.oreilly.javaskills;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("ReflectionExercise Tests")
class ReflectionExerciseTest {

    @Test
    @DisplayName("Exercise runs without errors and produces expected output")
    void exerciseRunsSuccessfully() {
        // Capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // This should run without throwing any exceptions
            assertThatNoException().isThrownBy(() -> ReflectionExercise.main(new String[]{}));
            
            String output = outputStream.toString();
            String normalized = output.replace("\r\n", "\n");

            // Verify main sections are present
            assertThat(normalized)
                .contains("=== Java Reflection API Exercise ===")
                .contains("1. BASIC REFLECTION - Getting Class Information:")
                .contains("2. FIELD ACCESS - Examining Class Fields:")
                .contains("3. METHOD INVOCATION - Calling Methods Dynamically:")
                .contains("4. CONSTRUCTOR ACCESS - Creating Instances Dynamically:")
                .contains("5. PRIVATE ACCESS - Bypassing Encapsulation:")
                .contains("6. FRAMEWORK PATTERN - Annotation-Based Processing:")
                .contains("7. YOUR TRY IT OUT EXERCISES:")
                .contains("=== Exercise Complete! ===");

            // Verify it demonstrates key reflection concepts
            assertThat(normalized)
                .contains("✓ Got class via Class.forName()")
                .contains("Class name:")
                .contains("Simple name:")
                .contains("Package:")
                .contains("Fields count:")
                .contains("Methods count:")
                .contains("All declared fields:")
                .contains("Field values:")
                .contains("getFormattedInfo() result:")
                .contains("qualifiesForBonus")
                .contains("All public methods:")
                .contains("Created employee via reflection:")
                .contains("All constructors:")
                .contains("Private secretCode:")
                .contains("Modified secretCode:")
                .contains("Private calculateBonus() result:")
                .contains("Methods marked with @Important annotation:")
                .contains("✓ Exercise 1: Analyzing field modifiers")
                .contains("✓ Exercise 2: Method parameter inspection completed")
                .contains("✓ Exercise 3: Annotation scanning completed")
                .contains("✓ Exercise 4: Dynamic object creation completed");
            
        } finally {
            System.setOut(originalOut);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    @DisplayName("SampleEmployee class has expected structure for reflection")
    void sampleEmployeeHasExpectedStructure() {
        Class<ReflectionExercise.SampleEmployee> clazz = ReflectionExercise.SampleEmployee.class;
        
        // Check class has expected fields
        Field[] fields = clazz.getDeclaredFields();
        assertThat(fields).hasSizeGreaterThanOrEqualTo(4);
        
        // Check for specific fields
        assertThatNoException().isThrownBy(() -> clazz.getDeclaredField("name"));
        assertThatNoException().isThrownBy(() -> clazz.getDeclaredField("id"));
        assertThatNoException().isThrownBy(() -> clazz.getDeclaredField("salary"));
        assertThatNoException().isThrownBy(() -> clazz.getDeclaredField("secretCode"));
        
        // Check fields are private and final
        try {
            Field nameField = clazz.getDeclaredField("name");
            assertThat(Modifier.isPrivate(nameField.getModifiers())).isTrue();
            assertThat(Modifier.isFinal(nameField.getModifiers())).isTrue();
        } catch (NoSuchFieldException e) {
            throw new AssertionError("Expected name field not found", e);
        }
    }

    @Test
    @DisplayName("SampleEmployee methods can be invoked via reflection")
    void sampleEmployeeMethodsCanBeInvoked() throws Exception {
        Class<ReflectionExercise.SampleEmployee> clazz = ReflectionExercise.SampleEmployee.class;
        ReflectionExercise.SampleEmployee employee = 
            clazz.getConstructor(String.class, int.class, double.class)
                 .newInstance("Test Employee", 999, 50000);

        // Test public method invocation
        Method getFormattedInfoMethod = clazz.getMethod("getFormattedInfo");
        String result = (String) getFormattedInfoMethod.invoke(employee);
        
        assertThat(result)
            .contains("Test Employee")
            .contains("999")
            .contains("50000");

        // Test method with parameters
        Method qualifiesForBonusMethod = clazz.getMethod("qualifiesForBonus", double.class);
        boolean qualifies = (boolean) qualifiesForBonusMethod.invoke(employee, 40000.0);
        assertThat(qualifies).isTrue(); // 50000 >= 40000
    }

    @Test
    @DisplayName("Private field access works correctly")
    void privateFieldAccessWorks() throws Exception {
        Class<ReflectionExercise.SampleEmployee> clazz = ReflectionExercise.SampleEmployee.class;
        ReflectionExercise.SampleEmployee employee = 
            clazz.getConstructor(String.class, int.class, double.class)
                 .newInstance("Private Test", 888, 60000);

        // Access private field
        Field secretCodeField = clazz.getDeclaredField("secretCode");
        secretCodeField.setAccessible(true);
        
        String secretCode = (String) secretCodeField.get(employee);
        assertThat(secretCode).isEqualTo("SECRET888");
        
        // Modify private field
        secretCodeField.set(employee, "MODIFIED");
        String modifiedCode = (String) secretCodeField.get(employee);
        assertThat(modifiedCode).isEqualTo("MODIFIED");
    }

    @Test
    @DisplayName("Private method access works correctly")
    void privateMethodAccessWorks() throws Exception {
        Class<ReflectionExercise.SampleEmployee> clazz = ReflectionExercise.SampleEmployee.class;
        ReflectionExercise.SampleEmployee employee = 
            clazz.getConstructor(String.class, int.class, double.class)
                 .newInstance("Method Test", 777, 80000);

        // Access private method
        Method calculateBonusMethod = clazz.getDeclaredMethod("calculateBonus");
        calculateBonusMethod.setAccessible(true);
        
        double bonus = (double) calculateBonusMethod.invoke(employee);
        assertThat(bonus).isEqualTo(8000.0); // 10% of 80,000
    }

    @Test
    @DisplayName("Annotations are properly detected")
    void annotationsAreDetected() {
        Class<ReflectionExercise.SampleEmployee> clazz = ReflectionExercise.SampleEmployee.class;
        
        // Check class annotation
        assertThat(clazz.isAnnotationPresent(ReflectionExercise.Entity.class)).isTrue();
        ReflectionExercise.Entity entity = clazz.getAnnotation(ReflectionExercise.Entity.class);
        assertThat(entity.name()).isEqualTo("Employee");
        
        // Check method annotations
        Method[] methods = clazz.getDeclaredMethods();
        long importantMethods = java.util.Arrays.stream(methods)
            .filter(m -> m.isAnnotationPresent(ReflectionExercise.Important.class))
            .count();
        
        assertThat(importantMethods).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Static fields and methods are accessible")
    void staticMembersAreAccessible() throws Exception {
        Class<ReflectionExercise.SampleEmployee> clazz = ReflectionExercise.SampleEmployee.class;
        
        // Access static field
        Field companyNameField = clazz.getDeclaredField("COMPANY_NAME");
        assertThat(Modifier.isStatic(companyNameField.getModifiers())).isTrue();
        assertThat(Modifier.isFinal(companyNameField.getModifiers())).isTrue();
        
        // Access static method
        Method getCompanyNameMethod = clazz.getMethod("getCompanyName");
        assertThat(Modifier.isStatic(getCompanyNameMethod.getModifiers())).isTrue();
        
        String companyName = (String) getCompanyNameMethod.invoke(null);
        assertThat(companyName).isEqualTo("Tech Corp");
    }

    @Test
    @DisplayName("Constructor reflection works correctly")
    void constructorReflectionWorks() throws Exception {
        Class<ReflectionExercise.SampleEmployee> clazz = ReflectionExercise.SampleEmployee.class;
        
        // Get constructor
        var constructor = clazz.getConstructor(String.class, int.class, double.class);
        assertThat(constructor).isNotNull();
        assertThat(constructor.getParameterCount()).isEqualTo(3);
        
        // Create instance via constructor
        var employee = constructor.newInstance("Constructor Test", 666, 70000);
        assertThat(employee).isNotNull();
        assertThat(employee.getName()).isEqualTo("Constructor Test");
        assertThat(employee.getId()).isEqualTo(666);
        assertThat(employee.getSalary()).isEqualTo(70000);
    }

    @Test
    @DisplayName("Field modifier analysis works correctly")
    void fieldModifierAnalysisWorks() {
        Class<ReflectionExercise.SampleEmployee> clazz = ReflectionExercise.SampleEmployee.class;
        Field[] fields = clazz.getDeclaredFields();
        
        int finalFields = 0;
        int privateFields = 0;
        int staticFields = 0;
        
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isFinal(modifiers)) finalFields++;
            if (Modifier.isPrivate(modifiers)) privateFields++;
            if (Modifier.isStatic(modifiers)) staticFields++;
        }
        
        // Most fields should be private and final (immutable design)
        assertThat(finalFields).isGreaterThanOrEqualTo(4);
        assertThat(privateFields).isGreaterThanOrEqualTo(4);
        assertThat(staticFields).isGreaterThanOrEqualTo(1); // COMPANY_NAME
    }
}