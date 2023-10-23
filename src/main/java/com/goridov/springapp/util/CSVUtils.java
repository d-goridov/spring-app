package com.goridov.springapp.util;

import com.goridov.springapp.entity.Book;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVUtils {
    public static List<Book> processCSVFile(MultipartFile file, String delimiter) throws IOException {
        List<Book> books = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(delimiter);
                if (parts.length == 3) {
                    String title = parts[0];
                    String author = parts[1];
                    int year = Integer.parseInt(parts[2]);

                    Book book = new Book();
                    book.setTitle(title);
                    book.setAuthor(author);
                    book.setYear(year);

                    books.add(book);
                } else {
                    throw new IllegalArgumentException("Invalid CSV format.");
                }
            }
        }

        return books;
    }
}
