package com.lib.mg.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String author;

    @Column(name = "book_format")
    private String bookFormat;

    @Column(name = "cover_img", columnDefinition = "TEXT")
    private String coverImg;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String edition;

    @Column(columnDefinition = "TEXT")
    private String genres;

    private String isbn;

    private String language;

    @Column(name = "liked_percent")
    private Double likedPercent;

    @Column(name = "num_ratings")
    private Long numRatings;

    @Column(name = "number_of_copies")
    private Integer numberOfCopies;

    private Integer pages;

    private Double price;

    @Column(name = "primary_author")
    private String primaryAuthor;

    @Column(name = "publish_date")
    private String publishDate;

    @Column(name = "publish_year")
    private String publishYear;

    private String publisher;

    private Double rating;

    private String series;

    private String title;
}
