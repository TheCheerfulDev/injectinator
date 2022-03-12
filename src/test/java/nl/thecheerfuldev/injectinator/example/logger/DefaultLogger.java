package nl.thecheerfuldev.injectinator.example.logger;

import nl.thecheerfuldev.injectinator.framework.annotation.InjectMe;
import nl.thecheerfuldev.injectinator.framework.annotation.InjectionType;

public class DefaultLogger implements Logger {

    @InjectMe(injectionType = InjectionType.SINGLETON)
    private AnotherLogger anotherLogger;

    private String extraMessage = "This is the default extra message";

    @Override
    public void log(final String message) {
        this.anotherLogger.info("I must print this: " + message + " " + this.extraMessage);
    }

    @Override
    public void setExtraMessage(final String extraMessage) {
        this.extraMessage = extraMessage;
    }
}
