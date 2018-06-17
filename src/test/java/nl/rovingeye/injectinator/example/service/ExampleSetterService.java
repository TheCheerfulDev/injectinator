package nl.rovingeye.injectinator.example.service;

import nl.rovingeye.injectinator.example.logger.Logger;
import nl.rovingeye.injectinator.framework.annotation.InjectMe;

public class ExampleSetterService {

    private Logger logger;

    public void doStuff() {
        this.logger.log("Setter Injection works.");
    }

    @InjectMe
    public void setLogger(final Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return this.logger;
    }
}
