package nl.rovingeye.injectinator.example.service;

import nl.rovingeye.injectinator.example.logger.Logger;
import nl.rovingeye.injectinator.framework.annotation.InjectMe;
import nl.rovingeye.injectinator.framework.annotation.InjectType;

public class ExampleFieldService {

    @InjectMe(injectionType = InjectType.SINGLETON)
    private Logger logger;

    public void doStuff() {
        this.logger.log("Field injection works.");
    }

    public void setExtraMessage(final String extraMessage) {
        this.logger.setExtraMessage(extraMessage);
    }
}
