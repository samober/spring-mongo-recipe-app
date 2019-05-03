package com.ober.springmongorecipeapp.services;

import com.ober.springmongorecipeapp.commands.UnitOfMeasureCommand;

import java.util.Set;

public interface UnitOfMeasureService {

    Set<UnitOfMeasureCommand> listAllUoms();

}
