package nl.rovingeye.injectinator.example;

import nl.rovingeye.injectinator.framework.annotation.InjectMe;

public class Logger implements ILogger {

    @InjectMe
    private IAnotherLogger anotherLogger;

    @Override
    public void log(final String message) {
        this.anotherLogger.info("I must print this: " + message + " ");
    }
}
