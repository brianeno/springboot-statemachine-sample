package com.brianeno.statemachine.service;

import com.brianeno.statemachine.domain.Article;
import com.brianeno.statemachine.domain.ArticleDto;
import com.brianeno.statemachine.domain.ArticleEvents;
import com.brianeno.statemachine.domain.ArticleStates;
import org.springframework.statemachine.StateMachine;

public interface ArticleService {

    Article newArticle(ArticleDto articleDto);

    StateMachine<ArticleStates, ArticleEvents> beginEditing(Long articleId);

    StateMachine<ArticleStates, ArticleEvents> beginReviewing(Long paymentId);

    StateMachine<ArticleStates, ArticleEvents> publish(Long paymentId);
}
