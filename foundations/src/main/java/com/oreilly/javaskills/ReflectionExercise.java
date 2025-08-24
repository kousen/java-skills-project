package com.oreilly.javaskills;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.*;
import java.util.Arrays;

/**
 * Interactive exercise demonstrating the Java Reflection API.
 * <p>
 * This exercise covers:
 * - Getting Class objects and basic introspection
 * - Examining fields, methods, and constructors
 * - Invoking methods dynamically 
 * - Accessing private members
 * - Practical use cases like framework operations
 */
@SuppressWarnings("ConstantValue")
public class ReflectionExercise {

    public static void main(String[] args) {
        System.out.println("=== Java Reflection API Exercise ===");
        
        demonstrateBasicReflection();
        demonstrateFieldAccess();
        demonstrateMethodInvocation();
        demonstrateConstructorAccess();
        demonstratePrivateAccess();
        demonstrateFrameworkPattern();
        
        // Try It Out exercises
        tryItOutExercises();
        
        System.out.println("\n=== Exercise Complete! ===");
    }

    private static void demonstrateBasicReflection() {
        System.out.println("\n1. BASIC REFLECTION - Getting Class Information:");
        
        // Different ways to get Class object
        Class<SampleEmployee> clazz1 = SampleEmployee.class;
        var employee = new SampleEmployee("Alice", 1001, 75000);
        Class<?> clazz2 = employee.getClass();
        
        try {
            Class<?> clazz3 = Class.forName("com.oreilly.javaskills.ReflectionExercise$SampleEmployee");
            System.out.println("✓ Got class via employee.getClass(): " + clazz2.getSimpleName());
            System.out.println("✓ Got class via Class.forName()    : " + clazz3.getSimpleName());
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Class not found: " + e.getMessage());
        }
        
        // Demonstrate .class vs .getClass() with inheritance
        System.out.println("\n** .class vs .getClass() with Polymorphism **");
        SampleEmployee polymorphicEmployee = new SalariedEmployee("Bob Manager", 2001, 95000, 15000);
        
        Class<SampleEmployee> staticType = SampleEmployee.class;      // Compile-time type
        Class<?> runtimeType = polymorphicEmployee.getClass();        // Runtime type
        
        System.out.println("Static type (.class): " + staticType.getSimpleName());
        System.out.println("Runtime type (.getClass()): " + runtimeType.getSimpleName());
        System.out.println("Are they the same? " + (staticType == runtimeType));
        System.out.println("Actual object is instance of: " + polymorphicEmployee.getClass().getName());
        
        // Basic class information
        System.out.println("\n** Class Metadata **");
        System.out.println("Class name: " + clazz1.getName());
        System.out.println("Simple name: " + clazz1.getSimpleName());
        System.out.println("Package: " + clazz1.getPackage().getName());
        System.out.println("Modifiers: " + Modifier.toString(clazz1.getModifiers()));
        System.out.println("Fields count: " + clazz1.getDeclaredFields().length);
        System.out.println("Methods count: " + clazz1.getDeclaredMethods().length);
    }

    private static void demonstrateFieldAccess() {
        System.out.println("\n2. FIELD ACCESS - Examining Class Fields:");
        
        Class<SampleEmployee> clazz = SampleEmployee.class;
        Field[] fields = clazz.getDeclaredFields();
        
        System.out.println("All declared fields:");
        for (Field field : fields) {
            System.out.printf("  - %s %s %s%n", 
                Modifier.toString(field.getModifiers()),
                field.getType().getSimpleName(),
                field.getName());
        }
        
        // Get field values
        SampleEmployee employee = new SampleEmployee("Bob", 1002, 80000);
        try {
            Field nameField = clazz.getDeclaredField("name");
            Field salaryField = clazz.getDeclaredField("salary");
            
            System.out.println("\nField values:");
            System.out.println("Name: " + nameField.get(employee));
            System.out.println("Salary: " + salaryField.get(employee));
            
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Field access error: " + e.getMessage());
        }
    }

    private static void demonstrateMethodInvocation() {
        System.out.println("\n3. METHOD INVOCATION - Calling Methods Dynamically:");
        
        SampleEmployee employee = new SampleEmployee("Carol", 1003, 85000);
        Class<?> clazz = employee.getClass();
        
        try {
            // Get and invoke no-argument method
            Method getFormattedInfoMethod = clazz.getMethod("getFormattedInfo");
            String result = (String) getFormattedInfoMethod.invoke(employee);
            System.out.println("getFormattedInfo() result: " + result);
            
            // Get and invoke method with parameters
            Method qualifiesForBonusMethod = clazz.getMethod("qualifiesForBonus", double.class);
            boolean qualifies = (boolean) qualifiesForBonusMethod.invoke(employee, 70000.0);
            System.out.println("qualifiesForBonus(70000): " + qualifies);
            
            // List all public methods
            System.out.println("\nAll public methods:");
            Method[] methods = clazz.getMethods();
            Arrays.stream(methods)
                .filter(m -> m.getDeclaringClass() != Object.class) // Skip Object methods
                .forEach(m -> System.out.println("  - " + m.getName() + 
                    "(" + Arrays.toString(m.getParameterTypes()) + ")"));
                    
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.err.println("Method invocation error: " + e.getMessage());
        }
    }

    private static void demonstrateConstructorAccess() {
        System.out.println("\n4. CONSTRUCTOR ACCESS - Creating Instances Dynamically:");
        
        Class<SampleEmployee> clazz = SampleEmployee.class;
        
        try {
            // Get constructor with specific parameter types
            Constructor<SampleEmployee> constructor = clazz.getConstructor(String.class, int.class, double.class);
            
            // Create new instance using reflection
            SampleEmployee newEmployee = constructor.newInstance("David", 1004, 90000);
            System.out.println("Created employee via reflection: " + newEmployee.getFormattedInfo());
            
            // List all constructors
            System.out.println("\nAll constructors:");
            Constructor<?>[] constructors = clazz.getConstructors();
            for (Constructor<?> ctor : constructors) {
                System.out.println("  - " + ctor.getName() + 
                    "(" + Arrays.toString(ctor.getParameterTypes()) + ")");
            }
            
        } catch (NoSuchMethodException | InstantiationException | 
                 IllegalAccessException | InvocationTargetException e) {
            System.err.println("Constructor access error: " + e.getMessage());
        }
    }

    private static void demonstratePrivateAccess() {
        System.out.println("\n5. PRIVATE ACCESS - Bypassing Encapsulation:");
        
        SampleEmployee employee = new SampleEmployee("Eve", 1005, 95000);
        Class<?> clazz = employee.getClass();
        
        try {
            // Access private field
            Field secretCodeField = clazz.getDeclaredField("secretCode");
            secretCodeField.setAccessible(true); // Bypass private modifier
            
            String secretCode = (String) secretCodeField.get(employee);
            System.out.println("Private secretCode: " + secretCode);
            
            // Modify private field
            secretCodeField.set(employee, "HACKED123");
            String newSecretCode = (String) secretCodeField.get(employee);
            System.out.println("Modified secretCode: " + newSecretCode);
            
            // Access private method
            Method calculateBonusMethod = clazz.getDeclaredMethod("calculateBonus");
            calculateBonusMethod.setAccessible(true);
            
            double bonus = (double) calculateBonusMethod.invoke(employee);
            System.out.println("Private calculateBonus() result: $" + bonus);
            
        } catch (NoSuchFieldException | NoSuchMethodException | 
                 IllegalAccessException | InvocationTargetException e) {
            System.err.println("Private access error: " + e.getMessage());
        }
    }

    private static void demonstrateFrameworkPattern() {
        System.out.println("\n6. FRAMEWORK PATTERN - Annotation-Based Processing:");
        
        // Simulate framework behavior with polymorphic objects
        SampleEmployee[] employees = {
            new SampleEmployee("Regular Employee", 3001, 60000),
            new SalariedEmployee("Manager", 3002, 80000, 20000)
        };
        
        System.out.println("Framework processing different employee types:");
        System.out.println("\nMethods marked with @Important annotation:");
        for (SampleEmployee emp : employees) {
            Class<?> actualClass = emp.getClass(); // Use getClass() for actual runtime type
            System.out.println("\nProcessing: " + actualClass.getSimpleName());
            
            // Find all @Important methods in the actual class (not just SampleEmployee)
            Method[] methods = actualClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Important.class)) {
                    Important annotation = method.getAnnotation(Important.class);
                    System.out.printf("  - %s (priority: %d, description: %s)%n", 
                        method.getName(), annotation.priority(), annotation.description());
                }
            }
        }
        
        // Demonstrate the difference: .class vs .getClass()
        System.out.println("\n** Why .getClass() matters for frameworks **");
        SampleEmployee manager = new SalariedEmployee("Framework Test", 3003, 90000, 25000);
        
        // Wrong approach - misses subclass methods
        System.out.println("Using SampleEmployee.class (static type):");
        Method[] staticMethods = SampleEmployee.class.getDeclaredMethods();
        long staticImportantMethods = Arrays.stream(staticMethods)
            .filter(m -> m.isAnnotationPresent(Important.class))
            .count();
        System.out.println("Found " + staticImportantMethods + " @Important methods");
        
        // Correct approach - finds all methods including subclass
        System.out.println("Using manager.getClass() (runtime type):");
        Method[] runtimeMethods = manager.getClass().getDeclaredMethods();
        long runtimeImportantMethods = Arrays.stream(runtimeMethods)
            .filter(m -> m.isAnnotationPresent(Important.class))
            .count();
        System.out.println("Found " + runtimeImportantMethods + " @Important methods");
    }

    private static void tryItOutExercises() {
        System.out.println("\n7. YOUR TRY IT OUT EXERCISES:");
        
        // Exercise 1: Field modifier analysis
        System.out.println("\n✓ Exercise 1: Analyzing field modifiers");
        analyzeFieldModifiers();
        
        // Exercise 2: Method parameter inspection
        System.out.println("\n✓ Exercise 2: Method parameter inspection completed");
        inspectMethodParameters();
        
        // Exercise 3: Annotation scanner
        System.out.println("\n✓ Exercise 3: Annotation scanning completed");
        scanForAnnotations();
        
        // Exercise 4: Dynamic object creation
        System.out.println("\n✓ Exercise 4: Dynamic object creation completed");
        createObjectsDynamically();
    }

    // Try It Out Exercise implementations
    private static void analyzeFieldModifiers() {
        Class<SampleEmployee> clazz = SampleEmployee.class;
        Field[] fields = clazz.getDeclaredFields();
        
        int finalFields = 0;
        int staticFields = 0;
        int privateFields = 0;
        
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isFinal(modifiers)) finalFields++;
            if (Modifier.isStatic(modifiers)) staticFields++;
            if (Modifier.isPrivate(modifiers)) privateFields++;
        }
        
        System.out.printf("Field analysis - Final: %d, Static: %d, Private: %d%n", 
            finalFields, staticFields, privateFields);
    }

    private static void inspectMethodParameters() {
        Class<SampleEmployee> clazz = SampleEmployee.class;
        Method[] methods = clazz.getDeclaredMethods();
        
        for (Method method : methods) {
            if (method.getParameterCount() > 0) {
                System.out.printf("Method %s has %d parameters: %s%n",
                    method.getName(), 
                    method.getParameterCount(),
                    Arrays.toString(method.getParameterTypes()));
            }
        }
    }

    private static void scanForAnnotations() {
        Class<SampleEmployee> clazz = SampleEmployee.class;
        
        // Class-level annotations
        if (clazz.isAnnotationPresent(Entity.class)) {
            Entity entity = clazz.getAnnotation(Entity.class);
            System.out.printf("Class has @Entity annotation with name: %s%n", entity.name());
        }
        
        // Method-level annotation count
        long annotatedMethods = Arrays.stream(clazz.getDeclaredMethods())
            .filter(m -> m.getDeclaredAnnotations().length > 0)
            .count();
        System.out.printf("Found %d methods with annotations%n", annotatedMethods);
    }

    private static void createObjectsDynamically() {
        try {
            // Create different employee types dynamically based on salary
            String[] names = {"Alice", "Bob", "Carol"};
            double[] salaries = {50000, 75000, 100000};
            
            for (int i = 0; i < names.length; i++) {
                SampleEmployee employee = SampleEmployee.class
                    .getConstructor(String.class, int.class, double.class)
                    .newInstance(names[i], 2000 + i, salaries[i]);
                
                System.out.printf("Created: %s%n", employee.getFormattedInfo());
            }
        } catch (Exception e) {
            System.err.println("Dynamic creation error: " + e.getMessage());
        }
    }

    // Sample classes and annotations for the exercise

    /**
     * Sample employee class for reflection demonstrations.
     */
    @SuppressWarnings("FieldCanBeLocal")
    @Entity(name = "Employee")
    public static class SampleEmployee {
        private final String name;
        private final int id;
        private final double salary;
        private final String secretCode;
        private static final String COMPANY_NAME = "Tech Corp";

        public SampleEmployee(String name, int id, double salary) {
            this.name = name;
            this.id = id;
            this.salary = salary;
            this.secretCode = "SECRET" + id;
        }

        @Important(priority = 1, description = "Essential employee information")
        public String getFormattedInfo() {
            return String.format("%s (ID: %d) - $%.2f", name, id, salary);
        }

        @Important(priority = 2, description = "Bonus eligibility check")
        public boolean qualifiesForBonus(double threshold) {
            return salary >= threshold;
        }

        private double calculateBonus() {
            return salary * 0.10; // 10% bonus
        }

        // Standard getters
        public String getName() { return name; }
        public int getId() { return id; }
        public double getSalary() { return salary; }
        public static String getCompanyName() { return COMPANY_NAME; }
    }

    /**
     * Salaried employee subclass to demonstrate inheritance with reflection.
     */
    @SuppressWarnings("unused")
    @Entity(name = "SalariedEmployee")
    public static class SalariedEmployee extends SampleEmployee {
        private final double annualBonus;

        public SalariedEmployee(String name, int id, double salary, double annualBonus) {
            super(name, id, salary);
            this.annualBonus = annualBonus;
        }

        @Override
        @Important(priority = 1, description = "Enhanced employee information with bonus")
        public String getFormattedInfo() {
            return String.format("%s - $%.2f (Bonus: $%.2f)", 
                super.getFormattedInfo(), getSalary(), annualBonus);
        }

        @Important(priority = 3, description = "Total compensation calculation")
        public double getTotalCompensation() {
            return getSalary() + annualBonus;
        }

        public double getAnnualBonus() { return annualBonus; }
    }

    // Custom annotations for demonstration
    @Retention(RetentionPolicy.RUNTIME)
    @interface Important {
        int priority() default 0;
        String description() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Entity {
        String name();
    }
}