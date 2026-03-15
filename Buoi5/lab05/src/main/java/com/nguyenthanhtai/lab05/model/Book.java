package com.nguyenthanhtai.lab05.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @NotBlank(message = "Author is required")
    @Column(name = "author", nullable = false, length = 150)
    private String author;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be >= 0")
    @Max(value = 10000000, message = "Price must be <= 10,000,000")
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull(message = "Category is required")
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
