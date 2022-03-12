package nl.thecheerfuldev.injectinator.example;

import nl.thecheerfuldev.injectinator.example.service.ExampleConstructorSingletonService;
import nl.thecheerfuldev.injectinator.example.service.ExampleFieldService;
import nl.thecheerfuldev.injectinator.example.service.ExampleSetterService;
import nl.thecheerfuldev.injectinator.example.service.ExampleSetterSingletonService;
import nl.thecheerfuldev.injectinator.framework.Injectinator;

public final class ExampleApp {

    private ExampleApp() {
    }

    public static void main(final String[] args) throws Exception {
        final Injectinator injectinator = Injectinator.getInjectinator();
        final ExampleFieldService exampleFieldService = injectinator.inject(ExampleFieldService.class);
        final ExampleFieldService exampleFieldService2 = injectinator.inject(ExampleFieldService.class);
        final ExampleConstructorSingletonService exampleConstructorSingletonService = injectinator.inject(ExampleConstructorSingletonService.class);
        final ExampleSetterService exampleSetterService = injectinator.inject(ExampleSetterService.class);
        final ExampleSetterSingletonService exampleSetterSingletonService = injectinator.inject(ExampleSetterSingletonService.class);

        exampleFieldService.setExtraMessage("This is an alternate extra message");
        exampleFieldService.doStuff();
        exampleFieldService2.doStuff();
        exampleConstructorSingletonService.doStuff();
        exampleSetterService.doStuff();
        exampleSetterSingletonService.doStuff();
    }
}
