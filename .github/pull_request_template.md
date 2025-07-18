# Pull Request Template

## 📋 Summary
<!-- Provide a clear and concise description of what this PR does -->

### What changed?
- [ ] New feature
- [ ] Bug fix
- [ ] Documentation update
- [ ] Refactoring
- [ ] Performance improvement
- [ ] Security fix
- [ ] Dependencies update

### Related Issues
<!-- Link any related issues using keywords: fixes #123, closes #456, resolves #789 -->
Fixes #

## 🔍 Changes Made
<!-- Describe the changes in detail -->

### Code Changes
- 
- 
- 

### Configuration Changes
- 
- 

### Database Changes
- [ ] Schema changes
- [ ] Data migration required
- [ ] Backwards compatible

## 🧪 Testing
<!-- Describe how you tested your changes -->

### Test Coverage
- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [ ] Manual testing completed
- [ ] All existing tests pass

### Test Scenarios
<!-- List the specific scenarios you tested -->
1. 
2. 
3. 

### Performance Impact
- [ ] Performance tested
- [ ] No performance regression
- [ ] Performance improvement: <!-- describe -->

## 📚 Documentation
<!-- Check all that apply -->
- [ ] Code is self-documenting
- [ ] JavaDoc comments added/updated
- [ ] README updated
- [ ] API documentation updated
- [ ] Configuration documentation updated

## 🔒 Security Considerations
<!-- Check all that apply -->
- [ ] No sensitive data exposed
- [ ] Input validation implemented
- [ ] Authentication/authorization considered
- [ ] Security scan passed
- [ ] Dependencies vulnerability check passed

## 🚀 Deployment Notes
<!-- Include any special deployment instructions -->

### Environment Variables
<!-- List any new environment variables needed -->
- `NEW_ENV_VAR`: Description

### Configuration Changes
<!-- List any configuration file changes -->
- 

### Database Migrations
<!-- Include migration commands or scripts -->
```sql
-- Migration script here
```

## ✅ Checklist
<!-- Check all items before requesting review -->

### Code Quality
- [ ] Code follows project style guidelines
- [ ] No hardcoded values (use configuration)
- [ ] Error handling implemented
- [ ] Logging added where appropriate
- [ ] Code is readable and maintainable

### Git Hygiene
- [ ] Commit messages are clear and descriptive
- [ ] Branch is up to date with target branch
- [ ] No merge conflicts
- [ ] No debug code or console.log statements
- [ ] .gitignore updated if needed

### Review Ready
- [ ] Self-review completed
- [ ] Screenshots attached (if UI changes)
- [ ] Breaking changes documented
- [ ] Backward compatibility considered

## 📸 Screenshots
<!-- Add screenshots for UI changes -->

### Before
<!-- Screenshot of current state -->

### After
<!-- Screenshot of new state -->

## 🚨 Breaking Changes
<!-- List any breaking changes and migration steps -->

- 

## 📝 Additional Notes
<!-- Any additional information for reviewers -->

## 🏷️ Review Guidelines
<!-- For reviewers -->

### Focus Areas
Please pay special attention to:
- [ ] Security implications
- [ ] Performance impact
- [ ] Error handling
- [ ] Test coverage
- [ ] Documentation completeness

### Deployment Risk
- [ ] Low risk (minor changes, well tested)
- [ ] Medium risk (moderate changes, some complexity)
- [ ] High risk (major changes, complex logic, external dependencies)

---

**Thank you for contributing! 🙏**

<!-- 
Template Usage Notes:
1. Delete sections that don't apply to your PR
2. Replace placeholder text with actual information
3. Check all applicable boxes before requesting review
4. Add relevant labels to the PR
5. Assign appropriate reviewers
-->