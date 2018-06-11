package nl.rovingeye.injectinator.example;

import nl.rovingeye.injectinator.example.config.MyInjectableConfig;
import nl.rovingeye.injectinator.example.service.ExampleConstructorService;
import nl.rovingeye.injectinator.example.service.ExampleFieldService;
import nl.rovingeye.injectinator.example.service.ExampleFieldService2;
import nl.rovingeye.injectinator.example.service.ExampleSetterService;
import nl.rovingeye.injectinator.framework.Injectinator;

public final class ExampleApp {

    private ExampleApp() {
    }

    public static void main(final String[] args) throws Exception {
        final Injectinator injectinator = Injectinator.getInjectinator(new MyInjectableConfig());
        final ExampleConstructorService exampleConstructorService = injectinator.inject(ExampleConstructorService.class);
        final ExampleFieldService exampleFieldService = injectinator.inject(ExampleFieldService.class);
        final ExampleFieldService2 exampleFieldService2 = injectinator.inject(ExampleFieldService2.class);
        final ExampleSetterService exampleSetterService = injectinator.inject(ExampleSetterService.class);

        exampleConstructorService.doStuff();
        exampleFieldService.doStuff();
        exampleFieldService2.doStuff();
        exampleSetterService.doStuff();
    }
}
