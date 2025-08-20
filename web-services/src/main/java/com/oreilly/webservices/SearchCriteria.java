package com.oreilly.webservices;

/**
 * Search criteria for advanced employee searches.
 * This record is used as a request body for POST /api/search/advanced
 * <p>
 * Example JSON:
 * {
 *   "department": "Engineering",
 *   "minSalary": 60000,
 *   "maxSalary": 90000,
 *   "nameContains": "john"
 * }
 */
public record SearchCriteria(
    String department,
    Double minSalary,
    Double maxSalary,
    String nameContains
) {
    // All fields are optional - null values will be ignored in search
}