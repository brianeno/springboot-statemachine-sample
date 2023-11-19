package com.brianeno.statemachine.service;

import com.brianeno.statemachine.domain.ArticleEvents;
import com.brianeno.statemachine.domain.ArticleStates;
import org.springframework.messaging.Message;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.Optional;


public class Listener extends StateMachineListenerAdapter<ArticleStates, ArticleEvents> {
    @Override
    public void stateChanged(State<ArticleStates, ArticleEvents> from, State<ArticleStates, ArticleEvents> to) {
        System.out.println("state changed from " + offNullableState(from) + " to " + offNullableState(to));
    }

    @Override
    public void eventNotAccepted(Message<ArticleEvents> eventsMessage) {
        System.out.println("Error event not accepted " + eventsMessage);
    }

    private Object offNullableState(State<ArticleStates, ArticleEvents> s) {
        return Optional.ofNullable(s).map(State::getId).orElse(null);
    }
}
