package com.brianeno.statemachine.repository;


import com.brianeno.statemachine.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {
}
