package com.ober.springmongorecipeapp.repositories;

import com.ober.springmongorecipeapp.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, String> {
}
