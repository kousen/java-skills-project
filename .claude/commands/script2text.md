# Script to Teleprompter Text Converter

Convert a video script markdown file to clean text for Elgato Prompter.

## Usage
```
/script2text <script_number>
```

Example: `/script2text 01` or `/script2text 7b`

## Instructions

1. Take the script number provided as an argument
2. Find the corresponding script file in the `scripts/` directory with pattern `video_script_<number>_*.md`
3. Read the markdown file and convert it to prompter-friendly text:
   - Remove all markdown formatting (headers, bold, italics)
   - Convert scene headers from `### SCENE X: Title (time)` to `[SCENE X: TITLE - duration]`
   - Remove all "YOU:" prefixes from dialogue
   - Keep slide references like "(Show Slide X: Title)" and "(Transition to...)"
   - Remove any empty lines between paragraphs within the same scene
   - Keep single blank lines only between scenes
   - Remove all leading and trailing spaces from each line
   - Ensure each paragraph is a single line (no line breaks within paragraphs)
4. Save the output as `script.txt` in the project root directory, overwriting if it exists
5. Inform the user that the file is ready at `script.txt`

## Example Conversion

Input:
```markdown
### SCENE 1: Introduction (0:00 - 0:30)

**(Show Slide 1: Title Slide)**

**YOU:**
"Hi everyone, and welcome to this series..."
```

Output:
```
[SCENE 1: INTRODUCTION - 30 SECONDS]

(Show Slide 1: Title Slide)

Hi everyone, and welcome to this series...
```

## Notes
- The output should have no leading spaces on any line
- Each paragraph should be on a single line with no internal line breaks
- Scene markers should be in uppercase within brackets
- Duration should be converted from (0:00 - 0:30) format to "30 SECONDS" or "60 SECONDS" etc.