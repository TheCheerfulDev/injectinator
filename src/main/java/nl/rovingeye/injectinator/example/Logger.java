package nl.rovingeye.injectinator.example;

public class Logger implements ILogger{

    @Override
    public void log(String message) {
        System.out.println("I must print this: " + message);
    }
}
