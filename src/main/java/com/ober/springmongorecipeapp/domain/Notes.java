package com.ober.springmongorecipeapp.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"recipe"})
public class Notes {

    private String id;

    private String recipeNotes;

    private Recipe recipe;

}
