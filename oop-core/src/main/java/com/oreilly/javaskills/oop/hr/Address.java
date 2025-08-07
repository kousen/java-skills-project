package com.oreilly.javaskills.oop.hr;

public record Address(String street, String city, String state, String zipCode, String country) {
    
    // Additional constructor for convenience (defaults to USA)
    public Address(String street, String city, String state, String zipCode) {
        this(street, city, state, zipCode, "USA");
    }
    
    // Compact constructor for validation
    public Address {
        street = validateAndTrim(street, "Street");
        city = validateAndTrim(city, "City");
        state = validateAndTrim(state, "State");
        zipCode = validateZipCode(zipCode);
        country = validateAndTrim(country, "Country");
    }
    
    private static String validateAndTrim(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
        return value.trim();
    }
    
    private static String validateZipCode(String zipCode) {
        if (zipCode == null || zipCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Zip code cannot be null or empty");
        }
        String trimmed = zipCode.trim();
        // Basic US zip code validation (5 digits or 5+4 format)
        if (!trimmed.matches("\\d{5}(-\\d{4})?")) {
            throw new IllegalArgumentException("Invalid zip code format. Expected: 12345 or 12345-6789");
        }
        return trimmed;
    }
    
    // Business methods - records can have methods too!
    public String getFormattedAddress() {
        return String.format("%s%n%s, %s %s%n%s", 
                           street, city, state, zipCode, country);
    }
    
    public String getMailingLabel() {
        return String.format("%s, %s %s", city, state, zipCode);
    }
    
    public boolean isInState(String targetState) {
        return state.equalsIgnoreCase(targetState);
    }
    
    public boolean isSameCity(Address other) {
        if (other == null) return false;
        return city.equalsIgnoreCase(other.city) && 
               state.equalsIgnoreCase(other.state);
    }
    
    @Override
    public String toString() {
        return getMailingLabel();
    }
}