package nl.rovingeye.injectinator.example.service;

import nl.rovingeye.injectinator.example.ILogger;
import nl.rovingeye.injectinator.framework.annotation.InjectMe;

public class ExampleSetterService {

    private ILogger logger;

    public void doStuff() {
        this.logger.log("Setter Injection works.");
    }

    @InjectMe
    public void setLogger(final ILogger logger) {
        this.logger = logger;
    }
}
