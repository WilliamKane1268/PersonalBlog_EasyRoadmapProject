package com.example.PersonalBlog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor    // Constructor
public class Article {
    private String id;
    private String title;
    private String content;
    private LocalDate publishedDate;
}
