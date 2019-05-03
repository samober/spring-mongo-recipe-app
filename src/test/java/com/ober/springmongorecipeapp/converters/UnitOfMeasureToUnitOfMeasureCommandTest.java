package com.ober.springmongorecipeapp.converters;

import com.ober.springmongorecipeapp.commands.UnitOfMeasureCommand;
import com.ober.springmongorecipeapp.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnitOfMeasureToUnitOfMeasureCommandTest {

    public static final String DESCRIPTION = "description";
    public static final String ID = "ID";

    UnitOfMeasureToUnitOfMeasureCommand converter;

    @Before
    public void setUp() throws Exception {
        converter = new UnitOfMeasureToUnitOfMeasureCommand();
    }

    @Test
    public void testNullParameter() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new UnitOfMeasure()));
    }

    @Test
    public void convert() {
        // given
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId(ID);
        uom.setDescription(DESCRIPTION);

        // when
        UnitOfMeasureCommand uomc = converter.convert(uom);

        // then
        assertNotNull(uomc);
        assertEquals(ID, uomc.getId());
        assertEquals(DESCRIPTION, uomc.getDescription());
    }
}