---
layout: cover
--- 

# The Java Reflection API

<div class="pt-12">
  <span class="px-2 py-1 rounded">
    Goal 12: Use the Reflection API to inspect and invoke classes, methods, and fields at runtime.
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
layout: default
---

# What is Reflection?

<v-clicks>

- A powerful API that allows a program to **inspect and manipulate itself** at runtime

- You can examine classes, interfaces, fields, and methods at runtime

- Works without knowing their names at compile time

</v-clicks>

---
layout: default
---

# Why Use Reflection?

<v-clicks>

- Create objects and invoke methods dynamically

- Get or set field values at runtime

- Foundation for many frameworks like **Spring**, **Hibernate**, and **JUnit**

</v-clicks>

---

# Important Warning

<div class="text-center pt-12">
  <div class="text-2xl font-bold text-red-500 mb-6">
    ⚠️ Use With Caution ⚠️
  </div>
  
  <v-clicks>
  
  - Reflection can be **slow**
  - Can **break encapsulation**
  - Use only when necessary
  
  </v-clicks>
</div>

---
layout: default
---

# The `Class<T>` Object

<v-clicks>

- **Entry point** for all reflection operations

- Represents a class or interface

- Three ways to get it:
  - `MyClass.class`
  - `obj.getClass()`
  - `Class.forName("com.pkg.MyClass")`

</v-clicks>

---

# Getting Class Objects

```java
// Method 1: Class literal (compile-time)
Class<String> clazz1 = String.class;

// Method 2: From instance (runtime)
String str = "hello";
Class<?> clazz2 = str.getClass();

// Method 3: By name (dynamic)
Class<?> clazz3 = Class.forName("java.lang.String");
```

---
layout: default
---

# `.class` vs `.getClass()`

<v-clicks>

- **`.class`**: Static, compile-time type
- **`.getClass()`**: Dynamic, runtime type
- **Different results with inheritance!**

</v-clicks>

---

# Inheritance Example

```java
class Animal { }
class Dog extends Animal { }

Animal animal = new Dog();  // Polymorphism

Class<Animal> staticType = Animal.class;     // Animal
Class<?> runtimeType = animal.getClass();    // Dog!

System.out.println(staticType.getSimpleName()); // "Animal"
System.out.println(runtimeType.getSimpleName()); // "Dog"
```

---
layout: default
---

# The `Field` Class

<v-clicks>

- Represents a **field** of a class

- Get field information and values

- Modify field values (even private ones!)

</v-clicks>

---

# Working with Fields

```java
Class<?> clazz = employee.getClass();

// Get all declared fields
Field[] fields = clazz.getDeclaredFields();

// Get specific field
Field nameField = clazz.getDeclaredField("name");

// Get field value
String name = (String) nameField.get(employee);
```

---
layout: default
---

# The `Method` Class

<v-clicks>

- Represents a **method** of a class

- Invoke methods dynamically

- Get method information and parameters

</v-clicks>

---

# Working with Methods

```java
Class<?> clazz = employee.getClass();

// Get specific method
Method getNameMethod = clazz.getMethod("getName");

// Invoke method
String result = (String) getNameMethod.invoke(employee);

// Method with parameters
Method setName = clazz.getMethod("setName", String.class);
setName.invoke(employee, "New Name");
```

---
layout: default
---

# The `Constructor<T>` Class

<v-clicks>

- Represents a **constructor** of a class

- Create new instances dynamically

- Pass parameters to constructors

</v-clicks>

---

# Working with Constructors

```java
Class<Employee> clazz = Employee.class;

// Get constructor with parameters
Constructor<Employee> ctor = clazz.getConstructor(
    String.class, int.class, double.class);

// Create new instance
Employee emp = ctor.newInstance("Alice", 1001, 75000);
```

---

# Common Operations: Getting Class Info

```java
Class<?> clazz = MyClass.class;

// Basic information
String name = clazz.getName();
String simpleName = clazz.getSimpleName();
Package pkg = clazz.getPackage();
```

---

# Common Operations: Getting Methods

```java
// Get all declared methods (including private)
Method[] allMethods = clazz.getDeclaredMethods();

// Get only public methods
Method[] publicMethods = clazz.getMethods();

// Get specific method
Method method = clazz.getMethod("methodName", String.class);
```

---

# Common Operations: Creating Instances

```java
// Default constructor
Object instance = clazz.getConstructor().newInstance();

// Constructor with parameters
Constructor<?> ctor = clazz.getConstructor(String.class, int.class);
Object instance = ctor.newInstance("Alice", 123);
```

---
layout: default
---

# Accessing Private Members

<v-clicks>

- Use `setAccessible(true)` to bypass access control

- Works for private fields and methods

- **Security risk** - use carefully!

</v-clicks>

---

# Private Field Access Example

```java
Field privateField = clazz.getDeclaredField("secretCode");

// Bypass private modifier
privateField.setAccessible(true);

// Now you can access it
String secret = (String) privateField.get(employee);

// And modify it
privateField.set(employee, "NEW_SECRET");
```

---

# Private Method Access Example

```java
Method privateMethod = clazz.getDeclaredMethod("calculateBonus");

// Bypass private modifier  
privateMethod.setAccessible(true);

// Now you can invoke it
double bonus = (double) privateMethod.invoke(employee);
```

---
layout: default
---

# Framework Pattern: Annotation Processing

<v-clicks>

- Frameworks scan for **annotations**

- Process classes based on metadata

- Example: `@Entity`, `@Service`, `@Controller`

</v-clicks>

---

# Annotation Scanning Example

```java
// Find methods with @Important annotation
Method[] methods = clazz.getDeclaredMethods();

for (Method method : methods) {
    if (method.isAnnotationPresent(Important.class)) {
        Important annotation = method.getAnnotation(Important.class);
        
        System.out.println("Found: " + method.getName());
        System.out.println("Priority: " + annotation.priority());
    }
}
```

---

# Real Example: Naming Convention Test

Let's see reflection in action with a practical example.

**(Show `foundations/src/test/java/com.oreilly.javaskills.NamingConventionsTest.java`)**

---

# Step 1: Get the Class

```java
@Test
void shouldHaveProperConstantNaming() {
    // Get the Class object
    var clazz = NamingConventions.class;
```

---

# Step 2: Get All Fields

```java
    // Get all declared fields
    var fields = clazz.getDeclaredFields();
```

---

# Step 3: Check Each Field

```java
    for (var field : fields) {
        // Check if field is static and final
        if (Modifier.isFinal(field.getModifiers()) &&
            Modifier.isStatic(field.getModifiers())) {
            
            // Verify naming convention
            String fieldName = field.getName();
            assertThat(fieldName).matches("[A-Z][A-Z_]*");
        }
    }
}
```

---
layout: default
---

# Why Frameworks Use `.getClass()`

<v-clicks>

- Need the **actual runtime type**

- Not the declared type

- Polymorphic objects may have different annotations

</v-clicks>

---

# Framework Example: Wrong Way

```java
// This misses subclass methods!
SampleEmployee employee = new SalariedEmployee(...);

Method[] methods = SampleEmployee.class.getDeclaredMethods();
// Only finds SampleEmployee methods
```

---

# Framework Example: Right Way

```java
// This finds ALL methods including subclass
SampleEmployee employee = new SalariedEmployee(...);

Method[] methods = employee.getClass().getDeclaredMethods();  
// Finds SalariedEmployee methods too!
```

---
layout: default
---

# Performance Considerations

<v-clicks>

- Reflection is **slower** than direct calls

- Use caching when possible

- Consider alternatives first

</v-clicks>

---
layout: default
---

# When to Use Reflection

<v-clicks>

- **Framework development** (Spring, Hibernate)

- **Testing utilities** (JUnit, Mockito)

- **Configuration systems**

- **Dynamic code generation**

</v-clicks>

---
layout: default
---

# When NOT to Use Reflection

<v-clicks>

- **Normal application code**

- **Performance-critical paths**

- **When type safety matters**

- **Simple object creation**

</v-clicks>

---
layout: default
---

# Security Implications

<v-clicks>

- Can bypass access control

- Potential for code injection

- Use `SecurityManager` in production

- Validate input carefully

</v-clicks>

---
layout: default
---

# Best Practices

<v-clicks>

- **Cache** Class objects when possible

- **Handle exceptions** properly

- **Validate** before accessing private members

- **Document** why reflection is needed

</v-clicks>

---
layout: default
---

# Key Takeaways

<v-clicks>

- Reflection inspects and modifies code at **runtime**

- Essential for frameworks and dynamic tools

</v-clicks>

---
layout: default
---

# Key Classes to Remember

<v-clicks>

- **`Class`** - Entry point for reflection

- **`Method`** - Invoke methods dynamically

- **`Field`** - Access and modify fields

- **`Constructor`** - Create instances dynamically

</v-clicks>

---
layout: default
---

# Final Advice

<v-clicks>

- Powerful but use **sparingly**

- Performance overhead exists

- Prefer **direct calls** when possible

- Great for framework development

</v-clicks>

---

# Try the Exercise!

<div class="text-center pt-12">
  <div class="text-2xl font-bold mb-6">
    🔧 ReflectionExercise.java
  </div>
  
  <v-clicks>
  
  - Hands-on practice with all concepts
  - Real framework patterns
  - Polymorphism examples
  
  </v-clicks>
</div>