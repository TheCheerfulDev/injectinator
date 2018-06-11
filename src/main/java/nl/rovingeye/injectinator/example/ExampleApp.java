package nl.rovingeye.injectinator.example;

import nl.rovingeye.injectinator.example.config.MyInjectableConfig;
import nl.rovingeye.injectinator.example.service.ExampleConstructorService;
import nl.rovingeye.injectinator.example.service.ExampleFieldService;
import nl.rovingeye.injectinator.example.service.ExampleSetterNewService;
import nl.rovingeye.injectinator.example.service.ExampleSetterService;
import nl.rovingeye.injectinator.framework.Injectinator;

public final class ExampleApp {

    private ExampleApp() {
    }

    public static void main(final String[] args) throws Exception {
        final Injectinator injectinator = Injectinator.getInjectinator(new MyInjectableConfig());
        final ExampleFieldService exampleFieldService = injectinator.inject(ExampleFieldService.class);
        final ExampleFieldService exampleFieldService2 = injectinator.inject(ExampleFieldService.class);
        final ExampleConstructorService exampleConstructorService = injectinator.inject(ExampleConstructorService.class);
        final ExampleSetterService exampleSetterService = injectinator.inject(ExampleSetterService.class);
        final ExampleSetterNewService exampleSetterNewService = injectinator.inject(ExampleSetterNewService.class);

        exampleFieldService.setExtraMessage("This is an alternate extra message");
        exampleFieldService.doStuff();
        exampleFieldService2.doStuff();
        exampleConstructorService.doStuff();
        exampleSetterService.doStuff();
        exampleSetterNewService.doStuff();
    }
}
