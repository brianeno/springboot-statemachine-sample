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

import java.util.Random;

@Slf4j
@Component
public class ReviewCompleteAction implements Action<ArticleStates, ArticleEvents> {

    @Override
    public void execute(StateContext<ArticleStates, ArticleEvents> context) {
        Long header = (Long) context.getMessageHeader(ArticleServiceImpl.ARTICLE_ID_HEADER);
        System.out.println("Doing action " + context.getTarget().getId() + " received article id " + header);

        /*
        this is pseudo logic that will statistically publish every 8 out 10 times and reject 2 out
        of 10.
         */
        if (new Random().nextInt(10) < 8) {
            context.getStateMachine().sendEvent(Mono.just(MessageBuilder.withPayload(ArticleEvents.PUBLISH_ARTICLE)
                .setHeader(ArticleServiceImpl.ARTICLE_ID_HEADER, header)
                .build())).doOnComplete(() -> System.out.println("Review Successful handling complete")).subscribe();
        } else {
            context.getStateMachine().sendEvent(Mono.just(MessageBuilder.withPayload(ArticleEvents.REJECT_ARTICLE)
                .setHeader(ArticleServiceImpl.ARTICLE_ID_HEADER, header)
                .build())).doOnComplete(() -> System.out.println("Review Reject handling complete")).subscribe();
        }
    }
}
