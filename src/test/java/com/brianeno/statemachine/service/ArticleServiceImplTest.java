package com.brianeno.statemachine.service;

import com.brianeno.statemachine.domain.Article;
import com.brianeno.statemachine.domain.ArticleDto;
import com.brianeno.statemachine.domain.ArticleEvents;
import com.brianeno.statemachine.domain.ArticleStates;
import com.brianeno.statemachine.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ArticleServiceImplTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;

    private ArticleDto dto;

    @BeforeEach
    void setUp() {
        dto = new ArticleDto();
        dto.setText("Sample text");
    }

    @Transactional
    @Test
    void beginEditing() {

        Article newArticle = articleService.newArticle(dto);

        assertThat(newArticle.getState()).isEqualTo(ArticleStates.UNEDITED_ARTICLE);

        StateMachine<ArticleStates, ArticleEvents> sm = articleService.beginEditing(newArticle.getId());

        articleRepository.getOne(newArticle.getId());

        assertThat(sm.getState().getId()).isEqualTo(ArticleStates.EDITING_IN_PROGRESS);
    }

    @Transactional
    @RepeatedTest(10)
    void testFullFlow() {
        Article newArticle = articleService.newArticle(dto);

        System.out.println(newArticle.getState());
        assertThat(newArticle.getState()).isEqualTo(ArticleStates.UNEDITED_ARTICLE);

        StateMachine<ArticleStates, ArticleEvents> sm = articleService.beginEditing(newArticle.getId());

        assertThat(sm.getState().getId()).isEqualTo(ArticleStates.EDITING_IN_PROGRESS);

        sm = articleService.beginReviewing(newArticle.getId());
        assertThat(sm.getState().getId()).isEqualTo(ArticleStates.ARTICLE_IN_REVIEW);

        sm = articleService.publish(newArticle.getId());
        if (sm.getState().getId().equals(ArticleStates.ARTICLE_PUBLISHED)) {
            assertThat(sm.getState().getId()).isEqualTo(ArticleStates.ARTICLE_PUBLISHED);
        } else {
            assertThat(sm.getState().getId()).isEqualTo(ArticleStates.EDITING_IN_PROGRESS);
        }
    }
}