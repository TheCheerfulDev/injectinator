package nl.rovingeye.injectinator.example.service;

import nl.rovingeye.injectinator.example.ILogger;
import nl.rovingeye.injectinator.framework.annotation.InjectMe;
import nl.rovingeye.injectinator.framework.annotation.InjectType;

public class ExampleConstructorService {

    private final ILogger logger;

    @InjectMe(injectionType = InjectType.SINGLETON)
    public ExampleConstructorService(final ILogger logger) {
        this.logger = logger;
    }

    public void doStuff() {
        this.logger.log("Constructor Injection works.");
    }
}
