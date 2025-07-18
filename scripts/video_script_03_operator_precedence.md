
# Video Script: Java Operator Precedence

**Goal:** 3. Combine multiple expressions using correct operator precedence.
**Target Duration:** 3-4 minutes

---

### SCENE 1: Introduction (0:00 - 0:30)

**(Show Slide 1: Title Slide - "Java Operator Precedence")**

**YOU:**
"Hi everyone. In this video, we're going to tackle a topic that can be the source of very subtle bugs: **Operator Precedence**."

**(Show Slide 2: Why Precedence Matters)**

**YOU:**
"Operator precedence is simply the set of rules that dictates the order in which operations are performed in a Java expression. Getting this wrong can lead to incorrect calculations and logic errors. The golden rule is: when in doubt, use parentheses!"

---

### SCENE 2: The Hierarchy (0:30 - 1:15)

**(Show Slide 3: The Precedence Hierarchy)**

**YOU:**
"Java has a well-defined hierarchy. At the top, with the highest precedence, we have postfix operators like `x++`, followed by unary operators like `++x` and the logical NOT `!`."

**YOU:**
"Next comes multiplicative operators—multiply, divide, and modulus—which are all evaluated before the additive operators, plus and minus."

**YOU:**
"Further down, we have relational and equality operators, then logical AND, then logical OR, and finally, near the bottom, the assignment operators."

---

### SCENE 3: Code Demo - Arithmetic and Boolean (1:15 - 2:15)

**(Show Slide 4: Arithmetic Precedence)**

**YOU:**
"Let's see how this works with arithmetic operations."

**(Transition to IDE showing `OperatorPrecedenceTest.java`)**

**YOU:**
"Let's see this in a test file, which is a great way to prove the behavior. In this first test, we have a salary calculation."

**(Highlight the arithmetic test)**

**YOU:**
"Even though addition appears first, the multiplication `overtime * overtimeRate * hourlyRate` is performed first because multiplication has higher precedence. The test assertion confirms this expected result."

**(Highlight the boolean test)**

**YOU:**
"It's the same with boolean logic. The `&&` operator has higher precedence than `||`. So this expression is evaluated as `isFullTime` OR the result of `hasBonus && yearsOfService > 2`. Again, parentheses make this much clearer, but it's important to know the default behavior."

---

### SCENE 4: Code Demo - Increment Operators (2:15 - 3:15)

**(Show Slide 5: Pre-increment vs Post-increment)**

**YOU:**
"Now for a classic point of confusion: pre-increment versus post-increment."

**(Return to IDE, highlight the increment/decrement test)**

**YOU:**
"In this test, we look at `++i` (pre-increment) first. The variable `i` is incremented *before* its value is used in the expression. So `i` becomes 6, and the result is 12."

**YOU:**
"But with `i++` (post-increment), the original value of `i` is used in the expression *first*, and *then* `i` is incremented. So the expression evaluates to 10, and only after that does `i` become 6. As you can see from the test, it's a real difference, so be careful when using these inside complex expressions."

---

### SCENE 5: Conclusion (3:15 - 3:45)

**(Show Slide 6: Key Takeaways)**

**YOU:**
"So, to wrap up: remember that multiplicative operators come before additive, and logical AND comes before logical OR. Most importantly, use parentheses whenever the order of operations isn't perfectly clear. It makes your code safer and easier for the next person to read."

**(Show Slide 7: Review)**

**YOU:**
"That's all for this lesson. Thanks for watching!"
