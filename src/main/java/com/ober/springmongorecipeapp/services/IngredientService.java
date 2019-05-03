package com.ober.springmongorecipeapp.services;

import com.ober.springmongorecipeapp.commands.IngredientCommand;
import reactor.core.publisher.Mono;

public interface IngredientService {

    Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId);

    Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command);

    Mono<Void> deleteByRecipeIdAndIngredientId(String recipeId, String ingredientId);

}
