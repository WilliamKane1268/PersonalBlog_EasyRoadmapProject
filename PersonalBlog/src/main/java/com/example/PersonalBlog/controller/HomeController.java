package com.example.PersonalBlog.controller;

import com.example.PersonalBlog.dto.ArticleForm;
import com.example.PersonalBlog.model.Article;
import com.example.PersonalBlog.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class HomeController {
    private final ArticleService articleService;

    public HomeController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping({"/", "/home", "/articles"})
    public String showHomePage(Model model){
        List<Article> list = articleService.getAllArticles();
        model.addAttribute("articles", list);
        return "guest/index";
    }
    @GetMapping({"/article/{id}", "/articles/{id}"})
    public String showArticleDetail(@PathVariable String id, Model model){
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "guest/article-detail";
    }

}
