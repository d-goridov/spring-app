package com.goridov.springapp.service;

import com.goridov.springapp.entity.Book;

import java.util.List;


public interface BookService {
    List<Book> getAllBooks();

    Book getBookById(Long id);

    Book createBook(Book book);

    Book updateBook(Long id, Book book);

    void deleteBook(Long id);

    void saveAll(List<Book> books);
}

