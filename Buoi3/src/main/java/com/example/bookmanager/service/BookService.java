package com.example.bookmanager.service;

import com.example.bookmanager.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BookService {

    private final List<Book> books = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    // Mock data
    public BookService() {
        books.add(new Book(idCounter.getAndIncrement(), "Lập Trình Java Cơ Bản", "Nguyễn Văn A"));
        books.add(new Book(idCounter.getAndIncrement(), "Spring Boot In Action", "Craig Walls"));
        books.add(new Book(idCounter.getAndIncrement(), "Clean Code", "Robert C. Martin"));
        books.add(new Book(idCounter.getAndIncrement(), "Design Patterns", "Gang of Four"));
    }

    /**
     * Lấy toàn bộ danh sách sách.
     */
    public List<Book> getAllBooks() {
        return books;
    }

    /**
     * Thêm sách mới vào danh sách.
     */
    public void addBook(Book book) {
        book.setId(idCounter.getAndIncrement());
        books.add(book);
    }

    /**
     * Tìm sách theo ID.
     */
    public Book findById(Long id) {
        return books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Cập nhật thông tin sách.
     */
    public void updateBook(Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(updatedBook.getId())) {
                books.set(i, updatedBook);
                return;
            }
        }
    }

    /**
     * Xóa sách theo ID.
     */
    public void deleteBook(Long id) {
        books.removeIf(b -> b.getId().equals(id));
    }
}
