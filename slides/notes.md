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

