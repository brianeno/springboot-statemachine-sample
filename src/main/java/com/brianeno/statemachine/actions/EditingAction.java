package com.brianeno.statemachine.actions;

import com.brianeno.statemachine.domain.ArticleEvents;
import com.brianeno.statemachine.domain.ArticleStates;
import com.brianeno.statemachine.service.ArticleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EditingAction implements Action<ArticleStates, ArticleEvents> {

    @Override
    public void execute(StateContext<ArticleStates, ArticleEvents> context) {
        Integer header = (Integer) context.getMessageHeader(ArticleServiceImpl.ARTICLE_ID_HEADER);
        System.out.println("Doing action " + context.getTarget().getId() + " received article id " + header);
    }
}
