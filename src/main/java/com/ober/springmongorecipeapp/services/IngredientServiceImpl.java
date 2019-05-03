package com.ober.springmongorecipeapp.services;

import com.ober.springmongorecipeapp.commands.IngredientCommand;
import com.ober.springmongorecipeapp.converters.IngredientCommandToIngredient;
import com.ober.springmongorecipeapp.converters.IngredientToIngredientCommand;
import com.ober.springmongorecipeapp.domain.Ingredient;
import com.ober.springmongorecipeapp.domain.Recipe;
import com.ober.springmongorecipeapp.repositories.reactive.RecipeReactiveRepository;
import com.ober.springmongorecipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeReactiveRepository recipeRepository;
    private final UnitOfMeasureReactiveRepository unitOfMeasureRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
                                 IngredientCommandToIngredient ingredientCommandToIngredient,
                                 RecipeReactiveRepository recipeRepository,
                                 UnitOfMeasureReactiveRepository unitOfMeasureRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        return recipeRepository
                .findById(recipeId)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
                .single()
                .map(ingredient -> {
                    IngredientCommand command = ingredientToIngredientCommand.convert(ingredient);
                    command.setRecipeId(recipeId);
                    return command;
                });
    }

    @Override
    @Transactional
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {
        return recipeRepository.findById(command.getRecipeId())
                .flatMap(recipe -> {
                    Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                            .stream()
                            .filter(ingredient -> ingredient.getId().equals(command.getId()))
                            .findFirst();

                    if (ingredientOptional.isPresent()) {
                        return unitOfMeasureRepository.findById(command.getUom().getId())
                                .flatMap(uom -> {
                                    Ingredient ingredientFound = ingredientOptional.get();
                                    ingredientFound.setDescription(command.getDescription());
                                    ingredientFound.setAmount(command.getAmount());
                                    ingredientFound.setUom(uom);
                                    return recipeRepository.save(recipe);
                                });
                    } else {
                        Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                        recipe.addIngredient(ingredient);
                        return recipeRepository.save(recipe);
                    }
                })
                .map(recipe -> {
                    Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                            .stream()
                            .filter(ingredient -> ingredient.getId().equals(command.getId()))
                            .findFirst();

                    if (!ingredientOptional.isPresent()) {
                        // check by description instead since ingredient does not have an ID value yet
                        ingredientOptional = recipe.getIngredients()
                                .stream()
                                .filter(ingredient -> ingredient.getDescription().equals(command.getDescription()))
                                .filter(ingredient -> ingredient.getAmount().equals(command.getAmount()))
                                .filter(ingredient -> ingredient.getUom().getId().equals(command.getUom().getId()))
                                .findFirst();
                    }

                    IngredientCommand ingredientCommand = ingredientToIngredientCommand.convert(ingredientOptional.get());
                    ingredientCommand.setRecipeId(recipe.getId());
                    return ingredientCommand;
                });
    }

    @Override
    public Mono<Void> deleteByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        log.debug("Deleting Ingredient with ID: " + ingredientId);

        return recipeRepository.findById(recipeId)
                .flatMap(recipe -> {
                    Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                            .stream()
                            .filter(ingredient -> ingredient.getId().equals(ingredientId))
                            .findFirst();

                    if (ingredientOptional.isPresent()) {
                        recipe.getIngredients().remove(ingredientOptional.get());
                        return recipeRepository.save(recipe);
                    }

                    return Mono.empty();
                })
                .then();
    }
}
