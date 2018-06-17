package nl.rovingeye.injectinator.example.service;

import nl.rovingeye.injectinator.example.logger.Logger;
import nl.rovingeye.injectinator.framework.annotation.InjectMe;
import nl.rovingeye.injectinator.framework.annotation.InjectType;

public class ExampleSetterSingletonService {

    private Logger logger;

    public void doStuff() {
        this.logger.log("Setter Injection works.");
    }

    @InjectMe(injectionType = InjectType.SINGLETON)
    public void setLogger(final Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return this.logger;
    }
}
