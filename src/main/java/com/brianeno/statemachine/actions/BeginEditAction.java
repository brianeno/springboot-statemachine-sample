package com.brianeno.statemachine.actions;

import com.brianeno.statemachine.domain.ArticleEvents;
import com.brianeno.statemachine.domain.ArticleStates;
import com.brianeno.statemachine.service.ArticleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class BeginEditAction implements Action<ArticleStates, ArticleEvents> {

    @Override
    public void execute(StateContext<ArticleStates, ArticleEvents> context) {
        Long header = (Long) context.getMessageHeader(ArticleServiceImpl.ARTICLE_ID_HEADER);
        System.out.println("Doing action " + context.getTarget().getId() + " received article id " + header);

        context.getStateMachine().sendEvent(Mono.just(MessageBuilder.withPayload(ArticleEvents.EDIT_ARTICLE)
            .setHeader(ArticleServiceImpl.ARTICLE_ID_HEADER, header)
            .build())).doOnComplete(() -> System.out.println("Begin Edit Article handling")).subscribe();
    }
}
