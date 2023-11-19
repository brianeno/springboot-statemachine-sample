package com.brianeno.statemachine.service;

import com.brianeno.statemachine.domain.Article;
import com.brianeno.statemachine.domain.ArticleDto;
import com.brianeno.statemachine.domain.ArticleEvents;
import com.brianeno.statemachine.domain.ArticleStates;
import com.brianeno.statemachine.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements ArticleService {

    public static final String ARTICLE_ID_HEADER = "article_id";
    private final ArticleRepository articleRepository;
    private final StateMachineFactory<ArticleStates, ArticleEvents> stateMachineFactory;
    private final ArticleStateChangeInterceptor articleStateChangeInterceptor;

    @Override
    public Article newArticle(ArticleDto articleDto) {
        Article article = Article.builder()
            .state(ArticleStates.UNEDITED_ARTICLE)
            .text(articleDto.getText())
            .build();
        return articleRepository.save(article);
    }

    @Transactional
    @Override
    public StateMachine<ArticleStates, ArticleEvents> beginEditing(Long articleId) {
        StateMachine<ArticleStates, ArticleEvents> sm = build(articleId);

        sendEvent(articleId, sm, ArticleEvents.EDIT_ARTICLE);

        return sm;
    }

    @Transactional
    @Override
    public StateMachine<ArticleStates, ArticleEvents> beginReviewing(Long articleId) {
        StateMachine<ArticleStates, ArticleEvents> sm = build(articleId);

        sendEvent(articleId, sm, ArticleEvents.REVIEW_ARTICLE);

        return sm;
    }

    @Transactional
    @Override
    public StateMachine<ArticleStates, ArticleEvents> publish(Long articleId) {
        StateMachine<ArticleStates, ArticleEvents> sm = build(articleId);

        sendEvent(articleId, sm, ArticleEvents.PUBLISH_ARTICLE);

        return sm;
    }

    private void sendEvent(Long articleId, StateMachine<ArticleStates, ArticleEvents> sm,
                           ArticleEvents event) {
        Message<ArticleEvents> msg = MessageBuilder.withPayload(event)
            .setHeader(ARTICLE_ID_HEADER, articleId)
            .build();

        sm.sendEvent(Mono.just(msg)).subscribe();
    }

    private StateMachine<ArticleStates, ArticleEvents> build(Long paymentId) {
        Article article = articleRepository.getOne(paymentId);

        StateMachine<ArticleStates, ArticleEvents> sm = stateMachineFactory.getStateMachine(Long.toString(article.getId()));

        sm.stop();

        sm.getStateMachineAccessor()
            .doWithAllRegions(smInt -> {
                smInt.addStateMachineInterceptor(articleStateChangeInterceptor);
                smInt.resetStateMachine(new DefaultStateMachineContext<>(article.getState(), null, null, null));
            });

        sm.start();

        return sm;
    }
}
