package nl.rovingeye.injectinator.example;

import nl.rovingeye.injectinator.framework.annotation.InjectMe;

public class Logger implements ILogger {

    @InjectMe
    private IAnotherLogger anotherLogger;

    private String mark = "MARK";

    @Override
    public void log(final String message) {
        this.anotherLogger.info("I must print this: " + message + " " + this.mark);
    }

    @Override
    public void setMark(final String mark) {
        this.mark = mark;
    }
}
