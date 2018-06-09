package nl.rovingeye.injectinator.example.service;

import nl.rovingeye.injectinator.example.ILogger;
import nl.rovingeye.injectinator.framework.annotation.InjectMe;

public class ExampleFieldService {

    @InjectMe
    private ILogger logger;

    public ExampleFieldService() {
    }

    public void doStuff() {
        this.logger.log("Field injection works.");
    }
}
