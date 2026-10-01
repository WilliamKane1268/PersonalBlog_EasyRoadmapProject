package com.example.PersonalBlog.repository;

import com.example.PersonalBlog.model.Article;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class FileArticleRepository implements ArticleRepository {
    private String folderPath;
    private ObjectMapper objectMapper;

    public FileArticleRepository(@Value("${blog.storage.path}") String folderPath){
        this.folderPath = folderPath;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    @PostConstruct
    public void setUpStorage(){
        Path path = Paths.get(folderPath);
        if(!Files.exists(path)){
            try{
                Files.createDirectories(path);
            } catch (IOException e){
                throw new RuntimeException("Unable to create a folder");
            }
        }
    }

    @Override
    public Article save(Article article){
        Path filePath = Paths.get(folderPath, article.getId() + ".json");
        try{
            objectMapper.writeValue(filePath.toFile(),article);
            return article;
        } catch (IOException e) {
            throw new RuntimeException("Failed when try to save article");
        }
    }

    @Override
    public Optional<Article> findById(String id){
        Path filePath = Paths.get(folderPath, id + ".json");
        if(!Files.exists(filePath)){
            return Optional.empty();
        }
        try{
            Article article = objectMapper.readValue(filePath.toFile(), Article.class);
            return Optional.of(article);
        } catch (IOException e) {
            throw new RuntimeException("Failed when try to read article");
        }
    }

    @Override
    public List<Article> findAll(){
        Path directory = Paths.get(folderPath);
        if(!Files.exists(directory)){
            return Collections.emptyList();
        }
        try(Stream<Path> fileStream = Files.list(directory)){
            return fileStream.filter(path -> path.toString().endsWith(".json"))
                    .filter(Files::isRegularFile)
                    .map(this::readArticleFromFile)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Article readArticleFromFile(Path path){
        try{
            return objectMapper.readValue(path.toFile(), Article.class);
        } catch (IOException e) {
            throw new RuntimeException("Cant read file");
        }
    }

    @Override
    public void deleteById(String id){
        Path filePath = Paths.get(folderPath, id + ".json");
        try{
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed when try to delete article");
        }
    }

    @Override
    public boolean existsById(String id){
        Path filePath = Paths.get(folderPath, id + ".json");
        return Files.exists(filePath);
    }
}