import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Try It Out Exercise: Refactoring with Tests
 * <p>
 * This exercise demonstrates test-driven refactoring by providing:
 * 1. A legacy class with multiple code smells
 * 2. Comprehensive tests that verify behavior (not implementation)
 * 3. TODO guidance for refactoring while keeping tests green
 * <p>
 * SCENARIO: A library management system that needs refactoring.
 * The LegacyLibrarySystem class has grown into a God class that handles
 * book management, member management, checkout logic, and fine calculations.
 * <p>
 * YOUR MISSION: Refactor this system while keeping all tests passing.
 * The tests are written to verify BEHAVIOR, not implementation details,
 * so they should remain green throughout your refactoring process.
 */

// Data classes for the exercise
record Book(String isbn, String title, String author, boolean isAvailable) {
}

record Member(String memberId, String name, String email, LocalDate joinDate, boolean isActive) {
}

record CheckoutRecord(String isbn, String memberId, LocalDate checkoutDate, LocalDate dueDate, boolean isReturned) {
    public boolean isOverdue() {
        return !isReturned && LocalDate.now().isAfter(dueDate);
    }
    
    public int getDaysOverdue() {
        if (!isOverdue()) return 0;
        return (int) dueDate.until(LocalDate.now()).getDays();
    }
}

/**
 * LEGACY CODE - This class has multiple code smells that need refactoring!
 * 
 * Code Smells Present:
 * 1. God Class - handles books, members, checkouts, and fines
 * 2. Long methods - some methods do too much
 * 3. Duplicated logic - member validation repeated
 * 4. Mixed responsibilities - business logic mixed with data management
 * 5. Hard to test - everything is tightly coupled
 * 
 * IMPORTANT: This class has comprehensive tests! Use them as your safety net
 * while refactoring. The tests verify behavior, not implementation details.
 */
@SuppressWarnings({"WeakerAccess", "ClassEscapesDefinedScope"})
public class RefactoringExercise {
    private final List<Book> books = new ArrayList<>();
    private final List<Member> members = new ArrayList<>();
    private final List<CheckoutRecord> checkouts = new ArrayList<>();
    
    // ========== BOOK MANAGEMENT ==========
    
    public void addBook(Book book) {
        if (book == null || book.isbn() == null || book.isbn().trim().isEmpty()) {
            throw new IllegalArgumentException("Book and ISBN cannot be null or empty");
        }
        
        // Check for duplicate ISBN
        for (Book existingBook : books) {
            if (existingBook.isbn().equals(book.isbn())) {
                throw new IllegalArgumentException("Book with ISBN " + book.isbn() + " already exists");
            }
        }
        
        books.add(book);
    }
    
    public Book findBookByIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return null;
        }
        
        for (Book book : books) {
            if (book.isbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }
    
    public List<Book> findBooksByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.author().equalsIgnoreCase(author)) {
                result.add(book);
            }
        }
        return result;
    }
    
    public List<Book> getAllAvailableBooks() {
        List<Book> available = new ArrayList<>();
        for (Book book : books) {
            if (book.isAvailable()) {
                available.add(book);
            }
        }
        return available;
    }
    
    // ========== MEMBER MANAGEMENT ==========
    
    public void addMember(Member member) {
        if (member == null || member.memberId() == null || member.memberId().trim().isEmpty()) {
            throw new IllegalArgumentException("Member and member ID cannot be null or empty");
        }
        
        // Check for duplicate member ID - DUPLICATED VALIDATION LOGIC!
        for (Member existingMember : members) {
            if (existingMember.memberId().equals(member.memberId())) {
                throw new IllegalArgumentException("Member with ID " + member.memberId() + " already exists");
            }
        }
        
        members.add(member);
    }
    
    public Member findMemberById(String memberId) {
        if (memberId == null || memberId.trim().isEmpty()) {
            return null;
        }
        
        // DUPLICATED LOGIC - same pattern as findBookByIsbn
        for (Member member : members) {
            if (member.memberId().equals(memberId)) {
                return member;
            }
        }
        return null;
    }
    
    public List<Member> getAllActiveMembers() {
        List<Member> active = new ArrayList<>();
        for (Member member : members) {
            if (member.isActive()) {
                active.add(member);
            }
        }
        return active;
    }
    
    // ========== CHECKOUT MANAGEMENT ==========
    
    public void checkoutBook(String isbn, String memberId) {
        // LONG METHOD - does too much validation and business logic
        
        // Validate member - DUPLICATED VALIDATION!
        Member member = null;
        for (Member m : members) {
            if (m.memberId().equals(memberId)) {
                member = m;
                break;
            }
        }
        
        if (member == null) {
            throw new IllegalArgumentException("Member not found: " + memberId);
        }
        
        if (!member.isActive()) {
            throw new IllegalArgumentException("Member is not active: " + memberId);
        }
        
        // Validate book - DUPLICATED VALIDATION!
        Book book = null;
        for (Book b : books) {
            if (b.isbn().equals(isbn)) {
                book = b;
                break;
            }
        }
        
        if (book == null) {
            throw new IllegalArgumentException("Book not found: " + isbn);
        }
        
        if (!book.isAvailable()) {
            throw new IllegalArgumentException("Book is not available: " + isbn);
        }
        
        // Check if member already has this book checked out
        for (CheckoutRecord record : checkouts) {
            if (record.isbn().equals(isbn) && record.memberId().equals(memberId) && !record.isReturned()) {
                throw new IllegalArgumentException("Member already has this book checked out");
            }
        }
        
        // Create checkout record
        LocalDate dueDate = LocalDate.now().plusWeeks(2); // 2-week loan period
        CheckoutRecord record = new CheckoutRecord(isbn, memberId, LocalDate.now(), dueDate, false);
        checkouts.add(record);
        
        // Update book availability
        Book unavailableBook = new Book(book.isbn(), book.title(), book.author(), false);
        books.removeIf(b -> b.isbn().equals(isbn));
        books.add(unavailableBook);
    }
    
    public void returnBook(String isbn, String memberId) {
        CheckoutRecord recordToReturn = null;
        
        // Find active checkout record - DUPLICATED SEARCH LOGIC!
        for (CheckoutRecord record : checkouts) {
            if (record.isbn().equals(isbn) && record.memberId().equals(memberId) && !record.isReturned()) {
                recordToReturn = record;
                break;
            }
        }
        
        if (recordToReturn == null) {
            throw new IllegalArgumentException("No active checkout found for book " + isbn + " and member " + memberId);
        }
        
        // Mark as returned
        CheckoutRecord returnedRecord = new CheckoutRecord(
            recordToReturn.isbn(), recordToReturn.memberId(), 
            recordToReturn.checkoutDate(), recordToReturn.dueDate(), true
        );
        checkouts.removeIf(r -> r.isbn().equals(isbn) && r.memberId().equals(memberId) && !r.isReturned());
        checkouts.add(returnedRecord);
        
        // Update book availability
        Book book = findBookByIsbn(isbn);
        if (book != null) {
            Book availableBook = new Book(book.isbn(), book.title(), book.author(), true);
            books.removeIf(b -> b.isbn().equals(isbn));
            books.add(availableBook);
        }
    }
    
    public List<CheckoutRecord> getActiveCheckouts() {
        List<CheckoutRecord> active = new ArrayList<>();
        for (CheckoutRecord record : checkouts) {
            if (!record.isReturned()) {
                active.add(record);
            }
        }
        return active;
    }
    
    public List<CheckoutRecord> getOverdueCheckouts() {
        List<CheckoutRecord> overdue = new ArrayList<>();
        for (CheckoutRecord record : checkouts) {
            if (record.isOverdue()) {
                overdue.add(record);
            }
        }
        return overdue;
    }
    
    // ========== FINE CALCULATION ==========
    
    public double calculateFine(String memberId) {
        double totalFine = 0.0;
        
        // ANOTHER LONG METHOD - mixed concerns
        for (CheckoutRecord record : checkouts) {
            if (record.memberId().equals(memberId) && record.isOverdue()) {
                int daysOverdue = record.getDaysOverdue();
                double finePerDay = 0.50; // $0.50 per day
                double maxFine = 10.00;   // Maximum fine per book
                
                double bookFine = Math.min(daysOverdue * finePerDay, maxFine);
                totalFine += bookFine;
            }
        }
        
        return totalFine;
    }
    
    public List<Member> getMembersWithFines() {
        List<Member> membersWithFines = new ArrayList<>();
        
        for (Member member : members) {
            if (calculateFine(member.memberId()) > 0) {
                // DUPLICATED LOGIC - iterating through members again
                membersWithFines.add(member);
            }
        }
        
        return membersWithFines;
    }
    
    // ========== REPORTING ==========
    
    public int getTotalBooks() {
        return books.size();
    }
    
    public int getTotalMembers() {
        return members.size();
    }
    
    public int getActiveCheckoutsCount() {
        return getActiveCheckouts().size();
    }
    
    public int getOverdueCheckoutsCount() {
        return getOverdueCheckouts().size();
    }
}

/**
 * TODO: Refactor the RefactoringExercise class following these steps:
 * 
 * STEP 1: Extract BookRepository class
 * - Move all book-related methods (addBook, findBookByIsbn, findBooksByAuthor, getAllAvailableBooks)
 * - Create a focused class responsible only for book data management
 * - Replace direct access to books list with repository method calls
 * 
 * STEP 2: Extract MemberRepository class  
 * - Move all member-related methods (addMember, findMemberById, getAllActiveMembers)
 * - Eliminate duplicated member validation logic
 * - Create consistent interface for member operations
 * 
 * STEP 3: Extract CheckoutService class
 * - Move checkout/return logic (checkoutBook, returnBook, getActiveCheckouts, getOverdueCheckouts)
 * - Break down the long checkoutBook method into smaller, focused methods
 * - Use the repository classes instead of direct data access
 * 
 * STEP 4: Extract FineCalculator class
 * - Move fine calculation logic (calculateFine, getMembersWithFines)
 * - Make fine rates configurable (currently hardcoded)
 * - Separate fine calculation from fine reporting
 * 
 * STEP 5: Create LibrarySystem coordinator class
 * - Use dependency injection to compose the services
 * - Keep only high-level orchestration methods
 * - Delegate all specific operations to appropriate services
 * 
 * STEP 6: Run the tests after each step!
 * - The tests should remain green throughout the refactoring process
 * - If tests fail, you've changed behavior (not just structure)
 * - Use the tests as your safety net to refactor confidently
 * 
 * BONUS CHALLENGES:
 * - Add validation service to eliminate duplicated validation logic
 * - Make loan period configurable instead of hardcoded 2 weeks
 * - Add book reservation system
 * - Implement member borrowing limits
 * 
 * Remember: The goal is to improve the structure while keeping the same behavior!
 */