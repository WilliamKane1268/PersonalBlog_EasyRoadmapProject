package com.example.PersonalBlog.service;

import com.example.PersonalBlog.dto.ArticleForm;
import com.example.PersonalBlog.exception.ArticleNotFoundException;
import com.example.PersonalBlog.model.Article;
import com.example.PersonalBlog.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> getAllArticles(){
        return articleRepository.findAll().stream()
                .sorted(Comparator.comparing(Article::getPublishedDate).reversed())
                .toList();
    }

    public Article getArticleById(String id){
        return articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
    }

    public void createArticle(ArticleForm articleForm){
        String id = UUID.randomUUID().toString();
        articleRepository.save(new Article(id,articleForm.getTitle(),
                articleForm.getContent(), LocalDate.now()));
    }

    public void updateArticle(String id, ArticleForm articleForm){
            Article foundArticle = getArticleById(id);
            foundArticle.setTitle(articleForm.getTitle());
            foundArticle.setContent(articleForm.getContent());
            articleRepository.save(foundArticle);
    }

    public void deleteArticle(String id){
        boolean isExisted = articleRepository.existsById(id);
        if(!isExisted){
            throw new ArticleNotFoundException(id);
        } else {
            articleRepository.deleteById(id);
        }
    }
}
