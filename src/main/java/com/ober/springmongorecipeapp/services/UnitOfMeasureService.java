package com.ober.springmongorecipeapp.services;

import com.ober.springmongorecipeapp.commands.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;

public interface UnitOfMeasureService {

    Flux<UnitOfMeasureCommand> listAllUoms();

}
