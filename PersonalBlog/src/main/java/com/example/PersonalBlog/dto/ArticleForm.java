package com.example.PersonalBlog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleForm {
    @NotBlank(message = "Title can't be blank")
    @Size(min = 5, max = 150, message = "Title isnt suitable")
    private String title;
    @NotBlank(message = "Content can't be blank")
    private String content;
}
