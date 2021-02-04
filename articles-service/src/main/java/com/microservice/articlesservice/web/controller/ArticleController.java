package com.microservice.articlesservice.web.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.microservice.articlesservice.exception.ArticleIntrouvableException;
import com.microservice.articlesservice.exception.InvalidArticlePriceValue;
import com.microservice.articlesservice.model.Article;
import com.microservice.articlesservice.dao.ArticleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class ArticleController {

    @Autowired
    private ArticleDao articleDao;


    //Récupérer un article par son Id
    @GetMapping(value="/Articles/{id}")
    public Article afficherUnArticle(@PathVariable int id) {
        Article article =  articleDao.findById(id);
        if(article==null) throw new ArticleIntrouvableException("L'article avec l'id "+id+" est introuvable.");
        return article;
    }

    //ajouter un article
    @PostMapping(value = "/Articles")
    public ResponseEntity<Void> ajouterArticle(@RequestBody
                                                       Article article) {
        if(article.getPrix()==0) throw new InvalidArticlePriceValue("La valeur de l'article ne peut être égale à 0");
        Article articleAdded = articleDao.save(article);
        if (articleAdded == null)
            return ResponseEntity.noContent().build();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(articleAdded.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    //Récupérer la liste des articles
    @RequestMapping(value = "/Articles", method =
            RequestMethod.GET)
    public MappingJacksonValue listeArticles() {
        List<Article> articles = articleDao.findAll();
        SimpleBeanPropertyFilter monFiltre =
                SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
        FilterProvider listDeNosFiltres = new
                SimpleFilterProvider().addFilter("monFiltreDynamique",
                monFiltre);
        MappingJacksonValue articlesFiltres = new
                MappingJacksonValue(articles);
        articlesFiltres.setFilters(listDeNosFiltres);
        return articlesFiltres;
    }

    @GetMapping(value = "/test/articles/{prixLimit}")
    public List<Article> testeDeRequetes(@PathVariable int
                                                 prixLimit) {
        return articleDao.findByPrixGreaterThan(prixLimit);
    }

    @GetMapping(value = "/test/articles/like/{recherche}")
    public List<Article> testeDeRequetes(@PathVariable String
                                                 recherche) {
        return articleDao.findByNomLike("%"+recherche+"%");
    }

    @DeleteMapping (value = "/Articles/{id}")
    public void supprimerArticle(@PathVariable int id) {
        articleDao.deleteById(id);
    }

    @PutMapping (value = "/Articles")
    public void updateArticle(@RequestBody Article article) {
        articleDao.save(article);
    }

    @GetMapping(value = "/AdminArticles/{id}")
    public int calculerMargeArticle(@PathVariable int id) {
        Article article = articleDao.findById(id);
        return article.getPrix()-article.getPrixAchat();
    }

    @GetMapping(value = "/Articles/OrderByName")
    public List<Article> trierArticlesParOrdreAlphabetique() {
        return articleDao.findByOrderByNomAsc();
    }



}