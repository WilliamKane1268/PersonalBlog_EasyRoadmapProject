package com.example.PersonalBlog.controller;

import com.example.PersonalBlog.dto.ArticleForm;
import com.example.PersonalBlog.model.Article;
import com.example.PersonalBlog.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminArticleController {
    private final ArticleService articleService;

    public AdminArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model){
        List<Article> list = articleService.getAllArticles();
        model.addAttribute("articles", list);
        return "admin/dashboard";
    }


    @GetMapping("/articles/new")
    public String showCreateForm(Model model){
        model.addAttribute("articleForm", new ArticleForm());
        model.addAttribute("isNew", true);
        return "admin/article-form";
    }

    @PostMapping("/articles")
    public String processCreateArticle(@Valid @ModelAttribute("articleForm") ArticleForm articleForm,
                                     BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("isNew", true);
            return "admin/article-form";
        }
        articleService.createArticle(articleForm);
        return "redirect:/admin/dashboard";

    }

    @GetMapping("/articles/{id}/edit")
    public String showEditForm(@PathVariable("id") String id, Model model){
        Article article = articleService.getArticleById(id);
        ArticleForm articleForm = new ArticleForm(article.getTitle(), article.getContent());
        model.addAttribute("articleForm", articleForm);
        model.addAttribute("articleId", id);
        model.addAttribute("isNew", false);
        return "admin/article-form";

    }

    @PostMapping("/articles/{id}")
    public String processUpdateArticle(@PathVariable String id,
                                     @Valid @ModelAttribute("articleForm") ArticleForm articleForm,
                                     BindingResult bindingResult,
                                     Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("articleId", id);
            model.addAttribute("isNew", false);
            return "admin/article-form";
        }

        articleService.updateArticle(id, articleForm);
        return "redirect:/admin/dashboard";

    }

    @PostMapping("/articles/{id}/delete")
    public String deleteArticle(@PathVariable("id") String id){
        articleService.deleteArticle(id);
        return "redirect:/admin/dashboard";
    }
}
