package com.ober.springmongorecipeapp.repositories.reactive;

import com.ober.springmongorecipeapp.domain.Recipe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, String> {
}
