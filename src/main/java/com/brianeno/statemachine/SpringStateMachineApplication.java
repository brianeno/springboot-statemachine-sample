package com.brianeno.statemachine;

import com.brianeno.statemachine.domain.ArticleEvents;
import com.brianeno.statemachine.domain.ArticleStates;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

@SpringBootApplication
@EnableStateMachine
@RequiredArgsConstructor
public class SpringStateMachineApplication implements CommandLineRunner {


    private final StateMachineFactory<ArticleStates, ArticleEvents> stateMachineFactory;

    public static void main(String[] args) {
        SpringApplication.run(SpringStateMachineApplication.class, args);
    }

    @Override
    public void run(String... args) {
     /*
        StateMachine<ArticleStates, ArticleEvents> stateMachine = stateMachineFactory.getStateMachine();

        // since version 3.x need to send events reactively
        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(ArticleEvents.EDIT_ARTICLE)
            .setHeader(ArticleServiceImpl.ARTICLE_ID_HEADER, 1)
            .build())).doOnComplete(() -> System.out.println("Initial Edit Article handling complete")).subscribe();

        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(ArticleEvents.REVIEW_ARTICLE)
            .setHeader(ArticleServiceImpl.ARTICLE_ID_HEADER, 1)
            .build())).doOnComplete(() -> System.out.println("Review Article handling complete")).subscribe();

        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(ArticleEvents.PUBLISH_ARTICLE)
            .setHeader(ArticleServiceImpl.ARTICLE_ID_HEADER, 1)
            .build())).doOnComplete(() -> System.out.println("Publish Article handling complete")).subscribe();
   */
    }
}