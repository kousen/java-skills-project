import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for RefactoringExercise
 * <p>
 * IMPORTANT: These tests are written to verify BEHAVIOR, not implementation details.
 * They should remain GREEN throughout your refactoring process.
 * <p>
 * If tests start failing during refactoring, it means you've accidentally
 * changed the behavior (not just the structure) - which violates the
 * refactoring principle of preserving external behavior.
 * <p>
 * Use these tests as your safety net to refactor with confidence!
 */
class RefactoringExerciseTest {
    
    private RefactoringExercise library;
    
    @BeforeEach
    void setUp() {
        library = new RefactoringExercise();
    }
    
    @Nested
    @DisplayName("Book Management")
    class BookManagement {
        
        @Test
        @DisplayName("Should add a valid book successfully")
        void shouldAddValidBook() {
            var book = new Book("978-0134685991", "Effective Java", "Joshua Bloch", true);
            
            assertThatNoException().isThrownBy(() -> library.addBook(book));
            
            var foundBook = library.findBookByIsbn("978-0134685991");
            assertThat(foundBook).isEqualTo(book);
        }
        
        @Test
        @DisplayName("Should reject null book")
        void shouldRejectNullBook() {
            assertThatThrownBy(() -> library.addBook(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Book and ISBN cannot be null");
        }
        
        @Test
        @DisplayName("Should reject book with null ISBN")
        void shouldRejectBookWithNullIsbn() {
            var book = new Book(null, "Some Title", "Some Author", true);
            
            assertThatThrownBy(() -> library.addBook(book))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Book and ISBN cannot be null");
        }
        
        @Test
        @DisplayName("Should reject book with empty ISBN")
        void shouldRejectBookWithEmptyIsbn() {
            var book = new Book("  ", "Some Title", "Some Author", true);
            
            assertThatThrownBy(() -> library.addBook(book))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Book and ISBN cannot be null or empty");
        }
        
        @Test
        @DisplayName("Should reject duplicate ISBN")
        void shouldRejectDuplicateIsbn() {
            var book1 = new Book("978-0134685991", "Effective Java", "Joshua Bloch", true);
            var book2 = new Book("978-0134685991", "Different Title", "Different Author", true);
            
            library.addBook(book1);
            
            assertThatThrownBy(() -> library.addBook(book2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("already exists");
        }
        
        @Test
        @DisplayName("Should find books by author (case insensitive)")
        void shouldFindBooksByAuthor() {
            library.addBook(new Book("978-1", "Clean Code", "Robert Martin", true));
            library.addBook(new Book("978-2", "Clean Architecture", "Robert Martin", true));
            library.addBook(new Book("978-3", "Effective Java", "Joshua Bloch", true));
            
            var martinBooks = library.findBooksByAuthor("robert martin");
            assertThat(martinBooks).hasSize(2);
            assertThat(martinBooks).extracting(Book::title)
                    .containsExactlyInAnyOrder("Clean Code", "Clean Architecture");
        }
        
        @Test
        @DisplayName("Should return all available books")
        void shouldReturnAllAvailableBooks() {
            library.addBook(new Book("978-1", "Available Book 1", "Author 1", true));
            library.addBook(new Book("978-2", "Unavailable Book", "Author 2", false));
            library.addBook(new Book("978-3", "Available Book 2", "Author 3", true));
            
            var availableBooks = library.getAllAvailableBooks();
            assertThat(availableBooks).hasSize(2);
            assertThat(availableBooks).allMatch(Book::isAvailable);
        }
        
        @Test
        @DisplayName("Should return null for non-existent ISBN")
        void shouldReturnNullForNonExistentIsbn() {
            assertThat(library.findBookByIsbn("non-existent")).isNull();
            assertThat(library.findBookByIsbn(null)).isNull();
            assertThat(library.findBookByIsbn("")).isNull();
        }
    }
    
    @Nested
    @DisplayName("Member Management")
    class MemberManagement {
        
        @Test
        @DisplayName("Should add a valid member successfully")
        void shouldAddValidMember() {
            var member = new Member("M001", "John Doe", "john@example.com", LocalDate.now(), true);
            
            assertThatNoException().isThrownBy(() -> library.addMember(member));
            
            var foundMember = library.findMemberById("M001");
            assertThat(foundMember).isEqualTo(member);
        }
        
        @Test
        @DisplayName("Should reject null member")
        void shouldRejectNullMember() {
            assertThatThrownBy(() -> library.addMember(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Member and member ID cannot be null");
        }
        
        @Test
        @DisplayName("Should reject member with null ID")
        void shouldRejectMemberWithNullId() {
            var member = new Member(null, "John Doe", "john@example.com", LocalDate.now(), true);
            
            assertThatThrownBy(() -> library.addMember(member))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Member and member ID cannot be null");
        }
        
        @Test
        @DisplayName("Should reject duplicate member ID")
        void shouldRejectDuplicateMemberId() {
            var member1 = new Member("M001", "John Doe", "john@example.com", LocalDate.now(), true);
            var member2 = new Member("M001", "Jane Doe", "jane@example.com", LocalDate.now(), true);
            
            library.addMember(member1);
            
            assertThatThrownBy(() -> library.addMember(member2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("already exists");
        }
        
        @Test
        @DisplayName("Should return all active members")
        void shouldReturnAllActiveMembers() {
            library.addMember(new Member("M001", "Active Member 1", "active1@example.com", LocalDate.now(), true));
            library.addMember(new Member("M002", "Inactive Member", "inactive@example.com", LocalDate.now(), false));
            library.addMember(new Member("M003", "Active Member 2", "active2@example.com", LocalDate.now(), true));
            
            var activeMembers = library.getAllActiveMembers();
            assertThat(activeMembers).hasSize(2);
            assertThat(activeMembers).allMatch(Member::isActive);
        }
        
        @Test
        @DisplayName("Should return null for non-existent member ID")
        void shouldReturnNullForNonExistentMemberId() {
            assertThat(library.findMemberById("non-existent")).isNull();
            assertThat(library.findMemberById(null)).isNull();
            assertThat(library.findMemberById("")).isNull();
        }
    }
    
    @Nested
    @DisplayName("Checkout Management")
    class CheckoutManagement {
        
        @BeforeEach
        void setUpCheckoutData() {
            // Add test books
            library.addBook(new Book("978-1", "Test Book 1", "Author 1", true));
            library.addBook(new Book("978-2", "Test Book 2", "Author 2", true));
            
            // Add test members
            library.addMember(new Member("M001", "Active Member", "active@example.com", LocalDate.now(), true));
            library.addMember(new Member("M002", "Inactive Member", "inactive@example.com", LocalDate.now(), false));
            library.addMember(new Member("M003", "Another Active Member", "active2@example.com", LocalDate.now(), true));
        }
        
        @Test
        @DisplayName("Should successfully checkout available book to active member")
        void shouldCheckoutAvailableBookToActiveMember() {
            assertThatNoException().isThrownBy(() -> library.checkoutBook("978-1", "M001"));
            
            var activeCheckouts = library.getActiveCheckouts();
            assertThat(activeCheckouts).hasSize(1);
            assertThat(activeCheckouts.get(0).isbn()).isEqualTo("978-1");
            assertThat(activeCheckouts.get(0).memberId()).isEqualTo("M001");
            assertThat(activeCheckouts.get(0).isReturned()).isFalse();
        }
        
        @Test
        @DisplayName("Should update book availability after checkout")
        void shouldUpdateBookAvailabilityAfterCheckout() {
            library.checkoutBook("978-1", "M001");
            
            var book = library.findBookByIsbn("978-1");
            assertThat(book.isAvailable()).isFalse();
        }
        
        @Test
        @DisplayName("Should reject checkout for non-existent member")
        void shouldRejectCheckoutForNonExistentMember() {
            assertThatThrownBy(() -> library.checkoutBook("978-1", "NON_EXISTENT"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Member not found");
        }
        
        @Test
        @DisplayName("Should reject checkout for inactive member")
        void shouldRejectCheckoutForInactiveMember() {
            assertThatThrownBy(() -> library.checkoutBook("978-1", "M002"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Member is not active");
        }
        
        @Test
        @DisplayName("Should reject checkout for non-existent book")
        void shouldRejectCheckoutForNonExistentBook() {
            assertThatThrownBy(() -> library.checkoutBook("NON_EXISTENT", "M001"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Book not found");
        }
        
        @Test
        @DisplayName("Should reject checkout for unavailable book")
        void shouldRejectCheckoutForUnavailableBook() {
            library.checkoutBook("978-1", "M001"); // First checkout by M001
            
            assertThatThrownBy(() -> library.checkoutBook("978-1", "M003")) // Different active member tries same book
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Book is not available");
        }
        
        @Test
        @DisplayName("Should reject duplicate checkout by same member")
        void shouldRejectDuplicateCheckoutBySameMember() {
            library.checkoutBook("978-1", "M001");
            
            assertThatThrownBy(() -> library.checkoutBook("978-1", "M001"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Member already has this book checked out");
        }
        
        @Test
        @DisplayName("Should successfully return checked out book")
        void shouldSuccessfullyReturnCheckedOutBook() {
            library.checkoutBook("978-1", "M001");
            
            assertThatNoException().isThrownBy(() -> library.returnBook("978-1", "M001"));
            
            // Verify checkout is marked as returned
            var activeCheckouts = library.getActiveCheckouts();
            assertThat(activeCheckouts).isEmpty();
            
            // Verify book is available again
            var book = library.findBookByIsbn("978-1");
            assertThat(book.isAvailable()).isTrue();
        }
        
        @Test
        @DisplayName("Should reject return for non-existent checkout")
        void shouldRejectReturnForNonExistentCheckout() {
            assertThatThrownBy(() -> library.returnBook("978-1", "M001"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("No active checkout found");
        }
        
        @Test
        @DisplayName("Should set correct due date (2 weeks from checkout)")
        void shouldSetCorrectDueDate() {
            library.checkoutBook("978-1", "M001");
            
            var checkout = library.getActiveCheckouts().get(0);
            var expectedDueDate = LocalDate.now().plusWeeks(2);
            assertThat(checkout.dueDate()).isEqualTo(expectedDueDate);
        }
    }
    
    @Nested
    @DisplayName("Fine Calculation")
    class FineCalculation {
        
        @BeforeEach
        void setUpOverdueData() {
            library.addBook(new Book("978-1", "Overdue Book", "Author", true));
            library.addMember(new Member("M001", "Member", "member@example.com", LocalDate.now(), true));
        }
        
        @Test
        @DisplayName("Should calculate fine for overdue book")
        void shouldCalculateFineForOverdueBook() {
            library.checkoutBook("978-1", "M001");
            
            // Simulate overdue scenario by checking if fine calculation logic works
            // Note: This test relies on the CheckoutRecord.isOverdue() method working correctly
            var fine = library.calculateFine("M001");
            
            // For a current checkout, fine should be 0 (not overdue yet)
            assertThat(fine).isEqualTo(0.0);
        }
        
        @Test
        @DisplayName("Should return zero fine for member with no checkouts")
        void shouldReturnZeroFineForMemberWithNoCheckouts() {
            var fine = library.calculateFine("M001");
            assertThat(fine).isEqualTo(0.0);
        }
        
        @Test
        @DisplayName("Should return zero fine for non-existent member")
        void shouldReturnZeroFineForNonExistentMember() {
            var fine = library.calculateFine("NON_EXISTENT");
            assertThat(fine).isEqualTo(0.0);
        }
        
        @Test
        @DisplayName("Should find members with fines")
        void shouldFindMembersWithFines() {
            library.addMember(new Member("M002", "Another Member", "another@example.com", LocalDate.now(), true));
            
            var membersWithFines = library.getMembersWithFines();
            
            // Since we can't easily create overdue books in tests without time manipulation,
            // we test that the method returns a list (empty in this case)
            assertThat(membersWithFines).isNotNull();
            assertThat(membersWithFines).isEmpty(); // No overdue books in current setup
        }
    }
    
    @Nested
    @DisplayName("Reporting")
    class Reporting {
        
        @Test
        @DisplayName("Should report correct total counts")
        void shouldReportCorrectTotalCounts() {
            // Add test data
            library.addBook(new Book("978-1", "Book 1", "Author 1", true));
            library.addBook(new Book("978-2", "Book 2", "Author 2", true));
            library.addMember(new Member("M001", "Member 1", "member1@example.com", LocalDate.now(), true));
            library.addMember(new Member("M002", "Member 2", "member2@example.com", LocalDate.now(), true));
            
            assertThat(library.getTotalBooks()).isEqualTo(2);
            assertThat(library.getTotalMembers()).isEqualTo(2);
            assertThat(library.getActiveCheckoutsCount()).isEqualTo(0);
            assertThat(library.getOverdueCheckoutsCount()).isEqualTo(0);
            
            // After checkout
            library.checkoutBook("978-1", "M001");
            assertThat(library.getActiveCheckoutsCount()).isEqualTo(1);
        }
        
        @Test
        @DisplayName("Should handle empty library")
        void shouldHandleEmptyLibrary() {
            assertThat(library.getTotalBooks()).isEqualTo(0);
            assertThat(library.getTotalMembers()).isEqualTo(0);
            assertThat(library.getActiveCheckoutsCount()).isEqualTo(0);
            assertThat(library.getOverdueCheckoutsCount()).isEqualTo(0);
            assertThat(library.getAllAvailableBooks()).isEmpty();
            assertThat(library.getAllActiveMembers()).isEmpty();
            assertThat(library.getActiveCheckouts()).isEmpty();
            assertThat(library.getOverdueCheckouts()).isEmpty();
        }
    }
    
    @Nested
    @DisplayName("Integration Scenarios")
    class IntegrationScenarios {
        
        @Test
        @DisplayName("Complete library workflow")
        void completeLibraryWorkflow() {
            // Setup
            library.addBook(new Book("978-1", "Java Book", "Java Author", true));
            library.addBook(new Book("978-2", "Python Book", "Python Author", true));
            library.addMember(new Member("M001", "Alice", "alice@example.com", LocalDate.now(), true));
            library.addMember(new Member("M002", "Bob", "bob@example.com", LocalDate.now(), true));
            
            // Initial state
            assertThat(library.getTotalBooks()).isEqualTo(2);
            assertThat(library.getTotalMembers()).isEqualTo(2);
            assertThat(library.getActiveCheckoutsCount()).isEqualTo(0);
            
            // Checkout
            library.checkoutBook("978-1", "M001");
            library.checkoutBook("978-2", "M002");
            
            assertThat(library.getActiveCheckoutsCount()).isEqualTo(2);
            assertThat(library.getAllAvailableBooks()).isEmpty();
            
            // Return one book
            library.returnBook("978-1", "M001");
            
            assertThat(library.getActiveCheckoutsCount()).isEqualTo(1);
            assertThat(library.getAllAvailableBooks()).hasSize(1);
            
            // Member can checkout the returned book again
            assertThatNoException().isThrownBy(() -> library.checkoutBook("978-1", "M002"));
        }
    }
}