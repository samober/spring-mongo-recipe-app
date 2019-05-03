package com.ober.springmongorecipeapp.services;

import com.ober.springmongorecipeapp.commands.IngredientCommand;
import com.ober.springmongorecipeapp.converters.IngredientCommandToIngredient;
import com.ober.springmongorecipeapp.converters.IngredientToIngredientCommand;
import com.ober.springmongorecipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.ober.springmongorecipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.ober.springmongorecipeapp.domain.Ingredient;
import com.ober.springmongorecipeapp.domain.Recipe;
import com.ober.springmongorecipeapp.repositories.RecipeRepository;
import com.ober.springmongorecipeapp.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class IngredientServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    IngredientToIngredientCommand ingredientToIngredientCommand;
    IngredientCommandToIngredient ingredientCommandToIngredient;

    IngredientServiceImpl ingredientService;

    public IngredientServiceImplTest() {
        this.ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        this.ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        this.ingredientService = new IngredientServiceImpl(
                ingredientToIngredientCommand,
                ingredientCommandToIngredient,
                recipeRepository,
                unitOfMeasureRepository);
    }

    @Test
    public void findByRecipeIdAndId() {
        // given
        Recipe recipe = new Recipe();
        recipe.setId("ID");

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId("ID1");

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId("ID2");

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId("ID3");

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

        // when
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId("ID", "ID3");

        // then
        assertEquals("ID3", ingredientCommand.getId());
        assertEquals("ID", ingredientCommand.getRecipeId());
        verify(recipeRepository, times(1)).findById(anyString());
    }

    @Test
    public void testSaveIngredientCommand() throws Exception {
        // given
        IngredientCommand command = new IngredientCommand();
        command.setId("3");
        command.setRecipeId("2");

        Optional<Recipe> recipeOptional = Optional.of(new Recipe());

        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(new Ingredient());
        savedRecipe.getIngredients().iterator().next().setId("3");

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);

        // when
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        // then
        assertEquals("3", savedCommand.getId());
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }

    @Test
    public void testDeleteIngredientByRecipeIdAndIngredientId() throws Exception {
        // given
        Recipe recipe = new Recipe();

        Ingredient ingredient = new Ingredient();
        ingredient.setId("3");

        recipe.addIngredient(ingredient);

        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));

        // when
        ingredientService.deleteByRecipeIdAndIngredientId("1", "3");

        // then
        assertEquals(0, recipe.getIngredients().size());
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }
}