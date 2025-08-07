package com.oreilly.javaskills.oop.hr;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.*;

@DisplayName("Address Record Tests")
class AddressTest {
    
    @Test
    @DisplayName("Should create address with all fields")
    void testAddressCreation() {
        Address address = new Address("123 Main St", "San Francisco", "CA", "94102", "USA");
        
        assertThat(address.street()).isEqualTo("123 Main St");
        assertThat(address.city()).isEqualTo("San Francisco");
        assertThat(address.state()).isEqualTo("CA");
        assertThat(address.zipCode()).isEqualTo("94102");
        assertThat(address.country()).isEqualTo("USA");
    }
    
    @Test
    @DisplayName("Should default to USA when country not specified")
    void testAddressDefaultCountry() {
        Address address = new Address("456 Oak Ave", "Oakland", "CA", "94601");
        
        assertThat(address.country()).isEqualTo("USA");
    }
    
    @Test
    @DisplayName("Should validate and trim input fields")
    void testAddressValidation() {
        Address address = new Address("  123 Main St  ", "  San Francisco  ", "  CA  ", "94102", "  USA  ");
        
        assertThat(address.street()).isEqualTo("123 Main St");
        assertThat(address.city()).isEqualTo("San Francisco");
        assertThat(address.state()).isEqualTo("CA");
        assertThat(address.country()).isEqualTo("USA");
    }
    
    @Test
    @DisplayName("Should reject null or empty fields")
    void testAddressRejectsInvalidInput() {
        assertThatThrownBy(() -> new Address(null, "City", "ST", "12345"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Street cannot be null or empty");
            
        assertThatThrownBy(() -> new Address("Street", "", "ST", "12345"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("City cannot be null or empty");
            
        assertThatThrownBy(() -> new Address("Street", "City", "   ", "12345"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("State cannot be null or empty");
    }
    
    @Test
    @DisplayName("Should validate zip code format")
    void testZipCodeValidation() {
        // Valid 5-digit zip
        Address addr1 = new Address("123 Main", "City", "ST", "12345");
        assertThat(addr1.zipCode()).isEqualTo("12345");
        
        // Valid 5+4 zip
        Address addr2 = new Address("123 Main", "City", "ST", "12345-6789");
        assertThat(addr2.zipCode()).isEqualTo("12345-6789");
        
        // Invalid formats
        assertThatThrownBy(() -> new Address("123 Main", "City", "ST", "1234"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid zip code format");
            
        assertThatThrownBy(() -> new Address("123 Main", "City", "ST", "ABCDE"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid zip code format");
    }
    
    @Test
    @DisplayName("Should format address for display")
    void testGetFormattedAddress() {
        Address address = new Address("123 Main St", "San Francisco", "CA", "94102");
        String formatted = address.getFormattedAddress();
        
        assertThat(formatted).contains("123 Main St");
        assertThat(formatted).contains("San Francisco, CA 94102");
        assertThat(formatted).contains("USA");
        
        // Should be multi-line format
        String[] lines = formatted.split("\n");
        assertThat(lines).hasSize(3);
        assertThat(lines[0]).isEqualTo("123 Main St");
        assertThat(lines[1]).isEqualTo("San Francisco, CA 94102");
        assertThat(lines[2]).isEqualTo("USA");
    }
    
    @Test
    @DisplayName("Should generate mailing label")
    void testGetMailingLabel() {
        Address address = new Address("123 Main St", "San Francisco", "CA", "94102");
        String label = address.getMailingLabel();
        
        assertThat(label).isEqualTo("San Francisco, CA 94102");
    }
    
    @Test
    @DisplayName("Should check if address is in a specific state")
    void testIsInState() {
        Address california = new Address("123 Main St", "San Francisco", "CA", "94102");
        Address texas = new Address("456 Oak St", "Austin", "TX", "78701");
        
        // Case-insensitive state matching
        assertThat(california.isInState("CA")).isTrue();
        assertThat(california.isInState("ca")).isTrue();
        assertThat(california.isInState("Ca")).isTrue();
        assertThat(california.isInState("TX")).isFalse();
        
        assertThat(texas.isInState("TX")).isTrue();
        assertThat(texas.isInState("tx")).isTrue();
        assertThat(texas.isInState("CA")).isFalse();
    }
    
    @Test
    @DisplayName("Should check if addresses are in the same city")
    void testIsSameCity() {
        Address addr1 = new Address("123 Main St", "San Francisco", "CA", "94102");
        Address addr2 = new Address("456 Oak Ave", "San Francisco", "CA", "94107");
        Address addr3 = new Address("789 Pine St", "san francisco", "ca", "94108"); // Different case
        Address addr4 = new Address("321 Elm St", "Oakland", "CA", "94601");
        Address addr5 = new Address("555 First St", "San Francisco", "TX", "75001"); // Same city, different state
        
        // Same city and state
        assertThat(addr1.isSameCity(addr2)).isTrue();
        
        // Case-insensitive comparison
        assertThat(addr1.isSameCity(addr3)).isTrue();
        
        // Different city, same state
        assertThat(addr1.isSameCity(addr4)).isFalse();
        
        // Same city name, different state
        assertThat(addr1.isSameCity(addr5)).isFalse();
        
        // Null safety
        assertThat(addr1.isSameCity(null)).isFalse();
    }
    
    @Test
    @DisplayName("Should implement value equality as a record")
    void testRecordEquality() {
        Address addr1 = new Address("123 Main St", "San Francisco", "CA", "94102");
        Address addr2 = new Address("123 Main St", "San Francisco", "CA", "94102");
        Address addr3 = new Address("456 Oak Ave", "San Francisco", "CA", "94102");
        
        // Records implement equals based on all fields
        assertThat(addr1).isEqualTo(addr2);
        assertThat(addr1).isNotEqualTo(addr3);
        
        // Hash codes should match for equal objects
        assertThat(addr1.hashCode()).isEqualTo(addr2.hashCode());
    }
    
    @Test
    @DisplayName("Should have useful toString representation")
    void testToString() {
        Address address = new Address("123 Main St", "San Francisco", "CA", "94102");
        String str = address.toString();
        
        // Default record toString includes all fields
        assertThat(str).contains("123 Main St");
        assertThat(str).contains("San Francisco");
        assertThat(str).contains("CA");
        assertThat(str).contains("94102");
        assertThat(str).contains("USA");
    }
}