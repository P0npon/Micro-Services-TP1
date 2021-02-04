package com.microservice.articlesservice.dao;

import com.microservice.articlesservice.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleDao extends JpaRepository<Article, Integer> {
    List<Article> findAll();
    Article findById(int id);
    Article save(Article article);
    List<Article> findByPrixGreaterThan(int prixLimit);
    List<Article> findByNomLike(String recherche);
    List<Article> findByOrderByNomAsc();
}
