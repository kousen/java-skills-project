
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
layout: section
---

# What is Reflection?

<v-clicks>

- A powerful API that allows a program to **inspect and manipulate itself** at runtime.
- You can examine classes, interfaces, fields, and methods at runtime, without knowing their names at compile time.
- You can also instantiate objects, invoke methods, and get or set field values dynamically.
- It is the foundation for many frameworks, like Spring, Hibernate, and JUnit.

</v-clicks>

<div class="mt-8">
<v-click>

**Warning:** Reflection is a powerful tool, but it can be slow and can break encapsulation. Use it with caution.

</v-click>
</div>

---

# Core Classes in `java.lang.reflect`

<div class="grid grid-cols-2 gap-8">

<div>

## **`Class<T>`**
- Represents a class or interface.
- The entry point for all reflection operations.
- Get it using `MyClass.class`, `obj.getClass()`, or `Class.forName("com.pkg.MyClass")`.

## **`Field`**
- Represents a field of a class.
- Allows you to get and set the value of the field.

</div>

<div>

## **`Method`**
- Represents a method of a class.
- Allows you to invoke the method on an object.

## **`Constructor<T>`**
- Represents a constructor of a class.
- Allows you to create new instances of the class.

</div>

</div>

---

# Common Reflection Operations

<v-clicks>

- **Getting a `Class` object:** `Class<?> clazz = MyClass.class;`
- **Getting all declared methods:** `Method[] methods = clazz.getDeclaredMethods();`
- **Getting a specific method:** `Method method = clazz.getMethod("myMethod", String.class);`
- **Invoking a method:** `Object result = method.invoke(myObject, "arg1");`
- **Creating a new instance:** `Object instance = clazz.getConstructor().newInstance();`
- **Accessing a private field:**
  - `Field field = clazz.getDeclaredField("myField");`
  - `field.setAccessible(true);`
  - `Object value = field.get(myObject);`

</v-clicks>

---

# Code Demo: `com.oreilly.javaskills.NamingConventionsTest.java`

Let's look at a practical use case: checking for `static final` constants.

**(Show `foundations/src/test/java/com.oreilly.javaskills.NamingConventionsTest.java`)**

```java
import java.lang.reflect.Modifier;
// ...

@Test
void allConstantNamesShouldBeScreamingSnakeCase() {
    // 1. Get the Class object
    var clazz = com.oreilly.javaskills.NamingConventions.class;
    // 2. Get all declared fields
    var fields = clazz.getDeclaredFields();

    for (var field : fields) {
        // 3. Get the field's modifiers
        int modifiers = field.getModifiers();

        // 4. Check if the field is public, static, and final
        if (Modifier.isPublic(modifiers) && 
            Modifier.isStatic(modifiers) && 
            Modifier.isFinal(modifiers)) {
            
            // 5. Check if the name matches the convention
            assertTrue(field.getName().matches("^[A-Z_]+$"));
        }
    }
}
```

---
layout: section
---

# Key Takeaways

<v-clicks>

- Reflection allows you to inspect and modify your code at **runtime**.
- It's essential for frameworks and tools that need to work with code they don't know at compile time.
- Key classes are `Class`, `Method`, `Field`, and `Constructor`.
- Be mindful of the performance overhead and the risks of breaking encapsulation.
- Use it when you need to, but prefer direct method calls when possible.

</v-clicks>
