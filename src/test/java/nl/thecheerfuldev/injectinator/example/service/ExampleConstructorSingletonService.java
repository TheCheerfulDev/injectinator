package nl.thecheerfuldev.injectinator.example.service;

import nl.thecheerfuldev.injectinator.example.logger.Logger;
import nl.thecheerfuldev.injectinator.framework.annotation.InjectMe;
import nl.thecheerfuldev.injectinator.framework.annotation.InjectionType;

public class ExampleConstructorSingletonService {

    private final Logger logger;

    @InjectMe(injectionType = InjectionType.SINGLETON)
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
