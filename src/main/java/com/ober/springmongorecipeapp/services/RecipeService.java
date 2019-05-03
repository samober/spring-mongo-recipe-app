package com.ober.springmongorecipeapp.services;

import com.ober.springmongorecipeapp.commands.RecipeCommand;
import com.ober.springmongorecipeapp.domain.Recipe;

import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();

    Recipe findById(String id);

    RecipeCommand findCommandById(String id);

    RecipeCommand saveRecipeCommand(RecipeCommand command);

    void deleteById(String id);

}
