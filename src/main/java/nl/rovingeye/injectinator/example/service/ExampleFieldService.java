package nl.rovingeye.injectinator.example.service;

import nl.rovingeye.injectinator.example.ILogger;
import nl.rovingeye.injectinator.framework.annotation.InjectMe;
import nl.rovingeye.injectinator.framework.annotation.InjectType;

public class ExampleFieldService {

    @InjectMe(injectionType = InjectType.SINGLETON)
    private ILogger logger;

    public void doStuff() {
        this.logger.setExtraMessage("This is the alternate extra message");
        this.logger.log("Field injection works.");
    }
}
