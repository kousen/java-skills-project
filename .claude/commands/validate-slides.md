---
name: validate-slides
description: Use Playwright to systematically validate Slidev presentations for overflow and visual issues
---

# Validate Slides Command

This command uses the Playwright MCP server to systematically check Slidev presentations for content overflow, visual consistency, and presentation quality issues.

## What it validates:
- Content overflow beyond browser window boundaries
- Code blocks that extend past slide boundaries
- Dense bullet points creating cramped appearance
- Visual inconsistencies in formatting and layout
- Text readability and appropriate font sizes
- Progressive disclosure (v-click) effectiveness

## Usage:
```bash
/validate-slides [slide-file]
```

## Options:
- No arguments: Auto-detects and validates the most recently modified slide file
- `slide-file`: Specific slide file to validate (e.g., `slides_23_cryptographic_apis.md`)
- `--all`: Validate all slide files in the slides/ directory
- `--port 3030`: Specify Slidev server port (default: 3030)
- `--screenshots`: Take screenshots of all slides (not just problematic ones)
- `--cleanup`: Clean up artifacts after validation (calls /playwright-cleanup)

## Examples:
```bash
/validate-slides                                    # Auto-detect recent slides
/validate-slides slides_22_input_validation.md     # Validate specific file
/validate-slides --all                              # Validate all slides
/validate-slides --screenshots --cleanup           # Full validation with cleanup
```

## Validation Process:

### 1. Setup Phase
- Detect or validate the specified slide file exists
- Start Slidev server if not already running (npm run dev)
- Initialize Playwright browser with standard viewport (1920x1080)
- Navigate to the presentation URL

### 2. Systematic Checking
- Navigate through all slides using arrow keys or space bar
- Take screenshots of slides that appear potentially problematic
- Check for:
  - Content extending beyond visible slide area
  - Code blocks with horizontal scroll indicators
  - Bullet point lists that appear cramped
  - Inconsistent spacing or alignment
  - Text that's too small or poorly formatted

### 3. Issue Detection
- **Content Overflow**: Text or code extending past slide boundaries
- **Dense Content**: Too many bullet points or sections per slide
- **Code Issues**: Long lines, poor formatting, missing syntax highlighting
- **Visual Problems**: Inconsistent fonts, spacing, colors
- **Navigation Issues**: v-click sequences that don't enhance understanding

### 4. Reporting
- Generate comprehensive validation report
- List specific slide numbers with identified issues
- Include screenshots of problematic slides
- Provide recommendations for fixes
- Summary of total slides checked and issues found

### 5. Cleanup (Optional)
- Close Playwright browser session
- Remove temporary screenshots (unless --screenshots flag used)
- Call /playwright-cleanup if --cleanup flag specified

## Integration with Slidev Development:

The command automatically:
1. **Detects Slidev setup** - Checks for package.json and Slidev dependencies
2. **Handles server startup** - Starts `npm run dev` if server isn't running
3. **Waits for readiness** - Ensures presentation is fully loaded before validation
4. **Provides actionable feedback** - Specific recommendations for slide improvements

## Example Output:
```
🔍 Validating slides_23_cryptographic_apis.md...
🌐 Starting Slidev server on port 3030...
📷 Taking screenshots and checking for issues...

✅ Slide 1: Title - OK
✅ Slide 2: Contact Info - OK
⚠️  Slide 15: Virtual Threads Code - Content overflow detected
⚠️  Slide 23: Summary - Too many bullet points (7 items)

📊 Validation Results:
- Total slides: 23
- Issues found: 2
- Screenshots saved: 2
- Recommendations: Split dense content, shorten code examples

🎯 Suggestions:
- Slide 15: Break long code example into 2 slides
- Slide 23: Use v-click to progressively reveal bullet points
```

## Advanced Features:

### Batch Validation
When using `--all`, the command:
- Finds all `.md` files in slides/ directory
- Validates each presentation sequentially
- Provides summary report across all presentations
- Identifies common patterns and issues

### Screenshot Management
- Problematic slides automatically captured
- Screenshots saved with descriptive names (slide_XX_issue_description.png)
- Option to preserve all screenshots for documentation
- Integration with /playwright-cleanup for artifact management

### Performance Optimization
- Reuses browser session across multiple slides
- Parallel validation when possible
- Intelligent skip of unchanged slides (based on file timestamps)
- Graceful handling of presentation loading delays

This command ensures all Slidev presentations meet professional standards for video recording and live presentation quality.