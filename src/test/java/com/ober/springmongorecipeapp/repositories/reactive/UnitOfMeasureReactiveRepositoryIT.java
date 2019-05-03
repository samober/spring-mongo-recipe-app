package com.ober.springmongorecipeapp.repositories.reactive;

import com.ober.springmongorecipeapp.domain.UnitOfMeasure;
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
public class UnitOfMeasureReactiveRepositoryIT {

    private static final String DESCRIPTION = "Cup";

    @Autowired
    UnitOfMeasureReactiveRepository repository;

    @Before
    public void setUp() throws Exception {
        repository.deleteAll().block();
    }

    @Test
    public void testUOMSave() throws Exception {
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setDescription(DESCRIPTION);

        repository.save(uom).block();

        Long count = repository.count().block();

        assertEquals(Long.valueOf(1L), count);
    }

    @Test
    public void testFindByDescription() throws Exception {
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setDescription(DESCRIPTION);

        repository.save(uom).then().block();

        UnitOfMeasure fetchedUOM = repository.findByDescription(DESCRIPTION).block();

        assertNotNull(fetchedUOM.getId());
    }

}