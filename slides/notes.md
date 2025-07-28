# Text Notes to Add to Videos

## Naming Conventions

- Code is read more often than it is written. (00:15)
- Well-named code is self-documenting. (00:21)

(00:39 - 01:05)
- Classes: `Employee`, `SalaryCalculator`
- Annotations: `@Override`, `@CustomAnnotation`
- Enums: `EmployeeStatus`, `PaymentType`
- Records: `PersonData`, `EmployeeRecord`

(01:06 - 01:18)
- <span style="color: #00D4FF;">**camelCase**</span>
- Variables: `employeeName`, `salaryAmount`
- Methods: `calculateSalary()`
- Parameters: `userId`, `isActive`
- Fields: `firstName`, `departmentId`

- Unchanging `static final` fields: `MAX_EMPLOYEES` (01:20)

- Packages: `com.oreilly.javaskills` (01:46)

- Types: UpperCamelCase (06:31)
- Variables/Methods: camelCase (06:51)
- Constants: UPPER_SNAKE_CASE (07:04)
- Packages: lowercase.with.dots (07:13)

## Escape Characters

- Special sequences that represent characters you can't easily type. (00:10)
- They always start with a backslash (`\\`). (00:18)

- `\n` - Newline** moves the cursor to the next line. (00:32)
- `\t` inserts a horizontal tab space. (00:37)
- `\"` inserts a `"` character. (00:51)
- `\\` inserts a `\` character. (00:59)

- Use text blocks (`"""`) for multi-line strings to avoid most escape characters! (01:12)

## Operator Precedence

- **Order of operations** for Java code. (00:10)
- When in doubt, use parentheses! Clarity over cleverness. (00:21)

### **Highest Precedence** (00:27 to 00:56)
- Postfix (`x++`, `x--`)
- Unary (`++x`, `--x`, `!`, `~`)
- Multiplicative (`*`, `/`, `%`)
- Additive (`+`, `-`)

### **Lower Precedence** (00:57 to 01:05)
- Relational (`<`, `>`, `<=`, `>=`)
- Equality (`==`, `!=`)
- Logical AND (`&&`)
- Logical OR (`||`)
- Ternary (`? :`)
- Assignment (`=`, `+=`, `-=`, etc.)

- **Multiplication/Division** comes before **Addition/Subtraction**. (01:11)
- **Logical AND (`&&`)** comes before **Logical OR (`||`)**. (01:27)

## Scanner

- `java.util.Scanner` makes your programs interactive (00:15)
- Can parse primitive types—but don't do it (00:41)
- Use readLine() and parse instead (01:12)
- Use a **validation loop** (`while` + `try-catch`) to handle bad inputs (6:35)
- Close your `Scanner` with `try-with-resources` (6:42)

