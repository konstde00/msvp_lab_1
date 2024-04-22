
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.konstde00.model.Book;
import org.konstde00.model.Genre;
import org.konstde00.service.BookManager;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class BookManagerTest {
    private static BookManager bookManager;

    @BeforeAll
    static void setup() {
        bookManager = new BookManager();
    }

    @BeforeEach
    void addBooks() {
        bookManager.addBook(new Book("1", "The Great Gatsby", "F. Scott Fitzgerald", Genre.ROMANCE));
        bookManager.addBook(new Book("2", "Moby Dick", "Herman Melville", Genre.ADVENTURE));
        bookManager.addBook(new Book("3", "1984", "George Orwell", Genre.DYSTOPIA));
        bookManager.addBook(new Book("4", "Brothers Karamazov", "Fyodor Dostoevsky", Genre.PHILOSOPHICAL_FICTION));
    }

    @AfterEach
    void cleanup() {
        bookManager.removeBook("1");
        bookManager.removeBook("2");
        bookManager.removeBook("3");
        bookManager.removeBook("4");
    }

    @Test
    void testAddBook() {
        assertEquals(1, bookManager.getBooksByAuthor("F. Scott Fitzgerald").size());
    }

    @Test
    void testRemoveBook() {
        assertNotNull(bookManager.removeBook("1"));
        assertNull(bookManager.removeBook("5"));
    }

    @Test
    void testFindBooksByTitle() {
        List<Book> foundBooks = bookManager.findBooksByTitle("1984");
        assertFalse(foundBooks.isEmpty());
        assertThat(foundBooks, Matchers.hasItem(Matchers.hasProperty("author", equalTo("George Orwell"))));
    }

    @Test
    void testGetBooksByAuthor() {
        List<Book> foundBooks = bookManager.getBooksByAuthor("Herman Melville");
        assertEquals(1, foundBooks.size());
        assertThat(foundBooks.get(0).getTitle(), Matchers.equalToIgnoringCase("moby dick"));
    }

    @Test
    void testGetBookThrowsExceptionForUnknownId() {
        String unknownId = "unknown";
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> bookManager.getBook(unknownId),
                "Expected getBook() to throw, but it didn't");

        assertTrue(thrown.getMessage().contains("No book found with ID: " + unknownId));
    }

    @ParameterizedTest
    @CsvSource({
            "The Great Gatsby, F. Scott Fitzgerald",
            "Moby Dick, Herman Melville"
    })
    void testBooksExist_csvSource(String title, String author) {
        List<Book> foundBooks = bookManager.findBooksByTitle(title);
        assertThat(foundBooks, Matchers.hasItems(Matchers.hasProperty("author", equalTo(author))));
    }

    @ParameterizedTest
    @EnumSource(value = Genre.class)
    void testBooksExist_EnumSource(Genre genre) {
        List<Book> foundBooks = bookManager.findBooksByGenre(genre);
        assertThat(foundBooks, Matchers.hasItems(Matchers.hasProperty("genre", equalTo(genre))));
    }

    @Test
    void testNoBooksByUnknownAuthor() {
        List<Book> booksByAuthor = bookManager.getBooksByAuthor("Unknown Author");
        assertThat(booksByAuthor, Matchers.is(empty()));
    }

    @Test
    void testBooksByAuthorAndTitle() {
        List<Book> booksByAuthor = bookManager.getBooksByAuthor("Fyodor Dostoevsky");
        assertThat(booksByAuthor, Matchers.hasItem(Matchers.allOf(
                Matchers.hasProperty("title", Matchers.equalTo("Brothers Karamazov")),
                Matchers.hasProperty("author", Matchers.equalTo("Fyodor Dostoevsky"))
        )));
    }
}
