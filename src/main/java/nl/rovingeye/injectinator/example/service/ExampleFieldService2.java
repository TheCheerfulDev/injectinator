package nl.rovingeye.injectinator.example.service;

import nl.rovingeye.injectinator.example.ILogger;
import nl.rovingeye.injectinator.framework.annotation.InjectMe;
import nl.rovingeye.injectinator.framework.annotation.InjectType;

public class ExampleFieldService2 {

    @InjectMe(injectionType = InjectType.SINGLETON)
    private ILogger logger;

    public void doStuff() {
        this.logger.log("Field injection works.");
    }
}
