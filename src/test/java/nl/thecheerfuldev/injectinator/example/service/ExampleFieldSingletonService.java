package nl.thecheerfuldev.injectinator.example.service;

import nl.thecheerfuldev.injectinator.example.logger.Logger;
import nl.thecheerfuldev.injectinator.framework.annotation.InjectMe;
import nl.thecheerfuldev.injectinator.framework.annotation.InjectionType;

public class ExampleFieldSingletonService {

    @InjectMe(injectionType = InjectionType.SINGLETON)
    private Logger logger;

    public void doStuff() {
        this.logger.log("Field injection works.");
    }

    public void setExtraMessage(final String extraMessage) {
        this.logger.setExtraMessage(extraMessage);
    }

    public Logger getLogger() {
        return this.logger;
    }
}
