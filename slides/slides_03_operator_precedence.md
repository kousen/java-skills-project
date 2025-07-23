
---
layout: cover
--- 

# Java Operator Precedence

<div class="pt-12">
  <span class="px-2 py-1 rounded">
    Goal 3: Combine multiple expressions using correct operator precedence.
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

# Why Precedence Matters

<v-clicks>

- It's the **order of operations** for Java code.
- Incorrect assumptions lead to subtle and frustrating bugs.
- Understanding precedence is key to writing correct and predictable code.

</v-clicks>

<div class="mt-8">
<v-click>

> When in doubt, use parentheses! Clarity is always better than cleverness.

</v-click>
</div>

---

# The Precedence Hierarchy (High to Low)

<div class="grid grid-cols-2 gap-8">

<div>

## **Highest Precedence**
- Postfix (`x++`, `x--`)
- Unary (`++x`, `--x`, `!`, `~`)
- Multiplicative (`*`, `/`, `%`)
- Additive (`+`, `-`)

</div>

<div>

## **Lower Precedence**  
- Relational (`<`, `>`, `<=`, `>=`)
- Equality (`==`, `!=`)
- Logical AND (`&&`)
- Logical OR (`||`)
- Ternary (`? :`)
- Assignment (`=`, `+=`, `-=`, etc.)

</div>

</div>

---

# Arithmetic & Boolean Precedence

Let's look at `OperatorPrecedenceTest.java` to see how this works.

```java
// Arithmetic: Multiplication (*) happens before Addition (+)
double calculation = 50000 + 20 * 25.0; // Result: 50500.0

// Boolean: Logical AND (&&) happens before Logical OR (||)
boolean eligible = true || false && false; // Evaluates to: true || (false) -> true
```

---

# Pre-increment vs Post-increment

This is a classic source of confusion!

```java
int i = 5;

// Pre-increment: ++i
// 1. Increment i to 6
// 2. Use the new value (6)
int a = ++i; // a is 6, i is 6

// Post-increment: i++
// 1. Use the current value (5)
// 2. Increment i to 6
int b = i++; // b is 5, i is 6
```

---
layout: section
---

# Key Takeaways

<v-clicks>

- **Multiplication/Division** comes before **Addition/Subtraction**.
- **Logical AND (`&&`)** comes before **Logical OR (`||`)**.
- Use **parentheses `()`** to force a specific order and improve readability.
- Be careful with **pre-increment (`++i`)** vs **post-increment (`i++`)** in complex expressions.

</v-clicks>
