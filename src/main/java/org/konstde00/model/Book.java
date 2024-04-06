package org.konstde00.model;

public class Book {
    private String id;
    private String title;
    private String author;
    private Genre genre;

    public Book(String id,
                String title,
                String author,
                Genre genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Genre getGenre() {
        return genre;
    }
}