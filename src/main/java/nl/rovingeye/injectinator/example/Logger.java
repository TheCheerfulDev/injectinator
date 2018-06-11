package nl.rovingeye.injectinator.example;

import nl.rovingeye.injectinator.framework.annotation.InjectMe;
import nl.rovingeye.injectinator.framework.annotation.InjectType;

public class Logger implements ILogger {

    @InjectMe(injectionType = InjectType.SINGLETON)
    private IAnotherLogger anotherLogger;

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
