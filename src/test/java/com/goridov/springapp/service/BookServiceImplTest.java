package com.goridov.springapp.service;

import com.goridov.springapp.entity.Book;
import com.goridov.springapp.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;


    @Test
    void getAllBooks() {
        Book book1 = new Book(1L, "Title 1", "Author 1", 1999);
        Book book2 = new Book(2L, "Title 2", "Author 2", 2000);

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        List<Book> result = bookRepository.findAll();

        assertThat(List.of(book1, book2).size()).isEqualTo(result.size());
    }

    @Test
    void testGetBookById() {
        Book testBook = new Book();
        testBook.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        Book result = bookService.getBookById(1L);

        assertThat(1L).isEqualTo(result.getId());

    }

    @Test
    void createBook() {
        Book book = new Book(1L, "Title 1", "Author 1", 1999);
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.createBook(book);

        assertThat(book.getAuthor()).isEqualTo(result.getAuthor());
    }

    @Test
    void testUpdateBook() {
        Long bookId = 1L;
        Book existingBook = new Book();
        existingBook.setId(bookId);

        Book updatedBookData = new Book();
        updatedBookData.setTitle("Updated Title");
        updatedBookData.setAuthor("Updated Author");
        updatedBookData.setYear(2023);

        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(bookRepository.save(any(Book.class))).thenAnswer(invocationOnMock -> {
            Book savedBook = invocationOnMock.getArgument(0);
            savedBook.setId(bookId);
            return savedBook;
        });

        Book updatedBook = bookService.updateBook(bookId, updatedBookData);


        assertThat(bookId).isEqualTo(updatedBook.getId());
        assertThat(updatedBookData.getTitle()).isEqualTo (updatedBook.getTitle());
        assertThat(updatedBookData.getAuthor()).isEqualTo (updatedBook.getAuthor());
        assertThat(updatedBookData.getYear()).isEqualTo (updatedBook.getYear());

        verify(bookRepository, times(1)).existsById(bookId);
        verify(bookRepository, times(1)).save(updatedBookData);
    }

    @Test
    void testDeleteBook() {
        Long bookId = 1L;

        when(bookRepository.existsById(bookId)).thenReturn(true);

        bookService.deleteBook(bookId);

        verify(bookRepository, times(1)).existsById(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void testSaveAll() {
        Book book1 = new Book(1L, "Title 1", "Author 1", 1999);
        Book book2 = new Book(2L, "Title 2", "Author 2", 2000);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);

        bookService.saveAll(books);

        verify(bookRepository, times(1)).saveAll(books);
    }

}
