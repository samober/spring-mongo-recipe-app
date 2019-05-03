package com.ober.springmongorecipeapp.services;

import com.ober.springmongorecipeapp.commands.RecipeCommand;
import com.ober.springmongorecipeapp.converters.RecipeCommandToRecipe;
import com.ober.springmongorecipeapp.converters.RecipeToRecipeCommand;
import com.ober.springmongorecipeapp.domain.Recipe;
import com.ober.springmongorecipeapp.exceptions.NotFoundException;
import com.ober.springmongorecipeapp.repositories.reactive.RecipeReactiveRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;

    @Mock
    RecipeReactiveRepository recipeRepository;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        recipeService = new RecipeServiceImpl(recipeRepository,
                recipeCommandToRecipe,
                recipeToRecipeCommand);
    }

    @Test
    public void getRecipebyIdTest() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId("1");

        when(recipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        Recipe recipeReturned = recipeService.findById("1").block();

        assertNotNull("Null recipe returned", recipeReturned);
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, never()).findAll();
    }



    @Test(expected = NotFoundException.class)
    @Ignore
    public void getRecipeByIdTestNotFound() throws Exception {
        when(recipeRepository.findById(anyString())).thenReturn(Mono.empty());

        Recipe recipeReturned = recipeService.findById("1").block();

        // should throw exception
    }

    @Test
    public void getRecipeCommandByIdTest() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId("1");

        when(recipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("1");

        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        RecipeCommand commandById = recipeService.findCommandById("1").block();

        assertNotNull("Null recipe returned", commandById);
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    public void getRecipes() {
        Recipe recipe = new Recipe();

        when(recipeRepository.findAll()).thenReturn(Flux.just(recipe));

        List<Recipe> recipes = recipeService.getRecipes().collectList().block();
        assertEquals(1, recipes.size());

        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteById() throws Exception {
        // given
        String idToDelete = "2";

        when(recipeRepository.deleteById(anyString())).thenReturn(Mono.empty());

        // when
        recipeService.deleteById(idToDelete).block();

        // then
        verify(recipeRepository, times(1)).deleteById(anyString());
    }
}