package com.brianeno.statemachine.service;

import com.brianeno.statemachine.domain.Article;
import com.brianeno.statemachine.domain.ArticleEvents;
import com.brianeno.statemachine.domain.ArticleStates;
import com.brianeno.statemachine.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 *
 */
@RequiredArgsConstructor
@Component
public class ArticleStateChangeInterceptor extends StateMachineInterceptorAdapter<ArticleStates, ArticleEvents> {

    private final ArticleRepository articleRepository;

    @Override
    public void preStateChange(State<ArticleStates, ArticleEvents> state, Message<ArticleEvents> message,
                               Transition<ArticleStates, ArticleEvents> transition, StateMachine<ArticleStates, ArticleEvents> stateMachine,
                               StateMachine<ArticleStates, ArticleEvents> rootStateMachine) {

        Optional.ofNullable(message).flatMap(msg ->
                Optional.ofNullable((Long) msg.getHeaders().getOrDefault(ArticleServiceImpl.ARTICLE_ID_HEADER, -1L))).
            ifPresent(articleId -> {
                Article article = articleRepository.getOne(articleId);
                article.setState(state.getId());
                articleRepository.save(article);
            });
    }
}
