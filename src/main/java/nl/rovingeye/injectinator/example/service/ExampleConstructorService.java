package nl.rovingeye.injectinator.example.service;

import nl.rovingeye.injectinator.example.ILogger;
import nl.rovingeye.injectinator.framework.annotation.InjectMe;

public class ExampleConstructorService {

    private final ILogger logger;

    @InjectMe
    public ExampleConstructorService(final ILogger logger) {
        this.logger = logger;
    }

    public void doStuff() {
        this.logger.log("Constructor Injection works.");
    }
}
