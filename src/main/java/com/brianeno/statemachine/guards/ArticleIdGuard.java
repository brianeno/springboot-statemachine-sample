package com.brianeno.statemachine.guards;

import com.brianeno.statemachine.domain.ArticleEvents;
import com.brianeno.statemachine.domain.ArticleStates;
import com.brianeno.statemachine.service.ArticleServiceImpl;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class ArticleIdGuard implements Guard<ArticleStates, ArticleEvents> {

    @Override
    public boolean evaluate(StateContext<ArticleStates, ArticleEvents> context) {
        boolean val = context.getMessageHeader(ArticleServiceImpl.ARTICLE_ID_HEADER) != null;
        System.out.println("Guard result is " + val);
        return val;
    }
}
