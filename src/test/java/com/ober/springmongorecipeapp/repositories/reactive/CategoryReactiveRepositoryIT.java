package com.ober.springmongorecipeapp.repositories.reactive;

import com.ober.springmongorecipeapp.domain.Category;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataMongoTest
public class CategoryReactiveRepositoryIT {

    private static final String DESCRIPTION = "American";

    @Autowired
    CategoryReactiveRepository repository;

    @Before
    public void setUp() throws Exception {
        repository.deleteAll().block();
    }

    @Test
    public void testCategorySave() throws Exception {
        Category category = new Category();
        category.setDescription(DESCRIPTION);

        repository.save(category).block();

        Long count = repository.count().block();

        assertEquals(Long.valueOf(1L), count);
    }

    @Test
    public void testFindByDescription() throws Exception {
        Category category = new Category();
        category.setDescription(DESCRIPTION);

        repository.save(category).then().block();

        Category fetchedCategory = repository.findByDescription(DESCRIPTION).block();

        assertNotNull(fetchedCategory.getId());
    }

}