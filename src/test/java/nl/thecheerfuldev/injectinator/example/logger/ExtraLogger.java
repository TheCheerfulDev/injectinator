package nl.thecheerfuldev.injectinator.example.logger;

public class ExtraLogger implements AnotherLogger {

    @Override
    public void info(final String message) {
        System.out.println("Via ExtraLogger: " + message);
    }
}
