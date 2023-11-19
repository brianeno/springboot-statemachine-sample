package com.brianeno.statemachine.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {

    private Long id;

    private ArticleStates state;

    private String text;
}
