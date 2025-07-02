import java.util.Objects;

public class Address {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    
    public Address(String street, String city, String state, String zipCode) {
        this(street, city, state, zipCode, "USA");
    }
    
    public Address(String street, String city, String state, String zipCode, String country) {
        this.street = validateAndTrim(street, "Street");
        this.city = validateAndTrim(city, "City");
        this.state = validateAndTrim(state, "State");
        this.zipCode = validateZipCode(zipCode);
        this.country = validateAndTrim(country, "Country");
    }
    
    private String validateAndTrim(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
        return value.trim();
    }
    
    private String validateZipCode(String zipCode) {
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
    
    // Getters
    public String getStreet() {
        return street;
    }
    
    public String getCity() {
        return city;
    }
    
    public String getState() {
        return state;
    }
    
    public String getZipCode() {
        return zipCode;
    }
    
    public String getCountry() {
        return country;
    }
    
    // Setters with validation
    public void setStreet(String street) {
        this.street = validateAndTrim(street, "Street");
    }
    
    public void setCity(String city) {
        this.city = validateAndTrim(city, "City");
    }
    
    public void setState(String state) {
        this.state = validateAndTrim(state, "State");
    }
    
    public void setZipCode(String zipCode) {
        this.zipCode = validateZipCode(zipCode);
    }
    
    public void setCountry(String country) {
        this.country = validateAndTrim(country, "Country");
    }
    
    // Business methods
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Address address = (Address) obj;
        return Objects.equals(street, address.street) &&
               Objects.equals(city, address.city) &&
               Objects.equals(state, address.state) &&
               Objects.equals(zipCode, address.zipCode) &&
               Objects.equals(country, address.country);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(street, city, state, zipCode, country);
    }
    
    @Override
    public String toString() {
        return getMailingLabel();
    }
}