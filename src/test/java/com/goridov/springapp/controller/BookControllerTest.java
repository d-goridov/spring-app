package com.goridov.springapp.controller;

import com.goridov.springapp.entity.Book;
import com.goridov.springapp.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() throws Exception {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "Book 1", "Author 1", 2022));
        books.add(new Book(2L, "Book 2", "Author 2", 2023));

        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Book 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value("Author 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].year").value(2022))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Book 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].author").value("Author 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].year").value(2023));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testGetBookById() throws Exception {
        Book book = new Book(1L, "Book 1", "Author 1", 2022);

        when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders.get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Book 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("Author 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(2022));

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void testCreateBook() throws Exception {
        Book book = new Book(1L, "Book 1", "Author 1", 2022);

        when(bookService.createBook(book)).thenReturn(book);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/books")
                                .contentType("application/json")
                                .content("{\"id\": 1, \"title\": \"Book 1\", \"author\": \"Author 1\", \"year\": 2022}")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Book 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("Author 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(2022));

        verify(bookService, times(1)).createBook(any());
    }

    @Test
    void testUpdateBook() throws Exception {

        Book book = new Book(1L, "Book 1", "Author 1", 2022);


        when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(book);

        String requestBody = "{\"id\":1,\"title\":\"Book 1\",\"author\":\"Author 1\",\"year\":2022}";

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/books/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Book 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("Author 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(2022));

        verify(bookService, times(1)).updateBook(eq(1L), any());
    }

    @Test
    void testDeleteBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/1"))
                .andExpect(status().isOk());

        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    public void testUploadCSVFile() throws Exception {
        String csvContent = "Book 1,Author 1,2022\nBook 2,Author 2,2023";
        MockMultipartFile csvFile = new MockMultipartFile("file", "books.csv", "text/csv", csvContent.getBytes());

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/books/upload")
                                .file(csvFile)
                                .param("delimiter", ",")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }
}
