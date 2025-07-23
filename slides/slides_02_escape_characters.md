

---
layout: cover
---

# Java Escape Characters

<div class="pt-12">
  <span class="px-2 py-1 rounded">
    Goal 2: Use escape characters (e.g., \n, \t) in String output to control formatting.
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

# What Are Escape Characters?

<v-clicks>

- Special sequences that represent characters you can't easily type.
- They always start with a backslash (`\\`).
- Used to control text formatting and include special characters in your strings.

</v-clicks>

---

# Common Escape Characters

<div class="grid grid-cols-2 gap-8">

<div>

## **`\n` - Newline**
- Moves the cursor to the next line.

## **`\t` - Tab**
- Inserts a horizontal tab space.

</div>

<div>

## **`\"` - Double Quote**
- Inserts a `"` character.

## **`\\` - Backslash**
- Inserts a `\` character.

</div>

</div>

---

# Let's See It In Action

Now, let's look at the `EscapeCharacters.java` file to see how these work.

```java
// Newline Example
String withNewline = "First line\nSecond line";

// Tab Example
String withTab = "Column 1\tColumn 2";

// Double Quote Example
String withQuotes = "She said, \"Hello, Java!\"";

// Backslash Example (for file paths)
String filePath = "C:\\Users\\Guest\\Documents";
```

---
layout: section
---

# Key Takeaways

<v-clicks>

- Escape characters give you precise control over string formatting.
- `\n` for new lines and `\t` for tabs are the most common.
- Use `\"` to put quotes in a string and `\\` to show a backslash.

</v-clicks>

<v-clicks>

- Mastering these simple tools makes your program's output much cleaner and more readable.

</v-clicks>

