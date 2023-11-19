package com.brianeno.statemachine.config;

import com.brianeno.statemachine.actions.BeginEditAction;
import com.brianeno.statemachine.actions.EditingAction;
import com.brianeno.statemachine.actions.EditingCompleteAction;
import com.brianeno.statemachine.actions.ReviewCompleteAction;
import com.brianeno.statemachine.domain.ArticleEvents;
import com.brianeno.statemachine.domain.ArticleStates;
import com.brianeno.statemachine.guards.ArticleIdGuard;
import com.brianeno.statemachine.service.Listener;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
@AllArgsConstructor
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<ArticleStates, ArticleEvents> {

    private final EditingAction editingAction;
    private final BeginEditAction beginEditAction;
    private final EditingCompleteAction editingCompleteAction;
    private final ReviewCompleteAction reviewCompleteAction;
    private final ArticleIdGuard articleIdGuard;

    @Override
    public void configure(StateMachineStateConfigurer<ArticleStates, ArticleEvents> states) throws Exception {
        states.withStates()
            .initial(ArticleStates.UNEDITED_ARTICLE)
            .states(EnumSet.allOf(ArticleStates.class))
            .end(ArticleStates.ARTICLE_PUBLISHED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<ArticleStates, ArticleEvents> transitions) throws Exception {
        transitions
            .withExternal()
            .source(ArticleStates.UNEDITED_ARTICLE)
            .target(ArticleStates.EDITING_IN_PROGRESS)
            .event(ArticleEvents.EDIT_ARTICLE)
            .guard(articleIdGuard)
            .action(beginEditAction)
            .and()
            .withExternal()
            .source(ArticleStates.EDITING_IN_PROGRESS)
            .target(ArticleStates.ARTICLE_IN_REVIEW)
            .event(ArticleEvents.REVIEW_ARTICLE)
            .action(editingCompleteAction)
            .and()
            .withExternal()
            .source(ArticleStates.ARTICLE_IN_REVIEW)
            .target(ArticleStates.EDITING_IN_PROGRESS)
            .event(ArticleEvents.REJECT_ARTICLE)
            .action(reviewCompleteAction)
            .and()
            .withExternal()
            .source(ArticleStates.ARTICLE_IN_REVIEW)
            .target(ArticleStates.ARTICLE_PUBLISHED)
            .event(ArticleEvents.PUBLISH_ARTICLE)
            .action(reviewCompleteAction);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<ArticleStates, ArticleEvents> config) throws Exception {
        config.withConfiguration()
            .autoStartup(true)
            .listener(new Listener());
    }
}

