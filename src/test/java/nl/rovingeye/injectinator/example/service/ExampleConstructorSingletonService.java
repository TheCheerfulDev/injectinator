package nl.rovingeye.injectinator.example.service;

import nl.rovingeye.injectinator.example.logger.Logger;
import nl.rovingeye.injectinator.framework.annotation.InjectMe;
import nl.rovingeye.injectinator.framework.annotation.InjectType;

public class ExampleConstructorSingletonService {

    private final Logger logger;

    @InjectMe(injectionType = InjectType.SINGLETON)
    public ExampleConstructorSingletonService(final Logger logger) {
        this.logger = logger;
    }

    public void doStuff() {
        this.logger.log("Constructor Injection works.");
    }

    public Logger getLogger() {
        return this.logger;
    }
}
